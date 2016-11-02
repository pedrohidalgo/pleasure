package com.qualixium.playnb.filetype.conf.visualconfs;

import java.util.Optional;

public enum JDBCDriverEnum {
    POSTGRESQL("org.postgresql.Driver"),
    MYSQL("com.mysql.jdbc.Driver"),
    MS_SQL_SERVER("com.microsoft.jdbc.sqlserver.SQLServerDriver"),
    ORACLE("oracle.jdbc.driver.OracleDriver");

    private JDBCDriverEnum(String driverName) {
        this.driverName = driverName;
    }

    public String driverName;

    public String getUrl(String host, Optional<String> portOptional, String database) {
        String port = "";
        if (portOptional.isPresent()) {
            port = ":" + portOptional.get();
        }
        switch (this) {
            case POSTGRESQL:
                return "jdbc:postgresql://" + host + port + "/" + database;
            case MYSQL:
                return "jdbc:mysql://" + host + port + "/" + database;
            case MS_SQL_SERVER:
                return "jdbc:microsoft:sqlserver://" + host + port + ";DatabaseName=" + database;
            case ORACLE:
                return "jdbc:oracle:thin:@" + host + port + ":" + database;
            default:
                break;
        }
        return "driver not supported";
    }
    
    public static Optional<JDBCDriverEnum> getByDriverName(String driverName){
        for (JDBCDriverEnum jdbcDriverEnum : JDBCDriverEnum.values()) {
            if(jdbcDriverEnum.driverName.equals(driverName)){
                return Optional.of(jdbcDriverEnum);
            }
        }
        
        return Optional.empty();
    }
    
    public static Optional<JDBCDriverEnum> getByURL(String url){
        if(url.contains("postgresql")){
            return Optional.of(POSTGRESQL);
        }else if(url.contains("mysql")){
            return Optional.of(MYSQL);
        }else if(url.contains("sqlserver")){
            return Optional.of(MS_SQL_SERVER);
        }else if(url.contains("oracle")){
            return Optional.of(ORACLE);
        }
        
        return Optional.empty();
    }
}
