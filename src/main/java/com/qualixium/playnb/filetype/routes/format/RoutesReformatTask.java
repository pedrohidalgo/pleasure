package com.qualixium.playnb.filetype.routes.format;

import com.qualixium.playnb.filetype.routes.RoutesLanguageHelper;
import com.qualixium.playnb.project.general.PlayPanel;
import static com.qualixium.playnb.project.general.PlayPanel.ROUTES_INDENT_SPACES;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.ReformatTask;
import org.openide.util.NbPreferences;

public class RoutesReformatTask implements ReformatTask {

    private final Context context;

    public RoutesReformatTask(Context context) {
        this.context = context;
    }

    @Override
    public void reformat() throws BadLocationException {
        Document document = context.document();
        String fileContent = document.getText(document.getStartPosition().getOffset(),
                document.getEndPosition().getOffset());

        int routesIndentSpaces = NbPreferences.forModule(PlayPanel.class).getInt(ROUTES_INDENT_SPACES, 4);
        String fileFormatted = RoutesLanguageHelper.formatFile(fileContent, routesIndentSpaces);
        document.remove(0, document.getEndPosition().getOffset() - 1);
        document.insertString(0, fileFormatted, null);
    }

    @Override
    public ExtraLock reformatLock() {
        return null;
    }
}
