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
public class LicenseRule {

    @Id
    private String license;

    private String spdx;

    private String riskLevel;

    private Boolean compatibleWithProprietary;

    private Boolean viral;

    private String notes;
}