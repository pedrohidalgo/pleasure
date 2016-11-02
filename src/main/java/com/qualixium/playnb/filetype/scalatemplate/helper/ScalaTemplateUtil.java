package com.qualixium.playnb.filetype.scalatemplate.helper;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.swing.text.Document;
import org.openide.filesystems.FileObject;

public class ScalaTemplateUtil {

    public static int getOpenedAmountBracketsAtCaret(String fileContent, int caretPosition) {
        int bracketsAmount = 0;
        char[] charArray = fileContent.toCharArray();
        for (int i = 0; i < caretPosition; i++) {
            char ch = charArray[i];
            switch (Character.toString(ch)) {
                case "{":
                    bracketsAmount++;
                    break;
                case "}":
                    bracketsAmount--;
                    break;
            }
        }

        return bracketsAmount;
    }

    public static Optional<String> getBlockDefinition(String fileContent, int caretPosition) {
        if (getOpenedAmountBracketsAtCaret(fileContent, caretPosition) > 0) {
            int indexOpenBracket = fileContent.lastIndexOf("{", caretPosition);
            int indexMagicCharacter = fileContent.lastIndexOf("@", indexOpenBracket);

            String blockDefinition = fileContent.substring(indexMagicCharacter + 1, indexOpenBracket);

            return Optional.of(blockDefinition.trim());
        } else {
            return Optional.empty();
        }

    }

    public static Optional<String> getValDefinition(String fileContent, int caretPosition) {
        if (getOpenedAmountBracketsAtCaret(fileContent, caretPosition) > 0) {

            int indexOpenBracket = fileContent.lastIndexOf("{", caretPosition);
            int indexLambdaSeparator = fileContent.indexOf("=>", indexOpenBracket);

            if (indexLambdaSeparator > 0) {
                String valDefinition = fileContent.substring(indexOpenBracket + 1, indexLambdaSeparator);

                return Optional.of(valDefinition.trim());
            }
        }

        return Optional.empty();
    }

    public static Optional<String> getResourcesRootFolder(String routesFileContent) {
        List<String> listLines = MiscUtil.getLinesFromFileContent(routesFileContent);
        final String stringToStart = "path=\"";

        for (String line : listLines) {
            if (line.contains("controllers.Assets.") && line.contains(stringToStart)) {
                int indexToStart = line.indexOf(stringToStart) + stringToStart.length();
                int indexToEnd = line.indexOf("\"", indexToStart);
                return Optional.of(line.substring(indexToStart, indexToEnd));
            }
        }

        return Optional.empty();
    }

    public static Optional<FileObject> getFileObjectResourcesRoot(Document documentScalaTemplate) {
        try {
            FileObject fo = MiscUtil.getFileObject(documentScalaTemplate);
            Optional<PlayProject> playProjectOptional = PlayProjectUtil.getPlayProject(fo);
            if (playProjectOptional.isPresent()) {
                PlayProject playProject = playProjectOptional.get();
                FileObject foRoutes = playProject.getProjectDirectory().getFileObject("conf/routes");
                if (foRoutes != null) {
                    Optional<String> resourcesRootFolderOptional = ScalaTemplateUtil.getResourcesRootFolder(foRoutes.asText(PlayProjectUtil.UTF_8));
                    if (resourcesRootFolderOptional.isPresent()) {
                        return Optional.of(playProject.getProjectDirectory().getFileObject(resourcesRootFolderOptional.get()));
                    }
                }
            }
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }

        return Optional.empty();
    }

}
