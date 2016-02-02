package todoer;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todoer.db.Database;
import todoer.handlers.TodoHandler;
import todoer.repos.AllTodos;
import todoer.transformers.JsonTransformer;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

import static spark.Spark.*;
import static todoer.models.tables.Todo.TODO;

class TodoApp {

    private static final int DEFAULT_PORT = 4567;

    private static final Logger LOG = LoggerFactory.getLogger(TodoApp.class);

    private static int getPortNumber() {
        Integer port = Optional.ofNullable(System.getenv("PORT"))
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
        LOG.info("Port set to " + port);
        return port;
    }

    private static void enableCrossOriginResourceSharing() {
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

    private static void populateDbWithFakeData(DSLContext db) {
        db.insertInto(TODO, TODO.ID, TODO.TITLE, TODO.COMPLETE, TODO.ORDERING)
                .values(1, "Note 1", true, 4)
                .values(2, "Note 2", false, 3)
                .values(3, "Note 3", true, 100)
                .onDuplicateKeyIgnore()
                .execute();
    }

    public static void main(String[] args) {

        LOG.info("Running migrations");
        Database.runFlywayMigrations();

        DSLContext db = Database.buildDSLContext();
        populateDbWithFakeData(db);

        AllTodos allTodos = new AllTodos(db);

        TodoHandler todoHandler = new TodoHandler(allTodos);

        port(getPortNumber());

        enableCrossOriginResourceSharing();

        after((request, response) -> response.type("application/json"));

        get("/", todoHandler.GET_INDEX, JsonTransformer::toJson);
        get("/:id", todoHandler.GET_DETAIL, JsonTransformer::toJson);
        post("/", todoHandler.POST, JsonTransformer::toJson);
        delete("/", todoHandler.DELETE);

        exception(NotFoundException.class ,(e, request, response) -> {
            response.status(404);
            response.body("Resource not found");
        });

    }
}
