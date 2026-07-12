package com.sbom.hackathon.repository;

import com.sbom.hackathon.entity.TransitiveDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransitiveDependencyRepo
        extends JpaRepository<TransitiveDependency,Long> {
    List<TransitiveDependency> findByApplicationId(String applicationId);
}