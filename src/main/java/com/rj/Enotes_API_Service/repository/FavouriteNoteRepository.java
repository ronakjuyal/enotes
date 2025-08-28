package com.rj.Enotes_API_Service.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.rj.Enotes_API_Service.entity.FavouriteNote;

public interface FavouriteNoteRepository extends JpaRepository<FavouriteNote,Integer>{

    List<FavouriteNote> findByUserId(int userId);

}
