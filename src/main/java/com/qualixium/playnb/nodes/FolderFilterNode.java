package com.qualixium.playnb.nodes;

import java.awt.Image;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.filesystems.FileObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

public class FolderFilterNode extends FilterNode {

    @StaticResource
    public static final String SOURCE_IMAGE_OPENED_PATH = "com/qualixium/playnb/project/folder_open.png";
    public static final Image SOURCE_IMAGE_OPENED = ImageUtilities.loadImage(SOURCE_IMAGE_OPENED_PATH);
    @StaticResource
    public static final String SOURCE_IMAGE_CLOSED_PATH = "com/qualixium/playnb/project/folder_closed.png";
    public static final Image SOURCE_IMAGE_CLOSED = ImageUtilities.loadImage(SOURCE_IMAGE_CLOSED_PATH);

    public FolderFilterNode(Node original) {
        super(original, new ProxyChildren(original));
    }

    @Override
    public Image getOpenedIcon(int type) {
        return SOURCE_IMAGE_OPENED;
    }

    @Override
    public Image getIcon(int type) {
        return SOURCE_IMAGE_CLOSED;
    }

    static class ProxyChildren extends FilterNode.Children {

        public ProxyChildren(Node owner) {
            super(owner);
        }

        @Override
        protected Node copyNode(Node original) {
            FileObject fileObject = (FileObject) original.getLookup().lookup(FileObject.class);
            if (fileObject.isFolder()) {
                return new FolderFilterNode(original);
            } else {
                if (fileObject.getExt().equals("sbt")) {
                    return new SBTFilterNode(original);
                }
                return original.cloneNode();
            }
        }
    }

}
