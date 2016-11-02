package com.qualixium.playnb.nodes;

import com.qualixium.playnb.filetype.sbt.SBTLanguage;
import java.awt.Image;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

public class SBTFilterNode extends FilterNode {

    public SBTFilterNode(Node original) {
        super(original, org.openide.nodes.Children.LEAF);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return SBTLanguage.IMAGE_ICON.getImage();
    }

    @Override
    public Image getIcon(int type) {
        return SBTLanguage.IMAGE_ICON.getImage();
    }

}
