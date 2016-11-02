package com.qualixium.playnb.actions;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.openide.LifecycleManager;
import org.openide.awt.HtmlBrowser.URLDisplayer;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;

public class ActionsHelper {

    public static void runSBTSCoverage(PlayProject playProject) {
        try {
            if (PlayProjectUtil.isActivatorExecutablePathSpecified()) {

                FileObject pluginsSBTFO = playProject.getProjectDirectory().getFileObject("project/plugins.sbt");
                String pluginsSBTContent = pluginsSBTFO.asText(PlayProjectUtil.UTF_8);
                String newFileContent = getNewContentWithSBTCoverageAdded(pluginsSBTContent);

                if (!pluginsSBTContent.equals(newFileContent)) {
                    Files.write(Paths.get(pluginsSBTFO.toURI()), newFileContent.getBytes());
                }

                ExecutionDescriptor descriptor = new ExecutionDescriptor()
                        .frontWindow(true)
                        .showProgress(true)
                        .inputVisible(true)
                        .controllable(true)
                        .preExecution(() -> LifecycleManager.getDefault().saveAll())
                        .postExecution(() -> {
                            try {
                                Optional<String> scalaVersionDirtyOptional = PlayProjectUtil.getScalaVersion(
                                        playProject.getProjectDirectory().getFileObject("build.sbt")
                                        .asText(PlayProjectUtil.UTF_8));
                                if (scalaVersionDirtyOptional.isPresent()) {

                                    String scalaVersionShort = scalaVersionDirtyOptional.get().substring(0, 4);

                                    String urlIndexFile = "file://" + playProject.getProjectDirectory().getFileObject("target").getPath()
                                            + "/scala-" + scalaVersionShort + "/scoverage-report/index.html";
                                    URLDisplayer.getDefault().showURL(new URL(urlIndexFile));
                                }
                            } catch (Exception ex) {
                                ExceptionManager.logException(ex);
                            }
                        });

                ExternalProcessBuilder processBuilder = new ExternalProcessBuilder(PlayProjectUtil.getActivatorExecutablePath())
                        .workingDirectory(new File(playProject.getProjectDirectory().getPath()));

                processBuilder = PlayProjectUtil.addJavaHomeVariable(processBuilder);

                processBuilder = processBuilder
                        .addArgument("clean")
                        .addArgument("coverage")
                        .addArgument("test");

                ExecutionService service = ExecutionService.newService(processBuilder,
                        descriptor, "(" + playProject.getProjectName() + ") clean coverage test");

                StatusDisplayer.getDefault().setStatusText("(" + playProject.getProjectName() + ") Executing SBT TASK: [ clean coverage test]");

                service.run();
            } else {
                PlayProjectUtil.openPlayFrameworkOptionWindow();
            }

        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }
    }

    public static void runJacoco4sbt(PlayProject playProject) {
        try {
            if (PlayProjectUtil.isActivatorExecutablePathSpecified()) {
                FileObject pluginsSBTFO = playProject.getProjectDirectory().getFileObject("project/plugins.sbt");
                String pluginsSBTContent = pluginsSBTFO.asText(PlayProjectUtil.UTF_8);
                String newPluginsSBTFileContent = getNewContentWithJaCoCo4SBTAdded(pluginsSBTContent);

                if (!pluginsSBTContent.equals(newPluginsSBTFileContent)) {
                    Files.write(Paths.get(pluginsSBTFO.toURI()), newPluginsSBTFileContent.getBytes());
                }

                final FileObject fileObjectBuildSBT = playProject.getProjectDirectory().getFileObject("build.sbt");
                String buildSBTContent = fileObjectBuildSBT.asText(PlayProjectUtil.UTF_8);
                String newContentJaCoCoConfigurationsAdded = getNewContentJaCoCoConfigurationsAdded(buildSBTContent);

                if (!buildSBTContent.equals(newContentJaCoCoConfigurationsAdded)) {
                    Files.write(Paths.get(fileObjectBuildSBT.toURI()), newContentJaCoCoConfigurationsAdded.getBytes());
                }

                ExecutionDescriptor descriptor = new ExecutionDescriptor()
                        .frontWindow(true)
                        .showProgress(true)
                        .inputVisible(true)
                        .controllable(true)
                        .preExecution(() -> LifecycleManager.getDefault().saveAll())
                        .postExecution(() -> {
                            try {
                                Optional<String> scalaVersionDirtyOptional = PlayProjectUtil.getScalaVersion(
                                        fileObjectBuildSBT.asText(PlayProjectUtil.UTF_8));
                                if (scalaVersionDirtyOptional.isPresent()) {

                                    String scalaVersionShort = scalaVersionDirtyOptional.get().substring(0, 4);

                                    String urlIndexFile = "file://" + playProject.getProjectDirectory().getFileObject("target").getPath()
                                            + "/scala-" + scalaVersionShort + "/jacoco/html/index.html";
                                    URLDisplayer.getDefault().showURL(new URL(urlIndexFile));
                                }
                            } catch (Exception ex) {
                                ExceptionManager.logException(ex);
                            }
                        });

                ExternalProcessBuilder processBuilder = new ExternalProcessBuilder(PlayProjectUtil.getActivatorExecutablePath())
                        .workingDirectory(new File(playProject.getProjectDirectory().getPath()));

                processBuilder = PlayProjectUtil.addJavaHomeVariable(processBuilder);

                processBuilder = processBuilder.addArgument("jacoco:cover");

                ExecutionService service = ExecutionService.newService(processBuilder,
                        descriptor, "(" + playProject.getProjectName() + ") jacoco:cover");

                StatusDisplayer.getDefault().setStatusText("(" + playProject.getProjectName() + ") Executing SBT TASK: [ jacoco:cover ]");

                service.run();

            } else {
                PlayProjectUtil.openPlayFrameworkOptionWindow();
            }
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }
    }

    public static String getNewContentWithSBTCoverageAdded(String pluginsSBTContent) {

        if (pluginsSBTContent.contains("org.scoverage") && pluginsSBTContent.contains("sbt-scoverage")) {
            return pluginsSBTContent;
        } else {
            String sbtCoverageDepString = "addSbtPlugin(\"org.scoverage\" % \"sbt-scoverage\" % \"1.3.3\")";
            return pluginsSBTContent + MiscUtil.LINE_SEPARATOR + sbtCoverageDepString;
        }

    }

    public static String getNewContentWithJaCoCo4SBTAdded(String pluginsSBTContent) {

        if (pluginsSBTContent.contains("de.johoop") && pluginsSBTContent.contains("jacoco4sbt")) {
            return pluginsSBTContent;
        } else {
            String sbtPluginToAdd = "addSbtPlugin(\"de.johoop\" % \"jacoco4sbt\" % \"2.1.6\")";
            return pluginsSBTContent + MiscUtil.LINE_SEPARATOR + sbtPluginToAdd;
        }
    }

    public static String getNewContentJaCoCoConfigurationsAdded(String buildSBTContent) {
        StringBuilder result = new StringBuilder(buildSBTContent);
        if (!buildSBTContent.contains("jacoco.settings")) {
            result.append(MiscUtil.LINE_SEPARATOR).append(MiscUtil.LINE_SEPARATOR)
                    .append("jacoco.settings");
        }
        if (!buildSBTContent.contains("parallelExecution")) {
            result.append(MiscUtil.LINE_SEPARATOR).append(MiscUtil.LINE_SEPARATOR)
                    .append("parallelExecution in jacoco.Config := false");
        }

        return result.toString();
    }
}
