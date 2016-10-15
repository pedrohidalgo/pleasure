package com.qualixium.playnb.nodes.sbtdependencies;

import com.qualixium.playnb.util.ExceptionManager;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;

public class SearchDepByQuerSwingWorker extends SwingWorker<List<Dependency>, Void> {

    private final AddDependencyDialog addDependencyDialog;
    private final String text;

    public SearchDepByQuerSwingWorker(AddDependencyDialog addDependencyDialog, String text) {
        this.addDependencyDialog = addDependencyDialog;
        this.text = text;
    }

    @Override
    protected List<Dependency> doInBackground() throws Exception {
        return MavenSearchHelper.searchByQuery(text);
    }

    @Override
    protected void done() {
        try {
            addDependencyDialog.getLblBusy().setVisible(false);
            DefaultListModel<Dependency> listModel = addDependencyDialog.getListModel();

            List<Dependency> dependencies = get();
            if (!dependencies.isEmpty()) {
                dependencies.stream().forEach((dependency) -> {
                    listModel.addElement(dependency);
                });
            } else {
                addDependencyDialog.getLblSearchInfo().setVisible(true);
            }
        } catch (InterruptedException | ExecutionException ex) {
            ExceptionManager.logException(ex);
        }
    }

}
