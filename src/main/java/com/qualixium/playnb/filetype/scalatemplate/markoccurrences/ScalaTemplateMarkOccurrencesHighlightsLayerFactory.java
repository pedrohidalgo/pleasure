package com.qualixium.playnb.filetype.scalatemplate.markoccurrences;

import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;

@MimeRegistration(mimeType = "text/html", service = HighlightsLayerFactory.class)
public class ScalaTemplateMarkOccurrencesHighlightsLayerFactory implements HighlightsLayerFactory {

    public static ScalaTemplateMarkOccurrencesHighlighter getMarkOccurrencesHighlighter(Document doc) {
        ScalaTemplateMarkOccurrencesHighlighter highlighter = (ScalaTemplateMarkOccurrencesHighlighter) doc.getProperty(ScalaTemplateMarkOccurrencesHighlighter.class);
        if (highlighter == null) {
            doc.putProperty(ScalaTemplateMarkOccurrencesHighlighter.class, highlighter = new ScalaTemplateMarkOccurrencesHighlighter(doc));
        }
        return highlighter;
    }

    @Override
    public HighlightsLayer[] createLayers(Context context) {
        Object property = context.getDocument().getProperty(Document.TitleProperty);
        if (property != null) {

            String fileName = property.toString();
            if (fileName.endsWith(".scala.html")) {
                return new HighlightsLayer[]{HighlightsLayer.create(ScalaTemplateMarkOccurrencesHighlighter.class.getName(), ZOrder.CARET_RACK.forPosition(2000), true, getMarkOccurrencesHighlighter(context.getDocument()).getHighlightsBag())};
            }
        }

        return new HighlightsLayer[0];
    }
}
