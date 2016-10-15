package com.qualixium.playnb.filetype.routes.parser;

import com.qualixium.playnb.util.ExceptionManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;

public class RoutesSyntaxErrorHighlightingTask extends ParserResultTask<ParserResult> {

    @Override
    public void run(ParserResult result, SchedulerEvent event) {
        List<? extends Error> listDiagnostics = result.getDiagnostics();
        Document document = result.getSnapshot().getSource().getDocument(false);
        List<ErrorDescription> errors = new ArrayList<>();
        listDiagnostics.stream().forEach((error) -> {
            try {
                ErrorDescription errorDescription = ErrorDescriptionFactory
                        .createErrorDescription(
                                Severity.ERROR,
                                "",
                                document,
                                document.createPosition(error.getStartPosition()),
                                document.createPosition(error.getEndPosition()));
                errors.add(errorDescription);
            } catch (BadLocationException ex) {
                ExceptionManager.logException(ex);
            }
        });
        HintsController.setErrors(document, "idontknow", errors);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {

    }

}
