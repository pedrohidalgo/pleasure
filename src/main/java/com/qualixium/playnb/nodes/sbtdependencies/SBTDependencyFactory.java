package com.qualixium.playnb.nodes.sbtdependencies;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.util.ExceptionManager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.openide.filesystems.FileObject;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

public class SBTDependencyFactory extends ChildFactory<Dependency> {

    private final PlayProject playProject;
    private final Optional<List<Dependency>> listElementsOptional;

    SBTDependencyFactory(PlayProject playProject) {
        this.playProject = playProject;
        this.listElementsOptional = Optional.empty();
    }

    public SBTDependencyFactory(PlayProject playProject, List<Dependency> listElements) {
        this.playProject = playProject;
        this.listElementsOptional = Optional.of(listElements);
    }

    @Override
    protected boolean createKeys(List<Dependency> toPopulate) {
        try {
            if (listElementsOptional.isPresent()) {

                toPopulate.addAll(listElementsOptional.get());
            } else {
                FileObject foBuildSBT = playProject.getProjectDirectory().getFileObject("build.sbt");
                String text = foBuildSBT.asText();
                toPopulate.addAll(BuildSBTManager.getLibraryDependencies(text));
            }
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Dependency key) {
        SBTDependencyNode node = new SBTDependencyNode(playProject, key);
        node.setDisplayName(key.toString());
        return node;
    }

}
