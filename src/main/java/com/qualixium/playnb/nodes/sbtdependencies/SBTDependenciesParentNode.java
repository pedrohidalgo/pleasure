package com.qualixium.playnb.nodes.sbtdependencies;

import com.qualixium.playnb.PlayProject;
import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;

public class SBTDependenciesParentNode extends AbstractNode {

    private final PlayProject playProject;

    @StaticResource
    public static final String LIBRARIES_OPENED_IMAGE_PATH = "com/qualixium/playnb/project/libraries_open.png";
    public static final Image LIBRARIES_OPENED_IMAGE = ImageUtilities.loadImage(LIBRARIES_OPENED_IMAGE_PATH);
    @StaticResource
    public static final String LIBRARIES_CLOSED_IMAGE_PATH = "com/qualixium/playnb/project/libraries_closed.png";
    public static final Image LIBRARIES_CLOSED_IMAGE = ImageUtilities.loadImage(LIBRARIES_CLOSED_IMAGE_PATH);

    public SBTDependenciesParentNode(PlayProject playProject) {
        super(Children.create(new SBTDependencyFactory(playProject), true));
        this.playProject = playProject;
        setDisplayName("Dependencies");
        setShortDescription("Only shows dependencies declared in build.sbt file (does not works for modules)");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return LIBRARIES_OPENED_IMAGE;
    }

    @Override
    public Image getIcon(int type) {
        return LIBRARIES_CLOSED_IMAGE;
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{new AddDependencyAction(this, playProject)};
    }

    public void reloadChildrens() {
        setChildren(Children.create(new SBTDependencyFactory(playProject), true));
    }

    public void reloadChildrens(String fileContent) {
        List<Dependency> libraryDependencies = BuildSBTManager.getLibraryDependencies(fileContent);
        setChildren(Children.create(new SBTDependencyFactory(playProject, libraryDependencies), true));
    }

}
