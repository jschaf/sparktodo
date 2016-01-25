package com.delta46.sparktodo;

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

    public static void main(String[] args) {

        port(getPortNumber());

        get("/hello", (req, res) -> "Hello World");

        get("/hello/:name", (request, response) ->
                "hello: " + request.params(":name"));


    }
}
