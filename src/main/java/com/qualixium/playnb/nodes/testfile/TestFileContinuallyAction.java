package com.qualixium.playnb.nodes.testfile;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.actions.ActionsProcessor;
import com.qualixium.playnb.actions.ActionsProcessor.ActionsEnum;
import static com.qualixium.playnb.nodes.testfile.TestFileFilterNode.getFullyQualifyClassName;
import java.awt.event.ActionEvent;
import java.util.Optional;
import javax.swing.AbstractAction;
import org.openide.filesystems.FileObject;

public class TestFileContinuallyAction extends AbstractAction {

    private final PlayProject playProject;
    private final FileObject fileObject;
    private final ActionsProcessor actionsProcessor;

    public TestFileContinuallyAction(PlayProject playProject, FileObject fileObject) {
        this.playProject = playProject;
        this.fileObject = fileObject;
        this.actionsProcessor = new ActionsProcessor(playProject);
        putValue(NAME, "Test File Continually (~testOnly)");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String fullyQualifyClassName = getFullyQualifyClassName(
                playProject.getProjectDirectory().getPath(), fileObject.getPath());

        actionsProcessor.executeAction(ActionsEnum.TEST_ONLY_AUTOCOMPILE, Optional.of(fullyQualifyClassName));
    }

}
