package com.sbom.hackathon.parser;

import com.opencsv.CSVReader;
import com.sbom.hackathon.dto.DependencyDTO;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class DependencyParser {

    public List<DependencyDTO> parseDependencies(InputStream inputStream) {

        List<DependencyDTO> dependencies = new ArrayList<>();

        try {


            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

            String[] line;

            reader.readNext();      // Skip Header

            while((line = reader.readNext()) != null){

                DependencyDTO dto = new DependencyDTO();

                dto.setDepId(line[0]);
                dto.setApplicationId(line[1]);
                dto.setApplicationName(line[2]);
                dto.setLibrary(line[3]);
                dto.setVersion(line[4]);
                dto.setLicense(line[5]);
                dto.setDependencyType(line[6]);
                dto.setLastUpdated(line[7]);
                dto.setTransitiveDeps(line[8]);

                dependencies.add(dto);

            }

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return dependencies;

    }

}