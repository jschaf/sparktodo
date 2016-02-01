package todoer;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import todoer.api.TodoEntry;
import todoer.db.Database;
import todoer.repos.AllTodos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TodoSteps {

    private TodoEntry storedTodo;

    private static final AllTodos allTodos;

    static {
        Flyway flyway = new Flyway();
        flyway.setDataSource(Database.buildDataSource());
        flyway.clean();
        flyway.migrate();

        DSLContext db = Database.buildDSLContext();
        allTodos = new AllTodos(db);
    }

    @Given("^I insert a todo with title \"(.+)\"")
    public void iInsertATodoWithTitle(String title) {
        TodoEntry todo = TodoEntry.builder().title(title).build();
        storedTodo = allTodos.put(todo);
    }

    @Then("^I have (\\d+) todos?")
    public void iHaveATodoAfterInserting(int numTodos) {
        assertThat(allTodos.getAll().size(), is(numTodos));
    }

    @Then("^the todo has title \"(.+)\"")
    public void todoHasATitle(String title) {
        assertThat(storedTodo.getTitle(), is(title));
    }
}
