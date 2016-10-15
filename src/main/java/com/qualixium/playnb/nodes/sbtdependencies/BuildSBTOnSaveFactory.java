package com.qualixium.playnb.nodes.sbtdependencies;

import com.qualixium.playnb.filetype.sbt.SBTLanguage;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.document.OnSaveTask;

@MimeRegistration(mimeType = SBTLanguage.MIME_TYPE, service = OnSaveTask.Factory.class, position = 1000)
public class BuildSBTOnSaveFactory implements OnSaveTask.Factory {

    @Override
    public OnSaveTask createTask(OnSaveTask.Context context) {
        return new BuildSBTOnSaveTask(context);
    }

}
