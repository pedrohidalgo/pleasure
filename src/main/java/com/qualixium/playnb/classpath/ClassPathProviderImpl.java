package com.qualixium.playnb.classpath;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.util.ExceptionManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.spi.java.classpath.ClassPathProvider;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Utilities;

public class ClassPathProviderImpl implements ClassPathProvider {

    private final PlayProject playProject;
    private ClassPath boot;
    private ClassPath compile;
    private ClassPath source;
    public ClassLoader classLoaderWithScalaLibrary;

    public ClassPathProviderImpl(PlayProject playProject) {
        this.playProject = playProject;
    }

    @Override
    public synchronized ClassPath findClassPath(FileObject file, String type) {
        if (FileUtil.isParentOf(playProject.getProjectDirectory(), file)) {
            if (ClassPath.BOOT.equals(type)) {
                if (boot == null) {
                    reloadBootClassPath();
                }

                return boot;
            }
            if (ClassPath.COMPILE.equals(type)) {

                if (compile == null) {
                    if (PlayProjectUtil.isActivatorExecutablePathSpecified()) {
                        reloadCompileClassPath();
                    } else {
                        PlayProjectUtil.openPlayFrameworkOptionWindow();
                    }
                }

                return compile;
            }
            if (ClassPath.SOURCE.equals(type)) {
                if (source == null) {
                    setUpSourceClassPath();
                }

                return source;
            }
        }

        return null;
    }

    private void setUpSourceClassPath() {
        if (source != null) {
            GlobalPathRegistry.getDefault().unregister(ClassPath.SOURCE, new ClassPath[]{source});
        }
        FileObject app = playProject.getProjectDirectory().getFileObject("app");
        FileObject test = playProject.getProjectDirectory().getFileObject("test");

        source = ClassPathSupport.createClassPath(app, test);
        GlobalPathRegistry.getDefault().register(ClassPath.SOURCE, new ClassPath[]{source});
    }

    public synchronized List<URL> getBootClassPath() {
        String cp = System.getProperty("sun.boot.class.path");
        String[] paths = cp.split(Pattern.quote(System.getProperty("path.separator")));

        List<URL> listUrls = Arrays.asList(paths)
                .stream()
                .map(path -> {
                    FileObject fileObject = FileUtil.toFileObject(new File(path));
                    if (fileObject != null && fileObject.canRead()) {

                        if (FileUtil.isArchiveFile(fileObject)) {
                            fileObject = FileUtil.getArchiveRoot(fileObject);
                        }

                        return fileObject.toURL();
                    } else {
                        return null;
                    }
                })
                .filter(url -> url != null)
                .collect(Collectors.toList());

        return listUrls;
    }

    public void reloadProject() {
        new Thread(() -> {
            ClassPathUtil.executeEclipseCommand(playProject);
            reloadCompileClassPath();
            setUpSourceClassPath();
        }).start();
    }

    public void reloadCompileClassPath() {
        File eclipseClassPathFile = new File(playProject.getProjectDirectory().getPath()
                + "/.classpath");
        if (!eclipseClassPathFile.exists()) {
            ClassPathUtil.executeEclipseCommand(playProject);
        }

        List<URL> listUrlClassPath = ClassPathUtil.getCompilePathsFromEclipseClassPathFile(playProject)
                .stream()
                .map(jarFilePath -> {
                    try {
                        return Utilities.toURI(new File(jarFilePath)).toURL();
                    } catch (Exception ex) {
                        return null;
                    }
                })
                .filter(url -> url != null)
                .collect(Collectors.toList());

        try {
            Optional<String> scalaVersionOptional = PlayProjectUtil.getScalaVersion(
                    playProject.getProjectDirectory().getFileObject("build.sbt").asText(PlayProjectUtil.UTF_8));
            if (scalaVersionOptional.isPresent()) {
                String scalaVersion = scalaVersionOptional.get();
                String folderPathTargetClass = PlayProjectUtil.getFolderPathTargetClass(scalaVersion);

                String completeTargetClassesPath = playProject.getProjectDirectory().getPath() + folderPathTargetClass;
                File fileTargetClassPath = new File(completeTargetClassesPath);
                if (fileTargetClassPath.exists()) {
                    listUrlClassPath.add(Utilities.toURI(fileTargetClassPath).toURL());
                }
            }

            Optional<String> ivyCacheDirOptional = ClassPathUtil.getIvyCacheDir(listUrlClassPath);
            if (ivyCacheDirOptional.isPresent()) {
                String ivyCacheDir = ivyCacheDirOptional.get();
                String pathScalaLibraryDir = ivyCacheDir + "org.scala-lang/scala-library/jars/";
                Optional<String> maxScalaLibVersionOpt = ClassPathUtil.getMaxScalaVersionFromPath(pathScalaLibraryDir);

                if (maxScalaLibVersionOpt.isPresent()) {
                    String maxScalaLibVersion = maxScalaLibVersionOpt.get();
                    String pathScalaLibraryFile = ivyCacheDir
                            + "org.scala-lang/scala-library/jars/scala-library-" + maxScalaLibVersion + ".jar";
                    URL urlScalaLibrary = Utilities.toURI(new File(pathScalaLibraryFile)).toURL();
                    //TODO this need to be tested on mac
                    if (urlScalaLibrary != null) {
                        listUrlClassPath.add(urlScalaLibrary);
                        classLoaderWithScalaLibrary = URLClassLoader.newInstance(
                                new URL[]{urlScalaLibrary},
                                getClass().getClassLoader()
                        );
                    }
                }

            }

        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }

        List<URL> listUrlFinal = listUrlClassPath.stream()
                .map(urlClassPath -> {
                    if (FileUtil.isArchiveFile(urlClassPath)) {
                        return FileUtil.getArchiveRoot(urlClassPath);
                    } else {
                        return urlClassPath;
                    }
                })
                .collect(Collectors.toList());

        if (compile != null) {
            try {
                GlobalPathRegistry.getDefault().unregister(ClassPath.COMPILE, new ClassPath[]{compile});
            } catch (IllegalArgumentException ex) {
            }
        }

        compile = ClassPathSupport.createClassPath(listUrlFinal.toArray(new URL[listUrlFinal.size()]));
        GlobalPathRegistry.getDefault().register(ClassPath.COMPILE, new ClassPath[]{compile});
    }

    public void reloadBootClassPath() {
        if (boot != null) {
            GlobalPathRegistry.getDefault().unregister(ClassPath.BOOT, new ClassPath[]{boot});
        }

        boot = ClassPathSupport.createClassPath(getBootClassPath().toArray(new URL[0]));
        GlobalPathRegistry.getDefault().register(ClassPath.BOOT, new ClassPath[]{boot});
    }

}
