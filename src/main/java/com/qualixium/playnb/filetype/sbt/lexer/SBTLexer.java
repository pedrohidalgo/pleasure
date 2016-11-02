package com.qualixium.playnb.filetype.sbt.lexer;

import com.qualixium.playnb.filetype.sbt.jcclexer.JavaCharStream;
import com.qualixium.playnb.filetype.sbt.jcclexer.JavaParserTokenManager;
import com.qualixium.playnb.filetype.sbt.jcclexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public class SBTLexer implements Lexer<SBTTokenId> {

    private LexerRestartInfo<SBTTokenId> info;
    private JavaParserTokenManager javaParserTokenManager;

    SBTLexer(LexerRestartInfo<SBTTokenId> info) {
        this.info = info;
        JavaCharStream stream = new JavaCharStream(info.input());
        javaParserTokenManager = new JavaParserTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<SBTTokenId> nextToken() {
        Token token = javaParserTokenManager.getNextToken();
        if (info.input().readLength() < 1) {
            return null;
        }
        return info.tokenFactory().createToken(SBTLanguageHierarchy.getToken(token.kind));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }
}
