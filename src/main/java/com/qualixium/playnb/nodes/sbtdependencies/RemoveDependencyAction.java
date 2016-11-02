package com.qualixium.playnb.nodes.sbtdependencies;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.filetype.sbt.format.SBTReformatTask;
import com.qualixium.playnb.project.general.PlayPanel;
import static com.qualixium.playnb.project.general.PlayPanel.SBT_INDENT_SPACES;
import com.qualixium.playnb.util.ExceptionManager;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.AbstractAction;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.NbPreferences;

public class RemoveDependencyAction extends AbstractAction {

    private final PlayProject playProject;
    private final SBTDependencyNode sbtDependencyNode;

    public RemoveDependencyAction(SBTDependencyNode sbtDependencyNode, PlayProject playProject) {
        this.sbtDependencyNode = sbtDependencyNode;
        this.playProject = playProject;
        putValue(NAME, "Remove dependency");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            FileObject foBuildSBT = playProject.getProjectDirectory().getFileObject("build.sbt");
            String fileContentWithRemovedDep = BuildSBTManager
                    .removeDependency(foBuildSBT.asText(), sbtDependencyNode.getDependency());

            int sbtIndentSpaces = NbPreferences.forModule(PlayPanel.class).getInt(SBT_INDENT_SPACES, 4);
            String newFileContent = SBTReformatTask.formatFile(fileContentWithRemovedDep, sbtIndentSpaces);

            Files.write(Paths.get(foBuildSBT.toURI()),
                    newFileContent.getBytes());

            StatusDisplayer.getDefault().setStatusText("Dependency: " + sbtDependencyNode.getDependency() + " removed");

            playProject.getSbtDependenciesParentNode().reloadChildrens();
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }
    }

}
