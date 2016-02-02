package todoer.api;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
public class TodoEntry {
    @Wither
    private final Integer id;

    @Wither
    private final String title;

    @Wither
    private final boolean isComplete;

    @Wither
    private final int ordering;
}
