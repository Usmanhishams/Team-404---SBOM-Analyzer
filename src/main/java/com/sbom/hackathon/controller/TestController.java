package com.sbom.hackathon.controller;

import com.sbom.hackathon.dto.RiskReportDTO;
import com.sbom.hackathon.service.RiskAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {

    private final RiskAnalysisService service;

    public TestController(RiskAnalysisService service) {
        this.service = service;
    }

    @GetMapping("/test/{appId}")
    public RiskReportDTO test(@PathVariable String appId) {
        return service.analyzeApplication(appId);
    }

    @GetMapping("/analyze/all")
    public List<RiskReportDTO> analyzeAllApplications() {
        return service.analyzeAllApplications();
    }

}