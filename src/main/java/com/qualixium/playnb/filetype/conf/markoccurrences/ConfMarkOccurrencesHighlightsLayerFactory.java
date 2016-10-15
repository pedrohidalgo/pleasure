package com.qualixium.playnb.filetype.conf.markoccurrences;

import com.qualixium.playnb.filetype.conf.ConfLanguage;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;

@MimeRegistration(mimeType = ConfLanguage.MIME_TYPE, service = HighlightsLayerFactory.class)
public class ConfMarkOccurrencesHighlightsLayerFactory implements HighlightsLayerFactory {

    public static ConfMarkOccurrencesHighlighter getMarkOccurrencesHighlighter(Document doc) {
        ConfMarkOccurrencesHighlighter highlighter = (ConfMarkOccurrencesHighlighter) doc.getProperty(ConfMarkOccurrencesHighlighter.class);
        if (highlighter == null) {
            doc.putProperty(ConfMarkOccurrencesHighlighter.class, highlighter = new ConfMarkOccurrencesHighlighter(doc));
        }
        return highlighter;
    }

    @Override
    public HighlightsLayer[] createLayers(Context context) {
        return new HighlightsLayer[]{HighlightsLayer.create(ConfMarkOccurrencesHighlighter.class.getName(), ZOrder.CARET_RACK.forPosition(2000), true, getMarkOccurrencesHighlighter(context.getDocument()).getHighlightsBag())};
    }
}
