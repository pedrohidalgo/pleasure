package com.qualixium.playnb.util;

import org.openide.util.Exceptions;

public abstract class ExceptionManager {

    public static final String PATH_EXCEPTION_REPORT = "/exception/report";

    public static void logException(Throwable ex) {
        GestureManager.registerErrorGesture(ex);
        Exceptions.printStackTrace(ex);
    }

}
