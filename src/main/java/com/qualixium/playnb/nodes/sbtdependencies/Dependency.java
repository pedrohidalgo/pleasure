package com.qualixium.playnb.nodes.sbtdependencies;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Dependency {

    private static final String SEPARATOR = " : ";

    public final String groupId;
    public final String artifactId;
    public final String version;
    public Optional<Scope> scope;
    public String repositoryId;
    public String packageType;
    public Date timestamp;
    public int versionCount;

    public Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = Optional.empty();
    }

    public Dependency(String groupId, String artifactId, String version, Scope scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = Optional.of(scope);
    }

    public Dependency(String groupId, String artifactId, String version,
            String repositoryId, String packageType, Date timestamp, int versionCount) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = Optional.empty();
        this.repositoryId = repositoryId;
        this.packageType = packageType;
        this.timestamp = timestamp;
        this.versionCount = versionCount;
    }

    @Override
    public String toString() {
        return groupId + SEPARATOR + artifactId + SEPARATOR + version;
    }

    public static Dependency createDependency(JsonNode jsonDocMavenCentral, boolean usingLatestVerion) {
        String groupId = jsonDocMavenCentral.get("g").textValue();
        String artifactId = jsonDocMavenCentral.get("a").textValue();
        String version;
        if (usingLatestVerion) {
            version = jsonDocMavenCentral.get("latestVersion").textValue();
        } else {
            version = jsonDocMavenCentral.get("v").textValue();
        }
        String repositoryId = jsonDocMavenCentral.path("repositoryId").asText();
        String packageType = jsonDocMavenCentral.path("p").asText();
        Date timestamp = new Date(jsonDocMavenCentral.path("timestamp").longValue());
        int versionCount = jsonDocMavenCentral.path("versionCount").asInt();

        return new Dependency(groupId, artifactId, version,
                repositoryId, packageType, timestamp, versionCount);
    }

    public static List<Dependency> getListDependencies(JsonNode jsonDocsMavenCentral) {
        List<Dependency> dependencyDTOs = new ArrayList<>();
        for (JsonNode jsonDoc : jsonDocsMavenCentral) {
            dependencyDTOs.add(createDependency(jsonDoc, true));
        }

        return dependencyDTOs;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public String getPackageType() {
        return packageType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getVersionCount() {
        return versionCount;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.groupId);
        hash = 59 * hash + Objects.hashCode(this.artifactId);
        hash = 59 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dependency other = (Dependency) obj;
        if (!Objects.equals(this.groupId, other.groupId)) {
            return false;
        }
        if (!Objects.equals(this.artifactId, other.artifactId)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    public enum Scope {

        COMPILE, TEST, RUNTIME, PROVIDED;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

        public static Scope getByName(String name) {
            for (Scope value : Scope.values()) {
                if (value.name().toLowerCase().equals(name.toLowerCase())) {
                    return value;
                }
            }

            throw new IllegalArgumentException("No Scope with name: [" + name + "] exists");
        }
    }

}
