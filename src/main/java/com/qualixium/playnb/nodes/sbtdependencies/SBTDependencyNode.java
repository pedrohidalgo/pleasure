package com.qualixium.playnb.nodes.sbtdependencies;

import com.qualixium.playnb.PlayProject;
import java.awt.Image;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.Action;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

public class SBTDependencyNode extends AbstractNode {

    private final PlayProject playProject;
    private final Dependency dependency;

    private static final Image imageIcon = ImageUtilities.loadImage(
            "com/qualixium/playnb/dependency_icon.png");

    public SBTDependencyNode(PlayProject playProject, Dependency dependency) {
        super(Children.create(new SBTDependencyChildFactory(), true),
                Lookups.singleton(dependency));
        this.playProject = playProject;
        this.dependency = dependency;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return imageIcon;
    }

    @Override
    public Image getIcon(int type) {
        return imageIcon;
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            new ViewArtifactDetailsAction(this, playProject),
            new RemoveDependencyAction(this, playProject)
        };
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        Dependency obj = getLookup().lookup(Dependency.class);
        try {
            Property groupIdProperty = new PropertySupport.Reflection(obj, String.class, "getGroupId", null);
            Property artifactIdProperty = new PropertySupport.Reflection(obj, String.class, "getArtifactId", null);
            Property versionProperty = new PropertySupport.Reflection(obj, String.class, "getVersion", null);

            groupIdProperty.setName("groupId");
            artifactIdProperty.setName("artifactId");
            versionProperty.setName("version");

            set.put(groupIdProperty);
            set.put(artifactIdProperty);
            set.put(versionProperty);

            Optional<Dependency> depDetailOptional = MavenSearchHelper.searchDependencyDetail(obj);
            if (depDetailOptional.isPresent()) {
                Dependency depDetail = depDetailOptional.get();
                Property packageTypeProperty = new PropertySupport.Reflection(depDetail, String.class, "getPackageType", null);
                Property timestampProperty = new PropertySupport.Reflection(depDetail, Date.class, "getTimestamp", null);

                packageTypeProperty.setName("packageType");
                timestampProperty.setName("timestamp");

                set.put(packageTypeProperty);
                set.put(timestampProperty);
            }
        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }
        sheet.put(set);
        return sheet;
    }

    public Dependency getDependency() {
        return dependency;
    }

}

class SBTDependencyChildFactory extends ChildFactory<String> {

    @Override
    protected boolean createKeys(List<String> toPopulate) {
        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        return null;
    }

}
