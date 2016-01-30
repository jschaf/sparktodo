package us.delta46.sparktodo;

import org.apache.commons.dbcp2.BasicDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import spark.Filter;
import spark.Request;
import spark.Response;
import us.delta46.sparktodo.repos.AllTodos;
import static us.delta46.sparktodo.models.Tables.*;

import javax.sql.DataSource;

import java.util.Optional;
import java.util.logging.LogManager;

import static spark.Spark.*;
import static us.delta46.sparktodo.transformers.JsonTransformer.json;

public class TodoApp {

    public static final int DEFAULT_PORT = 4567;

    private static int getPortNumber() {
        int port = Optional.ofNullable(System.getenv("PORT"))
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
        return port;
    }


    private static void enableCORS(final String origin, final String methods, final String headers) {
        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }

    private static DataSource buildDataSource() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        String dbUrl = "jdbc:postgresql://ec2-54-83-205-164.compute-1.amazonaws.com:5432/dd53cj9okjlgf3?user=dfhnvuxyfygpuy&password=m1Nm0F1PlC-0bpL4dZp8s2MOxM&sslmode=require";
//                .orElse("jdbc:postgresql://localhost:5432/todo-db?user=joe&password=password");
        ds.setUrl(dbUrl);
        return ds;
    }

    private static DSLContext buildDSLContext() {
        return DSL.using(buildDataSource(), SQLDialect.POSTGRES_9_4);
    }

    private static void populateDbWithFakeData(DSLContext db) {
        db.insertInto(TODO, TODO.ID, TODO.TITLE, TODO.IS_COMPLETE, TODO.ORDERING)
                .values(1, "Note 1", true, 4)
                .values(2, "Note 2", false, 3)
                .values(3, "Note 3", true, 100)
                .onDuplicateKeyIgnore()
                .execute();
    }

    public static void main(String[] args) {

        DSLContext db = buildDSLContext();
        populateDbWithFakeData(db);

        AllTodos allTodos = new AllTodos(db);

        port(getPortNumber());

        enableCORS("*", "*", "*");

        get("/todo", (request, response) -> allTodos.list(), json());
        after((req, res) -> res.type("application/json"));

        get("/todo/:id", (request, response) ->
                "hello: " + request.params(":id"));


    }
}
