package com.qualixium.playnb.unit.sbt;

import com.qualixium.playnb.filetype.sbt.format.SBTReformatTask;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SBTFormatTest {

    @Test
    public void testProcessLineSeparator() {
        String fileContent = "name := \"\"\"another-play-app\"\"\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "version := \"1.0-SNAPSHOT\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayJava)"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "scalaVersion := \"2.11.6\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies ++= Seq("+ LINE_SEPARATOR
                + "    javaJdbc,"+ LINE_SEPARATOR
                + "    cache,"+ LINE_SEPARATOR
                + "    javaWs"+ LINE_SEPARATOR
                + ")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"org.postgresql\" % \"postgresql\" % \"9.3-1102-jdbc41\""+ LINE_SEPARATOR
                + "libraryDependencies += \"info.cukes\" % \"cucumber-scala_2.11\" % \"1.2.4\" % \"test\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR;

        String expected = "name := \"\"\"another-play-app\"\"\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "version := \"1.0-SNAPSHOT\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayJava)"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "scalaVersion := \"2.11.6\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies ++= Seq("+ LINE_SEPARATOR
                + "    javaJdbc,"+ LINE_SEPARATOR
                + "    cache,"+ LINE_SEPARATOR
                + "    javaWs"+ LINE_SEPARATOR
                + ")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"org.postgresql\" % \"postgresql\" % \"9.3-1102-jdbc41\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"info.cukes\" % \"cucumber-scala_2.11\" % \"1.2.4\" % \"test\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR;

        String actual = SBTReformatTask.processLineSeparators(fileContent);

        assertEquals(expected, actual);
    }
}
