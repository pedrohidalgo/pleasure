package com.qualixium.playnb.filetype.routes.parser;

import com.qualixium.playnb.filetype.routes.RoutesLanguage;
import java.util.Collection;
import java.util.Collections;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.TaskFactory;

@MimeRegistration(mimeType = RoutesLanguage.MIME_TYPE, service = TaskFactory.class)
public class RoutesSyntaxErrorHighlightingTaskFactory extends TaskFactory {

    @Override
    public Collection create(Snapshot snapshot) {
        return Collections.singleton(new RoutesSyntaxErrorHighlightingTask());
    }
}
