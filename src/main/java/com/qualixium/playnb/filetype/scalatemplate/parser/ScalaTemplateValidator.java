package com.qualixium.playnb.filetype.scalatemplate.parser;

import com.qualixium.playnb.filetype.scalatemplate.helper.ScalaTemplateLanguageHelper;
import static com.qualixium.playnb.filetype.scalatemplate.helper.ScalaTemplateLanguageHelper.getTranslatedMemberName;
import com.qualixium.playnb.filetype.scalatemplate.helper.ScalaTemplateUtil;
import com.qualixium.playnb.filetype.scalatemplate.helper.TemplateParameter;
import com.qualixium.playnb.filetype.scalatemplate.parser.ScalaTemplateParsingError.ScalaTemplateErrorEnum;
import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import static com.qualixium.playnb.util.MiscUtil.Language.JAVA;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.TypeElement;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.ClassIndex;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.modules.csl.api.Severity;
import org.openide.filesystems.FileObject;

public class ScalaTemplateValidator {

    private ClasspathInfo info;

    public List<ScalaTemplateParsingError> validateFile(Document document) {
        List<ScalaTemplateParsingError> listErrors = new ArrayList<>();
        listErrors.addAll(validateExpressions(document));
        listErrors.addAll(validateResources(document));

        return listErrors;
    }

