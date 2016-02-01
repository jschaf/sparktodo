package todoer;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import todoer.api.TodoEntry;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TodoStepDefinitions {

    private TodoEntry todo;

    @Given("^I insert a todo with title \"(.+)\"")
    public void iHaveaTodoWithTitle(String title) {
        todo = new TodoEntry(2, title, false, 1);
    }

    @Then("^I have (\\d+) todos?")
    public void iHaveATodoAfterInserting(int numTodos) {
        assertThat(1, is(numTodos));
    }

    @Then("^the todo has title \"(.+)\"")
    public void todoHasATitle(String title) {
        assertThat(todo.getTitle(), is(title));
    }
}
