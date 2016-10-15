package com.qualixium.playnb.template;

import com.qualixium.playnb.util.MiscUtil;
import com.qualixium.playnb.util.MiscUtil.Language;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

public class TemplateUtil {

    public static List<String> getPackagesName(String pathDir) {
        List<String> result = new ArrayList<>();
        FileObject foDirParent = FileUtil.toFileObject(new File(pathDir));

        Enumeration<? extends FileObject> childrens = foDirParent.getChildren(true);
        while (childrens.hasMoreElements()) {
            FileObject foChild = childrens.nextElement();
            if (foChild.isFolder()) {
                String folderName = foChild.getPath()
                        .replace(pathDir + "/", "")
                        .replace("/", ".");
                result.add(folderName);
            }
        }

        return result;
    }

    public static void updateCreatedFileText(WizardDescriptor wizard, JComboBox cbxPackageName,
            JTextField txtName, JTextField txtCreatedFile, Language language) {
        String ext = language.equals(Language.JAVA) ? ".java" : ".scala";
        Project project = Templates.getProject(wizard);
        FileObject foTargetFolder = Templates.getTargetFolder(wizard);
        String completePath = null;
        String packagePart = cbxPackageName.getEditor().getItem().toString().replace(".", "/");
        if (foTargetFolder.getPath().contains(project.getProjectDirectory().getPath() + "/app")) {
            if (packagePart.isEmpty()) {
                completePath = project.getProjectDirectory().getPath() + "/app/" + txtName.getText() + ext;
            } else {
                completePath = project.getProjectDirectory().getPath() + "/app/" + packagePart + "/" + txtName.getText() + ext;
            }
        } else if (foTargetFolder.getPath().contains(project.getProjectDirectory().getPath() + "/test")) {
            if (packagePart.isEmpty()) {
                completePath = project.getProjectDirectory().getPath() + "/test/" + txtName.getText() + ext;
            } else {
                completePath = project.getProjectDirectory().getPath() + "/test/" + packagePart + "/" + txtName.getText() + ext;
            }
        }
        txtCreatedFile.setText(completePath);
    }

    public static Set<?> createTemplateFile(WizardDescriptor wizard, String sourceCode, Language language) throws IOException {
        String ext = language.equals(Language.JAVA) ? ".java" : ".scala";
        Set<FileObject> resultSet = new LinkedHashSet<>();
        FileObject foTargetFolder = Templates.getTargetFolder(wizard);
        Project project = Templates.getProject(wizard);
        String className = (String) wizard.getProperty("clasName");
        String packageName = "";
        if (wizard.getProperty("packageName") != null) {
            packageName = (String) wizard.getProperty("packageName");
        }
        String dirPath = null;
        if (foTargetFolder.getPath().contains(project.getProjectDirectory().getPath() + "/app")) {
            if (packageName.isEmpty()) {
                dirPath = project.getProjectDirectory().getPath() + "/app";
            } else {
                dirPath = project.getProjectDirectory().getPath() + "/app/" + packageName.replace(".", "/");
            }
        } else if (foTargetFolder.getPath().contains(project.getProjectDirectory().getPath() + "/test")) {
            if (packageName.isEmpty()) {
                dirPath = project.getProjectDirectory().getPath() + "/test";
            } else {
                dirPath = project.getProjectDirectory().getPath() + "/test/" + packageName.replace(".", "/");
            }
        }
        if (dirPath != null) {
            new File(dirPath).mkdirs();
            String newPathToCreate = dirPath + "/" + className + ext;
            Files.write(Paths.get(newPathToCreate), sourceCode.getBytes(), StandardOpenOption.CREATE);
            resultSet.add(FileUtil.toFileObject(new File(newPathToCreate)));
        }
        return resultSet;
    }

    public static String cleanTargetFolder(FileObject foTargetFolder, Project project) {
        String currentPackageName = foTargetFolder.getPath()
                .replace(project.getProjectDirectory().getPath() + "/app", "")
                .replace(project.getProjectDirectory().getPath() + "/test", "")
                .replace("/", ".");
        return currentPackageName;
    }

}
