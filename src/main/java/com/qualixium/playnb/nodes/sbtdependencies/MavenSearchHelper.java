package com.qualixium.playnb.nodes.sbtdependencies;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.HttpUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MavenSearchHelper {

    private static final String endPoint = "http://search.maven.org/solrsearch/select";
    private static final short rows = 40;
    private static final String returnType = "json";

    private static final ObjectMapper mapper = new ObjectMapper();

    private static String getCommonParametersPart(String query) {
        return "?q=" + query + "*&rows=" + rows + "&wt=" + returnType;
    }

    private static String getTargetURL(String query) {
        return endPoint + query;
    }

    //if a want to get all versions from an artifact I can get it using: http://search.maven.org/solrsearch/select?q=g:”com.google.inject
//”+AND+a:”guice”&rows=20&wt=json
    public static List<Dependency> searchByQuery(String query) {
        List<Dependency> dependencies = new ArrayList<>();
        try {
            String commonParametersPart = getCommonParametersPart(query);
            String targetURL = getTargetURL(commonParametersPart);

            Optional<String> responseOptional = HttpUtil.executeGetRequest(targetURL);
            if (responseOptional.isPresent()) {
                String jsonString = responseOptional.get();
                JsonNode jsonNode = mapper.readTree(jsonString);
                JsonNode docsNode = jsonNode.get("response").get("docs");

                dependencies.addAll(Dependency.getListDependencies(docsNode));
            }
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }

        return dependencies;
    }

    public static Optional<Dependency> searchDependencyDetail(Dependency dep) {
        try {
            String query = "?q=g:\"" + dep.groupId + "\"+AND+a:\"" + dep.artifactId + "\"+AND+v:\"" + dep.version + "\"&wt=" + returnType;
            String targetURL = getTargetURL(query);

            Optional<String> responseOptional = HttpUtil.executeGetRequest(targetURL);
            if (responseOptional.isPresent()) {
                String jsonString = responseOptional.get();
                JsonNode jsonNode = mapper.readTree(jsonString);
                JsonNode jsonResponse = jsonNode.get("response");
                int numFound = jsonResponse.get("numFound").asInt();
                if (numFound > 0) {
                    JsonNode docsNode = jsonNode.get("response").get("docs");
                    return Optional.of(Dependency.createDependency(docsNode.get(0), false));
                }
            }
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }

        return Optional.empty();
    }

}
