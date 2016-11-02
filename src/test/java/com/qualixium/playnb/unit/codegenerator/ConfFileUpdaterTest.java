package com.qualixium.playnb.unit.codegenerator;

import com.qualixium.playnb.filetype.conf.visualconfs.ConfFileUpdater;
import static com.qualixium.playnb.filetype.conf.visualconfs.ConfFileUpdater.BEGIN_BLOCK;
import static com.qualixium.playnb.filetype.conf.visualconfs.ConfFileUpdater.END_BLOCK;
import com.qualixium.playnb.filetype.conf.visualconfs.dto.ConfConnectionDTO;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ConfFileUpdaterTest {

    @Test
    public void testSetUpDatabaseInfo() {
        ConfConnectionDTO dto = new ConfConnectionDTO();
        dto.driverName = "mydrivername";
        dto.url = "theurl";
        dto.username = "theusername";
        dto.password = "thepassword";

        String expected
                = BEGIN_BLOCK + LINE_SEPARATOR
                + "# BLOCK AUTOMATICALLY GENERATED - DO NOT ADD YOUR CUSTOM CONFIGURATIONS HERE!!! TO AVOID LOSE YOUR CHANGES" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "db.default.driver = \"mydrivername\"" + LINE_SEPARATOR
                + "db.default.url=\"theurl\"" + LINE_SEPARATOR
                + "db.default.username = \"theusername\"" + LINE_SEPARATOR
                + "db.default.password = \"thepassword\"" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + END_BLOCK;

        String actual = ConfFileUpdater.getContentDatabaseInfo(dto);

        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveConnectionInfo() {
        String fileContent
                = "ebean.default = [\"models.*\"]" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + BEGIN_BLOCK + LINE_SEPARATOR
                + "# BLOCK AUTOMATICALLY GENERATED - DO NOT ADD YOUR CUSTOM CONFIGURATIONS HERE!!! TO AVOID LOSE YOUR CHANGES" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "db.default.driver = \"${driverName}\"" + LINE_SEPARATOR
                + "db.default.url=\"${url}\"" + LINE_SEPARATOR
                + "db.default.username = \"${username}\"" + LINE_SEPARATOR
                + "db.default.password = \"${password}\"" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + END_BLOCK + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "# Evolutions" + LINE_SEPARATOR
                + "play.evolutions.enabled=true" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "play.evolutions.autoApply = true";

        String expected
                = "ebean.default = [\"models.*\"]" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "# Evolutions" + LINE_SEPARATOR
                + "play.evolutions.enabled=true" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "play.evolutions.autoApply = true";

        String actual = ConfFileUpdater.removeConnectionInfo(fileContent);

        assertEquals(expected, actual);
    }

    @Test
    public void testAddConnectionInfo() {
        String fileContent
                = "ebean.default = [\"models.*\"]" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "# Evolutions" + LINE_SEPARATOR
                + "play.evolutions.enabled=true" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "play.evolutions.autoApply = true";

        String expected
                = "ebean.default = [\"models.*\"]" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "# Evolutions" + LINE_SEPARATOR
                + "play.evolutions.enabled=true" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "play.evolutions.autoApply = true" + LINE_SEPARATOR + LINE_SEPARATOR
                + BEGIN_BLOCK + LINE_SEPARATOR
                + "# BLOCK AUTOMATICALLY GENERATED - DO NOT ADD YOUR CUSTOM CONFIGURATIONS HERE!!! TO AVOID LOSE YOUR CHANGES" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "db.default.driver = \"mydrivername\"" + LINE_SEPARATOR
                + "db.default.url=\"theurl\"" + LINE_SEPARATOR
                + "db.default.username = \"theusername\"" + LINE_SEPARATOR
                + "db.default.password = \"thepassword\"" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + END_BLOCK;

        ConfConnectionDTO dto = new ConfConnectionDTO();
        dto.driverName = "mydrivername";
        dto.url = "theurl";
        dto.username = "theusername";
        dto.password = "thepassword";

        String actual = ConfFileUpdater.getUpdatedConnectionInfoContent(dto, fileContent);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetConfConnectionDTO() {
        String fileContent
                = "ebean.default = [\"models.*\"]" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + BEGIN_BLOCK + LINE_SEPARATOR
                + "# BLOCK AUTOMATICALLY GENERATED - DO NOT ADD YOUR CUSTOM CONFIGURATIONS HERE!!! TO AVOID LOSE YOUR CHANGES" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "db.default.driver = \"mydrivername\"" + LINE_SEPARATOR
                + "db.default.url=\"theurl\"" + LINE_SEPARATOR
                + "db.default.username = \"theusername\"" + LINE_SEPARATOR
                + "db.default.password = \"thepassword\"" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + END_BLOCK + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "# Evolutions" + LINE_SEPARATOR
                + "play.evolutions.enabled=true" + LINE_SEPARATOR
                + LINE_SEPARATOR
                + "play.evolutions.autoApply = true";

        ConfConnectionDTO expected = new ConfConnectionDTO();
        expected.driverName = "mydrivername";
        expected.url = "theurl";
        expected.username = "theusername";
        expected.password = "thepassword";

        ConfConnectionDTO actual = ConfFileUpdater.getConfConnectionDTO(fileContent);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOracleDatabaseNameFromUrl() {
        String url = "jdbc:oracle:thin:@localhost:1521:mkyong";
        String expected = "mkyong";
        Optional<String> actualOptional = ConfFileUpdater.getDatabaseNameFromUrl(url);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetSqlServerDatabaseNameFromUrl() {
        String url = "jdbc:microsoft:sqlserver://HOST:1433;DatabaseName=superdb";
        String expected = "superdb";
        Optional<String> actualOptional = ConfFileUpdater.getDatabaseNameFromUrl(url);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetDatabaseNameFromUrl() {
        String url = "jdbc:postgresql://localhost/mydb";
        String expected = "mydb";
        Optional<String> actualOptional = ConfFileUpdater.getDatabaseNameFromUrl(url);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetHostFromUrl() {
        String url = "jdbc:postgresql://localhost/mydb";
        String expected = "localhost";
        Optional<String> actualOptional = ConfFileUpdater.getHostFromUrl(url);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetHostFromUrlMySQL() {
        String url = "jdbc:mysql://localhost:1234/dbname";
        String expected = "localhost";
        Optional<String> actualOptional = ConfFileUpdater.getHostFromUrl(url);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetHostFromUrlOracle() {
        String url = "jdbc:oracle:thin:@127.0.0.1:1521:mkyong";
        String expected = "127.0.0.1";
        Optional<String> actualOptional = ConfFileUpdater.getHostFromUrl(url);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetHostFromUrlSqlServer() {
        String url = "jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=superdb";
        String expected = "127.0.0.1";
        Optional<String> actualOptional = ConfFileUpdater.getHostFromUrl(url);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetHostFromUrlSqlServerWithOutPort() {
        String url = "jdbc:microsoft:sqlserver://127.0.0.1;DatabaseName=superdb";
        String expected = "127.0.0.1";
        Optional<String> actualOptional = ConfFileUpdater.getHostFromUrl(url);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetHostFromUrlWhenPort() {
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String expected = "localhost";
        Optional<String> actualOptional = ConfFileUpdater.getHostFromUrl(url);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetPortEmptyFromUrl() {
        String url = "jdbc:postgresql://localhost/mydb";

        Optional<String> actualOptional = ConfFileUpdater.getPortFromUrl(url);

        assertFalse(actualOptional.isPresent());
    }

    @Test
    public void testGetPortEmptyFromUrlSqlServer() {
        String url = "jdbc:microsoft:sqlserver://127.0.0.1;DatabaseName=superdb";

        Optional<String> actualOptional = ConfFileUpdater.getPortFromUrl(url);

        assertFalse(actualOptional.isPresent());
    }

    @Test
    public void testGetPortFromUrl() {
        String url = "jdbc:postgresql://localhost:1234/mydb";
        String expected = "1234";

        Optional<String> actualOptional = ConfFileUpdater.getPortFromUrl(url);

        assertTrue(actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetPortFromUrlSqlServer() {
        String url = "jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=superdb";

        String expected = "1433";

        Optional<String> actualOptional = ConfFileUpdater.getPortFromUrl(url);

        assertTrue(actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }

    @Test
    public void testGetPortEmptyFromUrlOracle() {
        String url = "jdbc:oracle:thin:@127.0.0.1:mkyong";

        Optional<String> actualOptional = ConfFileUpdater.getPortFromUrl(url);

        assertFalse(actualOptional.isPresent());
    }

    @Test
    public void testGetPortFromUrlOracle() {
        String url = "jdbc:oracle:thin:@127.0.0.1:1521:mkyong";

        String expected = "1521";

        Optional<String> actualOptional = ConfFileUpdater.getPortFromUrl(url);

        assertTrue(actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }
}
