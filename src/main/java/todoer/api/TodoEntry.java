package todoer.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TodoEntry {
    Integer id;
    String title;
    boolean isComplete;
    int ordering;
}
