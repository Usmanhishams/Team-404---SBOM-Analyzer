package com.sbom.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskReportDTO {

    private String applicationId;

    private String applicationName;

    private int riskScore;

    private String riskLevel;

    private int vulnerableLibraries;

    private int licenseIssues;

    private int outdatedLibraries;

    private int transitiveIssues;

    private List<String> issues = new ArrayList<>();

    private List<String> recommendations = new ArrayList<>();
}