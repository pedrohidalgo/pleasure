package com.qualixium.playnb.nodes.sbtdependencies;

import static com.qualixium.playnb.filetype.sbt.SBTLanguage.COMMENT_SYMBOL;
import com.qualixium.playnb.nodes.sbtdependencies.Dependency.Scope;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import static com.qualixium.playnb.util.MiscUtil.getTimesStringIsInAnother;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BuildSBTManager {

    public static final String NEW_LIBRARY_DEP_SINGLE_LINE_IDENTIFICATOR = "libraryDependencies +=";
    public static final String NEW_LIBRARY_DEP_MULTIPLE_LINE_IDENTIFICATOR = "libraryDependencies ++=";

    public static String getNewDependencyString(Dependency dependency) {
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("libraryDependencies += \"")
                .append(dependency.groupId)
                .append("\" % \"")
                .append(dependency.artifactId)
                .append("\" % \"")
                .append(dependency.version);

        if (dependency.scope.isPresent() && !dependency.scope.get().equals(Scope.COMPILE)) {
            sbResult.append("\" % \"")
                    .append(dependency.scope.get());
        }
        sbResult.append("\"");

        return sbResult.toString();
    }

    public static String GetNewFileContentWithDependencyAdded(final String fileContent, Dependency dependencyDTO) {
        StringBuilder sbResult = new StringBuilder();

        if (fileContent.contains(NEW_LIBRARY_DEP_SINGLE_LINE_IDENTIFICATOR)) {
            int indexOfLineSeparator = fileContent.indexOf(LINE_SEPARATOR, fileContent.lastIndexOf(NEW_LIBRARY_DEP_SINGLE_LINE_IDENTIFICATOR));
            sbResult.append(fileContent.substring(0, indexOfLineSeparator))
                    .append(LINE_SEPARATOR).append(LINE_SEPARATOR)
                    .append(getNewDependencyString(dependencyDTO))
                    .append(fileContent.substring(indexOfLineSeparator, fileContent.length()));
        } else if (fileContent.contains(NEW_LIBRARY_DEP_MULTIPLE_LINE_IDENTIFICATOR)) {
            int indexOfSeprator = fileContent.indexOf(")", fileContent.lastIndexOf(NEW_LIBRARY_DEP_MULTIPLE_LINE_IDENTIFICATOR));
            sbResult.append(fileContent.substring(0, indexOfSeprator + 1))
                    .append(LINE_SEPARATOR).append(LINE_SEPARATOR)
                    .append(getNewDependencyString(dependencyDTO))
                    .append(fileContent.substring(indexOfSeprator + 1, fileContent.length()));
        } else {
            sbResult.append(fileContent.trim()).append(LINE_SEPARATOR)
                    .append(LINE_SEPARATOR)
                    .append(getNewDependencyString(dependencyDTO)).append(LINE_SEPARATOR)
                    .append(LINE_SEPARATOR);
        }

        return sbResult.toString();
    }

    public static List<Dependency> getLibraryDependencies(String fileContent) {
        List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);

        List<Dependency> listResult = listLines.stream()
                .filter(line -> {
                    if (line.contains("resolvers")) {
                        return false;
                    }
                    if (getTimesStringIsInAnother(line, "%") > 1 && !line.contains(COMMENT_SYMBOL)) {
                        return true;
                    } else {
                        if (line.contains(COMMENT_SYMBOL)) {
                            int indexComment = line.indexOf(COMMENT_SYMBOL);
                            int indexSeparator = line.indexOf("%");

                            if (indexSeparator > indexComment) {
                                return false;
                            } else {
                                return true;
                            }
                        } else {
                            return false;
                        }
                    }
                })
                .map(line -> {
                    String lineAfterSeparator = line.substring(line.indexOf("+=") + 2, line.length());
                    String groupIdDirty;
                    String artifactIdDirty;
                    String versionIdDirty;
                    String scopeStringDirty = null;
                    if (lineAfterSeparator.contains("%%")) {
                        int indexOfDoublePercent = lineAfterSeparator.indexOf("%%");
                        int indexOfAlonePercent = lineAfterSeparator.indexOf("%", indexOfDoublePercent + 2);
                        groupIdDirty = lineAfterSeparator.substring(0, indexOfDoublePercent);
                        artifactIdDirty = lineAfterSeparator.substring(
                                indexOfDoublePercent + 2,
                                indexOfAlonePercent);

                        Integer minIndex = MiscUtil.getMinimumIndex(lineAfterSeparator + LINE_SEPARATOR,
                                indexOfAlonePercent + 1, "%" + LINE_SEPARATOR).get();
                        versionIdDirty = lineAfterSeparator.substring(indexOfAlonePercent + 1, minIndex);

                        int indexOfPercentScopeSeparator = lineAfterSeparator.indexOf("%", lineAfterSeparator.indexOf(versionIdDirty) + versionIdDirty.length());
                        if (indexOfPercentScopeSeparator != -1) {
                            scopeStringDirty = lineAfterSeparator.substring(indexOfPercentScopeSeparator, lineAfterSeparator.length());
                        }
                    } else {
                        String[] depParts = lineAfterSeparator.split("%");
                        if (depParts.length >= 3) {
                            groupIdDirty = depParts[0];
                            artifactIdDirty = depParts[1];
                            versionIdDirty = depParts[2];
                            try {
                                scopeStringDirty = depParts[3];
                            } catch (Exception e) {
                            }
                        } else {
                            return null;
                        }
                    }

                    String groupId = cleanDependencyElement(groupIdDirty);
                    String artifacId = cleanDependencyElement(artifactIdDirty);
                    String version = cleanDependencyElement(versionIdDirty);
                    Scope scope = Scope.COMPILE;
                    try {
                        scope = Scope.getByName(cleanDependencyElement(scopeStringDirty));
                    } catch (Exception ex) {
                    }

                    return new Dependency(groupId, artifacId, version, scope);
                })
                .filter(dep -> dep != null)
                .collect(Collectors.toList());

        return listResult;

    }

    public static String cleanDependencyElement(String element) {
        return element.replace("\"", "").replace(",", "").trim();
    }

    public static String removeDependency(String fileContent, Dependency dependencyToRemove) {
        List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);
        StringBuilder sbResult = new StringBuilder();

        int lineRemoveIndex = 0;
        int index = 0;
        for (String line : listLines) {
            index++;
            if (line.contains(dependencyToRemove.groupId)
                    && line.contains(dependencyToRemove.artifactId)
                    && line.contains(dependencyToRemove.version)) {
                lineRemoveIndex = index;
            } else {
                if (!line.trim().isEmpty() || index != lineRemoveIndex + 1) {
                    sbResult.append(line).append(LINE_SEPARATOR);
                }
            }
        }

        return sbResult.toString();
    }

}
