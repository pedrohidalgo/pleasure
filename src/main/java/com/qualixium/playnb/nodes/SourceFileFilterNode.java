package com.qualixium.playnb.nodes;

import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

public class SourceFileFilterNode extends FilterNode {

    public SourceFileFilterNode(Node original) {
        super(original, org.openide.nodes.Children.LEAF);
    }

}
