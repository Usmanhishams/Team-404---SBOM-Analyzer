package com.sbom.hackathon.repository;

import com.sbom.hackathon.entity.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DependencyRepo
        extends JpaRepository<Dependency,String> {

}