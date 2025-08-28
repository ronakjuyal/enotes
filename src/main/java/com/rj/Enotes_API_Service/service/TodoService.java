package com.rj.Enotes_API_Service.service;

import java.util.List;

import com.rj.Enotes_API_Service.dto.TodoDto;
import com.rj.Enotes_API_Service.entity.Todo;

public interface TodoService {

    public Boolean saveTodo(TodoDto todo)throws Exception;

    public TodoDto getTodoById(Integer id) throws Exception;

    public List<TodoDto>getTodoByUser();


}
