package ru.rtf.todo.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rtf.todo.jpa.entity.ToDo;
import ru.rtf.todo.jpa.repo.ToDoRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ToDoService {
    ToDoRepository toDoRepository;

    @Transactional
    public ToDo saveToDo(String task){
        if(task == null) {
            log.error("Задача не может быть  null");
            throw new NullPointerException();
        }
        ToDo newToDo = ToDo.builder().task(task).deleted(false).build();
        ToDo save = toDoRepository.save(newToDo);
        log.info("Задача успешно сохранена с id: {}", save.getId());
        return save;
    }

    @Transactional
    public ToDo updateToDo(ToDo toDo){
        if(toDo.getDeleted() == null)
            toDo.setDeleted(false);
        Optional<ToDo> oldToDo = toDoRepository.findByIdAndDeletedIsFalse(toDo.getId());
        if(oldToDo.isPresent()){
            ToDo save = toDoRepository.save(toDo);
            log.info("Задача успешно обновлена с id: {}", save.getId());
            return save;
        }else {
            log.error("Нельзя обновить несуществующую задачу, todo: {}", toDo);
            throw new IllegalArgumentException();
        }


    }

    @Transactional
    public ToDo getToDo(Long todoId){
        Optional<ToDo> toDo = toDoRepository.findByIdAndDeletedIsFalse(todoId);
        if(toDo.isPresent()){
            log.info("Получена задча с id: {}", todoId);
            return toDo.get();
        }
        log.error("Задача с id: {} не найдена", todoId);
        throw new IllegalArgumentException();
    }

    @Transactional
    public List<ToDo> getAllToDos(){
        List<ToDo> toDos = toDoRepository.findAllByDeletedIsFalse();
        if (toDos == null) {
            log.info("Список задач пуст");
            return new ArrayList<>();
        }
        log.info("Всегоа получено {} задач", toDos.size());
        return toDos;
    }

    @Transactional
    public ToDo deleteToDo(Long todoId){
        Optional<ToDo> optionalToDo = toDoRepository.findByIdAndDeletedIsFalse(todoId);
        if(optionalToDo.isPresent()){
            ToDo todo = optionalToDo.get();
            todo.setDeleted(true);
            return todo;
        }
        else {
            throw new IllegalArgumentException();
        }
    }


}
