package com.qualixium.playnb.filetype.routes.parser;

import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;

public class RoutesParser extends Parser {

    private Snapshot snapshot;

    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent event) throws ParseException {
        this.snapshot = snapshot;
    }

    @Override
    public Result getResult(Task task) throws ParseException {
        return new ParserResult(snapshot) {

            @Override
            public List<? extends Error> getDiagnostics() {
                Document document = snapshot.getSource().getDocument(false);
                if (document != null) {
                    return RoutesValidator.validateFile(document);
                } else {
                    return Collections.EMPTY_LIST;
                }
            }

            @Override
            protected void invalidate() {
            }
        };
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
    }

}
