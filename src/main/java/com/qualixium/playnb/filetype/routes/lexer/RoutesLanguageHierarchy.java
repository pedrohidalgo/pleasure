package com.qualixium.playnb.filetype.routes.lexer;

import com.qualixium.playnb.filetype.routes.RoutesLanguage;
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
public class RoutesLanguageHierarchy extends LanguageHierarchy<RoutesTokenId> {

    private static List<RoutesTokenId> tokens;
    private static Map<Integer, RoutesTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<RoutesTokenId>asList(new RoutesTokenId[]{
            new RoutesTokenId("EOF", "whitespace", 0),
            new RoutesTokenId("WHITESPACE", "whitespace", 1),
            new RoutesTokenId("SINGLE_LINE_COMMENT", "comment", 2),
            new RoutesTokenId("GET", "keyword", 3),
            new RoutesTokenId("HEAD", "keyword", 4),
            new RoutesTokenId("POST", "keyword", 5),
            new RoutesTokenId("PUT", "keyword", 6),
            new RoutesTokenId("DELETE", "keyword", 7),
            new RoutesTokenId("TRACE", "keyword", 8),
            new RoutesTokenId("OPTIONS", "keyword", 9),
            new RoutesTokenId("CONNECT", "keyword", 10),
            new RoutesTokenId("PATCH", "keyword", 11),
            new RoutesTokenId("BOOLEAN", "keyword", 12),
            new RoutesTokenId("BYTE", "keyword", 13),
            new RoutesTokenId("CHAR", "keyword", 14),
            new RoutesTokenId("FALSE", "keyword", 15),
            new RoutesTokenId("FLOAT", "keyword", 16),
            new RoutesTokenId("INT", "keyword", 17),
            new RoutesTokenId("LONG", "keyword", 18),
            new RoutesTokenId("SHORT", "keyword", 19),
            new RoutesTokenId("TRUE", "keyword", 20),
            new RoutesTokenId("INTEGER_LITERAL", "literal", 21),
            new RoutesTokenId("DECIMAL_LITERAL", "literal", 22),
            new RoutesTokenId("HEX_LITERAL", "literal", 23),
            new RoutesTokenId("OCTAL_LITERAL", "literal", 24),
            new RoutesTokenId("FLOATING_POINT_LITERAL", "literal", 25),
            new RoutesTokenId("DECIMAL_FLOATING_POINT_LITERAL", "literal", 26),
            new RoutesTokenId("DECIMAL_EXPONENT", "number", 27),
            new RoutesTokenId("HEXADECIMAL_FLOATING_POINT_LITERAL", "literal", 28),
            new RoutesTokenId("HEXADECIMAL_EXPONENT", "number", 29),
            new RoutesTokenId("CHARACTER_LITERAL", "literal", 30),
            new RoutesTokenId("STRING_LITERAL", "literal", 31),
            new RoutesTokenId("IDENTIFIER", "identifier", 32),
            new RoutesTokenId("LETTER", "literal", 33),
            new RoutesTokenId("PART_LETTER", "literal", 34),
            new RoutesTokenId("LPAREN", "operator", 35),
            new RoutesTokenId("RPAREN", "operator", 36),
            new RoutesTokenId("LBRACE", "operator", 37),
            new RoutesTokenId("RBRACE", "operator", 38),
            new RoutesTokenId("LBRACKET", "operator", 39),
            new RoutesTokenId("RBRACKET", "operator", 40),
            new RoutesTokenId("SEMICOLON", "operator", 41),
            new RoutesTokenId("COMMA", "operator", 42),
            new RoutesTokenId("DOT", "operator", 43),
            new RoutesTokenId("AT", "operator", 44),
            new RoutesTokenId("ASSIGN", "operator", 45),
            new RoutesTokenId("LT", "operator", 46),
            new RoutesTokenId("BANG", "operator", 47),
            new RoutesTokenId("TILDE", "operator", 48),
            new RoutesTokenId("HOOK", "operator", 49),
            new RoutesTokenId("COLON", "operator", 50),
            new RoutesTokenId("EQ", "operator", 51),
            new RoutesTokenId("LE", "operator", 52),
            new RoutesTokenId("GE", "operator", 53),
            new RoutesTokenId("NE", "operator", 54),
            new RoutesTokenId("SC_OR", "operator", 55),
            new RoutesTokenId("SC_AND", "operator", 56),
            new RoutesTokenId("INCR", "operator", 57),
            new RoutesTokenId("DECR", "operator", 58),
            new RoutesTokenId("PLUS", "operator", 59),
            new RoutesTokenId("MINUS", "operator", 60),
            new RoutesTokenId("STAR", "operator", 61),
            new RoutesTokenId("SLASH", "operator", 62),
            new RoutesTokenId("BIT_AND", "operator", 63),
            new RoutesTokenId("BIT_OR", "operator", 64),
            new RoutesTokenId("XOR", "operator", 65),
            new RoutesTokenId("REM", "operator", 66),
            new RoutesTokenId("LSHIFT", "operator", 67),
            new RoutesTokenId("PLUSASSIGN", "operator", 68),
            new RoutesTokenId("MINUSASSIGN", "operator", 69),
            new RoutesTokenId("STARASSIGN", "operator", 70),
            new RoutesTokenId("SLASHASSIGN", "operator", 71),
            new RoutesTokenId("ANDASSIGN", "operator", 72),
            new RoutesTokenId("ORASSIGN", "operator", 73),
            new RoutesTokenId("XORASSIGN", "operator", 74),
            new RoutesTokenId("REMASSIGN", "operator", 75),
            new RoutesTokenId("LSHIFTASSIGN", "operator", 76),
            new RoutesTokenId("RSIGNEDSHIFTASSIGN", "operator", 77),
            new RoutesTokenId("RUNSIGNEDSHIFTASSIGN", "operator", 78),
            new RoutesTokenId("ELLIPSIS", "operator", 79),
            new RoutesTokenId("RUNSIGNEDSHIFT", "operator", 80),
            new RoutesTokenId("RSIGNEDSHIFT", "operator", 81),
            new RoutesTokenId("GT", "operator", 82),});

        idToToken = new HashMap<>();
        for (RoutesTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized RoutesTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<RoutesTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<RoutesTokenId> createLexer(LexerRestartInfo<RoutesTokenId> info) {
        return new RoutesLexer(info);
    }

    @Override
    protected String mimeType() {
        return RoutesLanguage.MIME_TYPE;
    }
}
