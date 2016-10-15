package com.qualixium.playnb.filetype.scalatemplate.lexer;

import com.qualixium.playnb.filetype.scalatemplate.jcclexer.JavaCharStream;
import com.qualixium.playnb.filetype.scalatemplate.jcclexer.JavaParserTokenManager;
import com.qualixium.playnb.filetype.scalatemplate.jcclexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public class ScalaTemplateLexer implements Lexer<ScalaTemplateTokenId> {

    private LexerRestartInfo<ScalaTemplateTokenId> info;
    private JavaParserTokenManager javaParserTokenManager;

    ScalaTemplateLexer(LexerRestartInfo<ScalaTemplateTokenId> info) {
        this.info = info;
        JavaCharStream stream = new JavaCharStream(info.input());
        javaParserTokenManager = new JavaParserTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<ScalaTemplateTokenId> nextToken() {
        Token token = javaParserTokenManager.getNextToken();
        if (info.input().readLength() < 1) {
            return null;
        }
        return info.tokenFactory().createToken(ScalaTemplateLanguageHierarchy.getToken(token.kind));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }
}
