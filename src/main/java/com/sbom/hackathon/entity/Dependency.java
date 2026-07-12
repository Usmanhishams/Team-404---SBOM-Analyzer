package com.sbom.hackathon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dependency {

    @Id
    private String depId;

    private String applicationId;

    private String applicationName;

    private String library;

    private String version;

    private String license;

    private String dependencyType;

    private String lastUpdated;

    private String transitiveDeps;
}