package com.sbom.hackathon.service;

import com.sbom.hackathon.dto.LicenseRuleDTO;
import com.sbom.hackathon.entity.LicenseRule;
import com.sbom.hackathon.parser.LicenseRuleParser;
import com.sbom.hackathon.repository.LicenseRuleRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenseRuleLoader {

    private final LicenseRuleParser parser;
    private final LicenseRuleRepo repository;

    public LicenseRuleLoader(LicenseRuleParser parser,
                             LicenseRuleRepo repository) {

        this.parser = parser;
        this.repository = repository;
    }

    @PostConstruct
    public void load() {

        List<LicenseRuleDTO> list = parser.parse();

        for (LicenseRuleDTO dto : list) {

            LicenseRule rule = new LicenseRule(

                    dto.getLicense(),
                    dto.getSpdx(),
                    dto.getRiskLevel(),
                    dto.getCompatibleWithProprietary(),
                    dto.getViral(),
                    dto.getNotes()

            );

            repository.save(rule);

        }


        System.out.println("--------------------------------");

    }

}