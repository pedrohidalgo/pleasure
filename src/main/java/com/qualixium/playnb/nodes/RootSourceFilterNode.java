package com.qualixium.playnb.nodes;

import com.qualixium.playnb.PlayProject;
import java.awt.Image;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.filesystems.FileObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

public class RootSourceFilterNode extends FilterNode {

    @StaticResource
    public static final String ROOT_SOURCE_ICON_PATH = "com/qualixium/playnb/project/root_source.png";
    public static final Image IMAGE_ICON = ImageUtilities.loadImage(ROOT_SOURCE_ICON_PATH);
    @StaticResource
    public static final String ROOT_SOURCE_OPENED_ICON_PATH = "com/qualixium/playnb/project/root_source_open.png";
    public static final Image OPENED_IMAGE_ICON = ImageUtilities.loadImage(ROOT_SOURCE_OPENED_ICON_PATH);

    public RootSourceFilterNode(Node original, PlayProject playProject) {
        super(original, new RootSourceProxyChildren(original, playProject));
    }

    @Override
    public Image getOpenedIcon(int type) {
        return OPENED_IMAGE_ICON;
    }

    @Override
    public Image getIcon(int type) {
        return IMAGE_ICON;
    }

    static class RootSourceProxyChildren extends FilterNode.Children {

        private final PlayProject playProject;

        public RootSourceProxyChildren(Node owner, PlayProject playProject) {
            super(owner);
            this.playProject = playProject;
        }

        @Override
        protected Node copyNode(Node original) {
            FileObject fileObject = (FileObject) original.getLookup().lookup(FileObject.class);
            if (fileObject.isFolder()) {
                if (fileObject.getName().equals("views")) {
                    return new ViewsPackageFilterNode(original);
                } else {
                    return new SourceFolderFilterNode(original, playProject);
                }
            } else {
                if (fileObject.getExt().equals("java") || fileObject.getExt().equals("scala")) {
                    return new SourceFileFilterNode(original);
                }
                return original.cloneNode();
            }
        }
    }

}
