package com.qualixium.playnb.filetype.sbt.completion;

import com.qualixium.playnb.filetype.sbt.SBTLanguage;
import com.qualixium.playnb.util.ExceptionManager;
import static com.qualixium.playnb.util.ui.AutoCompletionUtil.getFontColorToUse;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;

public class SBTCompletionItem implements CompletionItem {

    private final String text;
    private final int startOffset;
    private final int caretOffset;

    public SBTCompletionItem(String text, int startOffset, int caretOffset) {
        this.text = text;
        this.startOffset = startOffset;
        this.caretOffset = caretOffset;
    }

    @Override
    public void defaultAction(JTextComponent jtc) {
        try {
            StyledDocument doc = (StyledDocument) jtc.getDocument();
            doc.remove(startOffset, caretOffset - startOffset);
            doc.insertString(startOffset, text, null);
            Completion.get().hideAll();
        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }
    }

    @Override
    public void processKeyEvent(KeyEvent ke) {

    }

    @Override
    public int getPreferredWidth(Graphics graphics, Font font) {
        return CompletionUtilities.getPreferredWidth(text, null, graphics, font);
    }

    @Override
    public void render(Graphics graphics, Font font, Color color, Color color1,
            int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(SBTLanguage.IMAGE_ICON, text, null, graphics, font,
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
    public boolean instantSubstitution(JTextComponent jtc) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 0;
    }

    @Override
    public CharSequence getSortText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    }
}
