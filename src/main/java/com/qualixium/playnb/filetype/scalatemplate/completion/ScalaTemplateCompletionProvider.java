package com.qualixium.playnb.filetype.scalatemplate.completion;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.classpath.ClassPathUtil;
import com.qualixium.playnb.filetype.scalatemplate.helper.ScalaTemplateLanguageHelper;
import com.qualixium.playnb.filetype.scalatemplate.helper.TemplateParameter;
import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.CLASSPATH_TYPE_ELEMENT_FILTER;
import com.qualixium.playnb.util.MiscUtil.Language;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.TypeElement;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.ClassIndex;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.filesystems.FileObject;

@MimeRegistration(mimeType = "text/html", service = CompletionProvider.class)
public class ScalaTemplateCompletionProvider implements CompletionProvider {

    private static final char CHAR_TO_START_COMPLETION = '@';

    @Override
    public CompletionTask createTask(int queryType, JTextComponent jtc) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }

        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet crs, Document dcmnt, int caretOffset) {
                String fileName = dcmnt.getProperty(Document.TitleProperty).toString();
                if (fileName.endsWith(".scala.html")) {
                    String filter;
                    int startOffset;
                    try {
                            String fileContent = dcmnt.getText(dcmnt.getStartPosition().getOffset(),
                                    dcmnt.getEndPosition().getOffset());
                            final StyledDocument bDoc = (StyledDocument) dcmnt;
                            final int lineStartOffset = getRowFirstNonArroba(bDoc, caretOffset);
                            final int length = caretOffset - lineStartOffset;
                            final char[] line = bDoc.getText(lineStartOffset, length <= 0 ? 0 : length).toCharArray();
                            final int charToStartOffSet = indexOfChartToStartCompletion(line);
                            filter = new String(line, charToStartOffSet + 1, line.length - charToStartOffSet - 1);
                            if (!filter.contains("routes.Assets.")) {
                                if (charToStartOffSet > 0) {
                                    startOffset = lineStartOffset + charToStartOffSet + 1;
                                } else {
                                    startOffset = lineStartOffset;
                                }

                                FileObject fo = MiscUtil.getFileObject(dcmnt);
                                ClassPath bootCp = ClassPath.getClassPath(fo, ClassPath.BOOT);
                                ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
                                ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);

                                final ClasspathInfo info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
                                final Set<ElementHandle<TypeElement>> cpClasses = info.getClassIndex()
                                        .getDeclaredTypes("",
                                                ClassIndex.NameKind.PREFIX,
                                                EnumSet.of(ClassIndex.SearchScope.SOURCE,
                                                        ClassIndex.SearchScope.DEPENDENCIES));

                                List<TemplateParameter> listConstructorParameters = ScalaTemplateLanguageHelper.getConstructorParameters(fileContent);
                                int lastDotIndex = filter.lastIndexOf(".");
                                String elementToEvaluate = filter.substring(0, lastDotIndex > 0 ? lastDotIndex : 0);
                                int indexOfFirstDot = filter.indexOf(".");
                                String paramVariableName = filter.substring(0, indexOfFirstDot > 0 ? indexOfFirstDot : 0);
                                boolean elementIsATemplateParameter = ScalaTemplateLanguageHelper.isTemplateParameter(fileContent, caretOffset, paramVariableName);

                                if (!elementToEvaluate.chars().anyMatch(ch -> Character.isUpperCase(ch))
                                        && !elementIsATemplateParameter) {
                                    resolveFirstPartCompletion(fo, cpClasses, fileContent, filter, crs, startOffset, caretOffset);
                                } else {
                                    resolveMemberCompletion(fileContent, filter, paramVariableName, listConstructorParameters, compileCp,
                                            cpClasses, fo, crs, startOffset, caretOffset);
                                }
                            }
                    } catch (ClassNotFoundException ex) {
                        //do nothing if classNotFoundException
                    } catch (BadLocationException ex) {
                        ExceptionManager.logException(ex);
                    }
                }
                crs.finish();
            }

            private void resolveMemberCompletion(String fileContent, String filter, String paramVariableName, List<TemplateParameter> listConstructorParameters,
                    ClassPath compileCp, Set<ElementHandle<TypeElement>> cpClasses, FileObject fo,
                    CompletionResultSet crs, int startOffset, int caretOffset) throws ClassNotFoundException, SecurityException {

                PlayProject playProject = PlayProjectUtil.getPlayProject(fo).get();
                Language languageConfigured = PlayProjectUtil.getLanguageConfigured(fo);
                int lastDotIndex = filter.lastIndexOf(".");
                Optional<TemplateParameter> tpOptional;
                Optional<TemplateParameter> blockParameterOptional = ScalaTemplateLanguageHelper.getBlockParameter(fileContent, caretOffset);

                if (blockParameterOptional.isPresent()
                        && blockParameterOptional.get().variableName.equals(paramVariableName)) {
                    tpOptional = blockParameterOptional;
                } else {
                    tpOptional = listConstructorParameters.stream()
                            .filter(cp -> cp.variableName.equals(paramVariableName))
                            .findFirst();
                }

                String filterMember = "";
                if (!filter.endsWith(".")) {
                    filterMember = filter.substring(lastDotIndex + 1, filter.length());
                }

                Optional<Class> classOptional = Optional.empty();
                if (tpOptional.isPresent()) {
                    TemplateParameter templateParameter = tpOptional.get();

                    String classFullName = ScalaTemplateLanguageHelper.getClassFullName(
                            templateParameter.variableType, languageConfigured);
                    Optional<Class<?>> currentClazzOpt = ClassPathUtil.loadClass(playProject, classFullName);

                    if (!currentClazzOpt.isPresent()) {
                        Optional<String> classFullNameOptional = ScalaTemplateLanguageHelper.getClassFullName(cpClasses, templateParameter.variableType);
                        if (classFullNameOptional.isPresent()) {
                            currentClazzOpt = Optional.of(compileCp.getClassLoader(true).loadClass(classFullNameOptional.get()));
                        }
                    }

                    if (currentClazzOpt.isPresent()) {

                        classOptional = ScalaTemplateLanguageHelper.getClassToProvideCompletionForConstructorParameter(
                                filter, templateParameter.variableName, currentClazzOpt.get());
                    }
                } else {
                    classOptional = ScalaTemplateLanguageHelper.getClassToProvideCompletion(fileContent,
                            filter, compileCp.getClassLoader(true), languageConfigured);
                }

                //***************************** Adding ITEMS to completion ************************************
                if (classOptional.isPresent()) {

                    Class clazz = classOptional.get();
                    for (Field field : clazz.getFields()) {
                        if (field.getName().toLowerCase().startsWith(filterMember.toLowerCase())) {
                            if (tpOptional.isPresent() || Modifier.isStatic(field.getModifiers())) {
                                String fieldName = ScalaTemplateLanguageHelper.getCompletionItemMemberText(field);
                                crs.addItem(new ScalaTemplateFieldCompletionItem(fieldName, startOffset + lastDotIndex + 1, caretOffset));
                            }
                        }
                    }

                    //FUTURE  add the RichInt's methods :       RichBoolean RichByte RichChar RichDouble RichException RichFloat RichInt RichLong RichShort
                    for (Method method : clazz.getMethods()) {
                        if (method.getName().toLowerCase().startsWith(filterMember.toLowerCase())) {
                            if (tpOptional.isPresent() || Modifier.isStatic(method.getModifiers())) {
                                String methodName = ScalaTemplateLanguageHelper.getCompletionItemMethodText(method);
                                crs.addItem(new ScalaTemplateMethodCompletionItem(methodName, startOffset + lastDotIndex + 1, caretOffset));
                            }
                        }
                    }
                }

            }

            private void resolveFirstPartCompletion(FileObject fo, Set<ElementHandle<TypeElement>> cpClasses, String fileContent,
                    String filter, CompletionResultSet crs, int startOffset, int caretOffset)
                    throws ClassNotFoundException, IllegalStateException {

                List<String> listResults = transformListClass(fo, cpClasses, fileContent, caretOffset);
                listResults.stream()
                        .filter((itemName) -> itemName.toLowerCase().startsWith(filter.toLowerCase()))
                        .forEach((itemName) -> {
                            crs.addItem(new ScalaTemplateClassCompletionItem(itemName, startOffset, caretOffset));
                        });
            }

        }, jtc);
    }

    @Override
    public int getAutoQueryTypes(JTextComponent jtc, String string) {
        return 0;

    }

    static int getRowFirstNonArroba(StyledDocument doc, int offset) throws BadLocationException {
        Element lineElement = doc.getParagraphElement(offset);
        int start = lineElement.getStartOffset();
        while (start + 1 < lineElement.getEndOffset()) {
            try {
                if (doc.getText(start, 1).charAt(0) != CHAR_TO_START_COMPLETION) {
                    break;
                }
            } catch (BadLocationException ex) {
                throw (BadLocationException) new BadLocationException("calling getText(" + start + ", " + (start + 1) + ") on doc of length: " + doc.getLength(), start).initCause(ex);
            }
            start++;
        }
        return start;
    }

    static int indexOfChartToStartCompletion(char[] line) {
        int i = line.length;
        while (--i > -1) {
            final char c = line[i];
            if (c == CHAR_TO_START_COMPLETION) {
                return i;
            }
        }
        return -1;
    }

    private static List<String> transformListClass(FileObject fo,
            Set<ElementHandle<TypeElement>> elementHandles, String fileContent, int caretPosition) {

        List<String> listCPProcessed = elementHandles.stream()
                .filter(CLASSPATH_TYPE_ELEMENT_FILTER)
                .map(element -> element.getBinaryName())
                .collect(Collectors.toList());

        listCPProcessed.addAll(ScalaTemplateLanguageHelper
                .getAllScalaTemplateFileNames(elementHandles)
                .stream()
                .map(templateFileName -> templateFileName.replace("views.html.", ""))
                .collect(Collectors.toList()));

        //if java language is configured: so java.util is automatically import
        Language languageConfigured = PlayProjectUtil.getLanguageConfigured(fo);
        if (languageConfigured.equals(Language.JAVA)) {
            List<String> listJavaPackageUtilClasses = listCPProcessed.stream()
                    .filter(binaryName -> binaryName.startsWith("java.util."))
                    .map(binaryName -> binaryName.replace("java.util.", ""))
                    .collect(Collectors.toList());

            listCPProcessed.addAll(listJavaPackageUtilClasses);
        }

        listCPProcessed.addAll(ScalaTemplateLanguageHelper.getKeywords());
        listCPProcessed.addAll(ScalaTemplateLanguageHelper.getConstructorParameters(fileContent)
                .stream()
                .map(cp -> cp.variableName)
                .collect(Collectors.toList()));

        Optional<TemplateParameter> optionalTP = ScalaTemplateLanguageHelper.getBlockParameter(fileContent, caretPosition);
        if (optionalTP.isPresent()) {
            listCPProcessed.add(optionalTP.get().variableName);
        }

        //Add imported classes
        List<String> imports = ScalaTemplateLanguageHelper.getImports(fileContent);
        
        listCPProcessed.addAll(
                listCPProcessed.stream()
                .filter(cpProcessed -> imports.stream().anyMatch(imp -> cpProcessed.startsWith(imp)))
                .map(cpProcessed -> {
                    for (String anImport : imports) {
                        if (cpProcessed.startsWith(anImport)) {
                            return cpProcessed.replace(anImport + ".", "");
                        }
                    }
                    return null;
                })
                .filter(cpProcessed -> cpProcessed != null)
                .collect(Collectors.toList())
        );

        return listCPProcessed;
    }

}
