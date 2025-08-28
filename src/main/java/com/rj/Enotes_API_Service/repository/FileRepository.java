package com.rj.Enotes_API_Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rj.Enotes_API_Service.entity.FileDetails;

public interface FileRepository extends JpaRepository<FileDetails,Integer> {

}
