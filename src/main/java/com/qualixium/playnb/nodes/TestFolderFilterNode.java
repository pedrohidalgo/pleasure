package com.qualixium.playnb.nodes;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.nodes.testfile.TestFileFilterNode;
import java.awt.Image;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.filesystems.FileObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

public class TestFolderFilterNode extends FilterNode {

    @StaticResource
    public static final String TEST_IMAGE_OPENED_PATH = "com/qualixium/playnb/project/test_folder_open.png";
    public static final Image TEST_IMAGE_OPENED = ImageUtilities.loadImage(TEST_IMAGE_OPENED_PATH);
    @StaticResource
    public static final String TEST_IMAGE_CLOSED_PATH = "com/qualixium/playnb/project/test_folder_closed.png";
    public static final Image TEST_IMAGE_CLOSED = ImageUtilities.loadImage(TEST_IMAGE_CLOSED_PATH);

    public TestFolderFilterNode(Node original, PlayProject playProject) {
        super(original, new ProxyChildren(original, playProject));
    }

    @Override
    public Image getOpenedIcon(int type) {
        return TEST_IMAGE_OPENED;
    }

    @Override
    public Image getIcon(int type) {
        return TEST_IMAGE_CLOSED;
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
                if (fileObject.getExt().equals("java") || fileObject.getExt().equals("scala")) {
                    return new TestFileFilterNode(original, playProject);
                }
                return original.cloneNode();
            }
        }
    }

}
