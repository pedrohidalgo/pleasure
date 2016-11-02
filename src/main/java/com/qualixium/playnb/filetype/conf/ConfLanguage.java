package com.qualixium.playnb.filetype.conf;

import com.qualixium.playnb.filetype.conf.lexer.ConfTokenId;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.openide.util.ImageUtilities;

@LanguageRegistration(mimeType = ConfLanguage.MIME_TYPE)
public class ConfLanguage extends DefaultLanguageConfig {

    public static final String MIME_TYPE = "text/x-conf";
    public static final String COMMENT_SYMBOL = "#";
    @StaticResource
    public static final String ICON_STRING_BASE = "com/qualixium/playnb/filetype/conf/ConfigFile.png";
    public static final ImageIcon IMAGE_ICON = new ImageIcon(ImageUtilities.loadImage(ICON_STRING_BASE));

    @Override
    public Language getLexerLanguage() {
        return ConfTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "Conf files";
    }

    @Override
    public String getLineCommentPrefix() {
        return COMMENT_SYMBOL;
    }
}
