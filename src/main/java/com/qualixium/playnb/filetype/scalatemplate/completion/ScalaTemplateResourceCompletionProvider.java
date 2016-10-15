package com.qualixium.playnb.filetype.scalatemplate.completion;

import com.qualixium.playnb.filetype.scalatemplate.helper.ScalaTemplateUtil;
import com.qualixium.playnb.util.ExceptionManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.filesystems.FileObject;

@MimeRegistration(mimeType = "text/html", service = CompletionProvider.class, position = -1000)
public class ScalaTemplateResourceCompletionProvider implements CompletionProvider {

    private static final char CHAR_TO_START_COMPLETION = '"';

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
                        final StyledDocument bDoc = (StyledDocument) dcmnt;
                        final int lineStartOffset = getRowFirstNonArroba(bDoc, caretOffset);
                        final int length = caretOffset - lineStartOffset;
                        final char[] line = bDoc.getText(lineStartOffset, length <= 0 ? 0 : length).toCharArray();
                        final int charToStartOffSet = indexOfChartToStartCompletion(line);
                        filter = new String(line, charToStartOffSet + 1, line.length - charToStartOffSet - 1);
                        if (new String(line).contains("routes.Assets.")) {
                            if (charToStartOffSet > 0) {
                                startOffset = lineStartOffset + charToStartOffSet + 1;
                            } else {
                                startOffset = lineStartOffset;
                            }

                            Optional<FileObject> foResourceRootOptional = ScalaTemplateUtil.getFileObjectResourcesRoot(dcmnt);

                            if (foResourceRootOptional.isPresent()) {
                                FileObject foResourceRoot = foResourceRootOptional.get();
                                List<FileObject> listFOChildrens = new ArrayList<>();
                                Enumeration<? extends FileObject> childrens = foResourceRoot.getChildren(true);
                                while (childrens.hasMoreElements()) {
                                    listFOChildrens.add(childrens.nextElement());
                                }

                                listFOChildrens.stream()
                                        .forEach(foChild -> {
                                            if (!foChild.isFolder()) {
                                                String childPath = foChild.getPath()
                                                        .replace(foResourceRoot.getPath(), "").substring(1);
                                                if (childPath.contains(filter)) {
                                                    crs.addItem(new ScalaTemplateResourceCompletionItem(childPath, startOffset, caretOffset));
                                                }
                                            }
                                        });
                            }
                        }
                    } catch (BadLocationException ex) {
                        ExceptionManager.logException(ex);
                    }
                }
                crs.finish();
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

}
