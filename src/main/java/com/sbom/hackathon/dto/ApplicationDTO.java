package com.sbom.hackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {

    @JsonProperty("app_id")
    private String appId;

    private String name;

    private String language;

    private String criticality;

    @JsonProperty("license_model")
    private String licenseModel;

    @JsonProperty("business_owner")
    private String businessOwner;

    private String department;

    private String deployment;

}