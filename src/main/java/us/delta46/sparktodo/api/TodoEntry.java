package us.delta46.sparktodo.api;

import lombok.Value;

@Value public class TodoEntry {
    int id;
    String title;
    boolean isComplete;
    int ordering;
}
