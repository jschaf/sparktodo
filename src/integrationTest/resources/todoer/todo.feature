Feature: Todo management
  I can create todos
  I can delete todos
  I can edit todos

  Scenario: Add a todo
    When I insert a todo with title "Tigger"
    Then I have 1 todo
    Then the todo has title "Tigger"

  Scenario: Delete all todos
    When I delete the root node
    Then I have 0 todos
