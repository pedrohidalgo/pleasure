package com.qualixium.playnb.unit.template;

import com.qualixium.playnb.template.TemplateUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class TemplateUtilTest {

    public static String dirName = "playnbtest_template";
    public static String pathDir;

    @BeforeClass
    public static void setUp() {
        String desktoPath = System.getProperty("user.home") + "/Desktop";
        pathDir = desktoPath + "/" + dirName;
        pathDir = pathDir.replace("\\", "/");
        new File(pathDir + "/controllers").mkdirs();
        new File(pathDir + "/models/module1").mkdirs();
        new File(pathDir + "/models/module2").mkdirs();
    }

    @AfterClass
    public static void cleanUp() {
        deleteDir(new File(pathDir));
    }

    @Test
    public void testGetPackagesName() {
        List<String> listExpected = new ArrayList<>();
        listExpected.add("controllers");
        listExpected.add("models");
        listExpected.add("models.module1");
        listExpected.add("models.module2");

        List<String> listActual = TemplateUtil.getPackagesName(pathDir);

        assertEquals(listExpected.size(), listActual.size());
        assertTrue(listActual.containsAll(listExpected));
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

}
