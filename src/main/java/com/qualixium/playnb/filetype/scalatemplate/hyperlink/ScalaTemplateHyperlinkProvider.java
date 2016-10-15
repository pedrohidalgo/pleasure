package com.qualixium.playnb.filetype.scalatemplate.hyperlink;

import com.qualixium.playnb.filetype.scalatemplate.helper.ScalaTemplateLanguageHelper;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.ClassIndex;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.openide.awt.StatusDisplayer;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.Line;

@MimeRegistration(mimeType = "text/html", service = HyperlinkProviderExt.class)
public class ScalaTemplateHyperlinkProvider implements HyperlinkProviderExt {

    public static final String CHARS_TO_SEPARATE_CLASS_NAME = "@(";

    private int startOffset, endOffset;
    private ClasspathInfo info;

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
        String textToShow = null;
        try {
            String fileContent = doc.getText(0, doc.getLength());
            String word = doc.getText(startOffset, endOffset - startOffset);
            final Set<ElementHandle<TypeElement>> cpClasses = info.getClassIndex()
                    .getDeclaredTypes("",
                            ClassIndex.NameKind.PREFIX,
                            EnumSet.of(ClassIndex.SearchScope.SOURCE));

            Optional<String> classFullNameOptional = getFullClassNameForWord(word, cpClasses, fileContent);

            if (classFullNameOptional.isPresent()) {
                File fileToOpen = MiscUtil.getPathFile(doc, classFullNameOptional.get());

                if (fileToOpen.exists()) {
                    try {
                        FileObject foToOpen = FileUtil.toFileObject(fileToOpen);
                        String textDocument = foToOpen.asText();
                        int lineNumber = 1;
                        if (Character.isLowerCase(word.codePointAt(0))) {
                            lineNumber = MiscUtil.getLineNumber(textDocument, word);
                        }

                        int linesToShow = 15;
                        textToShow = MiscUtil.getLinesFromFileContent(textDocument, lineNumber, linesToShow)
                                + MiscUtil.LINE_SEPARATOR + "Showing max " + (linesToShow + 1) + " lines ...";
                    } catch (IOException ex) {
                        ExceptionManager.logException(ex);
                    }
                } else {
                    StatusDisplayer.getDefault().setStatusText(fileToOpen.getName() + " doesn't exist in project!");
                }

            }
        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }

