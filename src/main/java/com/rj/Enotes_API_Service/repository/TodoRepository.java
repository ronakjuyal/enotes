package com.rj.Enotes_API_Service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rj.Enotes_API_Service.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

    List<Todo> findByCreatedBy(Integer userId);

}
