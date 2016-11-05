package com.qualixium.playnb.filetype.routes;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.filetype.routes.parser.RoutesLineParsedDTO;
import com.qualixium.playnb.util.MiscUtil;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.openide.filesystems.FileObject;

public class RoutesLanguageHelper {

    public static final String COMMENT_SYMBOL = "#";
    public static final String URL_START_SYMBOL = "/";

    public static List<String> getHttpMethods() {
        List<String> listHttpMethods = new ArrayList<>();
        listHttpMethods.add("GET");
        listHttpMethods.add("HEAD");
        listHttpMethods.add("POST");
        listHttpMethods.add("PUT");
        listHttpMethods.add("DELETE");
        listHttpMethods.add("TRACE");
        listHttpMethods.add("OPTIONS");
        listHttpMethods.add("CONNECT");
        listHttpMethods.add("PATCH");

        return listHttpMethods;
    }

    public static RoutesLineParsedDTO divideLineInColumns(String line) {
        line = line.trim();
        StringBuilder httpMethod = new StringBuilder();
        StringBuilder url = new StringBuilder();
        StringBuilder method = new StringBuilder();

        WorkFlowEnum workFlowEnum = WorkFlowEnum.HTTP_METHOD_STARTED;

        char[] chars = line.toCharArray();
        for (char ch : chars) {
            if (workFlowEnum.equals(WorkFlowEnum.HTTP_METHOD_STARTED)) {
                if (!Character.isWhitespace(ch)) {
                    httpMethod.append(ch);
                } else {
                    workFlowEnum = WorkFlowEnum.HTTP_METHOD_FINISHED;
                }
            } else if (workFlowEnum.equals(WorkFlowEnum.HTTP_METHOD_FINISHED)) {
                if (!Character.isWhitespace(ch)) {
                    workFlowEnum = WorkFlowEnum.URL_STARTED;
                    url.append(ch);
                }
            } else if (workFlowEnum.equals(WorkFlowEnum.URL_STARTED)) {
                if (!Character.isWhitespace(ch)) {
                    url.append(ch);
                } else {
                    workFlowEnum = WorkFlowEnum.URL_FINISHED;
                }
            } else if (workFlowEnum.equals(WorkFlowEnum.URL_FINISHED)) {
                if (!Character.isWhitespace(ch)) {
                    workFlowEnum = WorkFlowEnum.METHOD_STARTED;
                    method.append(ch);
                }
            } else if (workFlowEnum.equals(WorkFlowEnum.METHOD_STARTED)) {
                method.append(ch);
            }
        }

        RoutesLineParsedDTO dto = new RoutesLineParsedDTO(httpMethod.toString(),
                url.toString(), method.toString());

        return dto;

    }

    public static String formatFile(String fileContent, int spaces) {
        StringBuilder result = new StringBuilder();
        List<String> listAllLines = MiscUtil.getLinesFromFileContent(fileContent);

        Optional<Integer> maxLengthHTTPMethodOptional = listAllLines.stream()
                .map(line -> {
                    if (lineIsEnableToFormat(line)) {
                        RoutesLineParsedDTO lineParsedDTO = divideLineInColumns(line);
                        if (lineParsedDTO.isCorrect()) {
                            return lineParsedDTO.httpMethod.length();
                        }
                    }

                    return 0;
                })
                .max(Integer::compare);

        Optional<Integer> maxLengthURLOptional = getAllUrlsFromRoutesFile(fileContent)
                .stream().map(url -> url.length()).max(Integer::compare);

        if (maxLengthHTTPMethodOptional.isPresent() && maxLengthURLOptional.isPresent()) {
            listAllLines.stream()
                    .forEach(line -> {
                        RoutesLineParsedDTO lineParsedDTO = divideLineInColumns(line);

                        if (lineIsEnableToFormat(line) && lineParsedDTO.isCorrect()) {
                            Integer maxLengthHTTPMethod = maxLengthHTTPMethodOptional.get();
                            Integer maxLenthURL = maxLengthURLOptional.get();
                            result.append(lineParsedDTO.httpMethod)
                            .append(MiscUtil.getAmountSeparatorChars(maxLengthHTTPMethod + spaces - lineParsedDTO.httpMethod.length()))
                            .append(lineParsedDTO.url)
                            .append(MiscUtil.getAmountSeparatorChars(maxLenthURL + spaces - lineParsedDTO.url.length()))
                            .append(lineParsedDTO.method);
                        } else {
                            result.append(line);
                        }

                        result.append(MiscUtil.LINE_SEPARATOR);
                    });
            
            return result.toString().substring(0, result.length() - 1); //substring to remove the last LINE_SEPARATOR
        }

        return fileContent;
    }

