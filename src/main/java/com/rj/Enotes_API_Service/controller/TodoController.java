package com.rj.Enotes_API_Service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rj.Enotes_API_Service.dto.TodoDto;
import com.rj.Enotes_API_Service.entity.Todo;
import com.rj.Enotes_API_Service.service.TodoService;
import com.rj.Enotes_API_Service.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping("/")
    private ResponseEntity<?>saveTodo(@RequestBody TodoDto todo)throws Exception{

        Boolean saevTodo=todoService.saveTodo(todo);

        if (saevTodo) {
            return CommonUtil.createBuildResponseMessage("saved successfully", HttpStatus.CREATED);
        }else{
            return CommonUtil.createErrorResponseMessage("Todo not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<?>getTodoById(@PathVariable Integer id)throws Exception{

        TodoDto todoById=todoService.getTodoById(id);

        return CommonUtil.createBuildResponse(todoById, HttpStatus.OK);
    }

    
    @GetMapping("/list")
    private ResponseEntity<?>getAllTodoByUser()throws Exception{


        List<TodoDto> todoList=todoService.getTodoByUser();
        if (CollectionUtils.isEmpty(todoList)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(todoList, HttpStatus.OK);
    }

}
