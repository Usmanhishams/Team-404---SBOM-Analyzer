package com.sbom.hackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LicenseRuleDTO {

    private String license;

    private String spdx;

    @JsonProperty("risk_level")
    private String riskLevel;

    @JsonProperty("compatible_with_proprietary")
    private Boolean compatibleWithProprietary;

    private Boolean viral;

    private String notes;
}