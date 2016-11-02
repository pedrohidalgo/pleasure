package com.qualixium.playnb.filetype.routes.markoccurrences;

import com.qualixium.playnb.filetype.routes.RoutesLanguage;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;

@MimeRegistration(mimeType = RoutesLanguage.MIME_TYPE, service = HighlightsLayerFactory.class)
public class RoutesMarkOccurrencesHighlightsLayerFactory implements HighlightsLayerFactory {

    public static RoutesMarkOccurrencesHighlighter getMarkOccurrencesHighlighter(Document doc) {
        RoutesMarkOccurrencesHighlighter highlighter = (RoutesMarkOccurrencesHighlighter) doc.getProperty(RoutesMarkOccurrencesHighlighter.class);
        if (highlighter == null) {
            doc.putProperty(RoutesMarkOccurrencesHighlighter.class, highlighter = new RoutesMarkOccurrencesHighlighter(doc));
        }
        return highlighter;
    }

    @Override
    public HighlightsLayer[] createLayers(Context context) {
        return new HighlightsLayer[]{HighlightsLayer.create(RoutesMarkOccurrencesHighlighter.class.getName(), ZOrder.CARET_RACK.forPosition(2000), true, getMarkOccurrencesHighlighter(context.getDocument()).getHighlightsBag())};
    }
}
