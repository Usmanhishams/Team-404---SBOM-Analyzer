package com.sbom.hackathon.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbom.hackathon.dto.LicenseRuleDTO;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class LicenseRuleParser {

    public List<LicenseRuleDTO> parse() {

        try {

            ObjectMapper mapper = new ObjectMapper();

            InputStream input =
                    getClass().getResourceAsStream("/data/license_rules.json");

            return mapper.readValue(
                    input,
                    new TypeReference<List<LicenseRuleDTO>>() {}
            );

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}