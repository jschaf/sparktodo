package com.delta46.sparktodo;

import static spark.Spark.*;

public class TodoApp {

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");

        get("/hello/:name", (request, response) ->
                "hello: " + request.params(":name"));


    }
}
