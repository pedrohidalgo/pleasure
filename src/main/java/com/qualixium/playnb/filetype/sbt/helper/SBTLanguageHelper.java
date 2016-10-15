package com.qualixium.playnb.filetype.sbt.helper;

import com.qualixium.playnb.util.MiscUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SBTLanguageHelper {

    public static List<String> getKeyWords() {
        List<String> keywords = new ArrayList<>();
        keywords.add("import");
        keywords.add("name");
        keywords.add("version");
        keywords.add("scalaVersion");
        keywords.add("libraryDependencies");
        keywords.add("resolvers");
        keywords.add("addSbtPlugin");

        return keywords;
    }

    public static List<String> getVariables(String fileContent) {
        List<String> variables = new ArrayList<>();
        List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);
        String valueIdentificator = "val ";
        String variableIdentificator = "var ";

        listLines.stream()
                .forEach(line -> {
                    int indexOfCaret = 0;
                    if (line.contains(valueIdentificator)) {
                        indexOfCaret = line.indexOf(valueIdentificator) + valueIdentificator.length();

                    } else if (line.contains(variableIdentificator)) {
                        indexOfCaret = line.indexOf(variableIdentificator) + variableIdentificator.length();
                    }
                    if (indexOfCaret > 0) {
                        Optional<String> wordOptional = MiscUtil.getWordFromCaretPosition(line, indexOfCaret);
                        if (wordOptional.isPresent()) {
                            variables.add(wordOptional.get());
                        }
                    }
                });

        return variables;
    }

}
