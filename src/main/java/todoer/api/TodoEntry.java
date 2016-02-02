package todoer.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@JsonDeserialize(builder = TodoEntry.TodoEntryBuilder.class)
@Builder
@Value
public class TodoEntry {
    @Wither
    private final Integer id;

    @Wither
    private final String title;

    @Wither
    private final boolean isComplete;

    @Wither
    private final int ordering;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TodoEntryBuilder {

    }
}
