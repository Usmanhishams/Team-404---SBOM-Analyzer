package com.sbom.hackathon.service;

import com.sbom.hackathon.dto.RiskReportDTO;
import com.sbom.hackathon.entity.Application;
import com.sbom.hackathon.entity.Dependency;
import com.sbom.hackathon.entity.LicenseRule;
import com.sbom.hackathon.entity.Vulnerability;
import com.sbom.hackathon.repository.ApplicationRepo;
import com.sbom.hackathon.repository.DependencyRepo;
import com.sbom.hackathon.repository.LicenseRuleRepo;
import com.sbom.hackathon.repository.TransitiveDependencyRepo;
import com.sbom.hackathon.repository.VulnerabilityRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.time.LocalDate;

@Service
public class RiskAnalysisService {

    private final ApplicationRepo applicationRepo;
    private final DependencyRepo dependencyRepo;
    private final VulnerabilityRepo vulnerabilityRepo;
    private final LicenseRuleRepo licenseRuleRepo;
    private final TransitiveDependencyRepo transitiveDependencyRepo;
    private final GraphBuilderService graphBuilderService;

    public RiskAnalysisService(
            ApplicationRepo applicationRepo,
            DependencyRepo dependencyRepo,
            VulnerabilityRepo vulnerabilityRepo,
            LicenseRuleRepo licenseRuleRepo,
            TransitiveDependencyRepo transitiveDependencyRepo,
            GraphBuilderService graphBuilderService) {

        this.applicationRepo = applicationRepo;
        this.dependencyRepo = dependencyRepo;
        this.vulnerabilityRepo = vulnerabilityRepo;
        this.licenseRuleRepo = licenseRuleRepo;
        this.graphBuilderService = graphBuilderService;
        this.transitiveDependencyRepo = transitiveDependencyRepo;
    }

    public RiskReportDTO analyzeApplication(String appId) {

        Application app = applicationRepo
                .findAll()
                .stream()
                .filter(a -> a.getAppId().equals(appId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Application not found"));

        List<Dependency> dependencies =
                dependencyRepo.findByApplicationId(appId);

        RiskReportDTO report = new RiskReportDTO();

        report.setApplicationId(appId);
        report.setApplicationName(app.getName());

        Set<String> processedLicenses = new HashSet<>();

        for (Dependency dependency : dependencies) {


            Set<String> libraries =
                    graphBuilderService
                            .getAllReachableLibraries(
                                    appId,
                                    dependency.getLibrary());


            for (String library : libraries) {

                List<Vulnerability> vulnerabilities =
                        vulnerabilityRepo.findByLibrary(library);

                for (Vulnerability vulnerability : vulnerabilities) {

                    if (vulnerability.getAffectedVersions()
                            .contains(dependency.getVersion())) {

                        if (!library.equals(dependency.getLibrary())) {

                            report.setTransitiveIssues(
                                    report.getTransitiveIssues() + 1);

                        }

                        double cvss = vulnerability.getCvssScore();

                        if (cvss >= 9) {
                            report.setRiskScore(report.getRiskScore() + 25);
                        }
                        else if (cvss >= 7) {
                            report.setRiskScore(report.getRiskScore() + 20);
                        }
                        else if (cvss >= 4) {
                            report.setRiskScore(report.getRiskScore() + 10);
                        }
                        else {
                            report.setRiskScore(report.getRiskScore() + 5);
                        }

                        report.setVulnerableLibraries(
                                report.getVulnerableLibraries() + 1);

                        report.getIssues().add(
                                vulnerability.getCveId()
                                        + " found in "
                                        + library
                        );

                        if (Boolean.TRUE.equals(vulnerability.getPatchAvailable())) {

                            report.getRecommendations().add(
                                    "Upgrade "
                                            + library
                                            + " to "
                                            + vulnerability.getFixedVersion());

                        } else {

                            report.getRecommendations().add(
                                    "No patch available for "
                                            + library);

                        }

                    }

                }

            }
            Optional<LicenseRule> optionalRule =
                    licenseRuleRepo.findByLicense(
                            dependency.getLicense());

            if (optionalRule.isPresent()) {

                LicenseRule rule = optionalRule.get();

                if (app.getLicenseModel().equalsIgnoreCase("proprietary")
                        && Boolean.FALSE.equals(rule.getCompatibleWithProprietary())
                        && !processedLicenses.contains(dependency.getLibrary())) {

                    processedLicenses.add(dependency.getLibrary());

                    switch (rule.getRiskLevel().toUpperCase()) {
                        case "CRITICAL":
                            report.setRiskScore(report.getRiskScore() + 25);
                            break;
                        case "HIGH":
                            report.setRiskScore(report.getRiskScore() + 20);
                            break;
                        case "MEDIUM":
                            report.setRiskScore(report.getRiskScore() + 10);
                            break;
                        default:
                            report.setRiskScore(report.getRiskScore() + 5);
                    }

                    report.setLicenseIssues(
                            report.getLicenseIssues() + 1);

                    report.getIssues().add(
                            dependency.getLibrary()
                                    + " uses "
                                    + dependency.getLicense()
                                    + " license which is incompatible with proprietary software."
                    );

                    report.getRecommendations().add(
                            "Replace "
                                    + dependency.getLibrary()
                                    + " with a library having a compatible license."
                    );
                }
            }
            try {
                LocalDate lastUpdated =
                        LocalDate.parse(dependency.getLastUpdated());

                if (lastUpdated.isBefore(LocalDate.now().minusYears(2))) {
                    report.setOutdatedLibraries(
                            report.getOutdatedLibraries() + 1);

                    report.setRiskScore(
                            report.getRiskScore() + 5);

                    report.getIssues().add(
                            dependency.getLibrary()
                                    + " has not been updated for over 2 years."
                    );
                    report.getRecommendations().add(
                            "Upgrade "
                                    + dependency.getLibrary()
                                    + " to the latest stable version."
                    );
                }
            } catch (Exception e) {
                // Ignore invalid or missing dates
            }

        }

        if (report.getRiskScore() >= 75) {
            report.setRiskLevel("CRITICAL");
        }
        else if (report.getRiskScore() >= 50) {
            report.setRiskLevel("HIGH");
        }
        else if (report.getRiskScore() >= 25) {
            report.setRiskLevel("MEDIUM");
        }
        else {
            report.setRiskLevel("LOW");
        }

        return report;

    }
    public List<RiskReportDTO> analyzeAllApplications() {

        List<RiskReportDTO> reports = new ArrayList<>();

        List<Application> applications = applicationRepo.findAll();

        for (Application app : applications) {

            reports.add(
                    analyzeApplication(app.getAppId())
            );

        }

        reports.sort(
                Comparator.comparingInt(RiskReportDTO::getRiskScore)
                        .reversed());

        return reports;
    }

}