package com.qualixium.playnb.nodes;

import java.awt.Image;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.filesystems.FileObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

public class ConfFolderFilterNode extends FilterNode {

    @StaticResource
    public static final String CONF_IMAGE_OPENED_PATH = "com/qualixium/playnb/project/conf_folder_open.png";
    public static final Image IMAGE_OPENED = ImageUtilities.loadImage(CONF_IMAGE_OPENED_PATH);
    @StaticResource
    public static final String CONF_IMAGE_CLOSED_PATH = "com/qualixium/playnb/project/conf_folder_closed.png";
    public static final Image IMAGE_CLOSED = ImageUtilities.loadImage(CONF_IMAGE_CLOSED_PATH);

    public ConfFolderFilterNode(Node original) {
        super(original, new ProxyChildren(original));
    }

    @Override
    public Image getOpenedIcon(int type) {
        return IMAGE_OPENED;
    }

    @Override
    public Image getIcon(int type) {
        return IMAGE_CLOSED;
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
                return original.cloneNode();
            }
        }
    }

}
