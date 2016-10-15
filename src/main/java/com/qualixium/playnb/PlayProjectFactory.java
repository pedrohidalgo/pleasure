package com.qualixium.playnb;

import java.io.IOException;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectFactory2;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ProjectFactory.class, position = -1000)
public class PlayProjectFactory implements ProjectFactory2 {

    public static final String PROJECT_FILE_1 = "app";
    public static final String PROJECT_FILE_2 = "conf";
    public static final ImageIcon IMAGE_ICON = new ImageIcon(
            ImageUtilities.loadImage("com/qualixium/playnb/play_icon.png"));

    //Specifies when a project is a project, i.e., 
    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(PROJECT_FILE_1) != null
                && projectDirectory.getFileObject(PROJECT_FILE_2) != null;
    }

//Specifies when the project will be opened, i.e., if the project exists: 
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new PlayProject(dir, state) : null;
    }

    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
// leave unimplemented for the moment 
    }

    @Override
    public ProjectManager.Result isProject2(FileObject projectDirectory) {
        if (isProject(projectDirectory)) {
            return new ProjectManager.Result(IMAGE_ICON);
        } else {
            return null;
        }
    }
}
