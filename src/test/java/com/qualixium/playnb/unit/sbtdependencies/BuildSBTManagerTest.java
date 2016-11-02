package com.qualixium.playnb.unit.sbtdependencies;

import com.qualixium.playnb.nodes.sbtdependencies.BuildSBTManager;
import com.qualixium.playnb.nodes.sbtdependencies.Dependency;
import com.qualixium.playnb.nodes.sbtdependencies.Dependency.Scope;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class BuildSBTManagerTest {

    @Test
    public void testGetNewDependencyString() {
        String groupId = "com.fasterxml.jackson.core";
        String artifactId = "jackson-databind";
        String version = "2.5.4";
        Dependency dependencyDTO = new Dependency(groupId, artifactId, version);

        String expected = "libraryDependencies += \"" + groupId + "\" % \"" + artifactId + "\" % \"" + version + "\"";

        String actual = BuildSBTManager.getNewDependencyString(dependencyDTO);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetNewDependencyStringWithScopeSpecified() {
        String groupId = "com.fasterxml.jackson.core";
        String artifactId = "jackson-databind";
        String version = "2.5.4";
        Scope scope = Scope.TEST;
        Dependency dependencyDTO = new Dependency(groupId, artifactId, version, scope);

        String expected = "libraryDependencies += \"" + groupId + "\" % \"" + artifactId + "\" % \"" + version + "\" % \"test\"";

        String actual = BuildSBTManager.getNewDependencyString(dependencyDTO);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetNewDependencyStringWithScopeCompileShouldNotShowCompileString() {
        String groupId = "com.fasterxml.jackson.core";
        String artifactId = "jackson-databind";
        String version = "2.5.4";
        Scope scope = Scope.COMPILE;
        Dependency dependencyDTO = new Dependency(groupId, artifactId, version, scope);

        String expected = "libraryDependencies += \"" + groupId + "\" % \"" + artifactId + "\" % \"" + version + "\"";

        String actual = BuildSBTManager.getNewDependencyString(dependencyDTO);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetNewFileContentWithDependencyAdded() {
        String fileContentBefore = "name := \"\"\"another-play-app\"\"\""+ LINE_SEPARATOR
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
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR
                + LINE_SEPARATOR;

        String fileContentExpected = "name := \"\"\"another-play-app\"\"\""+ LINE_SEPARATOR
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
                + "libraryDependencies += \"com.fasterxml.jackson.core\" % \"jackson-databind\" % \"2.5.4\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR
                + LINE_SEPARATOR;

        String groupId = "com.fasterxml.jackson.core";
        String artifactId = "jackson-databind";
        String version = "2.5.4";
        Dependency dependencyDTO = new Dependency(groupId, artifactId, version);

        String fileContentActual = BuildSBTManager.GetNewFileContentWithDependencyAdded(fileContentBefore, dependencyDTO);

        assertEquals(fileContentExpected, fileContentActual);
    }

    @Test
    public void testGetNewFileContentWhenNoSingleDependencyExists() {
        String fileContentBefore = "name := \"\"\"another-play-app\"\"\""+ LINE_SEPARATOR
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
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR
                + LINE_SEPARATOR;

        String fileContentExpected = "name := \"\"\"another-play-app\"\"\""+ LINE_SEPARATOR
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
                + "libraryDependencies += \"com.fasterxml.jackson.core\" % \"jackson-databind\" % \"2.5.4\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR
                + LINE_SEPARATOR;

        String groupId = "com.fasterxml.jackson.core";
        String artifactId = "jackson-databind";
        String version = "2.5.4";
        Dependency dependencyDTO = new Dependency(groupId, artifactId, version);

        String fileContentActual = BuildSBTManager.GetNewFileContentWithDependencyAdded(fileContentBefore, dependencyDTO);

        assertEquals(fileContentExpected, fileContentActual);
    }

    @Test
    public void testGetNewFileContentWhenThereIsNoDepDeclared() {
        String fileContentBefore = "name := \"\"\"another-play-app\"\"\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "version := \"1.0-SNAPSHOT\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayJava)"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "scalaVersion := \"2.11.6\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR
                + LINE_SEPARATOR;

        String fileContentExpected = "name := \"\"\"another-play-app\"\"\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "version := \"1.0-SNAPSHOT\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayJava)"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "scalaVersion := \"2.11.6\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"com.fasterxml.jackson.core\" % \"jackson-databind\" % \"2.5.4\""+ LINE_SEPARATOR
                + LINE_SEPARATOR;

        String groupId = "com.fasterxml.jackson.core";
        String artifactId = "jackson-databind";
        String version = "2.5.4";
        Dependency dependencyDTO = new Dependency(groupId, artifactId, version);

        String fileContentActual = BuildSBTManager.GetNewFileContentWithDependencyAdded(fileContentBefore, dependencyDTO);

        assertEquals(fileContentExpected, fileContentActual);
    }

    @Test
    public void testGetDependencies() {
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
                + LINE_SEPARATOR
                + "//libraryDependencies += \"info.cukes\" % \"cucumber-scala_2.11\" % \"1.2.4\" % \"test\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"com.fasterxml.jackson.core\" % \"jackson-databind\" % \"2.5.4\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR
                + LINE_SEPARATOR;

        List<Dependency> listDepsExpected = new ArrayList<>();
        listDepsExpected.add(new Dependency("org.postgresql", "postgresql", "9.3-1102-jdbc41"));
        listDepsExpected.add(new Dependency("com.fasterxml.jackson.core", "jackson-databind", "2.5.4"));

        List<Dependency> listDepsActual = BuildSBTManager.getLibraryDependencies(fileContent);

        assertTrue("dependencies expected are not the same than actuals. ", listDepsActual.equals(listDepsExpected));
    }

    @Test
    public void testGetDependenciesWithDoublePercentSymbolToo() {
        String fileContent = "name := \"\"\"my-blog-server\"\"\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "version := \"1.0-SNAPSHOT\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayScala)"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "scalaVersion := \"2.11.6\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies ++= Seq("+ LINE_SEPARATOR
                + "    cache,"+ LINE_SEPARATOR
                + "    ws,"+ LINE_SEPARATOR
                + "    specs2 % Test"+ LINE_SEPARATOR
                + ")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "resolvers += \"scalaz-bintray\" at \"http://dl.bintray.com/scalaz/releases\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"org.postgresql\" % \"postgresql\" % \"9.3-1102-jdbc41\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"com.typesafe.play\" %% \"play-slick\" % \"1.0.1\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"com.typesafe.play\" %% \"play-slick-evolutions\" % \"1.0.1\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"com.facebook.presto\" % \"presto-mysql\" % \"0.122\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator";

        List<Dependency> listDepsExpected = new ArrayList<>();
        listDepsExpected.add(new Dependency("org.postgresql", "postgresql", "9.3-1102-jdbc41"));
        listDepsExpected.add(new Dependency("com.typesafe.play", "play-slick", "1.0.1"));
        listDepsExpected.add(new Dependency("com.typesafe.play", "play-slick-evolutions", "1.0.1"));
        listDepsExpected.add(new Dependency("com.facebook.presto", "presto-mysql", "0.122"));

        List<Dependency> listDepsActual = BuildSBTManager.getLibraryDependencies(fileContent);

        assertTrue("dependencies expected are not the same than actuals. ", listDepsActual.equals(listDepsExpected));
    }

    @Test
    public void testRemoveDependency() {
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
                + LINE_SEPARATOR
                + "libraryDependencies += \"info.cukes\" % \"cucumber-scala_2.11\" % \"1.2.4\" % \"test\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies += \"com.fasterxml.jackson.core\" % \"jackson-databind\" % \"2.5.4\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR;

        String expectedFileContent = "name := \"\"\"another-play-app\"\"\""+ LINE_SEPARATOR
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
                + "libraryDependencies += \"com.fasterxml.jackson.core\" % \"jackson-databind\" % \"2.5.4\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"+ LINE_SEPARATOR;

        List<Dependency> listDepsExpected = new ArrayList<>();
        listDepsExpected.add(new Dependency("org.postgresql", "postgresql", "9.3-1102-jdbc41"));
        listDepsExpected.add(new Dependency("com.fasterxml.jackson.core", "jackson-databind", "2.5.4"));

        Dependency dependencyToRemove = new Dependency("info.cukes", "cucumber-scala_2.11", "1.2.4");

        String newFileContent = BuildSBTManager.removeDependency(fileContent, dependencyToRemove);

        List<Dependency> listDepsActual = BuildSBTManager.getLibraryDependencies(newFileContent);

        assertTrue("dependencies expected are not the same than actuals. ", listDepsActual.equals(listDepsExpected));
        assertEquals(expectedFileContent, newFileContent);
    }
}
