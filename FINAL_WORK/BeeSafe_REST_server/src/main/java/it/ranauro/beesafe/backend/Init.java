package it.ranauro.beesafe.backend;

import io.vertx.core.Vertx;

public class Init {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(
                new HttpServerVerticle()
        );

    }
}
