package com.spotify.it.frontend;

import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import spark.Spark;

public class Main {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Needs backend URL as command-line argument");
      System.exit(1);
      return;
    }

    URI backendUri = URI.create(args[0]);

    Client client = ClientBuilder.newClient();
    WebTarget backend = client.target(backendUri);

    Random random = new SecureRandom();

    Spark.port(1338);
    Spark.get("/", (req, res) -> {
      String uppercase = new BigInteger(130, random).toString(32).toUpperCase();

      String version = backend.path("/api/version")
          .request(MediaType.TEXT_PLAIN)
          .get(String.class);
      String lowercase = backend.path("/api/lowercase/{something}")
          .resolveTemplate("something", uppercase)
          .request(MediaType.TEXT_PLAIN)
          .get(String.class);

      return "<!DOCTYPE html><html>"
             + "<head><title>frontend</title></head>"
             + "<body>"
             + "<p>Backend version: " + version + "</p>"
             + "<p>Lower case of " + uppercase + " is according to backend " + lowercase + "</p>"
             + "</body></html>";
    });
  }
}
