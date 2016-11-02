package com.qualixium.playnb.unit.sbt;

import com.qualixium.playnb.filetype.sbt.helper.SBTLanguageHelper;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SBTLanguageHelperTest {

    @Test
    public void testGetVaLVariables() {
        String fileContent = "name := \"\"\"another-play-app\"\"\"\n"
                + "\n"
                + "version := \"1.0-SNAPSHOT\"\n"
                + "\n"
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayJava)\n"
                + "\n"
                + "scalaVersion := \"2.11.6\"\n"
                + "\n"
                + "libraryDependencies ++= Seq(\n"
                + "    javaJdbc,\n"
                + "    cache,\n"
                + "    javaWs\n"
                + ")\n"
                + "\n"
                + "libraryDependencies += \"org.postgresql\" % \"postgresql\" % \"9.3-1102-jdbc41\"\n"
                + "\n"
                + "// Play provides two styles of routers, one expects its actions to be injected, the\n"
                + "// other, legacy style, accesses its actions statically.\n"
                + "routesGenerator := InjectedRoutesGenerator\n"
                + "\n"
                + "val myValue = \"dfsf\"\n"
                + "\n";

        List<String> expectedList = new ArrayList<>();
        expectedList.add("root");
        expectedList.add("myValue");

        List<String> actualList = SBTLanguageHelper.getVariables(fileContent);

        assertTrue("does not contains all variables expected",
                actualList.containsAll(expectedList));
    }

    @Test
    public void testGetVaRVariables() {
        String fileContent = "name := \"\"\"another-play-app\"\"\"\n"
                + "\n"
                + "version := \"1.0-SNAPSHOT\"\n"
                + "\n"
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayJava)\n"
                + "\n"
                + "scalaVersion := \"2.11.6\"\n"
                + "\n"
                + "libraryDependencies ++= Seq(\n"
                + "    javaJdbc,\n"
                + "    cache,\n"
                + "    javaWs\n"
                + ")\n"
                + "\n"
                + "libraryDependencies += \"org.postgresql\" % \"postgresql\" % \"9.3-1102-jdbc41\"\n"
                + "\n"
                + "// Play provides two styles of routers, one expects its actions to be injected, the\n"
                + "// other, legacy style, accesses its actions statically.\n"
                + "routesGenerator := InjectedRoutesGenerator\n"
                + "\n"
                + "var myVariable = \"dfsf\"\n"
                + "\n";

        List<String> expectedList = new ArrayList<>();
        expectedList.add("myVariable");

        List<String> actualList = SBTLanguageHelper.getVariables(fileContent);

        assertTrue("does not contains all variables expected",
                actualList.containsAll(expectedList));
    }

}
