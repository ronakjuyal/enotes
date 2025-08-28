package com.rj.Enotes_API_Service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rj.Enotes_API_Service.entity.Notes;

public interface NotesRepository extends JpaRepository<Notes, Integer>{

    List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);
    Page<Notes> findByCreatedByAndIsDeletedFalse(Integer userId, Pageable pagable);
    List<Notes> findAllByIsDeletedAndDeletedOnBefore(boolean b, LocalDateTime cutOffDate);
    

}
