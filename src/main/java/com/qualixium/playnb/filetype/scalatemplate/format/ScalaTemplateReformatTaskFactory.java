package com.qualixium.playnb.filetype.scalatemplate.format;

import com.qualixium.playnb.filetype.scalatemplate.ScalaTemplateLanguage;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ReformatTask;

@MimeRegistration(mimeType = ScalaTemplateLanguage.MIME_TYPE, service = ReformatTask.Factory.class)
public class ScalaTemplateReformatTaskFactory implements ReformatTask.Factory {

    @Override
    public ReformatTask createTask(Context context) {
        return new ScalaTemplateReformatTask(context);
    }
}
