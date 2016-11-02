package com.qualixium.playnb.unit.scalatemplate;

import com.qualixium.playnb.filetype.scalatemplate.parser.ScalaTemplateValidator;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class ScalaTemplateErrorParsingTest {

    @Test
    public void testGetAllExpressions() {
        final String fileContent
                = "@(name:String, numbers:List[Int])\n"
                + "\n"
                + "@import helper._\n"
                + "<h1>@name.hy</h1>\n"
                + "@another\n"
                + "<div>\n"
                + "    <b>come on @name</b>\n"
                + "</div>\n"
                + "<input type='text' value=\"@name\" /> \n"
                + "\n"
                + "<h2>@numbers.size</h2>\n"
                + "@for( obj <- numbers ){\n"
                + "    this text\n"
                + "    @obj\n"
                + "}\n"
                + "<ul>\n";
        int expected = 6;

        final String charsToSeparateExpressions = " (<\"" + LINE_SEPARATOR;
        List<String> listExpressions = ScalaTemplateValidator.getAllExpressions(fileContent);

        assertEquals(expected, listExpressions.size());

        listExpressions.stream().forEach(exp -> {
            charsToSeparateExpressions.chars().forEach(ch -> {
                assertFalse("expression [" + exp + "] contains invalid char: " + String.valueOf(Character.toChars(ch)),
                        exp.contains(String.valueOf(Character.toChars(ch))));
            });
        });
    }

}
