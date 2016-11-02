package com.qualixium.playnb.unit.routes;

import com.qualixium.playnb.filetype.routes.RoutesLanguageHelper;
import com.qualixium.playnb.util.MiscUtil;
import java.util.Optional;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MarkOccurrencesTest {

    private static String routesFileContent;

    @BeforeClass
    public static void setUpClass() {
        routesFileContent = "# Routes\n"
                + "# This file defines all application routes (Higher priority routes first)\n"
                + "# ~~~~\n"
                + "\n"
                + "# Home page\n"
                + "GET        /                    controllers.Application.index()\n"
                + "GET        /scala               controllers.MainController.index()\n"
                + "PUT        /employee/update     controllers.Application.index\n"
                + "DELETE     /scalaDFFSD          controllers.Application.index\n"
                + "\n"
                + "\n"
                + "# Map static resources from the /public folder to the /assets URL path\n"
                + "GET        /assets/*file        controllers.Assets.versioned(path=\"/public\", file: Asset)";
    }

    @Test
    public void indexBeforeParentesisTest() {
        int caretPosition = 229;

        Optional<String> word = MiscUtil.getWordFromCaretPosition(routesFileContent, caretPosition);

        assertEquals("index", word.get());
    }

    @Test
    public void scalaAfterSlashTest() {
        int caretPosition = 182;

        Optional<String> word = MiscUtil.getWordFromCaretPosition(routesFileContent, caretPosition);

        assertEquals("scala", word.get());
    }

    @Test
    public void applicationWordBetweenDotsTest() {
        int caretPosition = 346;

        Optional<String> word = MiscUtil.getWordFromCaretPosition(routesFileContent, caretPosition);

        assertEquals("Application", word.get());
    }

    @Test
    public void controllersWordTest() {
        int caretPosition = 331;

        Optional<String> word = MiscUtil.getWordFromCaretPosition(routesFileContent, caretPosition);

        assertEquals("controllers", word.get());
    }

    @Test
    public void versionedWordTest() {
        int caretPosition = 486;

        Optional<String> word = MiscUtil.getWordFromCaretPosition(routesFileContent, caretPosition);

        assertEquals("versioned", word.get());
    }

    @Test
    public void fileWordTest() {
        int caretPosition = 452;

        Optional<String> word = MiscUtil.getWordFromCaretPosition(routesFileContent, caretPosition);

        assertEquals("file", word.get());
    }

    @Test
    public void noWordTest() {
        int caretPosition = 123;

        Optional<String> word = MiscUtil.getWordFromCaretPosition(routesFileContent, caretPosition);

        assertFalse(word.isPresent());
    }

    @Test
    public void stringInDoubleQuotesTest() {
        int caretPosition = 501;

        Optional<String> word = MiscUtil.getWordFromCaretPosition(routesFileContent, caretPosition);

        assertEquals("public", word.get());
    }

    @Test
    public void pathWordTest() {
        int caretPosition = 494;

        Optional<String> word = MiscUtil.getWordFromCaretPosition(routesFileContent, caretPosition);

        assertEquals("path", word.get());
    }
}
