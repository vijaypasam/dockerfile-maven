package com.spotify.it.frontend;

import com.google.common.io.CharStreams;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

import spark.Spark;

public class Main {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Needs backend URL as command-line argument");
      System.exit(1);
      return;
    }

    URI backendUri = URI.create(args[0]);

    Random random = new SecureRandom();

    Spark.port(1338);
    Spark.get("/", (req, res) -> {
      String uppercase = new BigInteger(130, random).toString(32).toUpperCase();

      String version;
      try (InputStream versionStream = backendUri.resolve("/api/version").toURL().openStream()) {
        version =
            CharStreams.toString(new InputStreamReader(versionStream, StandardCharsets.UTF_8));
      }

      String lowercase;
      try (InputStream lowercaseStream =
               backendUri.resolve("/api/lowercase/" + uppercase).toURL().openStream()) {
        lowercase =
            CharStreams.toString(new InputStreamReader(lowercaseStream, StandardCharsets.UTF_8));
      }

      return "<!DOCTYPE html><html>"
             + "<head><title>frontend</title></head>"
             + "<body>"
             + "<p>Backend version: " + version + "</p>"
             + "<p>Lower case of " + uppercase + " is according to backend " + lowercase + "</p>"
             + "</body></html>";
    });
  }
}
