package com.qualixium.playnb.nodes.sbtdependencies;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.util.ExceptionManager;
import java.util.Optional;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.editor.document.OnSaveTask;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;

public class BuildSBTOnSaveTask implements OnSaveTask {

    private final OnSaveTask.Context context;

    public BuildSBTOnSaveTask(Context context) {
        this.context = context;
    }

    @Override
    public void performTask() {
        try {
            Document document = context.getDocument();
            FileObject fileObject = NbEditorUtilities.getFileObject(document);
            String name = fileObject.getName();
            if (name.equals("build")) {
                String docText = document.getText(0, document.getLength());
                Optional<PlayProject> playProjectOptional = PlayProjectUtil.getPlayProject(fileObject);
                if (playProjectOptional.isPresent()) {
                    PlayProject playProject = playProjectOptional.get();
                    playProject.getSbtDependenciesParentNode().reloadChildrens(docText);
                    StatusDisplayer.getDefault().setStatusText("build.sbt reload");
                }
            }
        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }
    }

    @Override
    public void runLocked(Runnable r) {
        r.run();
    }

    @Override
    public boolean cancel() {
        return false;
    }

}
