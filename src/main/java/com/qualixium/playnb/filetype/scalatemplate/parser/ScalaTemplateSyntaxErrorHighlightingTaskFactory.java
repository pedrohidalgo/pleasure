package com.qualixium.playnb.filetype.scalatemplate.parser;

import java.util.Collection;
import java.util.Collections;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.TaskFactory;

@MimeRegistration(mimeType = "text/html", service = TaskFactory.class)
public class ScalaTemplateSyntaxErrorHighlightingTaskFactory extends TaskFactory {

    @Override
    public Collection create(Snapshot snapshot) {
        String nameExt = snapshot.getSource().getFileObject().getNameExt();
        if (nameExt.endsWith(".scala.html")) {
            return Collections.singleton(new ScalaTemplateSyntaxErrorHighlightingTask());
        }

        return null;
    }
}
