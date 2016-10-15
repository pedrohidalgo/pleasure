package com.qualixium.playnb.filetype.routes.completion;

import com.qualixium.playnb.filetype.routes.RoutesLanguage;
import com.qualixium.playnb.filetype.routes.RoutesLanguageHelper;
import com.qualixium.playnb.filetype.routes.parser.RoutesLineParsedDTO;
import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.StringJoiner;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.filesystems.FileObject;

@MimeRegistration(mimeType = RoutesLanguage.MIME_TYPE, service = CompletionProvider.class)
public class RoutesCompletionProvider implements CompletionProvider {

    private static final char CHAR_TO_START_COMPLETION = ' ';
    private Class javaControllerClass;
    private Class scalaControllerClass;
    private Class javaResultClass;
    private Class scalaActionClass;

    @Override
    public CompletionTask createTask(int queryType, JTextComponent jtc) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }

        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet crs, Document dcmnt, int caretOffset) {
                String filter = null;
                int startOffset = caretOffset - 1;
                String lineUntilcaret = null;
                try {
                    String fileContent = dcmnt.getText(dcmnt.getStartPosition().getOffset(),
                            dcmnt.getEndPosition().getOffset());
                    final StyledDocument bDoc = (StyledDocument) dcmnt;
                    final int lineStartOffset = getRowFirstNonArroba(bDoc, caretOffset);
                    final int length = caretOffset - lineStartOffset;
                    final char[] line = bDoc.getText(lineStartOffset, length <= 0 ? 0 : length).toCharArray();
                    lineUntilcaret = new String(line);
                    final int charToStartOffSet = indexOfChartToStartCompletion(line);
                    filter = new String(line, charToStartOffSet + 1, line.length - charToStartOffSet - 1);
                    if (charToStartOffSet > 0) {
                        startOffset = lineStartOffset + charToStartOffSet + 1;
                    } else {
                        startOffset = lineStartOffset;
                    }

                    if (RoutesLanguageHelper.lineIsEnableToAutoComplete(lineUntilcaret)) {
                        RoutesLineParsedDTO lineParsedDTO = RoutesLanguageHelper.divideLineInColumns(lineUntilcaret);
                        if (lineUntilcaret.trim().isEmpty()
                                || (!lineParsedDTO.httpMethod.isEmpty()
                                && !RoutesLanguageHelper.isWhiteSpaceCharacterAtIndex(lineUntilcaret, lineParsedDTO.httpMethod.length()))) {

                            resolveHTTPMethodCompletion(filter, crs, startOffset, caretOffset);
                        } else if ((!lineParsedDTO.httpMethod.isEmpty()
                                && RoutesLanguageHelper.isWhiteSpaceCharacterAtIndex(lineUntilcaret, lineParsedDTO.httpMethod.length())
                                && lineParsedDTO.url.isEmpty())
                                || (!lineParsedDTO.url.isEmpty()
                                && !RoutesLanguageHelper.isWhiteSpaceCharacterAtIndex(lineUntilcaret,
                                        lineUntilcaret.indexOf(lineParsedDTO.url) + lineParsedDTO.url.length()))) {

                            resolveURLCompletion(fileContent, filter, crs, startOffset, caretOffset);

                        } else if (!lineParsedDTO.method.isEmpty()
                                && lineParsedDTO.method.substring(0, lineParsedDTO.method.lastIndexOf(".") == -1 ? 0 : lineParsedDTO.method.lastIndexOf("."))
                                        .chars().anyMatch(ch -> Character.isUpperCase(ch))) {

                            resolveMethodCompletion(lineParsedDTO, dcmnt, crs, startOffset, caretOffset);

                        } else {
                            resolveClassCompletion(dcmnt, filter, crs, startOffset, caretOffset);
                        }
                    }
                } catch (BadLocationException | ClassNotFoundException | IOException ex) {
                    //FUTURE: provide documentation for the entries in the code completion box for build.sbt, plugins.sbt, etc.
                    ExceptionManager.logException(ex);
                }
                crs.finish();
            }

            private void resolveClassCompletion(Document dcmnt, String filter, CompletionResultSet crs, int startOffset, int caretOffset) throws ClassNotFoundException, IllegalStateException {
                try {
                    FileObject fo = MiscUtil.getFileObject(dcmnt);
                    ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);

                    setUpPlayClasses(compileCp);

                    List<String> listClassNames = RoutesLanguageHelper.getFullClassNamesFromSourceDir(PlayProjectUtil.getPlayProject(fo).get());
                    for (String className : listClassNames) {
                        if (className.startsWith(filter)) {
                            Class<?> clazz = compileCp.getClassLoader(true).loadClass(className);
                            if (javaControllerClass.isAssignableFrom(clazz)
                                    || scalaControllerClass.isAssignableFrom(clazz)) {
                                crs.addItem(new RoutesMethodClassCompletionItem(className, startOffset, caretOffset));
                            }
                        }
                    }
                } catch (ClassNotFoundException ex) {
                }
            }

            private void resolveMethodCompletion(RoutesLineParsedDTO lineParsedDTO, Document dcmnt,
                    CompletionResultSet crs, int startOffset, int caretOffset)
                    throws IOException, SecurityException, ClassNotFoundException {
                int lastDOTIndex = lineParsedDTO.method.lastIndexOf(".");
                String className = lineParsedDTO.method.substring(0, lastDOTIndex);
                final String filterForMethod = lineParsedDTO.method.substring(lastDOTIndex + 1, lineParsedDTO.method.length());

                FileObject fo = MiscUtil.getFileObject(dcmnt);
                ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
                setUpPlayClasses(compileCp);
                Class<?> clazz;
                try {
                    clazz = compileCp.getClassLoader(true).loadClass(className);

                    if (javaControllerClass.isAssignableFrom(clazz) || scalaControllerClass.isAssignableFrom(clazz)) {
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.getName().startsWith(filterForMethod)
                                    && (method.getReturnType().equals(javaResultClass) || method.getReturnType().equals(scalaActionClass))) {
                                StringJoiner sjParameters = new StringJoiner(", ", "(", ")");
                                Class<?>[] parameterTypes = method.getParameterTypes();
                                for (Class<?> parameterType : parameterTypes) {
                                    sjParameters.add(parameterType.getSimpleName());
                                }

                                final String parameterPart = parameterTypes.length > 0 ? sjParameters.toString() : "";
                                crs.addItem(new RoutesMethodMethodCompletionItem(method.getName() + parameterPart, startOffset + lastDOTIndex + 1, caretOffset));
                            }
                        }
                    }
                } catch (ClassNotFoundException ex) {
                }
            }

            private void resolveURLCompletion(String fileContent, String filter, CompletionResultSet crs, int startOffset, int caretOffset) {
                RoutesLanguageHelper.getAllUrlsFromRoutesFile(fileContent).stream()
                        .filter((url) -> (url.toLowerCase().startsWith(filter.toLowerCase())))
                        .forEach((url) -> {
                            crs.addItem(new RoutesURLCompletionItem(url, startOffset, caretOffset));
                        });
            }

            private void resolveHTTPMethodCompletion(String filter, CompletionResultSet crs, int startOffset, int caretOffset) {
                RoutesLanguageHelper.getHttpMethods().stream()
                        .filter((httpMethod) -> (httpMethod.toLowerCase().startsWith(filter.toLowerCase())))
                        .forEach((httpMethod) -> {
                            crs.addItem(new RoutesHTTPMethodCompletionItem(httpMethod, startOffset, caretOffset));
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

    private void setUpPlayClasses(ClassPath compileClassPath) throws ClassNotFoundException {
        ClassLoader classLoader = compileClassPath.getClassLoader(true);
//        if (javaControllerClass == null) {
        javaControllerClass = classLoader.loadClass("play.mvc.Controller");
        javaResultClass = classLoader.loadClass("play.mvc.Result");
//        }
//        if (scalaControllerClass == null) {
        scalaControllerClass = classLoader.loadClass("play.api.mvc.Controller");
        scalaActionClass = classLoader.loadClass("play.api.mvc.Action");
//        }
    }

}
