package com.qualixium.playnb.filetype.routes.hints;

import com.qualixium.playnb.filetype.routes.parser.RoutesParsingError.RoutesErrorEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.api.Hint;
import org.netbeans.modules.csl.api.HintFix;
import org.netbeans.modules.csl.api.HintsProvider;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.Rule;
import org.netbeans.modules.csl.api.RuleContext;
import org.netbeans.modules.csl.spi.ParserResult;

public class RoutesHintsProvider implements HintsProvider {

    @Override
    public void computeHints(HintsManager manager, RuleContext context, List<Hint> hints) {
    }

    @Override
    public void computeSuggestions(HintsManager manager, RuleContext context, List<Hint> suggestions, int caretOffset) {
    }

    @Override
    public void computeSelectionHints(HintsManager manager, RuleContext context, List<Hint> suggestions, int start, int end) {
    }

    @Override
    public void computeErrors(HintsManager manager, RuleContext context, List<Hint> hints, List<Error> listErrors) {
        listErrors.addAll(context.parserResult.getDiagnostics());

        createHints(context, hints, listErrors);
    }

    @Override
    public void cancel() {
    }

    @Override
    public List<Rule> getBuiltinRules() {
        return Collections.<Rule>emptyList();
    }

    @Override
    public RuleContext createRuleContext() {
        return new RuleContext();
    }

    private void createHints(RuleContext ctx, List<Hint> hints, List<Error> listErrors) {

        listErrors.stream().forEach(error -> {

            if (RoutesErrorEnum.METHOD_DOES_NOT_EXISTS.name().equals(error.getKey())) {
                createMethodHintFix(ctx, hints, error);
            }

        });

    }

    private void createMethodHintFix(RuleContext ctx, List<Hint> hints, Error error) {
        ParserResult parserResult = ctx.parserResult;
        List<HintFix> listFixes = new ArrayList<>();
        listFixes.add(new CreateMethodHintFix(ctx, error));

        Hint hint = new Hint(new RoutesHintRule(), "",
                parserResult.getSnapshot().getSource().getFileObject(),
                new OffsetRange(error.getStartPosition(), error.getEndPosition()),
                listFixes, 50);

        hints.add(hint);
    }

}
