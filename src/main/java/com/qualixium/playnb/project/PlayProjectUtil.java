package com.qualixium.playnb.project;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.project.general.PlayPanel;
import static com.qualixium.playnb.project.general.PlayPanel.ACTIVATOR_EXECUTABLE_PATH;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.GestureManager;
import com.qualixium.playnb.util.MiscUtil;
import com.qualixium.playnb.util.MiscUtil.Language;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.NbPreferences;

public class PlayProjectUtil {

    public static final String PLAY_FOLDER_TEMPLATES = "Play";
    public static final String PLAY_JAVA_FOLDER_TEMPLATES = "Play/Java";
    public static final String PLAY_SCALA_FOLDER_TEMPLATES = "Play/Scala";
    public static final String UTF_8 = "UTF-8";
    public static final String JDK_HOME = "jdk.home";

    public static Optional<String> getPlayVersion(String fileContent) {
        try {
            List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);

            for (String line : listLines) {
                if (line.contains("com.typesafe.play") && line.contains("sbt-plugin")) {
                    String[] elements = line.split("%");
                    String version = elements[2]
                            .replace("\"", "")
                            .replace(")", "")
                            .trim();

                    return Optional.of(version);
                }
            }
        } catch (Exception e) {
        }

        return Optional.empty();
    }

    public static Optional<String> getScalaVersion(String fileContent) {
        try {
            List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);

            for (String line : listLines) {
                if (line.contains("scalaVersion")) {
                    String versionDirty = line.substring(line.indexOf(":=") + 2, line.length());
                    String version = versionDirty
                            .replace("\"", "")
                            .trim();

                    return Optional.of(version);
                }
            }
        } catch (Exception e) {
        }

        return Optional.empty();
    }

    public static String getFolderPathTargetClass(String scalaVersion) {
        StringBuilder sbResult = new StringBuilder();

        String versionToUseInString = scalaVersion.substring(0, 4);
        sbResult.append("/target/")
                .append("scala-").append(versionToUseInString)
                .append("/classes/");

        return sbResult.toString();
    }

    public static Optional<PlayProject> getPlayProject(FileObject fileObject) {
        Project owner = FileOwnerQuery.getOwner(fileObject);
        if (owner instanceof PlayProject) {
            return Optional.of((PlayProject) owner);
        }

        return Optional.empty();
    }

    public static String getActivatorExecutablePath() {
        String activatorInstallationDir = NbPreferences.forModule(PlayPanel.class).get(ACTIVATOR_EXECUTABLE_PATH, "");
        return activatorInstallationDir + "/" + PlayProjectUtil.getActivatorCommandString();
    }

    public static String getActivatorCommandString() {
        String OSName = System.getProperty("os.name").toLowerCase();
        if (OSName.contains("win")) {
            return "activator.bat";
        } else {
            return "activator";
        }
    }

    public static Language getLanguageConfigured(String buildSBTFileContent) {
        try {
            if (buildSBTFileContent.contains("PlayJava")) {
                return Language.JAVA;
            }
        } catch (Exception ex) {
            ExceptionManager.logException(ex);
        }
        return Language.SCALA;
    }

    public static Language getLanguageConfigured(FileObject fo) {
        try {
            PlayProject playProject = getPlayProject(fo).get();
            FileObject foBuildSBT = playProject.getProjectDirectory().getFileObject("build.sbt");
            String buildSBTContent = foBuildSBT.asText(PlayProjectUtil.UTF_8);

            return getLanguageConfigured(buildSBTContent);
        } catch (Exception ex) {
            ExceptionManager.logException(ex);
        }
        return Language.SCALA;
    }

    public static void openPlayFrameworkOptionWindow() {
        OptionsDisplayer.getDefault().open(OptionsDisplayer.ADVANCED + "/Play");
    }

    public static boolean isActivatorExecutablePathSpecified() {
        String activatorInstallationDir = NbPreferences.forModule(PlayPanel.class).get(PlayPanel.ACTIVATOR_EXECUTABLE_PATH, "");
        return !activatorInstallationDir.trim().isEmpty();
    }

    public static ExternalProcessBuilder addJavaHomeVariable(ExternalProcessBuilder processBuilder) {
        String jdkHomeValue = System.getProperty(PlayProjectUtil.JDK_HOME);
        
        GestureManager.registerGesture(Level.INFO, "1- jdk.home:"+jdkHomeValue, null);
        if (jdkHomeValue != null) {
            processBuilder = processBuilder.addEnvironmentVariable("JAVA_HOME", jdkHomeValue);
        }
        return processBuilder;
    }

}
