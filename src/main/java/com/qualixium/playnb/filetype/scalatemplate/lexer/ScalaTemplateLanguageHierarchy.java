package com.qualixium.playnb.filetype.scalatemplate.lexer;

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
public class ScalaTemplateLanguageHierarchy extends LanguageHierarchy<ScalaTemplateTokenId> {

    private static List<ScalaTemplateTokenId> tokens;
    private static Map<Integer, ScalaTemplateTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<ScalaTemplateTokenId>asList(new ScalaTemplateTokenId[]{
            new ScalaTemplateTokenId("EOF", "whitespace", 0),
            new ScalaTemplateTokenId("WHITESPACE", "whitespace", 1),
            new ScalaTemplateTokenId("AT", "magic_char", 2),
            new ScalaTemplateTokenId("FOR", "st_keys", 3),
            new ScalaTemplateTokenId("IF", "st_keys", 4),
            new ScalaTemplateTokenId("ELSE", "st_keys", 5),
            new ScalaTemplateTokenId("MATCH", "st_keys", 6),
            new ScalaTemplateTokenId("VAL", "st_keys", 7),
            new ScalaTemplateTokenId("VAR", "st_keys", 8),
            new ScalaTemplateTokenId("DEFINING", "st_keys", 9),
            new ScalaTemplateTokenId("IMPORT", "st_keys", 10),
            new ScalaTemplateTokenId("HTML", "st_keys", 11),
            new ScalaTemplateTokenId("HELPER", "st_keys", 12),
            new ScalaTemplateTokenId("BOOLEAN", "st_keys", 13),
            new ScalaTemplateTokenId("BYTE", "st_keys", 14),
            new ScalaTemplateTokenId("CHAR", "st_keys", 15),
            new ScalaTemplateTokenId("FALSE", "st_keys", 16),
            new ScalaTemplateTokenId("INT", "st_keys", 17),
            new ScalaTemplateTokenId("LONG", "st_keys", 18),
            new ScalaTemplateTokenId("FLOAT", "st_keys", 19),
            new ScalaTemplateTokenId("DOUBLE", "st_keys", 20),
            new ScalaTemplateTokenId("SHORT", "st_keys", 21),
            new ScalaTemplateTokenId("TRUE", "st_keys", 22),
            new ScalaTemplateTokenId("INPUTTEXT", "form_components", 23),
            new ScalaTemplateTokenId("CHECKBOX", "form_components", 24),
            new ScalaTemplateTokenId("SELECT", "form_components", 25),
            new ScalaTemplateTokenId("TEXTAREA", "form_components", 26),
            new ScalaTemplateTokenId("INPUTRADIOGROUP", "form_components", 27),
            new ScalaTemplateTokenId("INPUTDATE", "form_components", 28),
            new ScalaTemplateTokenId("INPUTPASSWORD", "form_components", 29),
            new ScalaTemplateTokenId("INPUTFILE", "form_components", 30),
            new ScalaTemplateTokenId("FORM", "form_components", 31),
            new ScalaTemplateTokenId("INPUT", "form_components", 32),
            new ScalaTemplateTokenId("INTEGER_LITERAL", "literal", 33),
            new ScalaTemplateTokenId("DECIMAL_LITERAL", "literal", 34),
            new ScalaTemplateTokenId("HEX_LITERAL", "literal", 35),
            new ScalaTemplateTokenId("OCTAL_LITERAL", "literal", 36),
            new ScalaTemplateTokenId("FLOATING_POINT_LITERAL", "literal",37),
            new ScalaTemplateTokenId("DECIMAL_FLOATING_POINT_LITERAL", "literal", 38),
            new ScalaTemplateTokenId("DECIMAL_EXPONENT", "number", 39),
            new ScalaTemplateTokenId("HEXADECIMAL_FLOATING_POINT_LITERAL", "literal", 40),
            new ScalaTemplateTokenId("HEXADECIMAL_EXPONENT", "number", 41),
            new ScalaTemplateTokenId("CHARACTER_LITERAL", "literal", 42),
            new ScalaTemplateTokenId("STRING_LITERAL", "literal", 43),
            new ScalaTemplateTokenId("IDENTIFIER", "identifier", 44),
            new ScalaTemplateTokenId("LETTER", "literal", 45),
            new ScalaTemplateTokenId("PART_LETTER", "literal", 46),
            new ScalaTemplateTokenId("LPAREN", "operator", 47),
            new ScalaTemplateTokenId("RPAREN", "operator", 48),
            new ScalaTemplateTokenId("LBRACE", "operator", 49),
            new ScalaTemplateTokenId("RBRACE", "operator", 50),
            new ScalaTemplateTokenId("LBRACKET", "operator", 51),
            new ScalaTemplateTokenId("RBRACKET", "operator", 52),
            new ScalaTemplateTokenId("SEMICOLON", "operator", 53),
            new ScalaTemplateTokenId("COMMA", "operator", 54),
            new ScalaTemplateTokenId("DOT", "operator", 55),
            new ScalaTemplateTokenId("ASSIGN", "operator", 56),
            new ScalaTemplateTokenId("LT", "operator", 57),
            new ScalaTemplateTokenId("BANG", "operator", 58),
            new ScalaTemplateTokenId("TILDE", "operator", 59),
            new ScalaTemplateTokenId("HOOK", "operator", 60),
            new ScalaTemplateTokenId("COLON", "operator", 61),
            new ScalaTemplateTokenId("EQ", "operator", 62),
            new ScalaTemplateTokenId("LE", "operator", 63),
            new ScalaTemplateTokenId("GE", "operator", 64),
            new ScalaTemplateTokenId("NE", "operator", 65),
            new ScalaTemplateTokenId("SC_OR", "operator", 66),
            new ScalaTemplateTokenId("SC_AND", "operator", 67),
            new ScalaTemplateTokenId("INCR", "operator", 68),
            new ScalaTemplateTokenId("DECR", "operator", 69),
            new ScalaTemplateTokenId("PLUS", "operator", 70),
            new ScalaTemplateTokenId("MINUS", "operator", 71),
            new ScalaTemplateTokenId("STAR", "operator", 72),
            new ScalaTemplateTokenId("SLASH", "operator", 73),
            new ScalaTemplateTokenId("BIT_AND", "operator", 74),
            new ScalaTemplateTokenId("BIT_OR", "operator", 75),
            new ScalaTemplateTokenId("XOR", "operator", 76),
            new ScalaTemplateTokenId("REM", "operator", 77),
            new ScalaTemplateTokenId("LSHIFT", "operator", 78),
            new ScalaTemplateTokenId("PLUSASSIGN", "operator", 79),
            new ScalaTemplateTokenId("MINUSASSIGN", "operator", 80),
            new ScalaTemplateTokenId("STARASSIGN", "operator", 81),
            new ScalaTemplateTokenId("SLASHASSIGN", "operator", 82),
            new ScalaTemplateTokenId("ANDASSIGN", "operator", 83),
            new ScalaTemplateTokenId("ORASSIGN", "operator", 84),
            new ScalaTemplateTokenId("XORASSIGN", "operator", 85),
            new ScalaTemplateTokenId("REMASSIGN", "operator", 86),
            new ScalaTemplateTokenId("LSHIFTASSIGN", "operator", 87),
            new ScalaTemplateTokenId("RSIGNEDSHIFTASSIGN", "operator", 88),
            new ScalaTemplateTokenId("RUNSIGNEDSHIFTASSIGN", "operator", 89),
            new ScalaTemplateTokenId("ELLIPSIS", "operator", 90),
            new ScalaTemplateTokenId("HASH", "operator", 91),
            new ScalaTemplateTokenId("BAD_QUOTE", "operator", 92),
            new ScalaTemplateTokenId("BAD_QUOTE_2", "operator", 93),
            new ScalaTemplateTokenId("SINGLE_QUOTE", "operator", 94),
        });

        idToToken = new HashMap<>();
        for (ScalaTemplateTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized ScalaTemplateTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<ScalaTemplateTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<ScalaTemplateTokenId> createLexer(LexerRestartInfo<ScalaTemplateTokenId> info) {
        return new ScalaTemplateLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-scala-template";
    }
}
