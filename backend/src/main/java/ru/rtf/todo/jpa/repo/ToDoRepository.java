package ru.rtf.todo.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rtf.todo.jpa.entity.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}
