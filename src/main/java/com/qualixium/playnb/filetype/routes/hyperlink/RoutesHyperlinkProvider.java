package com.qualixium.playnb.filetype.routes.hyperlink;

import com.qualixium.playnb.filetype.routes.RoutesLanguage;
import com.qualixium.playnb.filetype.routes.RoutesLanguageHelper;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.openide.awt.StatusDisplayer;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.Line;

@MimeRegistration(mimeType = RoutesLanguage.MIME_TYPE, service = HyperlinkProviderExt.class)
public class RoutesHyperlinkProvider implements HyperlinkProviderExt {

    private int startOffset, endOffset;

    @Override
    public Set<HyperlinkType> getSupportedHyperlinkTypes() {
        return EnumSet.of(HyperlinkType.GO_TO_DECLARATION);
    }

    @Override
    public boolean isHyperlinkPoint(Document doc, int offset, HyperlinkType type) {
        return getHyperlinkSpan(doc, offset, type) != null;
    }

    @Override
    public int[] getHyperlinkSpan(Document doc, int offset, HyperlinkType type) {
        return getIdentifierSpan(doc, offset);
    }

    @Override
    public String getTooltipText(Document doc, int offset, HyperlinkType type) {
        int linesToShow = 20;
        String textToShow = null;
        try {
            String methodNameWitClassName = doc.getText(startOffset, endOffset - startOffset);
            String className = RoutesLanguageHelper.getOnlyClassNameFromCompleteMethodSignature(methodNameWitClassName);

            File fileWithPathToOpen = MiscUtil.getPathFile(doc, className);
            if (fileWithPathToOpen.exists()) {
                FileObject foToOpen = FileUtil.toFileObject(fileWithPathToOpen);
                String fileContent = foToOpen.asText();

                String methodName = RoutesLanguageHelper.getOnlyMethodNameFromCompleteMethodSignature(methodNameWitClassName);
                int lineNumber = MiscUtil.getLineNumber(fileContent, methodName);
                textToShow = MiscUtil.getLinesFromFileContent(fileContent, lineNumber, linesToShow)
                        + MiscUtil.LINE_SEPARATOR + "Showing max " + (linesToShow + 1) + " lines ...";
            }
        } catch (BadLocationException | IOException ex) {
            ExceptionManager.logException(ex);
        }
        //FUTURE: allow returned tool tip formatted to highlight java and scala syntax
//        return "<html><b>"+textToShow+"<b></html>";
        return textToShow;
    }

    @Override
    public void performClickAction(Document doc, int offset, HyperlinkType ht) {
        try {
            String classNameWithMethodName = doc.getText(startOffset, endOffset - startOffset);
            String className = RoutesLanguageHelper.getOnlyClassNameFromCompleteMethodSignature(classNameWithMethodName);
            String methodName = RoutesLanguageHelper.getOnlyMethodNameFromCompleteMethodSignature(classNameWithMethodName);
            File fileToOpen = MiscUtil.getPathFile(doc, className);

            if (fileToOpen.exists()) {
                try {
                    FileObject foToOpen = FileUtil.toFileObject(fileToOpen);
                    int lineNumber = MiscUtil.getLineNumber(foToOpen.asText(), methodName);

                    LineCookie lc = DataObject.find(foToOpen).getLookup().lookup(LineCookie.class);
                    Line line = lc.getLineSet().getOriginal(lineNumber);
                    line.show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FRONT);
                } catch (IOException ex) {
                    ExceptionManager.logException(ex);
                }
            } else {
                StatusDisplayer.getDefault().setStatusText(fileToOpen.getName() + " doesn't exist in project!");
            }
        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }
    }

    private int[] getIdentifierSpan(Document doc, int offset) {
        try {
            String routesFileContent = doc.getText(0, doc.getLength());
            Optional<String> routeMethodOptional = RoutesLanguageHelper.getRouteMethod(routesFileContent, offset);

            if (routeMethodOptional.isPresent()) {
                String methodName = routeMethodOptional.get();
                startOffset = routesFileContent.indexOf(methodName);
                endOffset = startOffset + methodName.length();

                return new int[]{startOffset, endOffset};
            }

        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }
        return null;
    }

}
