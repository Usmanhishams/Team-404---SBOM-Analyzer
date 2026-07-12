package com.sbom.hackathon.service;

import com.sbom.hackathon.entity.TransitiveDependency;
import com.sbom.hackathon.repository.TransitiveDependencyRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphBuilderService {

    private final TransitiveDependencyRepo repository;
    private final Map<String, Map<String, List<String>>> graph = new HashMap<>();

    public GraphBuilderService(TransitiveDependencyRepo repository) {
        this.repository = repository;
    }

    public void buildGraph() {

        graph.clear();
        List<TransitiveDependency> edges = repository.findAll();

        for (TransitiveDependency edge : edges) {

            graph
                    .computeIfAbsent(
                            edge.getApplicationId(),
                            k -> new HashMap<>()
                    )

                    .computeIfAbsent(
                            edge.getParentLibrary(),
                            k -> new ArrayList<>()
                    )

                    .add(edge.getChildLibrary());

        }

    }

    public List<String> getChildren(String appId,
                                    String library) {

        Map<String, List<String>> appGraph =
                graph.getOrDefault(appId,
                        Collections.emptyMap());

        return appGraph.getOrDefault(
                library,
                Collections.emptyList()
        );

    }

    public Set<String> getAllReachableLibraries(
            String appId,
            String library) {

        Set<String> visited = new HashSet<>();

        dfs(appId, library, visited);

        return visited;

    }

    private void dfs(String appId,
                     String current,
                     Set<String> visited) {

        if (visited.contains(current))
            return;

        visited.add(current);

        Map<String, List<String>> appGraph =
                graph.getOrDefault(appId,
                        Collections.emptyMap());

        List<String> children =
                appGraph.getOrDefault(
                        current,
                        Collections.emptyList());

        for (String child : children) {

            dfs(appId, child, visited);

        }

    }

    public Map<String, Map<String, List<String>>> getGraph() {
        return graph;
    }

}