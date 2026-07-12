package com.sbom.hackathon.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbom.hackathon.dto.TransitiveDependencyDTO;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class TransitiveDependencyParser {

    public List<TransitiveDependencyDTO> parse() {

        try {

            ObjectMapper mapper = new ObjectMapper();

            InputStream input =
                    getClass().getResourceAsStream("/data/transitive_dependency.json");

            return mapper.readValue(
                    input,
                    new TypeReference<List<TransitiveDependencyDTO>>() {}
            );

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}