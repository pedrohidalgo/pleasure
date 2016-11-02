package com.qualixium.playnb;

import com.qualixium.playnb.actions.ActionsProcessor;
import com.qualixium.playnb.actions.ActionsProcessor.ActionsEnum;
import com.qualixium.playnb.actions.PlayIDEActionProvider;
import com.qualixium.playnb.classpath.ClassPathProviderImpl;
import com.qualixium.playnb.nodes.sbtdependencies.SBTDependenciesParentNode;
import com.qualixium.playnb.project.specific.ProjectCustomizerProvider;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

public class PlayProject implements Project {

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;
    private final ClassPathProviderImpl classPathProviderImpl;
    private SBTDependenciesParentNode sbtDependenciesParentNode;
    
    public static final String PLUGIN_NAME = "Pleasure";
    public static final String VERSION = "1.2.2";
    public static final boolean IS_PRODUCTION = true;

    PlayProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
        this.classPathProviderImpl = new ClassPathProviderImpl(this);
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    public String getProjectName() {
        return projectDir.getName();
    }

    public ClassPathProviderImpl getClassPathProviderImpl() {
        return classPathProviderImpl;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                this,
                new Info(),
                new PlayProjectLogicalView(this),
                new PlayIDEActionProvider(this),
                classPathProviderImpl,
                new ProjectCustomizerProvider(this)
            });
        }
        return lkp;
    }

    private final class Info implements ProjectInformation {

        @StaticResource()
        public static final String PROJECT_ICON = "com/qualixium/playnb/play_icon.png";

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(PROJECT_ICON));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
//do nothing, won't change 
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
//do nothing, won't change 
        }

        @Override
        public Project getProject() {
            return PlayProject.this;
        }
    }

    class PlayProjectLogicalView implements LogicalViewProvider {

        private final PlayProject project;

        public PlayProjectLogicalView(PlayProject project) {
            this.project = project;
        }

        @Override
        public Node createLogicalView() {
            try {
//Obtain the project directory's node: 
                FileObject projectDirectory = project.getProjectDirectory();
                DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
                Node nodeOfProjectFolder = projectFolder.getNodeDelegate();
//Decorate the project directory's node: 
                return new ProjectNode(nodeOfProjectFolder, project);
            } catch (DataObjectNotFoundException donfe) {
                Exceptions.printStackTrace(donfe);
//Fallback-the directory couldn't be created - //read-only filesystem or something evil happened 
                return new AbstractNode(Children.LEAF);
            }
        }

        private final class ProjectNode extends FilterNode {

            final PlayProject project;
            private final ActionsProcessor actionsProcessor;

            public ProjectNode(Node node, PlayProject project) throws DataObjectNotFoundException {
                super(node,
                        NodeFactorySupport.createCompositeChildren(
                                project, "Projects/com-qualixium-playnb/Nodes"),
                        new ProxyLookup(
                                new Lookup[]{
                                    Lookups.singleton(project), node.getLookup()
                                }));
                this.project = project;
                this.actionsProcessor = new ActionsProcessor(project);
            }

            @Override
            public Action[] getActions(boolean arg0) {
                return new Action[]{
                    CommonProjectActions.newFileAction(),
                    CommonProjectActions.deleteProjectAction(),
                    CommonProjectActions.setAsMainProjectAction(),
                    null,
                    actionsProcessor.createAction(ActionsEnum.RUN_AUTOCOMPILE),
                    actionsProcessor.createAction(ActionsEnum.RUN),
                    null,
                    actionsProcessor.createAction(ActionsEnum.CLEAN),
                    actionsProcessor.createAction(ActionsEnum.COMPILE),
                    actionsProcessor.createAction(ActionsEnum.CLEAN_BUILD),
                    null,
                    actionsProcessor.createAction(ActionsEnum.DEBUG_AUTOCOMPILE),
                    actionsProcessor.createAction(ActionsEnum.DEBUG),
                    null,
                    actionsProcessor.createAction(ActionsEnum.TEST_AUTOCOMPILE),
                    actionsProcessor.createAction(ActionsEnum.TEST),
                    actionsProcessor.createAction(ActionsEnum.JACOCO4SBT),
                    actionsProcessor.createAction(ActionsEnum.SBT_SCOVERAGE),
                    null,
                    actionsProcessor.createAction(ActionsEnum.RELOAD_PROJECT),
                    null,
                    CommonProjectActions.closeProjectAction(),
                    null,
                    CommonProjectActions.customizeProjectAction()
                };

            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(Info.PROJECT_ICON);//use static variables for all icons of the app
            }

            @Override
            public Image getOpenedIcon(int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }
        }

        @Override
        public Node findPath(Node root, Object target) { //leave unimplemented for now 
            return null;
        }
    }

    public SBTDependenciesParentNode getSbtDependenciesParentNode() {
        return sbtDependenciesParentNode;
    }

    public void setSbtDependenciesParentNode(SBTDependenciesParentNode sbtDependenciesParentNode) {
        this.sbtDependenciesParentNode = sbtDependenciesParentNode;
    }

}
