package com.qualixium.playnb.filetype.scalatemplate.format;

import com.qualixium.playnb.project.general.PlayPanel;
import static com.qualixium.playnb.project.general.PlayPanel.SCALA_TEMPLATE_INDENT_SPACES;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.ReformatTask;
import org.openide.util.NbPreferences;

public class ScalaTemplateReformatTask implements ReformatTask {

    private final Context context;

    public ScalaTemplateReformatTask(Context context) {
        this.context = context;
    }

    @Override
    public void reformat() throws BadLocationException {
        Document document = context.document();
        if (document != null) {
            Object docTitleProperty = document.getProperty(Document.TitleProperty);
            if (docTitleProperty != null) {
                String fileName = docTitleProperty.toString();
                if (fileName.endsWith(".scala.html")) {
                    String fileContent = document.getText(document.getStartPosition().getOffset(),
                            document.getEndPosition().getOffset());
                    int scalaTemplateIndentSpaces = NbPreferences.forModule(PlayPanel.class).getInt(SCALA_TEMPLATE_INDENT_SPACES, 4);
                    String fileFormattedFinal = formatFile(fileContent, scalaTemplateIndentSpaces);
                    int length = document.getLength();
                    document.remove(0, length);
                    document.insertString(0, fileFormattedFinal, null);
                }
            }
        }
    }

    @Override
    public ExtraLock reformatLock() {
        return null;
    }

    public static String formatFile(String scalaTemplateFileContent, int spacesToIncrement) {
        short currentSpacesToUse = 0;

        StringBuilder sbResult = new StringBuilder();

        List<String> listLines = MiscUtil.getLinesFromFileContent(scalaTemplateFileContent);

        for (String line : listLines) {
            if (line.contains("{") && !line.contains("}")) {// line starts a block and is not a one line block
                int indexAfterSeparator;
                if (line.trim().contains("=>")) {

                    indexAfterSeparator = line.indexOf("=>") + 2;
                } else {
                    indexAfterSeparator = line.indexOf("{") + 1;
                }
                String firstLine = line.substring(0, indexAfterSeparator).trim();
                sbResult.append(MiscUtil.getAmountSeparatorChars(currentSpacesToUse))
                        .append(firstLine).append(LINE_SEPARATOR);

                currentSpacesToUse += spacesToIncrement;
                String lineAfterSeparator = line.substring(indexAfterSeparator, line.length());
                if (!lineAfterSeparator.trim().isEmpty()) {
                    sbResult.append(MiscUtil.getAmountSeparatorChars(currentSpacesToUse))
                            .append(lineAfterSeparator.trim()).append(MiscUtil.LINE_SEPARATOR);
                }

            } else if (line.contains("}") && !line.contains("{")) { // line close a block and is not a one line block
                currentSpacesToUse -= spacesToIncrement;
                int indexOfBracket = line.indexOf("}");
                if (indexOfBracket > 0) {
                    String lineBeforeBracket = line.substring(0, indexOfBracket - 1);
                    if (!lineBeforeBracket.trim().isEmpty()) {
                        sbResult.append(lineBeforeBracket).append(MiscUtil.LINE_SEPARATOR);
                    }
                }
                sbResult.append(MiscUtil.getAmountSeparatorChars(currentSpacesToUse)).append("}").append(LINE_SEPARATOR);
                String lineAfterBracket = line.substring(indexOfBracket + 1, line.length());
                if (!lineAfterBracket.trim().isEmpty()) {
                    sbResult.append(lineAfterBracket).append(MiscUtil.LINE_SEPARATOR);
                }
            } else {
                int openTagsAmount = getOpenTagsAmount(line);

                String amountSeparatorChars;
                if (openTagsAmount > 0) {
                    amountSeparatorChars = MiscUtil.getAmountSeparatorChars(currentSpacesToUse);
                    currentSpacesToUse += spacesToIncrement * openTagsAmount;
                } else {
                    currentSpacesToUse += spacesToIncrement * openTagsAmount;
                    amountSeparatorChars = MiscUtil.getAmountSeparatorChars(currentSpacesToUse);
                }

                sbResult.append(amountSeparatorChars).append(line.trim()).append(MiscUtil.LINE_SEPARATOR);
            }
        }

        return sbResult.toString();
    }

    public static int getOpenTagsAmount(String line) {
        int result = 0;
        try {
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char ch = chars[i];
                if (ch == '<') {
                    if (line.contains(">")) {
                        int indexOfGT = line.indexOf(">", i);
                        String tag = line.substring(i, indexOfGT + 1);
                        if (!tag.trim().isEmpty() && !isTagAllowedToBeUnclosed(tag)) {
                            if (!tag.contains("/>") && !tag.contains("-->")) {
                                if (tag.contains("</")) {
                                    result--;
                                } else {
                                    result++;
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ExceptionManager.logException(ex);
        }
        return result;
    }

    private static boolean isTagAllowedToBeUnclosed(String tag) {
        List<String> tagsAllowed = new ArrayList<>();

        tagsAllowed.add("<!DOCTYPE html");
        tagsAllowed.add("<meta");
        tagsAllowed.add("<link");

        return tagsAllowed.stream().anyMatch(tagAllowed -> tag.startsWith(tagAllowed));
    }

}
