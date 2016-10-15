package com.qualixium.playnb.unit.scalatemplate;

import com.qualixium.playnb.filetype.scalatemplate.helper.ScalaTemplateUtil;
import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.Test;

public class ScalaTemplateUtilTest {

    public static final String FILE_CONTENT
            = "@(emp: Employee, myVar: String, listValues: List[String])\n"
            + "\n"
            + "this is the @myVar.toLowerCase @emp.age\n"
            + "\n"
            + "<input type=\"text\" value=\"@emp.name\" />\n"
            + "\n"
            + "@for(v <- listValues) {\n"
            + "\n"
            + "<label class=\"label-warning\"></label>\n"
            + "\n"
            + "}\n"
            + "\n"
            + "<b>another text</b>\n"
            + "\n"
            + "@listValues.map { valMap =>\n"
            + "    <b></b>\n"
            + "}\n"
            + "\n"
            + "@litValues.foreach { fe =>\n"
            + "    <i></i>\n"
            + "}";

    @Test
    public void testGetOpenedAmountBracketsAtCaret() {
        int caretPosition = 177;
        int expResult = 1;
        int result = ScalaTemplateUtil.getOpenedAmountBracketsAtCaret(FILE_CONTENT, caretPosition);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetOpenedAmountBracketOutSideBrackets() {
        int caretPosition = 215;
        int expResult = 0;
        int result = ScalaTemplateUtil.getOpenedAmountBracketsAtCaret(FILE_CONTENT, caretPosition);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetBlockForDefinition() {
        String expected = "for(v <- listValues)";
        int caretPosition = 177;

        String actual = ScalaTemplateUtil.getBlockDefinition(FILE_CONTENT, caretPosition).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetValMapDefinition() {
        String expected = "valMap";
        int caretPosition = 264;

        String actual = ScalaTemplateUtil.getValDefinition(FILE_CONTENT, caretPosition).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetValForEachDefinition() {
        String expected = "fe";
        int caretPosition = 306;

        String actual = ScalaTemplateUtil.getValDefinition(FILE_CONTENT, caretPosition).get();

        assertEquals(expected, actual);
    }

    @Test
    public void fixBugScalaTemplateBlock() {
        String fileContent = "@(message: String)\n"
                + "\n"
                + "@main(\"Welcome to Play\") {\n"
                + "\n"
                + "    @play20.welcome(message, style = \"Java\")\n"
                + "\n"
                + "}";

        int caretPosition = 52;

        assertFalse("there is a val definition and it shouldn't", ScalaTemplateUtil.getValDefinition(fileContent, caretPosition).isPresent());
    }

    @Test
    public void testGetResourceRootFolder() {
        String expected = "/public";
        String routesFileContent = "# Routes\n"
                + "# This file defines all application routes (Higher priority routes first)\n"
                + "# ~~~~\n"
                + "\n"
                + "# Home page\n"
                + "GET     /                           controllers.Application.index()\n"
                + "\n"
                + "# Map static resources from the /public folder to the /assets URL path\n"
                + "GET     /assets/*file               controllers.Assets.versioned(path=\"/public\", file: Asset)";

        Optional<String> actual = ScalaTemplateUtil.getResourcesRootFolder(routesFileContent);
        
        assertTrue("the resources root folder could not be determined", actual.isPresent());
        assertEquals(expected, actual.get());
    }

}
