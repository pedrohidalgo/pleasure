package com.qualixium.playnb.filetype.routes.parser;

import com.qualixium.playnb.filetype.routes.RoutesLanguageHelper;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.api.Severity;
import org.openide.filesystems.FileObject;

public class RoutesParsingError implements Error {

    private final RoutesErrorEnum errorEnum;
    private final Severity severity;
    private final int startPosition;
    private final int endPosition;

    private RoutesParsingError(RoutesErrorEnum errorEnum, Severity severity,
            int startPostion, int endPosition) {
        this.errorEnum = errorEnum;
        this.severity = severity;
        this.startPosition = startPostion;
        this.endPosition = endPosition;
    }

    public static RoutesParsingError getNewError(RoutesErrorEnum errorEnum,
            int startPosition, int endPosition, Severity severity) {
        RoutesParsingError error = new RoutesParsingError(
                errorEnum, severity, startPosition, endPosition);

        return error;
    }

    @Override
    public String getDisplayName() {
        return errorEnum.description;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getKey() {
        return errorEnum.name();
    }

    @Override
    public FileObject getFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getStartPosition() {
        return startPosition;
    }

    @Override
    public int getEndPosition() {
        return endPosition;
    }

    @Override
    public boolean isLineError() {
        return false;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public Object[] getParameters() {
        return null;
    }

    public RoutesErrorEnum getErrorEnum() {
        return errorEnum;
    }

    public enum RoutesErrorEnum {

        BAD_LINE("This line is not compose of 3 parts (httpMethod, url, classMethod)"),
        HTTP_METHOD_ERROR("This is not an HTTP Method in Uppercase"),
        URL_START_INCORRECT_ERROR("An url should start with: " + RoutesLanguageHelper.URL_START_SYMBOL),
        METHOD_START_WITH_INVALID_CHAR("Method can not start with invalid character"),
        METHOD_DOES_NOT_EXISTS("Method does not exists");

        public final String description;

        private RoutesErrorEnum(String description) {
            this.description = description;
        }
    }

}
