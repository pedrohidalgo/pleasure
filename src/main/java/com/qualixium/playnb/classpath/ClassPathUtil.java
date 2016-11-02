package com.qualixium.playnb.classpath;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Utilities;

public class ClassPathUtil {

    private static void addEclipseSBTPluginIfNeeded(PlayProject playProject) {
        try {
            FileObject foPluginsSBTFile = playProject.getProjectDirectory().getFileObject("project/plugins.sbt");
            Optional<String> playVersionOptional = PlayProjectUtil.getPlayVersion(foPluginsSBTFile.asText());

            if (playVersionOptional.isPresent()) {
                String playVersion = playVersionOptional.get();
                double playVersionPrimaryNumbers = Double.parseDouble(playVersion.substring(0, 3));
                if (playVersionPrimaryNumbers >= 2.4) {
                    File pluginsSBTFile = FileUtil.toFile(foPluginsSBTFile);

                    Stream<String> lines = Files.lines(pluginsSBTFile.toPath());
                    boolean isSbtEclipsePluginAdded = lines
                            .anyMatch(line -> line.contains("com.typesafe.sbteclipse")
                                    && line.contains("sbteclipse-plugin"));

                    if (!isSbtEclipsePluginAdded) {
                        final String sbtEclipsePluginStringToAdd = LINE_SEPARATOR + LINE_SEPARATOR
                                + "addSbtPlugin(\"com.typesafe.sbteclipse\" % \"sbteclipse-plugin\" % \"5.0.1\")";
                        Files.write(pluginsSBTFile.toPath(),
                                sbtEclipsePluginStringToAdd.getBytes(),
                                StandardOpenOption.APPEND);

                        ExecutionDescriptor descriptor = new ExecutionDescriptor()
                                .frontWindow(true)
                                .frontWindowOnError(true)
                                .inputVisible(true)
                                .showProgress(true);

                        ExternalProcessBuilder processBuilderCompile = new ExternalProcessBuilder(PlayProjectUtil.getActivatorExecutablePath())
                                .workingDirectory(new File(playProject.getProjectDirectory().getPath()))
                                .addArgument("compile");

                        processBuilderCompile = PlayProjectUtil.addJavaHomeVariable(processBuilderCompile);

                        ExecutionService serviceCompile = ExecutionService.newService(processBuilderCompile, descriptor,
                                "Configuring (" + playProject.getProjectName() + ") for first time");
                        Future<Integer> compileFuture = serviceCompile.run();

                        try {
                            compileFuture.get();
                        } catch (InterruptedException | ExecutionException ex) {
                            ExceptionManager.logException(ex);
                        }
                    }
                }
            }

        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }

    }

    public static void executeEclipseCommand(PlayProject playProject) {
        try {
            addEclipseSBTPluginIfNeeded(playProject);
            ExecutionDescriptor descriptor = new ExecutionDescriptor()
                    .frontWindow(true)
                    .inputVisible(true)
                    .showProgress(true);

            ExternalProcessBuilder processBuilderEclipseCommand = new ExternalProcessBuilder(PlayProjectUtil.getActivatorExecutablePath())
                    .workingDirectory(new File(playProject.getProjectDirectory().getPath()))
                    .addArgument("eclipse");

            processBuilderEclipseCommand = PlayProjectUtil.addJavaHomeVariable(processBuilderEclipseCommand);

            ExecutionService serviceEclipseCommand = ExecutionService.newService(processBuilderEclipseCommand,
                    descriptor,
                    "Configuring (" + playProject.getProjectName() + ") Classpath");
            Future<Integer> eclipseCommandFuture = serviceEclipseCommand.run();

            eclipseCommandFuture.get(); //wait for this to finish before leave this method
        } catch (InterruptedException | ExecutionException ex) {
            ExceptionManager.logException(ex);
        }

    }

    public static List<String> getCompilePathsFromEclipseClassPathFile(PlayProject playProject) {
        File eclipseClassPathFile = new File(playProject.getProjectDirectory().getPath()
                + "/.classpath");

        try {
            String content = new String(Files.readAllBytes(
                    Paths.get(Utilities.toURI(eclipseClassPathFile))));

            List<String> libPaths = getLibPaths(playProject, content);
            return libPaths;
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }

        return Collections.EMPTY_LIST;
    }

    public static Optional<String> getIvyCacheDir(List<URL> listUrlsToSearch) {
        String stringUseToFind = "com.typesafe.play";
        String ivyCacheDirIdentificator = ".ivy2/cache";
        Optional<String> firstFoundOptional = listUrlsToSearch.stream()
                .map(url -> url.getPath())
                .filter(path -> path.contains(stringUseToFind) && path.contains(ivyCacheDirIdentificator))
                .findFirst();

        if (firstFoundOptional.isPresent()) {
            String urlString = firstFoundOptional.get();
            return Optional.of(urlString.substring(0, urlString.indexOf(stringUseToFind)));
        }

        return Optional.empty();
    }

    public static Optional<String> getMaxScalaVersionFromPath(String path) {
        File scalaLibraryDir = new File(path);
        FileObject scalaLibraryDirFO = FileUtil.toFileObject(scalaLibraryDir);

        return Arrays.asList(scalaLibraryDirFO.getChildren()).stream()
                .map(file -> {
                    String stringToCompare = "scala-library-";
                    return file.getName().substring(stringToCompare.length(), file.getName().length());
                })
                .sorted((v1, v2) -> v2.compareTo(v1))
                .findFirst();
    }

    public static Optional<Class<?>> loadClass(PlayProject playProject, String fullClassName) {
        try {
            if (playProject.getClassPathProviderImpl().classLoaderWithScalaLibrary != null) {
                return Optional.of(playProject.getClassPathProviderImpl().classLoaderWithScalaLibrary.loadClass(fullClassName));
            } else {
                return Optional.of(Class.forName(fullClassName));
            }
        } catch (ClassNotFoundException ex) {
            return Optional.empty();
        }
    }

    public static List<String> getLibPaths(PlayProject playProject, String fileContent) {
        List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);
        String startContent = " path=\"";

        return listLines.stream()
                .filter(line -> line.contains("kind=\"lib\"") && !line.contains("scala-library"))
                .map(line -> {
                    int indexOfStartContentPlusLength = line.indexOf(startContent) + startContent.length();
                    String path = line.substring(indexOfStartContentPlusLength,
                            line.indexOf("\"", indexOfStartContentPlusLength));

                    if (path.startsWith("./") || path.startsWith(".\\")) {
                        path = playProject.getProjectDirectory().getPath() + path.substring(1, path.length());
                    }
                    return path;
                })
                .collect(Collectors.toList());
    }

}
