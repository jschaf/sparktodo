DROP SCHEMA IF EXISTS TODO_BACKEND CASCADE;

CREATE SCHEMA TODO_BACKEND;

CREATE TABLE TODO_BACKEND.todo (
  id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  is_complete BOOLEAN,
  ordering INT
);