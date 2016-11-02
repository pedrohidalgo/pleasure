package com.qualixium.playnb.nodes.sbtdependencies;

import com.qualixium.playnb.PlayProject;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

class AddDependencyAction extends AbstractAction {

    private final PlayProject playProject;
    private final SBTDependenciesParentNode parentNode;

    public AddDependencyAction(SBTDependenciesParentNode parentNode, PlayProject playProject) {
        this.parentNode = parentNode;
        this.playProject = playProject;
        putValue(NAME, "Add Dependency...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AddDependencyDialog addDependencyDialog = new AddDependencyDialog(
                null, false, parentNode, playProject);

        addDependencyDialog.setVisible(true);
    }

}
