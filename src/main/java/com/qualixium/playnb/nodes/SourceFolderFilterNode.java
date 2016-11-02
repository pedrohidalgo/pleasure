package com.qualixium.playnb.nodes;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.nodes.testfile.TestFileFilterNode;
import java.awt.Image;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.filesystems.FileObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

public class SourceFolderFilterNode extends FilterNode {

    @StaticResource
    public static final String PACKAGE_IMAGE_PATH = "com/qualixium/playnb/project/package.png";
    public static final Image PACKAGE_IMAGE = ImageUtilities.loadImage(PACKAGE_IMAGE_PATH);

    public SourceFolderFilterNode(Node original, PlayProject playProject) {
        super(original, new ProxyChildren(original, playProject));
    }

    @Override
    public Image getOpenedIcon(int type) {
        return PACKAGE_IMAGE;
    }

    @Override
    public Image getIcon(int type) {
        return PACKAGE_IMAGE;
    }

    static class ProxyChildren extends FilterNode.Children {

        private final PlayProject playProject;

        public ProxyChildren(Node owner, PlayProject playProject) {
            super(owner);
            this.playProject = playProject;
        }

        @Override
        protected Node copyNode(Node original) {
            FileObject fileObject = (FileObject) original.getLookup().lookup(FileObject.class);
            if (fileObject.isFolder()) {
                return new SourceFolderFilterNode(original, playProject);
            } else {
                if (fileObject.getPath().contains(playProject.getProjectDirectory()
                        .getFileObject("test").getPath())) {
                    if (fileObject.getExt().equals("java") || fileObject.getExt().equals("scala")) {
                        return new TestFileFilterNode(original, playProject);
                    }
                } else if (fileObject.getExt().equals("java") || fileObject.getExt().equals("scala")) {
                    return new SourceFileFilterNode(original);
                }
                return original.cloneNode();
            }
        }
    }

}
