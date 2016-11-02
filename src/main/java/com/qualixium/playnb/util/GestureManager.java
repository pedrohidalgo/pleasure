package com.qualixium.playnb.util;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author pedro
 */
public abstract class GestureManager {

    public static final String CATEGORY_ERROR = "org.netbeans.ui.playnb.category_error";
    public static final String CATEGORY_GENERAL = "org.netbeans.ui.playnb.category_general";

    public static void registerErrorGesture(Throwable ex) {
        LogRecord logRecord = new LogRecord(Level.SEVERE, ex.toString());
        logRecord.setParameters(new Object[]{Arrays.toString(ex.getStackTrace())});
        logRecord.setLoggerName(CATEGORY_ERROR);
        Logger.getLogger(CATEGORY_ERROR).log(logRecord);
    }

    public static void registerGesture(Level level, String message, Object[] parameters) {
        LogRecord logRecord = new LogRecord(level, message);
        logRecord.setParameters(parameters);
        logRecord.setLoggerName(CATEGORY_GENERAL);
        Logger.getLogger(CATEGORY_GENERAL).log(logRecord);
        
    }

}
