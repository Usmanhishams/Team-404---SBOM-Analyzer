package com.sbom.hackathon.service;

import com.sbom.hackathon.dto.TransitiveDependencyDTO;
import com.sbom.hackathon.entity.TransitiveDependency;
import com.sbom.hackathon.parser.TransitiveDependencyParser;
import com.sbom.hackathon.repository.TransitiveDependencyRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.*;

@Service
public class TransitiveDependencyLoader {

    private final TransitiveDependencyParser parser;
    private final TransitiveDependencyRepo repository;
    private final GraphBuilderService graphBuilderService;

    public TransitiveDependencyLoader(
            TransitiveDependencyParser parser,
            TransitiveDependencyRepo repository,
            GraphBuilderService graphBuilderService) {

        this.parser = parser;
        this.repository = repository;
        this.graphBuilderService = graphBuilderService;
    }


    public void load(InputStream input) {

        List<TransitiveDependencyDTO> list = parser.parse(input);
        System.out.println("Parsed DTOs = " + list.size());

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
        graphBuilderService.buildGraph();
    }

}