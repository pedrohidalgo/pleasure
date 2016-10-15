package com.qualixium.playnb.filetype.routes.hints;

import com.qualixium.playnb.codegenerator.CodeGeneratorHelper;
import com.qualixium.playnb.filetype.routes.RoutesLanguageHelper;
import com.qualixium.playnb.util.MiscUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.text.Document;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.api.HintFix;
import org.netbeans.modules.csl.api.RuleContext;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

public class CreateMethodHintFix implements HintFix {

    private final RuleContext ruleContext;
    private final Error error;

    public CreateMethodHintFix(RuleContext ruleContext, Error error) {
        this.ruleContext = ruleContext;
        this.error = error;
    }

    @Override
    public String getDescription() {
        return "Create method to fix the route entry";
    }

    @Override
    public void implement() throws Exception {
        Document document = ruleContext.parserResult.getSnapshot().getSource().getDocument(false);
        String methodNameWitClassName = document.getText(error.getStartPosition(), error.getEndPosition() - error.getStartPosition());
        String className = RoutesLanguageHelper.getOnlyClassNameFromCompleteMethodSignature(methodNameWitClassName);

        File fileWithPath = MiscUtil.getPathFile(document, className);
        if (fileWithPath.exists()) {
            FileObject foToModify = FileUtil.toFileObject(fileWithPath);

            String fileContent = foToModify.asText();
            String methodName = RoutesLanguageHelper.getOnlyMethodNameFromCompleteMethodSignature(methodNameWitClassName);
            String newMethodCode = null;
            if (foToModify.getExt().equals("java")) {
                newMethodCode = CodeGeneratorHelper.getCodeNewMethodJava(methodName);

            } else if (foToModify.getExt().equals("scala")) {
                newMethodCode = CodeGeneratorHelper.getCodeNewMethodScala(methodName);
            }

            String newFileContent = CodeGeneratorHelper.getFileContentWithNewMethodCode(fileContent, newMethodCode);
            Files.write(Paths.get(fileWithPath.getPath()), newFileContent.getBytes());

            StatusDisplayer.getDefault().setStatusText("Method created");
        }

    }

    @Override
    public boolean isSafe() {
        return true;
    }

    @Override
    public boolean isInteractive() {
        return true;
    }

}
