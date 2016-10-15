package com.qualixium.playnb.filetype.sbt.lexer;

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
public class SBTLanguageHierarchy extends LanguageHierarchy<SBTTokenId> {

    private static List<SBTTokenId> tokens;
    private static Map<Integer, SBTTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<SBTTokenId>asList(new SBTTokenId[]{
            new SBTTokenId("EOF", "whitespace", 0),
            new SBTTokenId("WHITESPACE", "whitespace", 1),
            new SBTTokenId("SINGLE_LINE_COMMENT", "comment", 4),
            new SBTTokenId("FORMAL_COMMENT", "comment", 5),
            new SBTTokenId("MULTI_LINE_COMMENT", "comment", 6),
            new SBTTokenId("ABSTRACT", "keyword", 8),
            new SBTTokenId("ASSERT", "keyword", 9),
            new SBTTokenId("BOOLEAN", "keyword", 10),
            new SBTTokenId("BREAK", "keyword", 11),
            new SBTTokenId("BYTE", "keyword", 12),
            new SBTTokenId("CASE", "keyword", 13),
            new SBTTokenId("CATCH", "keyword", 14),
            new SBTTokenId("CHAR", "keyword", 15),
            new SBTTokenId("CLASS", "keyword", 16),
            new SBTTokenId("CONST", "keyword", 17),
            new SBTTokenId("CONTINUE", "keyword", 18),
            new SBTTokenId("DEF", "keyword", 19),
            new SBTTokenId("_DEFAULT", "keyword", 20),
            new SBTTokenId("DO", "keyword", 21),
            new SBTTokenId("DOUBLE", "keyword", 22),
            new SBTTokenId("ELSE", "keyword", 23),
            new SBTTokenId("ENUM", "keyword", 24),
            new SBTTokenId("EXTENDS", "keyword", 25),
            new SBTTokenId("FALSE", "keyword", 26),
            new SBTTokenId("FINAL", "keyword", 27),
            new SBTTokenId("FINALLY", "keyword", 28),
            new SBTTokenId("FLOAT", "keyword", 29),
            new SBTTokenId("FOR", "keyword", 30),
            new SBTTokenId("GOTO", "keyword", 31),
            new SBTTokenId("IF", "keyword", 32),
            new SBTTokenId("IMPLEMENTS", "keyword", 33),
            new SBTTokenId("IMPORT", "keyword", 34),
            new SBTTokenId("INSTANCEOF", "keyword", 35),
            new SBTTokenId("INT", "keyword", 36),
            new SBTTokenId("INTERFACE", "keyword", 37),
            new SBTTokenId("LONG", "keyword", 38),
            new SBTTokenId("NATIVE", "keyword", 39),
            new SBTTokenId("NEW", "keyword", 40),
            new SBTTokenId("NULL", "keyword", 41),
            new SBTTokenId("PACKAGE", "keyword", 42),
            new SBTTokenId("PRIVATE", "keyword", 43),
            new SBTTokenId("PROTECTED", "keyword", 44),
            new SBTTokenId("PUBLIC", "keyword", 45),
            new SBTTokenId("RETURN", "keyword", 46),
            new SBTTokenId("SHORT", "keyword", 47),
            new SBTTokenId("STATIC", "keyword", 48),
            new SBTTokenId("STRICTFP", "keyword", 49),
            new SBTTokenId("SUPER", "keyword", 50),
            new SBTTokenId("SWITCH", "keyword", 51),
            new SBTTokenId("SYNCHRONIZED", "keyword", 52),
            new SBTTokenId("THIS", "keyword", 53),
            new SBTTokenId("THROW", "keyword", 54),
            new SBTTokenId("THROWS", "keyword", 55),
            new SBTTokenId("TRANSIENT", "keyword", 56),
            new SBTTokenId("TRUE", "keyword", 57),
            new SBTTokenId("TRY", "keyword", 58),
            new SBTTokenId("VOID", "keyword", 59),
            new SBTTokenId("VOLATILE", "keyword", 60),
            new SBTTokenId("WHILE", "keyword", 61),
            new SBTTokenId("INTEGER_LITERAL", "literal", 62),
            new SBTTokenId("DECIMAL_LITERAL", "literal", 63),
            new SBTTokenId("HEX_LITERAL", "literal", 64),
            new SBTTokenId("OCTAL_LITERAL", "literal", 65),
            new SBTTokenId("FLOATING_POINT_LITERAL", "literal", 66),
            new SBTTokenId("DECIMAL_FLOATING_POINT_LITERAL", "literal", 67),
            new SBTTokenId("DECIMAL_EXPONENT", "number", 68),
            new SBTTokenId("HEXADECIMAL_FLOATING_POINT_LITERAL", "literal", 69),
            new SBTTokenId("HEXADECIMAL_EXPONENT", "number", 70),
            new SBTTokenId("CHARACTER_LITERAL", "literal", 71),
            new SBTTokenId("STRING_LITERAL", "literal", 72),
            new SBTTokenId("IDENTIFIER", "identifier", 73),
            new SBTTokenId("LETTER", "literal", 74),
            new SBTTokenId("PART_LETTER", "literal", 75),
            new SBTTokenId("LPAREN", "operator", 76),
            new SBTTokenId("RPAREN", "operator", 77),
            new SBTTokenId("LBRACE", "operator", 78),
            new SBTTokenId("RBRACE", "operator", 79),
            new SBTTokenId("LBRACKET", "operator", 80),
            new SBTTokenId("RBRACKET", "operator", 81),
            new SBTTokenId("SEMICOLON", "operator", 82),
            new SBTTokenId("COMMA", "operator", 83),
            new SBTTokenId("DOT", "operator", 84),
            new SBTTokenId("AT", "operator", 85),
            new SBTTokenId("ASSIGN", "operator", 86),
            new SBTTokenId("LT", "operator", 87),
            new SBTTokenId("BANG", "operator", 88),
            new SBTTokenId("TILDE", "operator", 89),
            new SBTTokenId("HOOK", "operator", 90),
            new SBTTokenId("COLON", "operator", 91),
            new SBTTokenId("EQ", "operator", 92),
            new SBTTokenId("LE", "operator", 93),
            new SBTTokenId("GE", "operator", 94),
            new SBTTokenId("NE", "operator", 95),
            new SBTTokenId("SC_OR", "operator", 96),
            new SBTTokenId("SC_AND", "operator", 97),
            new SBTTokenId("INCR", "operator", 98),
            new SBTTokenId("DECR", "operator", 99),
            new SBTTokenId("PLUS", "operator", 100),
            new SBTTokenId("MINUS", "operator", 101),
            new SBTTokenId("STAR", "operator", 102),
            new SBTTokenId("SLASH", "operator", 103),
            new SBTTokenId("BIT_AND", "operator", 104),
            new SBTTokenId("BIT_OR", "operator", 105),
            new SBTTokenId("XOR", "operator", 106),
            new SBTTokenId("REM", "operator", 107),
            new SBTTokenId("LSHIFT", "operator", 108),
            new SBTTokenId("PLUSASSIGN", "operator", 109),
            new SBTTokenId("MINUSASSIGN", "operator", 110),
            new SBTTokenId("STARASSIGN", "operator", 111),
            new SBTTokenId("SLASHASSIGN", "operator", 112),
            new SBTTokenId("ANDASSIGN", "operator", 113),
            new SBTTokenId("ORASSIGN", "operator", 114),
            new SBTTokenId("XORASSIGN", "operator", 115),
            new SBTTokenId("REMASSIGN", "operator", 116),
            new SBTTokenId("LSHIFTASSIGN", "operator", 117),
            new SBTTokenId("RSIGNEDSHIFTASSIGN", "operator", 118),
            new SBTTokenId("RUNSIGNEDSHIFTASSIGN", "operator", 119),
            new SBTTokenId("ELLIPSIS", "operator", 120),
            new SBTTokenId("HASH", "operator", 121),
            new SBTTokenId("BAD_QUOTE", "operator", 122),});

        idToToken = new HashMap<>();
        for (SBTTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized SBTTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<SBTTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<SBTTokenId> createLexer(LexerRestartInfo<SBTTokenId> info) {
        return new SBTLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-sbt";
    }
}
