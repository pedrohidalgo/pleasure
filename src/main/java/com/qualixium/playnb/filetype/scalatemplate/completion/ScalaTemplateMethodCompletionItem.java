package com.qualixium.playnb.filetype.scalatemplate.completion;

import com.qualixium.playnb.util.ExceptionManager;
import static com.qualixium.playnb.util.ui.AutoCompletionUtil.getFontColorToUse;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.ImageUtilities;

public class ScalaTemplateMethodCompletionItem implements CompletionItem {

    //FUTURE in the autocomplete use the netbeans ${variableName} notation (LET'S MAKE IT SOOOO COOOOOL)
    //FUTURE in the render HTML method think if I should render the return type aligned to the right
    //FUTURE render the arguments  or returned type with another color
    private final String text;
    private static final ImageIcon fieldIcon = new ImageIcon(ImageUtilities.loadImage("com/qualixium/playnb/method_icon.png"));
    private final int startOffset;
    private final int caretOffset;

    public ScalaTemplateMethodCompletionItem(String text, int startOffset, int caretOffset) {
        this.text = text;
        this.startOffset = startOffset;
        this.caretOffset = caretOffset;
    }

    @Override
    public void defaultAction(JTextComponent jtc) {
        try {
            StyledDocument doc = (StyledDocument) jtc.getDocument();
            doc.remove(startOffset, caretOffset - startOffset);

            if (text.contains("(")) {

                String params = text.substring(text.indexOf("(") + 1, text.indexOf(")"));
                String paramsSanitizated = sanitizateParams(params);
                String textWithOutParams = text.substring(0, text.indexOf("("));
                doc.insertString(startOffset, textWithOutParams + paramsSanitizated, null);

                jtc.setSelectionStart(startOffset + text.indexOf("(") + 1);
                jtc.setSelectionEnd(startOffset + text.indexOf(":"));
            } else {
                doc.insertString(startOffset, text.substring(0, text.indexOf(":")), null);
            }

            Completion.get().hideAll();
        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }
    }

    @Override
    public void processKeyEvent(KeyEvent ke
    ) {

    }

    @Override
    public int getPreferredWidth(Graphics graphics, Font font
    ) {
        return CompletionUtilities.getPreferredWidth(text, null, graphics, font);
    }

    @Override
    public void render(Graphics graphics, Font font, Color color, Color color1,
            int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(fieldIcon, text, null, graphics, font,
                getFontColorToUse(selected), width, height, selected);
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return null;
    }

    @Override
    public CompletionTask createToolTipTask() {
        return null;
    }

    @Override
    public boolean instantSubstitution(JTextComponent jtc
    ) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 5;
    }

    @Override
    public CharSequence getSortText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    }

    private static String sanitizateParams(String text) {
        List<String> listParameters = Arrays.asList(text.split(","));

        return listParameters.stream()
                .map(par -> par.substring(0, par.indexOf(":")))
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
