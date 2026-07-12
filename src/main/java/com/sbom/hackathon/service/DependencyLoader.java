package com.sbom.hackathon.service;

import com.sbom.hackathon.dto.DependencyDTO;
import com.sbom.hackathon.entity.Dependency;
import com.sbom.hackathon.parser.DependencyParser;
import com.sbom.hackathon.repository.DependencyRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependencyLoader {

    private final DependencyParser parser;
    private final DependencyRepo repository;

    public DependencyLoader(DependencyParser parser,
                                   DependencyRepo repository) {

        this.parser = parser;
        this.repository = repository;

    }

    @PostConstruct
    public void loadDependencies(){

        List<DependencyDTO> dependencies =
                parser.parseDependencies();

        for(DependencyDTO dto : dependencies){

            Dependency dependency = new Dependency(

                    dto.getDepId(),
                    dto.getApplicationId(),
                    dto.getApplicationName(),
                    dto.getLibrary(),
                    dto.getVersion(),
                    dto.getLicense(),
                    dto.getDependencyType(),
                    dto.getLastUpdated(),
                    dto.getTransitiveDeps()

            );

            repository.save(dependency);

        }

        System.out.println("--------------------------------");
        System.out.println("Dependencies Loaded : "
                + repository.count());
        System.out.println("--------------------------------");

    }

}