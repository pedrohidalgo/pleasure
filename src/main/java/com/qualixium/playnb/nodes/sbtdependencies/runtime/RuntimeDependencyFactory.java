package com.qualixium.playnb.nodes.sbtdependencies.runtime;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.nodes.sbtdependencies.BuildSBTManager;
import com.qualixium.playnb.nodes.sbtdependencies.Dependency;
import com.qualixium.playnb.util.ExceptionManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

public class RuntimeDependencyFactory extends ChildFactory<RuntimeDependency> {

    private final PlayProject playProject;

    public RuntimeDependencyFactory(PlayProject playProject) {
        this.playProject = playProject;
    }

    @Override
    protected boolean createKeys(List<RuntimeDependency> toPopulate) {

        try {
            String buildSBTFileContent = playProject.getProjectDirectory().getFileObject("build.sbt").asText();
            List<Dependency> libraryDependencies = BuildSBTManager.getLibraryDependencies(buildSBTFileContent);
            
            ClassPath classPathCompile = ClassPath.getClassPath(playProject.getProjectDirectory()
                    .getFileObject("app"), ClassPath.COMPILE);
            List<RuntimeDependency> listRuntimeDependencies = Arrays.asList(classPathCompile.getRoots()).stream()
                    .map(fileObject -> new RuntimeDependency(fileObject.toURL().getPath()))
                    .filter(rtDep -> !libraryDependencies.stream().anyMatch(dep -> rtDep.path.contains(dep.artifactId)))
                    .sorted((rt1, rt2) -> rt1.path.compareTo(rt2.path))
                    .collect(Collectors.toList());

            toPopulate.addAll(listRuntimeDependencies);
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(RuntimeDependency key) {
        RuntimeDependencyNode node = new RuntimeDependencyNode(key);
        node.setDisplayName(key.path);
        return node;
    }

}
