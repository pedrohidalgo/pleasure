package com.qualixium.playnb.filetype.conf.visualconfs;

import com.qualixium.playnb.util.ExceptionManager;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;

public class JDBCHelper {

    public static void connect(FileObject fo, JDBCDriverEnum jDBCDriverEnum,
            String url, String username, String password) throws ClassNotFoundException, SQLException {
        ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);

        Driver driver = null;
        try {

            driver = (Driver) Class.forName(jDBCDriverEnum.driverName, true, compileCp.getClassLoader(true)).newInstance();

        } catch (InstantiationException | IllegalAccessException ex) {
            ExceptionManager.logException(ex);
        }

        DriverManager.registerDriver(new DelegatingDriver(driver)); // register using the Delegating Driver

        DriverManager.getConnection(url, username, password);
    }
}
