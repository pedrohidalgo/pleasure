package com.qualixium.playnb.filetype.conf.format;

import com.qualixium.playnb.filetype.conf.ConfLanguage;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ReformatTask;

@MimeRegistration(mimeType = ConfLanguage.MIME_TYPE, service = ReformatTask.Factory.class)
public class ConfReformatTaskFactory implements ReformatTask.Factory {

    @Override
    public ReformatTask createTask(Context context) {
        return new ConfReformatTask(context);
    }
}
