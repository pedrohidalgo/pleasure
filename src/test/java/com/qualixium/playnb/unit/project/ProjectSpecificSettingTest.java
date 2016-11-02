package com.qualixium.playnb.unit.project;

import com.qualixium.playnb.project.specific.ProjectSpecificSettings;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProjectSpecificSettingTest {

    private static String directoryPath;

    @BeforeClass
    public static void setUp() {
        File file = new File(ProjectSpecificSettingTest.class.getClassLoader()
                .getResource(".nb-play.properties").getPath());
        directoryPath = file.getParent();
    }

    @Test
    public void testGetProperty() throws IOException {
        String key = "thekey";
        String expected = "avalue";

        ProjectSpecificSettings settings = new ProjectSpecificSettings(directoryPath);
        Optional<String> actualOptional = settings.getValue(key);

        assertTrue("the key: [" + key + "] is not present", actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testSavePropertyOnMemory() throws IOException {
        String key = "anotherkey2";
        String expected = "theNewValue2";
        ProjectSpecificSettings settings = new ProjectSpecificSettings(directoryPath);

        settings.setValue(key, expected, false);
        Optional<String> actualOptional = settings.getValue(key);

        assertTrue("the key: [" + key + "] is not present", actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testSavePropertyOnDisk() throws IOException {
        String key = "anotherkey2";
        String expected = "theNewValue" + UUID.randomUUID().toString();
        ProjectSpecificSettings settings = new ProjectSpecificSettings(directoryPath);

        settings.setValue(key, expected, true);

        ProjectSpecificSettings settingsOnDiskSaved = new ProjectSpecificSettings(directoryPath);
        Optional<String> actualOptional = settingsOnDiskSaved.getValue(key);

        assertTrue("the key: [" + key + "] is not present", actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }
}
