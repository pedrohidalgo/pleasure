package com.qualixium.playnb.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.lang.model.element.TypeElement;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.spi.editor.highlighting.support.OffsetsBag;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

public class MiscUtil {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String charactersThatDivideWord = "(),: /.*@#$%^&{}[]\"=?" + LINE_SEPARATOR;

    public enum Language {

        JAVA("java"), SCALA("scala");
        
        /**
         * Extension without the period at the beginning
         */
        private final String extention;

        private Language(String extention) {
            this.extention = extention;
        }

        /**
         * Returns the extension without the period at the beginning
         */
        public String getExtention() {
            return extention;
        }
    }

    public static final Comparator<FileObject> FILE_OBJECT_COMPARATOR = (FileObject f1, FileObject f2) -> {
        if (f1.isFolder() && !f2.isFolder()) {
            return -1;
        } else if (!f1.isFolder() && f2.isFolder()) {
            return 1;
        } else {
            return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
        }
    };

    public static final Predicate<? super ElementHandle<TypeElement>> CLASSPATH_TYPE_ELEMENT_FILTER
            = element -> {
                return !element.getBinaryName().isEmpty()
                && !element.getBinaryName().contains("$")
                && !element.getBinaryName().endsWith("0");
            };

    public static Optional<String> getWordFromCaretPosition(String textDocument, int caretPosition) {
        try {
            int indexBefore = charactersThatDivideWord.chars().map((int ch) -> textDocument.lastIndexOf(ch, caretPosition)).max().getAsInt();
            int indexAfter = charactersThatDivideWord.chars().map((int ch) -> textDocument.indexOf(ch, caretPosition)).filter((int ch) -> ch >= 0).min().getAsInt();
            return Optional.of(textDocument.substring(indexBefore + 1, indexAfter));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static File getPathFile(Document doc, String className) {
        Project project = FileOwnerQuery.getOwner(getFileObject(doc));
        String pathToFileToOpen = project.getProjectDirectory().getFileObject("/app")
                .getPath() + "/" + className.replace(".", "/");
        final File fileJava = new File(pathToFileToOpen + ".java");
        File fileToOpen = fileJava.exists() ? fileJava : new File(pathToFileToOpen + ".scala");
        return fileToOpen;
    }

    public static FileObject getFileObject(Document doc) {
        Object sdp = doc.getProperty(Document.StreamDescriptionProperty);
        if (sdp instanceof FileObject) {
            return (FileObject) sdp;
        }
        if (sdp instanceof DataObject) {
            DataObject dobj = (DataObject) sdp;
            return dobj.getPrimaryFile();
        }
        return null;
    }

    public static int getLineNumber(String fileContent, String stringToSearch) {
        List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);
        int lineNumber = 0;
        for (String line : listLines) {
            lineNumber++;
            if (line.contains(stringToSearch)) {
                break;
            }
        }
        return lineNumber;
    }

    public static String getLinesFromFileContent(String fileContent, int lineStart, int linesToReturn) {
        StringBuilder sbResult = new StringBuilder();
        List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);
        int lineNumber = 0;
        for (String line : listLines) {
            lineNumber++;
            if (lineNumber >= lineStart && lineNumber <= lineStart + linesToReturn) {
                sbResult.append(line).append(LINE_SEPARATOR);
            } else if (lineNumber > lineStart + linesToReturn) {
                break;
            }
        }
        return sbResult.toString();
    }

    public static String getAmountSeparatorChars(int amount) {
        if (amount <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.rangeClosed(1, amount).forEach((int i) -> {
            stringBuilder.append(" ");
        });
        return stringBuilder.toString();
    }

    public static Optional<Integer> getMinimumIndex(String fileContent, int fromIndex, String charsString) {
        return charsString.chars().map(ch -> fileContent.indexOf(ch, fromIndex))
                .filter(index -> index >= 0)
                .boxed()
                .min(Integer::compare);
    }

    public static int getStartPosition(String fileContent, String line, String token) {
        int indexOfLine = fileContent.indexOf(line);
        int indexOfToken = line.indexOf(token);
        return indexOfLine + indexOfToken;
    }

    public static int getTimesStringIsInAnother(String parentString, String occurrenceString) {
        Pattern pattern = Pattern.compile(occurrenceString);
        Matcher matcher = pattern.matcher(parentString);
        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public static List<ElementHandle<TypeElement>> getCleanDependencies(
            Set<ElementHandle<TypeElement>> dependencies) {
        return dependencies.stream()
                .filter(CLASSPATH_TYPE_ELEMENT_FILTER)
                .collect(Collectors.toList());
    }

    public static int getOpenSymbolsAmount(String line) {
        int result = 0;
        char[] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == '{' || ch == '(') {
                result++;
            } else if (ch == '}' || ch == ')') {
                result--;
            }
        }
        return result;
    }

    public static List<String> getLinesFromFileContent(String fileContent) {
        List<String> result = new ArrayList<>();
        try (Scanner scanner = new Scanner(fileContent)) {
            while (scanner.hasNextLine()) {
                result.add(scanner.nextLine());
            }
        }

        return result;
    }

    public static void taskSelectWordOnlyWithCaretPosition(JTextComponent comp,
            OffsetsBag bag, AttributeSet defaultColors) {
        try {
            String documentText = comp.getText();
            String selectedText = comp.getSelectedText();
            String wordSelected = null;
            if (selectedText != null) {
                if (selectedText.length() <= 50) {
                    wordSelected = selectedText;
                }
            } else {
                Optional<String> wordOptional = MiscUtil.getWordFromCaretPosition(documentText,
                        comp.getCaretPosition());
                if (wordOptional.isPresent()) {
                    wordSelected = wordOptional.get();
                }
            }
            if (wordSelected != null) {
                Pattern p = Pattern.compile(wordSelected);
                Matcher m = p.matcher(documentText);
                while (m.find() == true) {
                    int startOffset = m.start();
                    int endOffset = m.end();
                    bag.addHighlight(startOffset, endOffset, defaultColors);
                }
            }
        } catch (Exception ex) {

        }
    }

}
