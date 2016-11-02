package com.qualixium.playnb.unit.classpath;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.PlayProjectFactory;
import com.qualixium.playnb.classpath.ClassPathUtil;
import com.qualixium.playnb.unit.codegenerator.CodeGeneratorHelperTest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

//TODO this test should not be dependent on any user
@Ignore
public class ClassPathUtilTest {

    private static String eclipsePlay23CP;
    private static String eclipsePlay24CP;
    private static String windows7;
    private static String eclipseSourceTrueCP;
    private static PlayProject playProject;

    @BeforeClass
    public static void setUp() throws IOException, URISyntaxException {
        URI uriFile = CodeGeneratorHelperTest.class.getClassLoader().getResource("eclipse_play23.classpath").toURI();
        eclipsePlay23CP = new String(Files.readAllBytes(Paths.get(uriFile)));
        URI uriFile2 = CodeGeneratorHelperTest.class.getClassLoader().getResource("eclipse_play24.classpath").toURI();
        eclipsePlay24CP = new String(Files.readAllBytes(Paths.get(uriFile2)));
        URI uriFile3 = CodeGeneratorHelperTest.class.getClassLoader().getResource("windows_7.classpath").toURI();
        windows7 = new String(Files.readAllBytes(Paths.get(uriFile3)));
        URI uriFile4 = CodeGeneratorHelperTest.class.getClassLoader().getResource("eclipse_source_true.classpath").toURI();
        eclipseSourceTrueCP = new String(Files.readAllBytes(Paths.get(uriFile4)));
        
        String userHomeDir = System.getProperty("user.home");
        FileObject fo = FileUtil.toFileObject(new File(userHomeDir+"/Desktop"));
        PlayProjectFactory fa = new PlayProjectFactory();
        //TODO: not working Play Project is always null
        playProject = (PlayProject) fa.loadProject(fo, null);
    }

    @Test
    public void getLibPathsVersion23Test() throws IOException {
        String libExpected = "/home/pedro/NetBeansProjects/newPlayApp23/target/scala-2.11/classes_managed";

        List<String> paths = ClassPathUtil.getLibPaths(playProject, eclipsePlay23CP);

        assertTrue("contains classed managed path", paths.contains(libExpected));
    }

    @Test
    public void getLibPathsVersion23DoNotReturnScalaLibraryTest() {
        String libNotExpected = "/home/pedro/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-2.11.6.jar";

        List<String> paths = ClassPathUtil.getLibPaths(playProject, eclipsePlay23CP);

        assertFalse("scala lib is not expected", paths.contains(libNotExpected));
    }

    @Test
    public void getLibPathsWhenEclipseSourceTrue() {
        String libExpected = "/home/pedro/.ivy2/cache/org.javassist/javassist/bundles/javassist-3.19.0-GA.jar";

        List<String> paths = ClassPathUtil.getLibPaths(playProject, eclipseSourceTrueCP);

        assertTrue(paths.contains(libExpected));
    }

    @Test
    public void getLibPathsVersion24Test() {
        String libExpected = "/home/pedro/.ivy2/cache/commons-logging/commons-logging/jars/commons-logging-1.1.3.jar";

        List<String> paths = ClassPathUtil.getLibPaths(playProject, eclipsePlay24CP);

        assertTrue(paths.contains(libExpected));
    }

    @Test
    public void getLibPathsOnWindows7Test() {
        String libExpected = "C:\\Users\\dude\\.ivy2\\cache\\com.typesafe.play\\play_2.11\\jars\\play_2.11-2.4.3.jar";

        List<String> paths = ClassPathUtil.getLibPaths(playProject, windows7);

        assertTrue(paths.contains(libExpected));
    }
    
    @Test
    public void testGetMaxScalaVersionFromPath() {
        String homeDir = System.getProperty("user.home");
        String path = homeDir + "/.ivy2/cache/org.scala-lang/scala-library/jars";

        String expected = "2.11.8";
        String actual = ClassPathUtil.getMaxScalaVersionFromPath(path).get();

        assertEquals(expected, actual);
    }

}
