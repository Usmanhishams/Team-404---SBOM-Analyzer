package com.sbom.hackathon.repository;

import com.sbom.hackathon.entity.TransitiveDependency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransitiveDependencyRepo
        extends JpaRepository<TransitiveDependency,Long> {

}