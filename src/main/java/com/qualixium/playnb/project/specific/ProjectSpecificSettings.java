package com.qualixium.playnb.project.specific;

import com.qualixium.playnb.util.ExceptionManager;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.Properties;

public class ProjectSpecificSettings {

    private static final String FILE_NAME = ".nb-play.properties";
    //Keys
    public static final String KEYS_RUN_PORT = "run_port";
    public static final String KEYS_DEBUG_PORT = "debug_port";
    public static final String KEYS_COMMAND_PARAMETERS = "command_parameters";

    private final Properties properties = new Properties();
    private final String fileAbsolutePath;
    private final String directory;

    public ProjectSpecificSettings(String directory) throws IOException {
        this.directory = directory;
        fileAbsolutePath = directory + "/" + FILE_NAME;
        File file = new File(fileAbsolutePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        addToGitIgnoreIfNotAlready();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
        }
    }

    public Optional<String> getValue(String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

    public void setValue(String key, String value, boolean shouldBePersisted) throws IOException {
        properties.setProperty(key, value);
        if (shouldBePersisted) {
            store();
        }
    }

    public void store() throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileAbsolutePath)) {
            properties.store(fileOutputStream, "Play Plugin Properties for specific Project");
        }
    }

    private void addToGitIgnoreIfNotAlready() {
        try {
            String gitIgnorePath = directory + "/.gitignore";
            File fileGitIgnore = new File(gitIgnorePath);
            if (fileGitIgnore.exists()) {
                boolean isAlreadyAdded = Files.lines(Paths.get(gitIgnorePath),
                        StandardCharsets.UTF_8)
                        .anyMatch(line -> line.contains(FILE_NAME));
                if (!isAlreadyAdded) {
                    String gitIgnoreLineToAdd = "/" + FILE_NAME + LINE_SEPARATOR;
                    Files.write(Paths.get(gitIgnorePath),
                            gitIgnoreLineToAdd.getBytes(),
                            StandardOpenOption.APPEND);
                }
            }
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }
    }

}
