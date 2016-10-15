package com.qualixium.playnb.nodes.testfile;

import com.qualixium.playnb.PlayProject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Action;
import org.openide.filesystems.FileObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

public class TestFileFilterNode extends FilterNode {

    private final PlayProject playProject;
    private final Node original;

    public TestFileFilterNode(Node original, PlayProject playProject) {
        super(original, org.openide.nodes.Children.LEAF);
        this.original = original;
        this.playProject = playProject;
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> listActions = new ArrayList<>();
        List<Action> listParentActions = Arrays.asList(super.getActions(context))
                .stream()
                .filter(action -> {
                    if (action != null) {
                        Object actionNameObj = action.getValue(Action.NAME);
                        String actionName = actionNameObj == null ? "" : actionNameObj.toString();
                        return action.isEnabled()
                        && !actionName.equals("Fi&nd Usages")
                        && !actionName.equals("Refactor");
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());

        listActions.addAll(listParentActions);

        FileObject fileObject = (FileObject) original.getLookup().lookup(FileObject.class);
        TestFileAction testFileAction = new TestFileAction(playProject, fileObject);
        TestFileContinuallyAction testFileContinuallyAction
                = new TestFileContinuallyAction(playProject, fileObject);

        listActions.add(2, null);
        listActions.add(3, testFileAction);
        listActions.add(4, testFileContinuallyAction);
        listActions.add(5, null);

        return listActions.toArray(new Action[listActions.size()]);
    }

    public static String getFullyQualifyClassName(String projectDir, String path) {
        String pathInsideTestFolder = path.replace(projectDir + "/test/", "");

        return pathInsideTestFolder.substring(0, pathInsideTestFolder.lastIndexOf("."))
                .replace("/", ".");
    }

}
