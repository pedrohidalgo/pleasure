package com.qualixium.playnb.filetype.routes.parser;

import com.qualixium.playnb.filetype.routes.RoutesLanguageHelper;
import com.qualixium.playnb.filetype.routes.parser.RoutesParsingError.RoutesErrorEnum;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.modules.csl.api.Severity;
import org.openide.filesystems.FileObject;

public class RoutesValidator {

    private static final String INVALID_CHARS_FOR_METHOD = "!$%^&*()[]-=/+";

    public static List<RoutesParsingError> validateFile(Document document) {
        List<RoutesParsingError> listErrors = new ArrayList<>();
        try {
            String fileContent = document.getText(0, document.getLength());

            List<String> lines = MiscUtil.getLinesFromFileContent(fileContent);

            lines.stream()
                    .map(line -> line.trim())
                    .filter(line -> !line.isEmpty()
                            && !line.startsWith(RoutesLanguageHelper.COMMENT_SYMBOL))
                    .forEach(line -> {
                        RoutesLineParsedDTO lineParsedDTO = RoutesLanguageHelper.divideLineInColumns(line);

                        if (lineParsedDTO.isCorrect()) {

                            listErrors.addAll(validateHttpMethod(fileContent, line, lineParsedDTO.httpMethod));

                            listErrors.addAll(validateURL(fileContent, line, lineParsedDTO.url));
//
                            listErrors.addAll(validateMethod(document, line, lineParsedDTO.method));

                        } else {
                            int startPosition = fileContent.indexOf(line);
                            int endPosition = startPosition + line.length();
                            RoutesParsingError error = RoutesParsingError.getNewError(
                                    RoutesErrorEnum.BAD_LINE,
                                    startPosition, endPosition, Severity.ERROR);

                            listErrors.add(error);
                        }

                    });

        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }
        return listErrors;
    }

    public static List<RoutesParsingError> validateHttpMethod(String fileContent, String line, String httpMethod) {
        List<RoutesParsingError> listErrors = new ArrayList<>();
        boolean isHttpMethodValid = RoutesLanguageHelper.getHttpMethods().stream()
                .anyMatch(w3cHttpMethod -> w3cHttpMethod.equals(httpMethod));

        if (!isHttpMethodValid) {
            int startPosition = MiscUtil.getStartPosition(fileContent, line, httpMethod);
            listErrors.add(RoutesParsingError.getNewError(
                    RoutesErrorEnum.HTTP_METHOD_ERROR, startPosition, startPosition + httpMethod.length(), Severity.ERROR));
        }

        return listErrors;
    }

    public static List<RoutesParsingError> validateURL(String fileContent, String line, String url) {
        List<RoutesParsingError> listErrors = new ArrayList<>();
        boolean urlStartCorrect = url.startsWith(RoutesLanguageHelper.URL_START_SYMBOL);

        if (!urlStartCorrect) {
            int startPosition = MiscUtil.getStartPosition(fileContent, line, url);
            listErrors.add(RoutesParsingError.getNewError(
                    RoutesErrorEnum.URL_START_INCORRECT_ERROR, startPosition, startPosition + url.length(), Severity.ERROR));
        }

        return listErrors;
    }

    private static List<RoutesParsingError> validateMethod(Document document, String line, String method) {
        List<RoutesParsingError> listErrors = new ArrayList<>();
        boolean methodExists = false;
        try {
            String fileContent = document.getText(0, document.getLength());
            Optional<RoutesParsingError> errorOptional = validateMethodStartWithInvalidCharacter(fileContent, line, method);
            errorOptional.ifPresent(error -> listErrors.add(error));

            String className = RoutesLanguageHelper.getOnlyClassNameFromCompleteMethodSignature(method);
            if (className.contains("Assets")) {//if contains Assets should not be validated
                methodExists = true;
            } else {
                String methodName = RoutesLanguageHelper.getOnlyMethodNameFromCompleteMethodSignature(method);
                FileObject foDocument = MiscUtil.getFileObject(document);
                ClassPath compileCp = ClassPath.getClassPath(foDocument, ClassPath.COMPILE);
                Class<?> clazz;
                try {
                    clazz = compileCp.getClassLoader(true).loadClass(className);
                    List<Method> listMethods = Arrays.asList(clazz.getDeclaredMethods());
                    methodExists = listMethods.stream()
                            .anyMatch(declaredMethod -> declaredMethod.getName().equals(methodName));
                } catch (ClassNotFoundException ex) {
                }
            }

            if (!methodExists) {
                int startPosition = MiscUtil.getStartPosition(fileContent, line, method);
                listErrors.add(RoutesParsingError.getNewError(
                        RoutesErrorEnum.METHOD_DOES_NOT_EXISTS, startPosition, startPosition + method.length(), Severity.ERROR));
            }
        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }

        return listErrors;
    }

    public static Optional<RoutesParsingError> validateMethodStartWithInvalidCharacter(
            String fileContent, String line, String method) {
        boolean methodStartWithInvalidChar = INVALID_CHARS_FOR_METHOD.chars().anyMatch(ch
                -> method.startsWith(Character.toString((char) ch)));

        if (methodStartWithInvalidChar) {
            int startPosition = MiscUtil.getStartPosition(fileContent, line, method);
            return Optional.of(RoutesParsingError.getNewError(
                    RoutesErrorEnum.METHOD_START_WITH_INVALID_CHAR, startPosition, startPosition + method.length(), Severity.ERROR));
        } else {
            return Optional.empty();
        }

    }

}
