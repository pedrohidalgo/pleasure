package com.qualixium.playnb.filetype.scalatemplate.parser;

import com.qualixium.playnb.util.MiscUtil;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.api.Severity;
import org.openide.filesystems.FileObject;

public class ScalaTemplateParsingError implements Error {

    private final ScalaTemplateErrorEnum errorEnum;
    private final Severity severity;
    private final String displayName;
    private final int startPosition;
    private final int endPosition;

    public ScalaTemplateParsingError(ScalaTemplateErrorEnum errorEnum, Severity severity, String displayName, int startPosition, int endPosition) {
        this.errorEnum = errorEnum;
        this.severity = severity;
        this.displayName = displayName;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public static ScalaTemplateParsingError getNewError(ScalaTemplateErrorEnum errorEnum,
            String displayName, int startPosition, int endPosition, Severity severity) {
        ScalaTemplateParsingError error = new ScalaTemplateParsingError(
                errorEnum, severity, displayName, startPosition, endPosition);

        return error;
    }

    @Override
    public String getDisplayName() {
        return displayName + MiscUtil.LINE_SEPARATOR + "------------";
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

    public ScalaTemplateErrorEnum getErrorEnum() {
        return errorEnum;
    }

    public enum ScalaTemplateErrorEnum {

        ERROR_PARSING_FILE("Error parsing file"),
        MEMBER_DOES_NOT_EXISTS("Member does not exists"),
        BAD_EXPRESSION("Bad Expression"),
        RESOURCE_DOES_NOT_EXISTS("Resource does not exits");

        public final String description;

        private ScalaTemplateErrorEnum(String description) {
            this.description = description;
        }
    }

}
