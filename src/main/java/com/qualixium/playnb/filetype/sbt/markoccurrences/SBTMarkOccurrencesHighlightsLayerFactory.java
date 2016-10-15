package com.qualixium.playnb.filetype.sbt.markoccurrences;

import com.qualixium.playnb.filetype.sbt.SBTLanguage;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;

@MimeRegistration(mimeType = SBTLanguage.MIME_TYPE, service = HighlightsLayerFactory.class)
public class SBTMarkOccurrencesHighlightsLayerFactory implements HighlightsLayerFactory {

    public static SBTMarkOccurrencesHighlighter getMarkOccurrencesHighlighter(Document doc) {
        SBTMarkOccurrencesHighlighter highlighter = (SBTMarkOccurrencesHighlighter) doc.getProperty(SBTMarkOccurrencesHighlighter.class);
        if (highlighter == null) {
            doc.putProperty(SBTMarkOccurrencesHighlighter.class, highlighter = new SBTMarkOccurrencesHighlighter(doc));
        }
        return highlighter;
    }

    @Override
    public HighlightsLayer[] createLayers(Context context) {
        return new HighlightsLayer[]{HighlightsLayer.create(SBTMarkOccurrencesHighlighter.class.getName(), ZOrder.CARET_RACK.forPosition(2000), true, getMarkOccurrencesHighlighter(context.getDocument()).getHighlightsBag())};
    }
}
