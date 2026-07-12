package com.sbom.hackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransitiveDependencyDTO {

    @JsonProperty("parent_library")
    private String parentLibrary;

    @JsonProperty("parent_version")
    private String parentVersion;

    @JsonProperty("child_library")
    private String childLibrary;

    @JsonProperty("child_version")
    private String childVersion;

    @JsonProperty("application_id")
    private String applicationId;
}