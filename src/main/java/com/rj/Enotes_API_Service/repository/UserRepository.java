package com.rj.Enotes_API_Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rj.Enotes_API_Service.entity.User;

public interface UserRepository extends JpaRepository<User , Integer>{

    Boolean existsByEmail(String email);

}
