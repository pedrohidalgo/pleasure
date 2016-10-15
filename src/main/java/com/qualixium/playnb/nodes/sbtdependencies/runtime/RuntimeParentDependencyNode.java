package com.qualixium.playnb.nodes.sbtdependencies.runtime;

import com.qualixium.playnb.PlayProject;
import java.awt.Image;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;

public class RuntimeParentDependencyNode extends AbstractNode {

    private final PlayProject playProject;

    @StaticResource
    public static final String LIBRARIES_OPENED_IMAGE_PATH = "com/qualixium/playnb/project/libraries_open.png";
    public static final Image LIBRARIES_OPENED_IMAGE = ImageUtilities.loadImage(LIBRARIES_OPENED_IMAGE_PATH);
    @StaticResource
    public static final String LIBRARIES_CLOSED_IMAGE_PATH = "com/qualixium/playnb/project/libraries_closed.png";
    public static final Image LIBRARIES_CLOSED_IMAGE = ImageUtilities.loadImage(LIBRARIES_CLOSED_IMAGE_PATH);

    public RuntimeParentDependencyNode(PlayProject playProject) {
        super(Children.create(new RuntimeDependencyFactory(playProject), true));
        this.playProject = playProject;
        setDisplayName("Runtime Dependencies");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return LIBRARIES_OPENED_IMAGE;
    }

    @Override
    public Image getIcon(int type) {
        return LIBRARIES_CLOSED_IMAGE;
    }

}
