package com.spotify.it.backend;

import spark.Spark;

public class Main {

  public static void main(String[] args) {
    Spark.port(1337);
    Spark.get("/api/version", (req, res) -> "v1.0");
    Spark.get("/api/lowercase/:message", (req, res) -> req.params("message").toLowerCase());
  }
}
