package com.qualixium.playnb.nodes.sbtdependencies.runtime;

import java.awt.Image;
import java.util.List;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

public class RuntimeDependencyNode extends AbstractNode {

    private final RuntimeDependency classPathDependency;

    @StaticResource()
    public static final String CLASSPATH_DEPENDENCY_IMAGE = "com/qualixium/playnb/classpath_dependency.png";
    
    private static final Image imageIcon = ImageUtilities.loadImage(CLASSPATH_DEPENDENCY_IMAGE);

    public RuntimeDependencyNode(RuntimeDependency classPathDependency) {
        super(Children.create(new PlayFrameworkDependencyChildFactory(), true),
                Lookups.singleton(classPathDependency));
        this.classPathDependency = classPathDependency;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return imageIcon;
    }

    @Override
    public Image getIcon(int type) {
        return imageIcon;
    }

    public RuntimeDependency getClassPathDependency() {
        return classPathDependency;
    }

}

class PlayFrameworkDependencyChildFactory extends ChildFactory<String> {

    @Override
    protected boolean createKeys(List<String> toPopulate) {
        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        return null;
    }

}
