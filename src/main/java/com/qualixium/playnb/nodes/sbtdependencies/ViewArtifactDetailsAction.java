package com.qualixium.playnb.nodes.sbtdependencies;

import com.qualixium.playnb.PlayProject;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class ViewArtifactDetailsAction extends AbstractAction {

    private final PlayProject playProject;
    private final SBTDependencyNode sbtDependencyNode;

    public ViewArtifactDetailsAction(SBTDependencyNode sbtDependencyNode, PlayProject playProject) {
        this.sbtDependencyNode = sbtDependencyNode;
        this.playProject = playProject;
        putValue(NAME, "View Details...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ViewArtifactDetailsDialog dialog = new ViewArtifactDetailsDialog(null, true,
                sbtDependencyNode.getDependency());
        dialog.setVisible(true);
    }

}
