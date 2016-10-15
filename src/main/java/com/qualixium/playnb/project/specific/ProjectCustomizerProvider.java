package com.qualixium.playnb.project.specific;

import com.qualixium.playnb.PlayProject;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.awt.StatusDisplayer;
import org.openide.util.lookup.Lookups;

public class ProjectCustomizerProvider implements CustomizerProvider {

    public final PlayProject playProject;
    public static final String CUSTOMIZER_FOLDER_PATH = "Projects/com-qualixium-playnb/Customizer";

    public ProjectCustomizerProvider(PlayProject playProject) {
        this.playProject = playProject;
    }

    @Override
    public void showCustomizer() {
        Dialog dialog = ProjectCustomizer.createCustomizerDialog(
                //Path to layer folder: 
                CUSTOMIZER_FOLDER_PATH,
                //Lookup, which must contain, at least, the Project: 
                Lookups.fixed(playProject),
                //Preselected category: 
                "",
                //OK button listener: 
                new OKOptionListener(),
                //HelpCtx for Help button of dialog: 
                null);
        dialog.setTitle(ProjectUtils.getInformation(playProject).getDisplayName());
        dialog.setVisible(true);
    }

    private class OKOptionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StatusDisplayer.getDefault().setStatusText("Project (" + playProject.getProjectName() + ") options saved");
        }
    }
}
