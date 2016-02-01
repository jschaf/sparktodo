Feature: Todo management
  I can create todos
  I can edit todos
  I can delete todos

  Scenario: Add a todo
    When I insert a todo with title "Tigger"
    Then I have 1 todo
    Then the todo has title "Tigger"
