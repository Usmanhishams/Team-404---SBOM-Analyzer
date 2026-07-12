package com.sbom.hackathon.repository;

import com.sbom.hackathon.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {

}