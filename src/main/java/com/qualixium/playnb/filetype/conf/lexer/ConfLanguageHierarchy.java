package com.qualixium.playnb.filetype.conf.lexer;

import com.qualixium.playnb.filetype.conf.ConfLanguage;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author pedro
 */
public class ConfLanguageHierarchy extends LanguageHierarchy<ConfTokenId> {

    private static List<ConfTokenId> tokens;
    private static Map<Integer, ConfTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<ConfTokenId>asList(new ConfTokenId[]{
            new ConfTokenId("EOF", "whitespace", 0),
            new ConfTokenId("WHITESPACE", "whitespace", 1),
            new ConfTokenId("SINGLE_LINE_COMMENT", "comment", 2),
            new ConfTokenId("INTEGER_LITERAL", "literal", 3),
            new ConfTokenId("DECIMAL_LITERAL", "literal", 4),
            new ConfTokenId("HEX_LITERAL", "literal", 5),
            new ConfTokenId("OCTAL_LITERAL", "literal", 6),
            new ConfTokenId("FLOATING_POINT_LITERAL", "literal", 7),
            new ConfTokenId("DECIMAL_FLOATING_POINT_LITERAL", "literal", 8),
            new ConfTokenId("DECIMAL_EXPONENT", "number", 9),
            new ConfTokenId("HEXADECIMAL_FLOATING_POINT_LITERAL", "literal", 10),
            new ConfTokenId("HEXADECIMAL_EXPONENT", "number", 11),
            new ConfTokenId("CHARACTER_LITERAL", "literal", 12),
            new ConfTokenId("STRING_LITERAL", "literal", 13),
            new ConfTokenId("IDENTIFIER", "identifier", 14),
            new ConfTokenId("LETTER", "literal", 15),
            new ConfTokenId("PART_LETTER", "literal", 16),
            new ConfTokenId("LPAREN", "operator", 17),
            new ConfTokenId("RPAREN", "operator", 18),
            new ConfTokenId("LBRACE", "operator", 19),
            new ConfTokenId("RBRACE", "operator", 20),
            new ConfTokenId("LBRACKET", "operator", 21),
            new ConfTokenId("RBRACKET", "operator", 22),
            new ConfTokenId("SEMICOLON", "operator", 23),
            new ConfTokenId("COMMA", "operator", 24),
            new ConfTokenId("DOT", "operator", 25),
            new ConfTokenId("AT", "operator", 26),
            new ConfTokenId("ASSIGN", "operator", 27),
            new ConfTokenId("LT", "operator", 28),
            new ConfTokenId("BANG", "operator", 29),
            new ConfTokenId("TILDE", "operator", 30),
            new ConfTokenId("HOOK", "operator", 31),
            new ConfTokenId("COLON", "operator", 32),
            new ConfTokenId("EQ", "operator", 33),
            new ConfTokenId("LE", "operator", 34),
            new ConfTokenId("GE", "operator", 35),
            new ConfTokenId("NE", "operator", 36),
            new ConfTokenId("SC_OR", "operator", 37),
            new ConfTokenId("SC_AND", "operator", 38),
            new ConfTokenId("INCR", "operator", 39),
            new ConfTokenId("DECR", "operator", 40),
            new ConfTokenId("PLUS", "operator", 41),
            new ConfTokenId("MINUS", "operator", 42),
            new ConfTokenId("STAR", "operator", 43),
            new ConfTokenId("SLASH", "operator", 44),
            new ConfTokenId("BIT_AND", "operator", 45),
            new ConfTokenId("BIT_OR", "operator", 46),
            new ConfTokenId("XOR", "operator", 47),
            new ConfTokenId("REM", "operator", 48),
            new ConfTokenId("LSHIFT", "operator", 49),
            new ConfTokenId("PLUSASSIGN", "operator", 50),
            new ConfTokenId("MINUSASSIGN", "operator", 51),
            new ConfTokenId("STARASSIGN", "operator", 52),
            new ConfTokenId("SLASHASSIGN", "operator", 53),
            new ConfTokenId("ANDASSIGN", "operator", 54),
            new ConfTokenId("ORASSIGN", "operator", 55),
            new ConfTokenId("XORASSIGN", "operator", 56),
            new ConfTokenId("REMASSIGN", "operator", 57),
            new ConfTokenId("LSHIFTASSIGN", "operator", 58),
            new ConfTokenId("RSIGNEDSHIFTASSIGN", "operator", 59),
            new ConfTokenId("RUNSIGNEDSHIFTASSIGN", "operator", 60),
            new ConfTokenId("ELLIPSIS", "operator", 61),
            new ConfTokenId("BAD_QUOTE", "operator", 62),
        });

        idToToken = new HashMap<>();
        for (ConfTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized ConfTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<ConfTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<ConfTokenId> createLexer(LexerRestartInfo<ConfTokenId> info) {
        return new ConfLexer(info);
    }

    @Override
    protected String mimeType() {
        return ConfLanguage.MIME_TYPE;
    }
}
