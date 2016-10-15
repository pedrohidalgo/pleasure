package com.qualixium.playnb.unit.scalatemplate;

import com.qualixium.playnb.filetype.scalatemplate.helper.ScalaTemplateLanguageHelper;
import com.qualixium.playnb.filetype.scalatemplate.helper.TemplateParameter;
import com.qualixium.playnb.util.MiscUtil.Language;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Utilities;

public class ScalaTemplateLanguageHelperTest {

    private static String scalaTemplateFileContent;
    private static ClassLoader classLoaderWithScalaLibrary;

    @BeforeClass
    public static void setUpClass() throws URISyntaxException, IOException {
        URI uri = ScalaTemplateLanguageHelperTest.class.getClassLoader().getResource("ScalaTemplateTemplate.scala.html").toURI();
        scalaTemplateFileContent = new String(Files.readAllBytes(Paths.get(uri)));
        String userHomeDir = System.getProperty("user.home");
        String scalaLibraryAbsolutePath = userHomeDir + "/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-2.11.7.jar";
        classLoaderWithScalaLibrary = URLClassLoader.newInstance(
                new URL[]{Utilities.toURI(new File(scalaLibraryAbsolutePath)).toURL()},
                ScalaTemplateLanguageHelperTest.class.getClassLoader()
        );
    }

    @Test
    public void testGetConstructorParametersOneParameter() {
        final String expected = "notification";
        List<TemplateParameter> listParameters = ScalaTemplateLanguageHelper.getConstructorParameters(scalaTemplateFileContent);

        boolean result = listParameters.stream()
                .anyMatch(parameter -> parameter.variableName.equals(expected));

        assertTrue(result);
    }

    @Test
    public void testGetConstructorParametersTwoParameters() {
        final String fileContent
                = "@(notification: Notification, anotherPar: String)\n"
                + "\n"
                + "@import helper._\n"
                + "\n"
                + "@main() {\n"
                + "\n"
                + "<div id=\"first_div_row\" class=\"row\">\n"
                + "    <div class=\"col-lg-12\">\n"
                + "        <h1 class=\"page-header\">\n"
                + "            @notification.title\n"
                + "            <small> \n"
                + "            </small>\n"
                + "        </h1>\n"
                + "    </div>\n"
                + "</div>"
                + "}";

        final List<String> listExpectedParameters = new ArrayList<>();
        listExpectedParameters.add("notification");
        listExpectedParameters.add("anotherPar");

        List<String> listParameters = ScalaTemplateLanguageHelper
                .getConstructorParameters(fileContent).stream()
                .map(cp -> cp.variableName)
                .collect(Collectors.toList());

        boolean result = listParameters.containsAll(listExpectedParameters);

        assertTrue(result);
    }

    @Test
    public void testGetConstructorParametersWithGenerics() {
        final String fileContent
                = "@(notification: Notification, anotherPar: String, numbers: List[Int])\n"
                + "\n"
                + "@import helper._\n"
                + "\n"
                + "@main() {\n"
                + "\n"
                + "<div id=\"first_div_row\" class=\"row\">\n"
                + "    <div class=\"col-lg-12\">\n"
                + "        <h1 class=\"page-header\">\n"
                + "            @notification.title\n"
                + "            <small> \n"
                + "            </small>\n"
                + "        </h1>\n"
                + "    </div>\n"
                + "</div>"
                + "}";

        List<TemplateParameter> constructorParameters = ScalaTemplateLanguageHelper.getConstructorParameters(fileContent);

        TemplateParameter numberParam = constructorParameters.stream().filter(cp -> cp.variableName.equals("numbers")).findFirst().get();

        assertEquals("List[Int]", numberParam.variableType);
    }

    @Test
    public void testMethodNameTranslator() {
        assertEquals("+", ScalaTemplateLanguageHelper.getTranslatedMemberName("$plus"));
        assertEquals("++", ScalaTemplateLanguageHelper.getTranslatedMemberName("$plus$plus"));
        assertEquals("&lt;", ScalaTemplateLanguageHelper.getTranslatedMemberName("$less"));
        assertEquals(">", ScalaTemplateLanguageHelper.getTranslatedMemberName("$greater"));
        assertEquals("&lt;=", ScalaTemplateLanguageHelper.getTranslatedMemberName("$less$eq"));
        assertEquals(">=", ScalaTemplateLanguageHelper.getTranslatedMemberName("$greater$eq"));
        assertEquals("|", ScalaTemplateLanguageHelper.getTranslatedMemberName("$bar"));
        assertEquals("-", ScalaTemplateLanguageHelper.getTranslatedMemberName("$minus"));
        assertEquals("&lt;&lt;", ScalaTemplateLanguageHelper.getTranslatedMemberName("$less$less"));
        assertEquals("&", ScalaTemplateLanguageHelper.getTranslatedMemberName("$amp"));
        assertEquals("toChar", ScalaTemplateLanguageHelper.getTranslatedMemberName("toChar"));
        assertEquals("/", ScalaTemplateLanguageHelper.getTranslatedMemberName("$div"));
        assertEquals("%", ScalaTemplateLanguageHelper.getTranslatedMemberName("$percent"));
        assertEquals(">>", ScalaTemplateLanguageHelper.getTranslatedMemberName("$greater$greater"));
        assertEquals("^", ScalaTemplateLanguageHelper.getTranslatedMemberName("$up"));
        assertEquals("unary_-", ScalaTemplateLanguageHelper.getTranslatedMemberName("unary_$minus"));
        assertEquals("==", ScalaTemplateLanguageHelper.getTranslatedMemberName("$eq$eq"));
        assertEquals("!=", ScalaTemplateLanguageHelper.getTranslatedMemberName("$bang$eq"));
        assertEquals("*", ScalaTemplateLanguageHelper.getTranslatedMemberName("$times"));
        assertEquals("unary_~", ScalaTemplateLanguageHelper.getTranslatedMemberName("unary_$tilde"));
    }

