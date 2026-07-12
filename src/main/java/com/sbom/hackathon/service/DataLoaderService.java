package com.sbom.hackathon.service;

import com.sbom.hackathon.dto.ApplicationDTO;
import com.sbom.hackathon.entity.Application;
import com.sbom.hackathon.parser.ApplicationParser;
import com.sbom.hackathon.repository.ApplicationRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class DataLoaderService {

    private final ApplicationParser parser;
    private final ApplicationRepo repository;

    public DataLoaderService(ApplicationParser parser,
                             ApplicationRepo repository) {
        this.parser = parser;
        this.repository = repository;
    }


    public void loadApplications(InputStream input) {

        List<ApplicationDTO> applications = parser.parseApplications(input);

        for (ApplicationDTO dto : applications) {

            Application app = new Application(
                    null,
                    dto.getAppId(),
                    dto.getName(),
                    dto.getLanguage(),
                    dto.getCriticality(),
                    dto.getLicenseModel(),
                    dto.getBusinessOwner(),
                    dto.getDepartment(),
                    dto.getDeployment()
            );

            repository.save(app);
        }


    }
}