package com.qualixium.playnb.filetype.sbt.format;

import com.qualixium.playnb.project.general.PlayPanel;
import static com.qualixium.playnb.project.general.PlayPanel.SBT_INDENT_SPACES;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import static com.qualixium.playnb.util.MiscUtil.getOpenSymbolsAmount;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.ReformatTask;
import org.openide.util.NbPreferences;

public class SBTReformatTask implements ReformatTask {

    private final Context context;

    public SBTReformatTask(Context context) {
        this.context = context;
    }

    @Override
    public void reformat() throws BadLocationException {
        Document document = context.document();
        String fileContent = document.getText(document.getStartPosition().getOffset(),
                document.getEndPosition().getOffset());
        int sbtIndentSpaces = NbPreferences.forModule(PlayPanel.class).getInt(SBT_INDENT_SPACES, 4);
        String fileFormattedFinal = formatFile(fileContent, sbtIndentSpaces);
        int length = document.getLength();
        document.remove(0, length);
        document.insertString(0, fileFormattedFinal, null);
    }

    @Override
    public ExtraLock reformatLock() {
        return null;
    }

    public static String formatFile(String fileContent, int spacesToIncrement) {
        short currentSpacesToUse = 0;

        StringBuilder sbResult = new StringBuilder();

        List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);
        for (String line : listLines) {
            int openTagsAmount = getOpenSymbolsAmount(line);

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

        String stringResult = processLineSeparators(sbResult.toString());

        return stringResult;
    }

    public static String processLineSeparators(String fileContent) {
        List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);
        String[] lines = listLines.toArray(new String[listLines.size()]);
        StringBuilder sbResult = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String nextLine = "";
            if (i + 1 < lines.length) {
                nextLine = lines[i + 1];
            }

            if (line.trim().isEmpty() && nextLine.trim().isEmpty()) {
                //two consecutives lines empty... do not print anything
            } else {
                sbResult.append(line).append(LINE_SEPARATOR);
                if (line.contains("libraryDependencies")
                        && line.contains(" +=")
                        && !nextLine.trim().isEmpty()) {
                    sbResult.append(LINE_SEPARATOR);
                }
            }
        }

        return sbResult.toString();
    }

}