    @Test
    public void testTypeNameTranslation() {
        assertEquals("Boolean", ScalaTemplateLanguageHelper.getTranslatedType("boolean"));
        assertEquals("Double", ScalaTemplateLanguageHelper.getTranslatedType("double"));
        assertEquals("Any", ScalaTemplateLanguageHelper.getTranslatedType("java.lang.Object"));
        assertEquals("Unit", ScalaTemplateLanguageHelper.getTranslatedType("void"));
    }

    @Test
    public void testArgumentNameTranslation() {
        assertEquals("x$1", ScalaTemplateLanguageHelper.getTranslatedArgument("arg0"));
        assertEquals("x$2", ScalaTemplateLanguageHelper.getTranslatedArgument("arg1"));
        assertEquals("firstName", ScalaTemplateLanguageHelper.getTranslatedArgument("firstName"));
    }

    @Test
    public void testGetCompletionItemMethodText() throws ClassNotFoundException {
        Class<?> clazz = classLoaderWithScalaLibrary.loadClass("scala.Int");
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals("$eq$eq")
                    && method.getParameters()[0].getType().getSimpleName().equals("double")) {
                final String expected = "==(x$1: Double): Boolean";

                String completionItemText = ScalaTemplateLanguageHelper.getCompletionItemMethodText(method);

                assertEquals(expected, completionItemText);
            }
        }
    }

    @Test
    public void testGetCompletionItemMemberText() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("java.lang.Integer");
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.getName().equals("SIZE")) {
                final String expected = "SIZE: Int";

                String completionItemText = ScalaTemplateLanguageHelper.getCompletionItemMemberText(field);

                assertEquals(expected, completionItemText);
            }
        }
    }

    @Test
    public void testGetFirstClassFromLine() {
        String line = "java.lang.String.valueOf(3).";
        String expected = "java.lang.String";

        String actual = ScalaTemplateLanguageHelper.getFirstClassFromLine(line).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetClassToProvideCompletion() throws ClassNotFoundException {
        String line = "java.lang.Integer.toString(5).toLowerCase";
        String expected = "java.lang.String";

        Optional<Class> actual = ScalaTemplateLanguageHelper.getClassToProvideCompletion("", line, getClass().getClassLoader(), Language.SCALA);

        assertEquals(expected, actual.get().getName());
    }

    @Test
    public void testGetClassToProvideCompletionWithDate() throws ClassNotFoundException {
        String line = "java.util.Date.toInstant.adjustInto";
        String expected = "java.time.temporal.Temporal";

        Optional<Class> classOptional = ScalaTemplateLanguageHelper.getClassToProvideCompletion("", line, getClass().getClassLoader(), Language.JAVA);
        String actual = classOptional.get().getName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTemplateParameterInBlock() {
        String fileContent = "@(emp: Employee, myVar: String, listValues: List[String])\n"
                + "\n"
                + "this is the @myVar.toLowerCase @emp.age\n"
                + "\n"
                + "<input type=\"text\" value=\"@emp.name\" />\n"
                + "\n"
                + "@for(v <- listValues) {\n"
                + "\n"
                + "<label class=\"label-warning\"></label>\n"
                + "\n"
                + "}";

        String expectedVariableName = "v";
        String expectedVariableType = "String";
        int caretPosition = 177;

        Optional<TemplateParameter> tpOptional = ScalaTemplateLanguageHelper.getBlockParameter(fileContent, caretPosition);

        assertTrue("tpOptional is not present", tpOptional.isPresent());
        TemplateParameter tp = tpOptional.get();
        assertEquals(expectedVariableName, tp.variableName);
        assertEquals(expectedVariableType, tp.variableType);
    }

    @Test
    public void testGetTemplateParameterInMapBlock() {
        String fileContent
                = "@(emp: Employee, myVar: String, listValues: List[Int])\n"
                + "\n"
                + "this is the @myVar.toLowerCase @emp.age\n"
                + "\n"
                + "<input type=\"text\" value=\"@emp.name\" />\n"
                + "\n"
                + "@for(v -> listValues) {\n"
                + "\n"
                + "<label class=\"label-warning\"></label>\n"
                + "\n"
                + "}\n"
                + "\n"
                + "<b>another text</b>\n"
                + "\n"
                + "@listValues map { valMap =>\n"
                + "    <b></b>\n"
                + "}";

        String expectedVariableName = "valMap";
        String expectedVariableType = "Int";
        int caretPosition = 264;

        Optional<TemplateParameter> tpOptional = ScalaTemplateLanguageHelper.getBlockParameter(fileContent, caretPosition);

        assertTrue("tpOptional is not present", tpOptional.isPresent());
        TemplateParameter tp = tpOptional.get();
        assertEquals(expectedVariableName, tp.variableName);
        assertEquals(expectedVariableType, tp.variableType);
    }

    @Test
    public void testGetTemplateParameterInForEachBlock() {
        String fileContent
                = "@(emp: Employee, myVar: String, listValues: List[String])\n"
                + "\n"
                + "this is the @myVar.toLowerCase @emp.age\n"
                + "\n"
                + "<input type=\"text\" value=\"@emp.name\" />\n"
                + "\n"
                + "@for(v -> listValues) {\n"
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
                + "@listValues.foreach { fe =>\n"
                + "    <i></i>\n"
                + "}";

        String expectedVariableName = "fe";
        String expectedVariableType = "String";
        int caretPosition = 306;

        Optional<TemplateParameter> tpOptional = ScalaTemplateLanguageHelper.getBlockParameter(fileContent, caretPosition);

        assertTrue("tpOptional is not present", tpOptional.isPresent());
        TemplateParameter tp = tpOptional.get();
        assertEquals(expectedVariableName, tp.variableName);
        assertEquals(expectedVariableType, tp.variableType);
    }

    @Test
    public void testIsTemplateParameter() {
        String fileContent = "@(emp: Employee, myVar: String, listValues: List[String])\n"
                + "\n"
                + "this is the @myVar.toLowerCase @emp.age\n"
                + "\n"
                + "<input type=\"text\" value=\"@emp.name\" />\n"
                + "\n"
                + "@for(v <- listValues) {\n"
                + "\n"
                + "<label class=\"label-warning\"></label>\n"
                + "\n"
                + "}";

        int caretPosition = 177;

        assertTrue(ScalaTemplateLanguageHelper.isTemplateParameter(fileContent, caretPosition, "v"));
        assertTrue(ScalaTemplateLanguageHelper.isTemplateParameter(fileContent, caretPosition, "emp"));
        assertFalse(ScalaTemplateLanguageHelper.isTemplateParameter(fileContent, caretPosition, "anotherVariable"));
    }

    @Test
    public void getClassFullNameTest() {
        String expected = "scala.collection.immutable.List";

        String actual = ScalaTemplateLanguageHelper.getClassFullName("List", Language.SCALA);

        assertEquals(expected, actual);
    }

    @Test
    public void getClassFullNameTestMoreTypes() {
        assertEquals("scala.Int", ScalaTemplateLanguageHelper.getClassFullName("Int", Language.SCALA));
        assertEquals("scala.Boolean", ScalaTemplateLanguageHelper.getClassFullName("Boolean", Language.SCALA));
        assertEquals("scala.Double", ScalaTemplateLanguageHelper.getClassFullName("Double", Language.SCALA));
        assertEquals("java.lang.String", ScalaTemplateLanguageHelper.getClassFullName("String", Language.SCALA));
    }

    @Test
    public void getClassFullNameJavaTypes() {
        assertEquals("java.lang.Integer", ScalaTemplateLanguageHelper.getClassFullName("Integer", Language.JAVA));
        assertEquals("java.lang.Boolean", ScalaTemplateLanguageHelper.getClassFullName("Boolean", Language.JAVA));
        assertEquals("java.lang.String", ScalaTemplateLanguageHelper.getClassFullName("String", Language.JAVA));
        assertEquals("java.util.Set", ScalaTemplateLanguageHelper.getClassFullName("Set", Language.JAVA));
        assertEquals("java.util.List", ScalaTemplateLanguageHelper.getClassFullName("List", Language.JAVA));
        assertEquals("java.util.Map", ScalaTemplateLanguageHelper.getClassFullName("Map", Language.JAVA));
    }

    @Test
    public void testGetImports() {
        final String fileContent
                = "@(notification: Notification, anotherPar: String)\n"
                + "\n"
                + "@import helper._\n"
                + "@import modu._\n"
                + "@import misc._\n"
                + "\n"
                + "@main() {\n"
                + "\n"
                + "<div id=\"first_div_row\" class=\"row\">\n"
                + "    <div class=\"col-lg-12\">\n"
                + "        <h1 class=\"page-header\">\n"
                + "            @notification.title\n"
                + "            <small> \n"
                + "            </small>\n"
                + "        </h1>\n"
                + "    </div>\n"
                + "</div>"
                + "}";

        final List<String> listExpectedImports = new ArrayList<>();
        listExpectedImports.add("helper");
        listExpectedImports.add("modu");
        listExpectedImports.add("misc");

        List<String> listImports = ScalaTemplateLanguageHelper.getImports(fileContent);

        boolean result = listImports.containsAll(listExpectedImports);

        assertTrue(result);
    }

}
