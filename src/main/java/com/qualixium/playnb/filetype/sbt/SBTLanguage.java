package com.qualixium.playnb.filetype.sbt;

import com.qualixium.playnb.filetype.sbt.lexer.SBTTokenId;
import javax.swing.ImageIcon;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.openide.util.ImageUtilities;

@LanguageRegistration(mimeType = SBTLanguage.MIME_TYPE)
public class SBTLanguage extends DefaultLanguageConfig {

    public static final String MIME_TYPE = "text/x-sbt";
    public static final String COMMENT_SYMBOL = "//";
    public static final String ICON_STRING_BASE = "com/qualixium/playnb/filetype/sbt/sbt_icon.png";
    public static final ImageIcon IMAGE_ICON = new ImageIcon(ImageUtilities.loadImage(ICON_STRING_BASE));

    @Override
    public Language getLexerLanguage() {
        return SBTTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "SBT";
    }

    @Override
    public String getLineCommentPrefix() {
        return COMMENT_SYMBOL;
    }
}
