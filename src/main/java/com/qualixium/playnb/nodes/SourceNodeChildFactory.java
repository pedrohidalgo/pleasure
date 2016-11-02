package com.qualixium.playnb.nodes;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

public class SourceNodeChildFactory extends ChildFactory<FileObject> {

    private final FileObject fileObject;
    private final PlayProject playProject;

    public SourceNodeChildFactory(FileObject fileObject, PlayProject playProject) {
        this.fileObject = fileObject;
        this.playProject = playProject;
    }

    @Override
    protected boolean createKeys(List<FileObject> toPopulate) {
        List<FileObject> listFileObjects = Arrays.asList(fileObject.getChildren())
                .stream()
                .sorted(MiscUtil.FILE_OBJECT_COMPARATOR)
                .collect(Collectors.toList());

        toPopulate.addAll(listFileObjects);
        return true;
    }

    @Override
    protected Node createNodeForKey(FileObject key) {
        try {
            Node keyNode = DataObject.find(key).getNodeDelegate();
            if (key.isFolder()) {
                SourceFolderFilterNode sourceFolder = new SourceFolderFilterNode(keyNode, playProject);
                sourceFolder.setDisplayName(key.getName());
                return sourceFolder;
            } else {
                if (key.getExt().equals("sbt")) {
                    return new SBTFilterNode(keyNode);
                }
                return keyNode;
            }
        } catch (DataObjectNotFoundException ex) {
            ExceptionManager.logException(ex);
        }

        return null;
    }

}
