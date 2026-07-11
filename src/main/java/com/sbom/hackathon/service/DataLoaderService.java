package com.sbom.hackathon.service;

import com.sbom.hackathon.dto.ApplicationDTO;
import com.sbom.hackathon.entity.Application;
import com.sbom.hackathon.parser.ApplicationParser;
import com.sbom.hackathon.repository.ApplicationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoaderService {

    private final ApplicationParser parser;
    private final ApplicationRepository repository;

    public DataLoaderService(ApplicationParser parser,
                             ApplicationRepository repository) {
        this.parser = parser;
        this.repository = repository;
    }

    @PostConstruct
    public void loadApplications() {

        List<ApplicationDTO> applications = parser.parseApplications();

        for (ApplicationDTO dto : applications) {

            Application app = new Application(
                    dto.getId(),
                    dto.getName(),
                    dto.getOwner(),
                    dto.getCriticality()
            );

            repository.save(app);
        }

        System.out.println("--------------------------------");
        System.out.println("Applications Loaded : " + repository.count());
        System.out.println("--------------------------------");
    }
}