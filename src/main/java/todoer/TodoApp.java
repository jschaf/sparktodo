package todoer;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import todoer.api.TodoEntry;
import todoer.repos.AllTodos;
import todoer.transformers.JsonTransformer;

import javax.sql.DataSource;
import javax.ws.rs.NotFoundException;

import java.util.Optional;

import static spark.Spark.*;
import static todoer.transformers.JsonTransformer.json;
import static todoer.models.tables.Todo.*;

class TodoApp {

    private static final int DEFAULT_PORT = 4567;

    private static int getPortNumber() {
        return Optional.ofNullable(System.getenv("PORT"))
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
    }


    private static void enableCORS() {
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
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });
    }

    private static DataSource buildDataSource() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        String dbUrl = Optional.ofNullable(System.getenv("JDBC_DATABASE_URL"))
                .orElse(Optional.ofNullable(System.getenv("JDBC_TODO_DEV_URL"))
                        .orElseThrow(() -> new RuntimeException("No matching environmental variables for "
                        + "JDBC_DATABASE_URL or JDBC_TODO_DEV_URL")));
        ds.setUrl(dbUrl);
        return ds;
    }

    private static void runFlywayMigrations() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(buildDataSource());
        flyway.repair();
        flyway.migrate();
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

        runFlywayMigrations();

        DSLContext db = buildDSLContext();
        populateDbWithFakeData(db);

        AllTodos allTodos = new AllTodos(db);

        port(getPortNumber());

        enableCORS();

        get("/todo", (request, response) -> allTodos.getAll(), json());
        after((request, response) -> response.type("application/json"));

        get("/todo/:id",
                (request, response) -> {
                    Optional<TodoEntry> todo = allTodos.getById(Integer.parseInt(request.params(":id")));
                    return todo.orElseThrow(NotFoundException::new);
                },
                JsonTransformer::toJson);

        exception(NotFoundException.class ,(e, request, response) -> {
            response.status(404);
            response.body("Resource not found");
        });

    }
}
