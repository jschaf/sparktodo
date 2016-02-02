package todoer.handlers;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import spark.Route;
import todoer.api.TodoEntry;
import todoer.repos.AllTodos;

import javax.ws.rs.NotFoundException;

@Slf4j
public final class TodoHandler {

    private final AllTodos allTodos;
    private final ObjectMapper MAPPER = new ObjectMapper();

    public TodoHandler(AllTodos allTodos) {
        this.allTodos = allTodos;
    }

    public final Route GET_INDEX = (request, response) -> TodoHandler.this.allTodos.getAll();

    public final Route GET_DETAIL = (request, response) -> {
        int id = Integer.parseInt(request.params(":id"));
        return TodoHandler.this.allTodos.getById(id).orElseThrow(NotFoundException::new);
    };

    public final Route POST = (request, response) -> {
        try {
            log.info("POST with " + request.body());
            TodoEntry todo = MAPPER.readValue(request.body(), TodoEntry.class).withId(null);
            TodoEntry savedTodo = TodoHandler.this.allTodos.save(todo);
            response.status(HttpStatus.SC_OK);
            return savedTodo;
        } catch (JsonMappingException | JsonParseException e) {
            response.status(HttpStatus.SC_BAD_REQUEST);
            return "Error: " + e.getMessage();
        }
    };

    public final Route PUT = (request, response) -> {
        return null;
    };

    public final Route DELETE = (request, response) -> {
        TodoHandler.this.allTodos.deleteAll();
        return "";
    };
}