        return textToShow;
    }

    @Override
    public void performClickAction(Document doc, int offset, HyperlinkType ht) {
        try {
            String fileContent = doc.getText(0, doc.getLength());
            String word = doc.getText(startOffset, endOffset - startOffset);
            final Set<ElementHandle<TypeElement>> cpClasses = info.getClassIndex()
                    .getDeclaredTypes("",
                            ClassIndex.NameKind.PREFIX,
                            EnumSet.of(ClassIndex.SearchScope.SOURCE));

            Optional<String> classFullNameOptional = getFullClassNameForWord(word, cpClasses, fileContent);

            if (classFullNameOptional.isPresent()) {
                File fileToOpen = MiscUtil.getPathFile(doc, classFullNameOptional.get());

                if (fileToOpen.exists()) {
                    try {
                        FileObject foToOpen = FileUtil.toFileObject(fileToOpen);
                        int lineNumber = 1;
                        if (Character.isLowerCase(word.codePointAt(0))) {
                            lineNumber = MiscUtil.getLineNumber(foToOpen.asText(), word);
                        }

                        LineCookie lc = DataObject.find(foToOpen).getLookup().lookup(LineCookie.class);
                        Line line = lc.getLineSet().getOriginal(lineNumber);
                        line.show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FRONT);
                    } catch (IOException ex) {
                        ExceptionManager.logException(ex);
                    }
                } else {
                    StatusDisplayer.getDefault().setStatusText(fileToOpen.getName() + " doesn't exist in project!");
                }

            }
        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }
    }

    private Optional<String> getFullClassNameForWord(String word, 
            final Set<ElementHandle<TypeElement>> cpClasses, String fileContent) throws IllegalStateException {
        Optional<String> classFullNameOptional = Optional.empty();
        if (Character.isUpperCase(word.codePointAt(0))) {
            classFullNameOptional = ScalaTemplateLanguageHelper.getClassFullNameForHyperLink(cpClasses, word);
        } else {
            Integer maxIndexBefore = CHARS_TO_SEPARATE_CLASS_NAME.chars()
                    .map((int ch) -> fileContent.lastIndexOf(ch, startOffset))
                    .filter((int ch) -> ch >= 0)
                    .max().getAsInt();

            int indexOfLastDotBeforeCaret = fileContent.lastIndexOf(".", startOffset);

            List<String> imports = ScalaTemplateLanguageHelper.getImports(fileContent);
            String nameBeforeWord = fileContent.substring(maxIndexBefore + 1, indexOfLastDotBeforeCaret);
            for (ElementHandle<TypeElement> te : cpClasses) {
                String binaryName = te.getBinaryName();
                if (binaryName.startsWith(nameBeforeWord)) {
                    classFullNameOptional = Optional.of(nameBeforeWord);
                    break;
                } else {
                    for (String anImport : imports) {
                        String nameWithImport = anImport + "." + nameBeforeWord;
                        if (binaryName.startsWith(nameWithImport)) {
                            classFullNameOptional = Optional.of(nameWithImport);
                            break;
                        }
                    }
                }
            }
        }
        return classFullNameOptional;
    }

    private int[] getIdentifierSpan(Document doc, int offset) {
        String fileName = doc.getProperty(Document.TitleProperty).toString();
        if (fileName.endsWith(".scala.html")) {
            try {
                String fileContent = doc.getText(0, doc.getLength());
                Optional<String> elementToNavigateOptional = getElementToNavigate(doc, offset);

                if (elementToNavigateOptional.isPresent()) {
                    String elementName = elementToNavigateOptional.get();
                    startOffset = fileContent.lastIndexOf(elementName, offset);
                    endOffset = startOffset + elementName.length();

                    return new int[]{startOffset, endOffset};
                }

            } catch (Exception ex) {
                ExceptionManager.logException(ex);
            }
        }
        return null;
    }

    private Optional<String> getElementToNavigate(Document doc, int caretPosition) throws BadLocationException {
        String fileContent = doc.getText(0, doc.getLength());
        Optional<String> wordOptional = MiscUtil.getWordFromCaretPosition(fileContent, caretPosition);
        if (wordOptional.isPresent()) {
            String word = wordOptional.get();
            setUpClassPathInfo(doc);
            final Set<ElementHandle<TypeElement>> cpClasses = info.getClassIndex()
                    .getDeclaredTypes("",
                            ClassIndex.NameKind.PREFIX,
                            EnumSet.of(ClassIndex.SearchScope.SOURCE));

            if (Character.isUpperCase(word.codePointAt(0))) {
                for (ElementHandle<TypeElement> te : cpClasses) {
                    String binaryName = te.getBinaryName();
                    if (binaryName.endsWith(word)) {
                        return Optional.of(word);
                    }
                }
            }

            Integer maxIndexBefore = CHARS_TO_SEPARATE_CLASS_NAME.chars()
                    .map((int ch) -> fileContent.lastIndexOf(ch, caretPosition))
                    .filter((int ch) -> ch >= 0)
                    .max().getAsInt();

            int indexOfLastDotBeforeCaret = fileContent.lastIndexOf(".", caretPosition);
            int charsBetweenWordAndLastDot = caretPosition - indexOfLastDotBeforeCaret;

            if (maxIndexBefore < indexOfLastDotBeforeCaret
                    && charsBetweenWordAndLastDot <= word.length()) {

                List<String> imports = ScalaTemplateLanguageHelper.getImports(fileContent);
                String nameBeforeWord = fileContent.substring(maxIndexBefore + 1, indexOfLastDotBeforeCaret);
                for (ElementHandle<TypeElement> te : cpClasses) {
                    String binaryName = te.getBinaryName();
                    if (binaryName.startsWith(nameBeforeWord)
                            || imports.stream().anyMatch(imp -> binaryName.startsWith(imp + "." + nameBeforeWord))) {

                        return Optional.of(word);
                    }
                }
            }
        }

        return Optional.empty();
    }

    private void setUpClassPathInfo(Document doc) {
//        if (info == null) {
        FileObject fo = MiscUtil.getFileObject(doc);
        ClassPath bootCp = ClassPath.getClassPath(fo, ClassPath.BOOT);
        ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
        ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);

        info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
//        }
    }

}
