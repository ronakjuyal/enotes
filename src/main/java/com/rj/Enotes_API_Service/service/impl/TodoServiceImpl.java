package com.rj.Enotes_API_Service.service.impl;

import java.util.List;

import org.apache.logging.log4j.status.StatusData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.rj.Enotes_API_Service.dto.TodoDto;
import com.rj.Enotes_API_Service.dto.TodoDto.StatusDto;
import com.rj.Enotes_API_Service.entity.Todo;
import com.rj.Enotes_API_Service.enums.TodoStatus;
import com.rj.Enotes_API_Service.exception.ResourceNotFoundException;
import com.rj.Enotes_API_Service.repository.TodoRepository;
import com.rj.Enotes_API_Service.service.TodoService;
import com.rj.Enotes_API_Service.util.Validation;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Validation validation;

    @Override
    public Boolean saveTodo(TodoDto todoDto) throws Exception {

        // validate todo status
        validation.todoValidation(todoDto);
        Todo todo = mapper.map(todoDto, Todo.class);
        todo.setStatusId(todoDto.getStatus().getId());

        Todo saveTodo = todoRepository.save(todo);

        if (!ObjectUtils.isEmpty(saveTodo)) {
            return true;
        }
        return false;
    }

    @Override
    public TodoDto getTodoById(Integer id) throws Exception {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found ! ID invalid"));
        TodoDto todoDto = mapper.map(todo, TodoDto.class);
        setStaus(todoDto, todo);
        return todoDto;

    }

    private void setStaus(TodoDto todoDto, Todo todo) {
       
        for( TodoStatus st: TodoStatus.values()){

            if (st.getId().equals(todo.getStatusId())) {
                StatusDto statusDto=StatusDto.builder()
                .id(st.getId())
                .name(st.getName())
                .build();

                todoDto.setStatus(statusDto);
            }
        }
    }

    @Override
    public List<TodoDto> getTodoByUser() {

        Integer userId = 1;
        List<Todo> todos = todoRepository.findByCreatedBy(userId);
        return todos.stream().map(td -> mapper.map(td, TodoDto.class)).toList();
    }

}
