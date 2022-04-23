package ru.rtf.todo.jpa.repo;

import org.springframework.data.repository.CrudRepository;
import ru.rtf.todo.jpa.entity.ToDo;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    Optional<ToDo> findByIdAndDeletedIsFalse(Long id);
    List<ToDo> findAllByDeletedIsFalse();
}
