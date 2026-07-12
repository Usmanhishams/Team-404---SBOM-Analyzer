package com.sbom.hackathon.controller;

import com.sbom.hackathon.repository.*;
import com.sbom.hackathon.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final DataLoaderService applicationLoader;
    private final DependencyLoader dependencyLoader;
    private final VulnerabilityLoader vulnerabilityLoader;
    private final LicenseRuleLoader licenseRuleLoader;
    private final TransitiveDependencyLoader transitiveDependencyLoader;
    private final GraphBuilderService graphBuilderService;
    private final ApplicationRepo applicationRepo;
    private final DependencyRepo dependencyRepo;
    private final VulnerabilityRepo vulnerabilityRepo;
    private final LicenseRuleRepo licenseRuleRepo;
    private final TransitiveDependencyRepo transitiveDependencyRepo;

    public UploadController(
            DataLoaderService applicationLoader,
            DependencyLoader dependencyLoader,
            VulnerabilityLoader vulnerabilityLoader,
            LicenseRuleLoader licenseRuleLoader,
            TransitiveDependencyLoader transitiveDependencyLoader,
            GraphBuilderService graphBuilderService, ApplicationRepo applicationRepo, DependencyRepo dependencyRepo, VulnerabilityRepo vulnerabilityRepo, LicenseRuleRepo licenseRuleRepo, TransitiveDependencyRepo transitiveDependencyRepo) {

        this.applicationLoader = applicationLoader;
        this.dependencyLoader = dependencyLoader;
        this.vulnerabilityLoader = vulnerabilityLoader;
        this.licenseRuleLoader = licenseRuleLoader;
        this.transitiveDependencyLoader = transitiveDependencyLoader;
        this.graphBuilderService = graphBuilderService;
        this.applicationRepo = applicationRepo;
        this.dependencyRepo = dependencyRepo;
        this.vulnerabilityRepo = vulnerabilityRepo;
        this.licenseRuleRepo = licenseRuleRepo;
        this.transitiveDependencyRepo = transitiveDependencyRepo;
    }

    @PostMapping("/upload")
    public String uploadFiles(

            @RequestParam MultipartFile applications,

            @RequestParam MultipartFile dependencies,

            @RequestParam MultipartFile vulnerabilities,

            @RequestParam MultipartFile licenses,

            @RequestParam MultipartFile transitive

    ) throws IOException {

        if (applications.isEmpty() ||
                dependencies.isEmpty() ||
                vulnerabilities.isEmpty() ||
                licenses.isEmpty() ||
                transitive.isEmpty()) {

            return "Please upload all required files.";
        }

        applicationRepo.deleteAll();
        dependencyRepo.deleteAll();
        vulnerabilityRepo.deleteAll();
        licenseRuleRepo.deleteAll();
        transitiveDependencyRepo.deleteAll();

        applicationLoader.loadApplications(
                applications.getInputStream());

        dependencyLoader.loadDependencies(
                dependencies.getInputStream());

        vulnerabilityLoader.load(
                vulnerabilities.getInputStream());

        licenseRuleLoader.load(
                licenses.getInputStream());

        transitiveDependencyLoader.load(
                transitive.getInputStream());

        graphBuilderService.buildGraph();

        return "Files Uploaded Successfully";
    }

}