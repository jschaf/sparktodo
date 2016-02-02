
ALTER TABLE todo_backend.todo
    RENAME COLUMN is_complete TO complete;

ALTER TABLE todo_backend.todo
    ADD COLUMN url TEXT;
