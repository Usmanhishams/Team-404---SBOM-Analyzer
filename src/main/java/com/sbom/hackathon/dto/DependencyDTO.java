package com.sbom.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DependencyDTO {

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