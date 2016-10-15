package com.qualixium.playnb.filetype.sbt.format;

import com.qualixium.playnb.filetype.sbt.SBTLanguage;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ReformatTask;

@MimeRegistration(mimeType = SBTLanguage.MIME_TYPE, service = ReformatTask.Factory.class)
public class SBTReformatTaskFactory implements ReformatTask.Factory {

    @Override
    public ReformatTask createTask(Context context) {
        return new SBTReformatTask(context);
    }
}
