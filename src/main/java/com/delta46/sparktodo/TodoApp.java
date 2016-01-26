package com.delta46.sparktodo;

import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class TodoApp {

    public static final int DEFAULT_PORT = 4567;

    private static int getPortNumber() {
        String portString = System.getenv("PORT");
        if (portString == null) {
            return DEFAULT_PORT;
        } else {
            int port = Integer.parseInt(portString);
            return port;
        }
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
}

    public static void main(String[] args) {

        port(getPortNumber());

        enableCORS("*", "*", "*");

        get("/todo", (req, res) -> "Hello World");

        get("/todo/:id", (request, response) ->
                "hello: " + request.params(":id"));


    }
}
