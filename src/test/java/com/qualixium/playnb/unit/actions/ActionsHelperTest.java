package com.qualixium.playnb.unit.actions;

import com.qualixium.playnb.actions.ActionsHelper;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ActionsHelperTest {

    @Test
    public void testAddSBTCoveragePlugin() {
        String pluginsSBTContent = "// The Play plugin"
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.play\" % \"sbt-plugin\" % \"2.4.3\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// web plugins"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-coffeescript\" % \"1.0.0\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-less\" % \"1.0.6\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-jshint\" % \"1.0.3\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-rjs\" % \"1.0.7\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-digest\" % \"1.1.0\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-mocha\" % \"1.1.0\")";

        String expected = "// The Play plugin"+ LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.play\" % \"sbt-plugin\" % \"2.4.3\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// web plugins"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-coffeescript\" % \"1.0.0\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-less\" % \"1.0.6\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-jshint\" % \"1.0.3\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-rjs\" % \"1.0.7\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-digest\" % \"1.1.0\")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "addSbtPlugin(\"com.typesafe.sbt\" % \"sbt-mocha\" % \"1.1.0\")"
                + LINE_SEPARATOR
                + "addSbtPlugin(\"org.scoverage\" % \"sbt-scoverage\" % \"1.3.3\")";

        String actual = ActionsHelper.getNewContentWithSBTCoverageAdded(pluginsSBTContent);

        assertEquals(expected, actual);
    }

    @Test
    public void testAddJaCoCoConfigurationToBuildSBT() {
        String buildSBTContent = "name := \"\"\"another4\"\"\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "version := \"1.0-SNAPSHOT\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayScala)"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "scalaVersion := \"2.11.6\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies ++= Seq("+ LINE_SEPARATOR
                + "  jdbc,"+ LINE_SEPARATOR
                + "  cache,"+ LINE_SEPARATOR
                + "  ws,"+ LINE_SEPARATOR
                + "  specs2 % Test"+ LINE_SEPARATOR
                + ")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "resolvers += \"scalaz-bintray\" at \"http://dl.bintray.com/scalaz/releases\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator";

        String expected = "name := \"\"\"another4\"\"\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "version := \"1.0-SNAPSHOT\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "lazy val root = (project in file(\".\")).enablePlugins(PlayScala)"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "scalaVersion := \"2.11.6\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "libraryDependencies ++= Seq("+ LINE_SEPARATOR
                + "  jdbc,"+ LINE_SEPARATOR
                + "  cache,"+ LINE_SEPARATOR
                + "  ws,"+ LINE_SEPARATOR
                + "  specs2 % Test"+ LINE_SEPARATOR
                + ")"+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "resolvers += \"scalaz-bintray\" at \"http://dl.bintray.com/scalaz/releases\""+ LINE_SEPARATOR
                + LINE_SEPARATOR
                + "// Play provides two styles of routers, one expects its actions to be injected, the"+ LINE_SEPARATOR
                + "// other, legacy style, accesses its actions statically."+ LINE_SEPARATOR
                + "routesGenerator := InjectedRoutesGenerator"
                + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "jacoco.settings"
                + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "parallelExecution in jacoco.Config := false";

        String actual = ActionsHelper.getNewContentJaCoCoConfigurationsAdded(buildSBTContent);

        assertEquals(expected, actual);
    }

}
