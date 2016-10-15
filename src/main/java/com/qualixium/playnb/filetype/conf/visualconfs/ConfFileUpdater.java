package com.qualixium.playnb.filetype.conf.visualconfs;

import com.qualixium.playnb.filetype.conf.visualconfs.dto.ConfConnectionDTO;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import java.util.List;
import java.util.Optional;

public class ConfFileUpdater {

    public final static String BEGIN_BLOCK = "#***-BEGIN-DATABASE CONFIGURATIONS***";
    public final static String END_BLOCK = "#***-END-DATABASE CONFIGURATIONS***";

    public static String getContentDatabaseInfo(ConfConnectionDTO dto) {
        String template
                = BEGIN_BLOCK + "\n"
                + "# BLOCK AUTOMATICALLY GENERATED - DO NOT ADD YOUR CUSTOM CONFIGURATIONS HERE!!! TO AVOID LOSE YOUR CHANGES\n"
                + "\n"
                + "db.default.driver = \"${driverName}\"\n"
                + "db.default.url=\"${url}\"\n"
                + "db.default.username = \"${username}\"\n"
                + "db.default.password = \"${password}\"\n"
                + "\n"
                + END_BLOCK;

        return template
                .replace("${driverName}", dto.driverName)
                .replace("${url}", dto.url)
                .replace("${username}", dto.username)
                .replace("${password}", dto.password);
    }

    public static String removeConnectionInfo(String fileContent) {
        if (fileContent.contains(BEGIN_BLOCK)) {

            StringBuilder sbResult = new StringBuilder();

            int indexOfStartBlock = fileContent.indexOf(BEGIN_BLOCK);
            int indexOfEndBlock = fileContent.indexOf(END_BLOCK);

            sbResult.append(fileContent.substring(0, indexOfStartBlock))
                    .append(fileContent.substring(indexOfEndBlock + END_BLOCK.length(), fileContent.length()));

            return sbResult.toString();
        } else {
            return fileContent;
        }
    }

    public static String getUpdatedConnectionInfoContent(ConfConnectionDTO dto, String fileContent) {
        String contentWithOutDBInfo = removeConnectionInfo(fileContent);
        String contentDatabaseInfo = getContentDatabaseInfo(dto);

        if (contentWithOutDBInfo.endsWith(LINE_SEPARATOR)) {
            return contentWithOutDBInfo + contentDatabaseInfo;
        } else {
            return contentWithOutDBInfo + LINE_SEPARATOR + LINE_SEPARATOR + contentDatabaseInfo;
        }

    }

    public static ConfConnectionDTO getConfConnectionDTO(String fileContent) {
        ConfConnectionDTO dto = new ConfConnectionDTO();
        List<String> lines = MiscUtil.getLinesFromFileContent(fileContent);

        lines.stream()
                .filter(line -> !line.startsWith("#"))
                .forEach(line -> {
                    if (line.contains("db.default.driver")) {
                        dto.driverName = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (line.contains("db.default.url")) {
                        dto.url = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (line.contains("db.default.username")) {
                        dto.username = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (line.contains("db.default.password")) {
                        dto.password = line.substring(line.indexOf("=") + 1, line.length());
                    }
                });

        dto.cleanVariables();

        return dto;
    }

    public static Optional<String> getDatabaseNameFromUrl(String url) {
        try {
            Optional<JDBCDriverEnum> jdbcdriverEnumOptional = JDBCDriverEnum.getByURL(url);
            if (jdbcdriverEnumOptional.isPresent()) {
                JDBCDriverEnum jDBCDriverEnum = jdbcdriverEnumOptional.get();
                switch (jDBCDriverEnum) {
                    case ORACLE:
                        return Optional.of(url.substring(url.lastIndexOf(":") + 1, url.length()));
                    case MS_SQL_SERVER:
                        String identificator = "DatabaseName=";

                        return Optional.of(url.substring(url.lastIndexOf(identificator) + identificator.length(), url.length()));
                    case POSTGRESQL:
                    case MYSQL:
                        return Optional.of(url.substring(url.lastIndexOf("/") + 1, url.length()));
                    default:
                        break;
                }
            }
        } catch (Exception e) {
        }

        return Optional.empty();
    }

    public static Optional<String> getHostFromUrl(String url) {
        try {
            Optional<JDBCDriverEnum> jdbcDriverEnumOptional = JDBCDriverEnum.getByURL(url);
            if (jdbcDriverEnumOptional.isPresent()) {
                JDBCDriverEnum jDBCDriverEnum = jdbcDriverEnumOptional.get();
                switch (jDBCDriverEnum) {
                    case ORACLE: {
                        int startIndex = url.lastIndexOf("@") + 1;
                        int endIndex = url.indexOf(":", startIndex);

                        return Optional.of(url.substring(startIndex, endIndex));
                    }
                    case MS_SQL_SERVER: {
                        int startIndex = url.lastIndexOf("://") + 3;
                        int endIndex = url.indexOf(":", startIndex);
                        if (endIndex == -1) {
                            endIndex = url.indexOf(";", startIndex);
                        }
                        return Optional.of(url.substring(startIndex, endIndex));
                    }
                    case POSTGRESQL:
                    case MYSQL: {
                        int startIndex = url.lastIndexOf("://") + 3;
                        int endIndex = url.indexOf(":", startIndex);
                        if (endIndex == -1) {
                            endIndex = url.indexOf("/", startIndex);
                        }
                        return Optional.of(url.substring(startIndex, endIndex));
                    }
                    default:
                        break;
                }
            }

        } catch (Exception e) {
        }
        return Optional.empty();
    }

    public static Optional<String> getPortFromUrl(String url) {
        try {
            Optional<JDBCDriverEnum> jdbcDriverEnumOptional = JDBCDriverEnum.getByURL(url);
            if (jdbcDriverEnumOptional.isPresent()) {
                JDBCDriverEnum jDBCDriverEnum = jdbcDriverEnumOptional.get();
                switch (jDBCDriverEnum) {
                    case ORACLE: {
                        int startIndexPort = url.indexOf(":", url.indexOf(":@") + 2);
                        String port = null;
                        if (startIndexPort != -1) {
                            port = url.substring(startIndexPort + 1, url.indexOf(":", startIndexPort + 1));
                        }

                        return Optional.ofNullable(port);
                    }
                    case MS_SQL_SERVER: {
                        int startIndexPort = url.indexOf(":", url.indexOf("://") + 3);
                        String port = null;
                        if (startIndexPort != -1) {
                            port = url.substring(startIndexPort + 1, url.indexOf(";", startIndexPort));
                        }

                        return Optional.ofNullable(port);
                    }
                    case POSTGRESQL:
                    case MYSQL: {
                        int startIndexPort = url.indexOf(":", url.indexOf("://") + 3);
                        String port = null;
                        if (startIndexPort != -1) {
                            port = url.substring(startIndexPort + 1, url.indexOf("/", startIndexPort));
                        }

                        return Optional.ofNullable(port);
                    }
                    default:
                        break;
                }
            }
        } catch (Exception e) {
        }

        return Optional.empty();
    }
}
