package com.sbom.hackathon.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbom.hackathon.dto.ApplicationDTO;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class ApplicationParser {

    public List<ApplicationDTO> parseApplications() {

        try {

            ObjectMapper mapper = new ObjectMapper();

            InputStream inputStream =
                    getClass().getResourceAsStream("/data/applications.json");

            if (inputStream == null) {
                throw new RuntimeException("applications.json not found");
            }

            return Arrays.asList(
                    mapper.readValue (inputStream, ApplicationDTO[].class)
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}