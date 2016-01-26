package us.delta46.sparktodo.repos;

import org.jooq.DSLContext;
//import us.delta46.sparktodo.api.Todo;
import us.delta46.sparktodo.api.Todo;
import us.delta46.sparktodo.models.tables.records.TodoRecord;

import static us.delta46.sparktodo.models.Tables.*;
import java.util.List;

public class AllTodos {

    private final DSLContext db;

    public AllTodos(DSLContext db) {
        this.db = db;
    }


    public List<Todo> list() {
        List<Todo> todos = db.selectFrom(TODO).fetchInto(Todo.class);
        return todos;
    }
}
