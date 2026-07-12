package com.sbom.hackathon.service;

import com.sbom.hackathon.dto.TransitiveDependencyDTO;
import com.sbom.hackathon.entity.TransitiveDependency;
import com.sbom.hackathon.parser.TransitiveDependencyParser;
import com.sbom.hackathon.repository.TransitiveDependencyRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransitiveDependencyLoader {

    private final TransitiveDependencyParser parser;
    private final TransitiveDependencyRepo repository;

    public TransitiveDependencyLoader(
            TransitiveDependencyParser parser,
            TransitiveDependencyRepo repository) {

        this.parser = parser;
        this.repository = repository;
    }

    @PostConstruct
    public void load() {

        List<TransitiveDependencyDTO> list = parser.parse();

        for (TransitiveDependencyDTO dto : list) {

            repository.save(

                    new TransitiveDependency(

                            null,
                            dto.getParentLibrary(),
                            dto.getParentVersion(),
                            dto.getChildLibrary(),
                            dto.getChildVersion(),
                            dto.getApplicationId()

                    )

            );

        }

        System.out.println("--------------------------------");
        System.out.println("Transitive Dependencies Loaded : "
                + repository.count());
        System.out.println("--------------------------------");

    }

}