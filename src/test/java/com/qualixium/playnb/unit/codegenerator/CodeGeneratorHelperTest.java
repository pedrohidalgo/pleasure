package com.qualixium.playnb.unit.codegenerator;

import com.qualixium.playnb.codegenerator.CodeGeneratorHelper;
import static com.qualixium.playnb.codegenerator.CodeGeneratorHelper.TAB_SPACES;
import com.qualixium.playnb.util.MiscUtil;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeGeneratorHelperTest {

    private static String scalaTestFileContent;
    private static String javaTestFileContent;

    @BeforeClass
    public static void setUpClass() throws IOException, URISyntaxException {
        URI uriScala = CodeGeneratorHelperTest.class.getClassLoader().getResource("ScalaTestFile.source").toURI();
        scalaTestFileContent = new String(Files.readAllBytes(Paths.get(uriScala)));
        URI uriJava = CodeGeneratorHelperTest.class.getClassLoader().getResource("JavaTestFile.source").toURI();
        javaTestFileContent = new String(Files.readAllBytes(Paths.get(uriJava)));
    }

    @Test
    public void testGetCodeNewMethodScala() {
        String methodName = "aNewMethodNameScala";
        String newMethodCode = "def aNewMethodNameScala = TODO" + MiscUtil.LINE_SEPARATOR;

        String expResult = CodeGeneratorHelper.applySpaces(newMethodCode);
        String result = CodeGeneratorHelper.getCodeNewMethodScala(methodName);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCodeNewMethodJava() {
        String methodName = "aNewMethodNameJava";
        String newMethodCode = "public Result aNewMethodNameJava() {" + MiscUtil.LINE_SEPARATOR
                + TAB_SPACES + "return play.mvc.Results.TODO;"
                + MiscUtil.LINE_SEPARATOR
                + "}";
        String expResult = CodeGeneratorHelper.applySpaces(newMethodCode);
        String result = CodeGeneratorHelper.getCodeNewMethodJava(methodName);
        assertEquals(expResult, result);
    }

    @Test
    public void testAddCodeNewMethodJava() {
        String methodName = "aNewMethodNameJava";
        String newMethodCode = CodeGeneratorHelper.getCodeNewMethodJava(methodName);
        String newFileContent = CodeGeneratorHelper.getFileContentWithNewMethodCode(javaTestFileContent, newMethodCode);

        assertTrue(newFileContent.contains(newMethodCode));
    }

    @Test
    public void testAddCodeNewMethodScala() {
        String methodName = "aNewMethodNameScala";
        String newMethodCode = CodeGeneratorHelper.getCodeNewMethodScala(methodName);
        String newFileContent = CodeGeneratorHelper.getFileContentWithNewMethodCode(scalaTestFileContent, newMethodCode);

        assertTrue(newFileContent.contains(newMethodCode));
    }

}
