package com.qualixium.playnb.filetype.scalatemplate;

import com.qualixium.playnb.filetype.scalatemplate.lexer.ScalaTemplateTokenId;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;

@LanguageRegistration(mimeType = ScalaTemplateLanguage.MIME_TYPE)
public class ScalaTemplateLanguage extends DefaultLanguageConfig {

    public static final String MIME_TYPE = "text/x-scala-template";

    @Override
    public Language getLexerLanguage() {
        return ScalaTemplateTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "Scala Template";
    }

}
