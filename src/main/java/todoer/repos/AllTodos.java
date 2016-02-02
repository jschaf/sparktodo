package todoer.repos;

import org.jooq.DSLContext;
import static todoer.models.Tables.*;
import todoer.api.TodoEntry;
import todoer.models.tables.records.TodoRecord;

import java.util.List;
import java.util.Optional;

public class AllTodos {

    private final DSLContext db;

    public AllTodos(DSLContext db) {
        this.db = db;
    }

    public Optional<TodoEntry> getById(int id) {
        return Optional.ofNullable(db.selectFrom(TODO)
                .where(TODO.ID.eq(id))
                .fetchOneInto(TodoEntry.class));
    }

    public List<TodoEntry> getAll() {
        return db.selectFrom(TODO).fetchInto(TodoEntry.class);
    }

    public TodoEntry save(TodoEntry todo) {
        TodoRecord todoRecord = db.newRecord(TODO, todo);
        todoRecord.store();
        return db.selectFrom(TODO)
                .where(TODO.ID.eq(todoRecord.getId()))
                .fetchOneInto(TodoEntry.class);
    }

    public void deleteAll() {
        db.truncate(TODO).execute();
    }
}
