package com.sbom.hackathon.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbom.hackathon.dto.LicenseRuleDTO;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class LicenseRuleParser {

    public List<LicenseRuleDTO> parse(InputStream input){

        try {

            ObjectMapper mapper = new ObjectMapper();


            return mapper.readValue(
                    input,
                    new TypeReference<List<LicenseRuleDTO>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}