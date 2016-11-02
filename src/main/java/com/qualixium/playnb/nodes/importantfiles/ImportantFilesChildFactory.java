package com.qualixium.playnb.nodes.importantfiles;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.nodes.SBTFilterNode;
import com.qualixium.playnb.util.ExceptionManager;
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

public class ImportantFilesChildFactory extends ChildFactory<FileObject> {

    private final PlayProject playProject;

    public ImportantFilesChildFactory(PlayProject playProject) {
        this.playProject = playProject;
    }

    @Override
    protected boolean createKeys(List<FileObject> toPopulate) {
        final FileObject projectDirectory = playProject.getProjectDirectory();
        FileObject buildSBT = projectDirectory.getFileObject("build.sbt");
        FileObject pluginsSBT = projectDirectory.getFileObject("project/plugins.sbt");
        FileObject readme = projectDirectory.getFileObject("README");
        FileObject readmeWithExt = projectDirectory.getFileObject("README.md");

        toPopulate.add(buildSBT);
        toPopulate.add(pluginsSBT);

        if (readme != null) {
            toPopulate.add(readme);
        }
        if (readmeWithExt != null) {
            toPopulate.add(readmeWithExt);
        }

        return true;
    }

    @Override
    protected Node createNodeForKey(FileObject key) {
        try {
            Node nodeDelegate = DataObject.find(key).getNodeDelegate();
            if (key.getExt().equals("sbt")) {
                return new SBTFilterNode(nodeDelegate);
            }
            return nodeDelegate;
        } catch (DataObjectNotFoundException ex) {
            ExceptionManager.logException(ex);
        }

        return null;
    }

}