    public List<ScalaTemplateParsingError> validateExpressions(Document doc) {
        List<ScalaTemplateParsingError> listErrors = new ArrayList<>();
        try {
            String fileContent = doc.getText(0, doc.getLength());
            setUpClassPathInfo(doc);
            FileObject fo = MiscUtil.getFileObject(doc);
            MiscUtil.Language languageConfigured = PlayProjectUtil.getLanguageConfigured(fo);
            ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
            final Set<ElementHandle<TypeElement>> cpClasses = info.getClassIndex()
                    .getDeclaredTypes("",
                            ClassIndex.NameKind.PREFIX,
                            EnumSet.of(ClassIndex.SearchScope.SOURCE,
                                    ClassIndex.SearchScope.DEPENDENCIES));

            List<TemplateParameter> constructorParameters = ScalaTemplateLanguageHelper.getConstructorParameters(fileContent);

            List<String> allExpressionsToValidate = getAllExpressions(fileContent).stream()
                    .filter(exp -> {

                        return !exp.trim().isEmpty()
                                && !exp.startsWith("helper.")
                                && !exp.startsWith("play20")
                                && !exp.startsWith("routes.Assets")
                                && !exp.startsWith("implicitFieldConstructor")
                                && !exp.equals("*") //for comments
                                && !exp.equals("param") // for scala template docs
                                && !exp.equals("content"); //FUTURE: content is a little forced but... it is better for the plugin right now
                    })
                    .collect(Collectors.toList());

            for (String expression : allExpressionsToValidate) {
                try {
                    String firstClassFromLine = null;
                    int caretPosition = fileContent.indexOf(expression);

                    List<String> listScalaTemplateFiles = ScalaTemplateLanguageHelper.getAllScalaTemplateFileNames(cpClasses);
                    boolean isScalaTemplateFile = listScalaTemplateFiles
                            .stream()
                            .anyMatch(tn -> tn.equals(expression));

                    if (!isScalaTemplateFile) {
                        String firstPart;
                        if (expression.contains(".")
                                && expression.chars().anyMatch(ch -> Character.isUpperCase(ch))) {
                            firstPart = expression.substring(0, expression.indexOf("."));

                            Optional<TemplateParameter> tpOptional = Optional.empty();
                            Optional<TemplateParameter> blockParameterOptional = ScalaTemplateLanguageHelper.getBlockParameter(fileContent, caretPosition);
                            if (blockParameterOptional.isPresent() && firstPart.equals(blockParameterOptional.get().variableName)) {
                                tpOptional = blockParameterOptional;
                            } else if (constructorParameters.stream().anyMatch(cp -> cp.variableName.equals(firstPart))) {
                                tpOptional = constructorParameters.stream()
                                        .filter(cp -> cp.variableName.equals(firstPart))
                                        .findFirst();
                            }

                            Optional<Class> currentClassOptional = Optional.empty();
                            if (tpOptional.isPresent()) {
                                TemplateParameter templateParameter = tpOptional.get();

                                try {
                                    currentClassOptional = Optional.of(Class.forName("scala." + templateParameter.variableType));
                                } catch (ClassNotFoundException ex) {
                                    Optional<String> classFullNameOptional = ScalaTemplateLanguageHelper.getClassFullName(cpClasses, templateParameter.variableType);
                                    if (classFullNameOptional.isPresent()) {
                                        currentClassOptional = Optional.of(compileCp.getClassLoader(true).loadClass(classFullNameOptional.get()));
                                    }
                                }

                            } else {
                                Optional<String> firstClassFromLineOptional = ScalaTemplateLanguageHelper.getFirstClassFromLine(expression);
                                if (firstClassFromLineOptional.isPresent()) {
                                    firstClassFromLine = firstClassFromLineOptional.get();
                                    try {
                                        currentClassOptional = Optional.of(compileCp.getClassLoader(true).loadClass(firstClassFromLine));
                                    } catch (ClassNotFoundException ex) {
                                        currentClassOptional = ScalaTemplateLanguageHelper.getClassFromImports(fileContent, compileCp.getClassLoader(true), firstClassFromLine);
                                        if (!currentClassOptional.isPresent()) {
                                            if (languageConfigured.equals(JAVA)) {
                                                try {
                                                    currentClassOptional = Optional.of(compileCp.getClassLoader(true).loadClass("java.util." + firstClassFromLine));
                                                } catch (ClassNotFoundException e) {
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    listErrors.add(createNewErrorBadExpression(fileContent, expression, expression));
                                }
                            }

                            if (currentClassOptional.isPresent()) {
                                Class currentClazz = currentClassOptional.get();
                                String lineWithOutFirstType;
                                if (firstClassFromLine == null) {
                                    lineWithOutFirstType = expression.substring(expression.indexOf(".") + 1, expression.length());
                                } else {
                                    lineWithOutFirstType = expression.substring(
                                            expression.indexOf(".", firstClassFromLine.length()) + 1, expression.length());
                                }

                                List<String> parts = Arrays.asList(lineWithOutFirstType.split("\\.")).stream()
                                        .map(part -> {
                                            if (part.contains("(")) {
                                                return part.substring(0, part.indexOf("("));
                                            } else {
                                                return part;
                                            }
                                        })
                                        .collect(Collectors.toList());

                                for (String part : parts) {
                                    Optional<Field> fieldOptional = Arrays.asList(currentClazz.getFields()).stream()
                                            .filter(m -> getTranslatedMemberName(m.getName(), false).equals(part))
                                            .findFirst();

                                    if (fieldOptional.isPresent()) {
                                        currentClazz = fieldOptional.get().getType();
                                    } else {
                                        Optional<Method> methodOptional = Arrays.asList(currentClazz.getMethods()).stream()
                                                .filter(m -> getTranslatedMemberName(m.getName(), false).equals(part))
                                                .findFirst();

                                        if (methodOptional.isPresent()) {
                                            currentClazz = methodOptional.get().getReturnType();
                                        } else {
                                            ScalaTemplateParsingError newError = createNewErrorMemberDoesNotExists(fileContent, expression, part);
                                            listErrors.add(newError);
                                        }
                                    }

                                }
                            } else {
                                listErrors.add(createNewErrorBadExpression(fileContent, expression, expression));
                            }
                        } else {
                            firstPart = expression;
                            List<String> imports = ScalaTemplateLanguageHelper.getImports(fileContent);
                            List<String> templatesFileNames = ScalaTemplateLanguageHelper.getAllScalaTemplateFileNames(cpClasses)
                                    .stream()
                                    .map(templateName -> templateName.replace("views.html.", ""))
                                    .collect(Collectors.toList());

                            boolean isTemplateFile = templatesFileNames.stream().anyMatch(tn -> {
                                if (tn.equals(firstPart)) {
                                    return true;
                                } else {
                                    return imports.stream()
                                            .anyMatch((anImport) -> tn.equals(anImport+"."+firstPart));
                                }
                            });
                            
                            boolean isConstructorParameter = constructorParameters.stream().anyMatch(cp -> cp.variableName.equals(firstPart));
                            Optional<TemplateParameter> blockParameterOptional = ScalaTemplateLanguageHelper.getBlockParameter(fileContent, caretPosition);
                            if (!blockParameterOptional.isPresent()
                                    && !isConstructorParameter
                                    && !isTemplateFile) {
                                listErrors.add(createNewErrorBadExpression(fileContent, expression, expression));
                            }
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    //do nothing with class not found exception
                }
            }
        } catch (BadLocationException ex) {
            ExceptionManager.logException(ex);
        }

        return listErrors;
    }

    private static ScalaTemplateParsingError createNewErrorMemberDoesNotExists(String fileContent, String expression, String part) {
        int startPosition = fileContent.indexOf('@' + expression);
        ScalaTemplateParsingError newError = ScalaTemplateParsingError.getNewError(
                ScalaTemplateErrorEnum.MEMBER_DOES_NOT_EXISTS,
                ScalaTemplateErrorEnum.MEMBER_DOES_NOT_EXISTS.description + " [" + part + "]",
                startPosition, startPosition + expression.length(), Severity.ERROR);
        return newError;
    }

    private static ScalaTemplateParsingError createNewErrorBadExpression(String fileContent, String expression, String part) {
        int startPosition = fileContent.indexOf('@' + expression);
        ScalaTemplateParsingError newError = ScalaTemplateParsingError.getNewError(
                ScalaTemplateErrorEnum.BAD_EXPRESSION,
                ScalaTemplateErrorEnum.BAD_EXPRESSION.description + " [@" + part + "]",
                startPosition, startPosition + expression.length(), Severity.ERROR);
        return newError;
    }

    private static ScalaTemplateParsingError createNewErrorResourceDoesNotExists(String fileContent, String resource) {
        int startPosition = fileContent.indexOf(resource);
        ScalaTemplateParsingError newError = ScalaTemplateParsingError.getNewError(
                ScalaTemplateErrorEnum.RESOURCE_DOES_NOT_EXISTS,
                ScalaTemplateErrorEnum.RESOURCE_DOES_NOT_EXISTS.description + " [" + resource + "]",
                startPosition, startPosition + resource.length(), Severity.ERROR);
        return newError;
    }

    private void setUpClassPathInfo(Document doc) {
//        if (info == null) {//FUTURE use a better way to manage the ClassPathInfo creation
        FileObject fo = MiscUtil.getFileObject(doc);
        ClassPath bootCp = ClassPath.getClassPath(fo, ClassPath.BOOT);
        ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
        ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);

        info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
//        }
    }

    public static List<String> getAllExpressions(String fileContent) {
        //FUTURE make sure the comments are syntax highlighted
        List<String> keywords = ScalaTemplateLanguageHelper.getKeywords();
        final String charsToSeparateExpressions = " (<\"" + LINE_SEPARATOR;
        List<String> expressions = new ArrayList<>();
        char[] charArray = fileContent.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];
            if (i - 1 >= 0 && i + 1 < charArray.length) {
                char charBefore = charArray[i - 1];
                char charAfter = charArray[i + 1];
                if (ch == '@' && (charBefore != '@' && charAfter != '@' && charAfter != '(')) {
                    Optional<Integer> indexSeparatorOptional = MiscUtil.getMinimumIndex(fileContent, i, charsToSeparateExpressions);
                    if (indexSeparatorOptional.isPresent()) {
                        String expression = fileContent.substring(i + 1, indexSeparatorOptional.get());
                        if (!keywords.contains(expression)) {
                            expressions.add(expression);
                        }
                    }
                }
            }
        }

        return expressions;
    }

    private List<ScalaTemplateParsingError> validateResources(Document document) {
        List<ScalaTemplateParsingError> listErrors = new ArrayList<>();
        String identificator = "@routes.Assets.";
        FileObject fo = MiscUtil.getFileObject(document);
        try {
            String fileContent = fo.asText(PlayProjectUtil.UTF_8);
            if (fileContent.contains(identificator)) {
                Optional<FileObject> fileObjectResourcesRootOptional = ScalaTemplateUtil.getFileObjectResourcesRoot(document);
                if (fileObjectResourcesRootOptional.isPresent()) {
                    FileObject foResourcesRoot = fileObjectResourcesRootOptional.get();
                    List<String> linesFromFileContent = MiscUtil.getLinesFromFileContent(fileContent);
                    linesFromFileContent.stream()
                            .forEach(line -> {
                                if (line.contains(identificator)) {
                                    Optional<String> resNameOptional = getScalaTemplateResourceName(line);
                                    if (resNameOptional.isPresent()) {
                                        String resourceName = resNameOptional.get();
                                        FileObject fileObjectResName = foResourcesRoot.getFileObject(resourceName);
                                        if (fileObjectResName == null) {
                                            listErrors.add(createNewErrorResourceDoesNotExists(fileContent, resourceName));
                                        }
                                    }
                                }
                            });
                }
            }
        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }

        return listErrors;
    }

    public static Optional<String> getScalaTemplateResourceName(String line) {
        String identificator24 = "@routes.Assets.versioned(\"";
        String identificator23 = "@routes.Assets.at(\"";
        if (line.contains(identificator24)) {
            int indexStart = line.indexOf(identificator24) + identificator24.length();
            int indexEnd = line.indexOf("\"", indexStart);
            return Optional.of(line.substring(indexStart, indexEnd));
        }
        if (line.contains(identificator23)) {
            int indexStart = line.indexOf(identificator23) + identificator23.length();
            int indexEnd = line.indexOf("\"", indexStart);
            return Optional.of(line.substring(indexStart, indexEnd));
        }

        return Optional.empty();
    }

}
