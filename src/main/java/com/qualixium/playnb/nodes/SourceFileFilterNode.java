package com.qualixium.playnb.nodes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Action;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

public class SourceFileFilterNode extends FilterNode {

    public SourceFileFilterNode(Node original) {
        super(original, org.openide.nodes.Children.LEAF);
    }

    @Override
    public Action[] getActions(boolean context) {
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

        return listParentActions.toArray(new Action[listParentActions.size()]);
    }

}