    public static boolean lineIsEnableToFormat(String line) {
        return !line.trim().isEmpty() && !line.trim().startsWith(COMMENT_SYMBOL);
    }

    public static boolean lineIsEnableToAutoComplete(String line) {
        return !line.trim().startsWith(COMMENT_SYMBOL);
    }

    public static boolean isWhiteSpaceCharacterAtIndex(String line, int indexAt) {
        try {
            return Character.isWhitespace(line.charAt(indexAt));
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public static boolean isCharacterAtIndex(char theChar, String line, int indexAt) {
        try {
            return theChar == line.charAt(indexAt);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public static List<String> getAllUrlsFromRoutesFile(String fileContent) {
        List<String> listAllLines = MiscUtil.getLinesFromFileContent(fileContent);
        List<String> listUrls = listAllLines.stream()
                .map(line -> {
                    if (lineIsEnableToFormat(line)) {
                        RoutesLineParsedDTO lineParsedDTO = divideLineInColumns(line);
                        if (lineParsedDTO.isCorrect()) {
                            return lineParsedDTO.url;
                        }
                    }

                    return null;
                })
                .filter(line -> line != null)
                .distinct()
                .collect(Collectors.toList());

        return listUrls;
    }

    public static List<String> getAllMethodsFromRoutesFile(String fileContent, boolean withRepeatedValues) {
        List<String> listAllLines = MiscUtil.getLinesFromFileContent(fileContent);
        Stream<String> filterStream = listAllLines.stream()
                .map(line -> {
                    if (lineIsEnableToFormat(line)) {
                        RoutesLineParsedDTO lineParsedDTO = divideLineInColumns(line);
                        if (lineParsedDTO.isCorrect()) {
                            return lineParsedDTO.method;
                        }
                    }

                    return null;
                })
                .filter(line -> line != null);

        List<String> listUrls;
        if (withRepeatedValues) {
            listUrls = filterStream.collect(Collectors.toList());
        } else {
            listUrls = filterStream.distinct().collect(Collectors.toList());
        }

        return listUrls;
    }

    public static Optional<String> getRouteMethod(String routesFileContent, int offset) {
        List<String> listMethods = getAllMethodsFromRoutesFile(routesFileContent, true);

        return listMethods.stream()
                .filter(method -> {
                    int indexStart = routesFileContent.indexOf(method);
                    int indexEnd = indexStart + method.length();

                    return (offset >= indexStart && offset <= indexEnd);
                })
                .findFirst()
                .map(method -> getOnlyClassAndMethodNameFromCompleteMethodSignature(method));
    }

    public static String getOnlyClassAndMethodNameFromCompleteMethodSignature(String completeMethodSignature) {
        if (completeMethodSignature.contains("(")) {
            return completeMethodSignature.substring(0, completeMethodSignature.indexOf("("));
        } else {
            return completeMethodSignature;
        }
    }

    public static String getOnlyClassNameFromCompleteMethodSignature(String completeSignatureMethod) {
        if (completeSignatureMethod.contains(".")) {

            String classWithMethodName = getOnlyClassAndMethodNameFromCompleteMethodSignature(completeSignatureMethod);

            return classWithMethodName.substring(0, classWithMethodName.lastIndexOf("."));
        } else {
            return "";
        }
    }

    public static String getOnlyMethodNameFromCompleteMethodSignature(String completeSignatureMethod) {
        String classWithMethodName = getOnlyClassAndMethodNameFromCompleteMethodSignature(completeSignatureMethod);

        return classWithMethodName.substring(classWithMethodName.lastIndexOf(".") + 1, classWithMethodName.length());
    }

    public enum WorkFlowEnum {

        HTTP_METHOD_STARTED, HTTP_METHOD_FINISHED,
        URL_STARTED, URL_FINISHED,
        METHOD_STARTED, METHOD_FINISHED
    }

    public static List<String> getFullClassNamesFromSourceDir(PlayProject playProject) {
        List<String> listSourceFiles = new ArrayList<>();
        FileObject sourceDirFO = playProject.getProjectDirectory().getFileObject("app");
        Enumeration<? extends FileObject> childrens = sourceDirFO.getChildren(true);
        while(childrens.hasMoreElements()){
            FileObject children = childrens.nextElement();
            if (!children.isFolder()) {
                if (children.getExt().equals("java") || children.getExt().equals("scala")) {
                    String fullClassName = children.getPath()
                            .replace(sourceDirFO.getPath()+"/", "")
                            .replace("/", ".")
                            .replace(".java", "")
                            .replace(".scala", "");
                   listSourceFiles.add(fullClassName);
                }
            }
        }

        return listSourceFiles;
    }

}
