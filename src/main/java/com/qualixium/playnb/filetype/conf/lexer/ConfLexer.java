package com.qualixium.playnb.filetype.conf.lexer;

import com.qualixium.playnb.filetype.conf.jcclexer.JavaCharStream;
import com.qualixium.playnb.filetype.conf.jcclexer.JavaParserTokenManager;
import com.qualixium.playnb.filetype.conf.jcclexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public class ConfLexer implements Lexer<ConfTokenId> {

    private LexerRestartInfo<ConfTokenId> info;
    private JavaParserTokenManager javaParserTokenManager;

    ConfLexer(LexerRestartInfo<ConfTokenId> info) {
        this.info = info;
        JavaCharStream stream = new JavaCharStream(info.input());
        javaParserTokenManager = new JavaParserTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<ConfTokenId> nextToken() {
        Token token = javaParserTokenManager.getNextToken();
        if (info.input().readLength() < 1) {
            return null;
        }
        return info.tokenFactory().createToken(ConfLanguageHierarchy.getToken(token.kind));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }
}
