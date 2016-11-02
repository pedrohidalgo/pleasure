package com.qualixium.playnb.integration;

import com.qualixium.playnb.nodes.sbtdependencies.Dependency;
import com.qualixium.playnb.nodes.sbtdependencies.MavenSearchHelper;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MavenSearchHelperTest {

    @Test
    public void testSearchByQuery() {
        String query = "postg";
        List<Dependency> dependenciesOptional = MavenSearchHelper.searchByQuery(query);

        assertFalse(dependenciesOptional.isEmpty());
    }

    @Test
    public void testSearchByQueryDependencyDetail() {
        String groupId = "org.postgresql";
        String artifactId = "postgresql";
        String version = "9.3-1102-jdbc41";
        Dependency dependencyWithOutDetail = new Dependency(groupId, artifactId, version);
        Dependency dependencyReturned = MavenSearchHelper.searchDependencyDetail(dependencyWithOutDetail).get();

        assertTrue(dependencyReturned.groupId.equals(dependencyWithOutDetail.groupId));
        assertTrue(dependencyReturned.artifactId.equals(dependencyWithOutDetail.artifactId));
        assertTrue(dependencyReturned.version.equals(dependencyWithOutDetail.version));
        assertTrue(dependencyReturned.packageType.equals("jar"));
        assertTrue(dependencyReturned.timestamp.getTime() == 1405694357000l);
        assertNotNull(dependencyReturned.repositoryId);
        assertTrue(dependencyReturned.versionCount == 0);
    }

}
