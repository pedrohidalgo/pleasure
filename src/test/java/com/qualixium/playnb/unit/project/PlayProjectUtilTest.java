package com.qualixium.playnb.unit.project;

import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.util.MiscUtil.Language;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PlayProjectUtilTest {

    @Test
    public void testGetPlayVersion23() {
        String fileContent
                = "// Comment to get more information during initialization\n"
                + "logLevel := Level.Warn\n"
                + "\n"
                + "// The Typesafe repository\n"
                + "resolvers += \"Typesafe repository\" at \"http://repo.typesafe.com/typesafe/releases/\"\n"
                + "\n"
                + "// Cloudbees\n"
                + "resolvers += \"Sonatype OSS Snasphots\" at \"http://oss.sonatype.org/content/repositories/snapshots\"\n"
                + "\n"
                + "// Use the Play sbt plugin for Play projects\n"
                + "addSbtPlugin(\"com.typesafe.play\" % \"sbt-plugin\" % \"2.3.0-RC2\")\n"
                + "\n"
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-less\" % \"1.0.0-RC2\")\n"
                + "\n"
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-coffeescript\" % \"1.0.0-RC3\")\n"
                + "\n"
                + "addSbtPlugin(\"com.typesafe.sbteclipse\" % \"sbteclipse-plugin\" % \"4.0.0\")";
        String expected = "2.3.0-RC2";

        Optional<String> actualOptional = PlayProjectUtil.getPlayVersion(fileContent);

        assertTrue("actual is not present", actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetPlayVersion24() {
        String fileContent
                = "// The Play plugin\n"
                + "addSbtPlugin(\"com.typesafe.play\" % \"sbt-plugin\" % \"2.4.2\")\n"
                + "\n"
                + "// Web plugins\n"
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-coffeescript\" % \"1.0.0\")\n"
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-less\" % \"1.0.6\")\n"
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-jshint\" % \"1.0.3\")\n"
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-rjs\" % \"1.0.7\")\n"
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-digest\" % \"1.1.0\")\n"
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-mocha\" % \"1.1.0\")\n"
                + "\n"
                + "// Play enhancer - this automatically generates getters/setters for public fields\n"
                + "// and rewrites accessors of these fields to use the getters/setters. Remove this\n"
                + "// plugin if you prefer not to have this feature, or disable on a per project\n"
                + "// basis using disablePlugins(PlayEnhancer) in your build.sbt\n"
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-play-enhancer\" % \"1.1.0\")\n"
                + "\n"
                + "// Play Ebean support, to enable, uncomment this line, and enable in your build.sbt using\n"
                + "// enablePlugins(SbtEbean). Note, uncommenting this line will automatically bring in\n"
                + "// Play enhancer, regardless of whether the line above is commented out or not.\n"
                + "// addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-play-ebean\" % \"1.0.0\")\n"
                + "addSbtPlugin(\"com.typesafe.sbteclipse\" % \"sbteclipse-plugin\" % \"4.0.0\")";
        String expected = "2.4.2";

        Optional<String> actualOptional = PlayProjectUtil.getPlayVersion(fileContent);

        assertTrue("actual is not present", actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetScalaVersion2_11_7() {
        String fileContent
                = "name := \"\"\"trying23app\"\"\"\n"
                + "\n"
                + "version := \"0.0.1-SNAPSHOT\"\n"
                + "\n"
                + "scalaVersion := \"2.11.7\"\n"
                + "\n"
                + "resolvers += \"scalaz-bintray\" at \"https://dl.bintray.com/scalaz/releases\"\n"
                + "\n"
                + "libraryDependencies ++= Seq(\n"
                + "  jdbc,\n"
                + "  evolutions,\n"
                + "  specs2 % Test,\n"
                + "  \"com.typesafe.play\" %% \"anorm\" % \"2.4.0\",\n"
                + "  \"org.webjars\" % \"jquery\" % \"2.1.4\",\n"
                + "  \"org.webjars\" % \"bootstrap\" % \"3.3.5\"\n"
                + ")     \n"
                + "\n"
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayScala)\n"
                + "\n"
                + "routesGenerator := InjectedRoutesGenerator";
        String expected = "2.11.7";

        Optional<String> actualOptional = PlayProjectUtil.getScalaVersion(fileContent);

        assertTrue("actual is not present", actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetScalaVersion2_10_4() {
        String fileContent
                = "import sbt.Keys._\n"
                + "\n"
                + "name := \"\"\"trying23app2\"\"\"\n"
                + "\n"
                + "version := \"1.0-SNAPSHOT\"\n"
                + "\n"
                + "scalaVersion := \"2.10.4\"\n"
                + "\n"
                + "libraryDependencies ++= Seq(\n"
                + "  jdbc,\n"
                + "  javaEbean,\n"
                + "  cache,\n"
                + "  \"org.mindrot\" % \"jbcrypt\" % \"0.3m\",\n"
                + "  \"com.typesafe\" %% \"play-plugins-mailer\" % \"2.2.0\",\n"
                + "  filters\n"
                + ")\n"
                + "\n"
                + "resolvers ++= Seq(\n"
                + "    \"Apache\" at \"http://repo1.maven.org/maven2/\",\n"
                + "    \"jBCrypt Repository\" at \"http://repo1.maven.org/maven2/org/\",\n"
                + "    \"Sonatype OSS Snasphots\" at \"http://oss.sonatype.org/content/repositories/snapshots\"\n"
                + ")\n"
                + "\n"
                + "\n"
                + "lazy val root = (project in file(\".\")).enablePlugins(play.PlayJava)";
        String expected = "2.10.4";

        Optional<String> actualOptional = PlayProjectUtil.getScalaVersion(fileContent);

        assertTrue("actual is not present", actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testTargetClassPathFromVersion_2_11_7() {
        String scalaVersion = "2.11.7";
        String expected = "/target/scala-2.11/classes/";

        String actual = PlayProjectUtil.getFolderPathTargetClass(scalaVersion);

        assertEquals(expected, actual);
    }

    @Test
    public void testTargetClassPathFromVersion_2_10_4() {
        String scalaVersion = "2.10.4";
        String expected = "/target/scala-2.10/classes/";

        String actual = PlayProjectUtil.getFolderPathTargetClass(scalaVersion);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLanguageConfigured() {
        String fileContent
                = "name := \"\"\"another2\"\"\"\n"
                + "\n"
                + "version := \"1.0-SNAPSHOT\"\n"
                + "\n"
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayJava)\n"
                + "\n"
                + "scalaVersion := \"2.11.6\"\n"
                + "\n"
                + "libraryDependencies ++= Seq(\n"
                + "  javaJdbc,\n"
                + "  cache,\n"
                + "  javaWs\n"
                + ")\n"
                + "\n"
                + "// Play provides two styles of routers, one expects its actions to be injected, the\n"
                + "// other, legacy style, accesses its actions statically.\n"
                + "routesGenerator := InjectedRoutesGenerator";

        Language expected = Language.JAVA;

        Language actual = PlayProjectUtil.getLanguageConfigured(fileContent);

        assertEquals(expected, actual);
    }
}
