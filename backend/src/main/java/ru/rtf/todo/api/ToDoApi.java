package ru.rtf.todo.api;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rtf.todo.jpa.entity.ToDo;
import ru.rtf.todo.service.ToDoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ToDoApi {
    ToDoService toDoService;

    @PostMapping()
    public ResponseEntity<ToDo> saveToDo(@RequestBody String  toDoDto){
        try {
            ToDo toDo = toDoService.saveToDo(toDoDto);
            return new ResponseEntity<>(toDo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<ToDo> updateToDo(@RequestBody ToDo toDo){
        try {

            ToDo newToDo = toDoService.updateToDo(toDo);
            return new ResponseEntity<>(newToDo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{todoId}")
    public ResponseEntity<ToDo> getToDo(@PathVariable Long todoId){
        try {
            ToDo newToDo = toDoService.getToDo(todoId);
            return new ResponseEntity<>(newToDo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public List<ToDo> getAllToDos(){
        return toDoService.getAllToDos();
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable Long todoId){
        try {
            ToDo newToDo = toDoService.deleteToDo(todoId);
            return new ResponseEntity<>(newToDo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
