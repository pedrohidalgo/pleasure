package com.qualixium.playnb.filetype.scalatemplate.helper;

import com.qualixium.playnb.util.ExceptionManager;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.CLASSPATH_TYPE_ELEMENT_FILTER;
import com.qualixium.playnb.util.MiscUtil.Language;
import static com.qualixium.playnb.util.MiscUtil.Language.JAVA;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import javax.lang.model.element.TypeElement;
import org.netbeans.api.java.source.ElementHandle;

public class ScalaTemplateLanguageHelper {

    public static List<TemplateParameter> getConstructorParameters(String fileContent) {
        try {
            final String symbolsStart = "@(";
            int indexOfStart = fileContent.indexOf(symbolsStart);
            int indexOfEnd = fileContent.indexOf(")", indexOfStart);

            String allParametersTogether = fileContent
                    .substring(indexOfStart + symbolsStart.length(), indexOfEnd);

            if (!allParametersTogether.isEmpty()) {
                List<String> listParametersWithType = Arrays.asList(allParametersTogether.split(","));

                return listParametersWithType.stream()
                        .filter(parameter -> parameter.split(":").length > 1)
                        .map(parameter -> {
                            String[] part = parameter.split(":");
                            TemplateParameter cp = new TemplateParameter();
                            cp.variableName = part[0] == null ? "" : part[0].trim();
                            cp.variableType = part[1] == null ? "" : part[1].trim();
                            return cp;
                        })
                        .collect(Collectors.toList());
            }
        } catch (Exception ex) {

        }
        return Collections.EMPTY_LIST;
    }

    public static List<String> getKeywords() {
        List<String> listKeywords = new ArrayList<>();
        listKeywords.add("for");
        listKeywords.add("if");
        listKeywords.add("defining");
        listKeywords.add("import");
        listKeywords.add("Html");

        return listKeywords;
    }

    public static List<String> getFormsComponentsName() {
        List<String> listKeywords = new ArrayList<>();
        listKeywords.add("form");
        listKeywords.add("inputText");
        listKeywords.add("inputPassword");
        listKeywords.add("inputDate");
        listKeywords.add("inputFile");
        listKeywords.add("inputRadioGroup");
        listKeywords.add("select");
        listKeywords.add("textarea");
        listKeywords.add("checkbox");
        listKeywords.add("input");

        return listKeywords;
    }

    public static Optional<String> getClassFullName(Set<ElementHandle<TypeElement>> cpClasses, String className) {
        if (className.contains("[")) {
            className = className.substring(0, className.indexOf("[")); //removing generics part
        }

        for (ElementHandle<TypeElement> te : cpClasses) {
            String binaryName = te.getBinaryName();
            if (binaryName.startsWith("java.lang.")
                    || binaryName.startsWith("models.")) {
                final String exactClassName = binaryName.substring(
                        binaryName.lastIndexOf(".") + 1, binaryName.length());
                if (exactClassName.equals(className)) {
                    return Optional.of(binaryName);
                }
            } else if (binaryName.equals(className)) {
                return Optional.of(binaryName);
            }
        }

        return Optional.empty();
    }

    public static List<String> getAllScalaTemplateFileNames(Set<ElementHandle<TypeElement>> cpClasses) {
        return cpClasses.stream()
                .filter(CLASSPATH_TYPE_ELEMENT_FILTER)
                .filter(te -> te.getBinaryName().startsWith("views.html."))
                .map(te -> te.getBinaryName())
                .collect(Collectors.toList());
    }

    public static Optional<String> getClassFullNameForHyperLink(Set<ElementHandle<TypeElement>> cpClasses, String className) {
        for (ElementHandle<TypeElement> te : cpClasses) {
            String binaryName = te.getBinaryName();
            if (binaryName.endsWith(className)) {
                return Optional.of(binaryName);
            }
        }

        return Optional.empty();
    }

    public static String getTranslatedMemberName(String methodName) {
        return getTranslatedMemberName(methodName, true);
    }

    public static String getTranslatedMemberName(String methodName, boolean isEscaped) {
        String less = isEscaped ? "&lt;" : "<";

        return methodName
                .replace("$plus", "+")
                .replace("$less", less)
                .replace("$greater", ">")
                .replace("$eq", "=")
                .replace("$minus", "-")
                .replace("$amp", "&")
                .replace("$div", "/")
                .replace("$percent", "%")
                .replace("$up", "^")
                .replace("$bang", "!")
                .replace("$times", "*")
                .replace("$tilde", "~")
                .replace("$colon", ":")
                .replace("$bslash", "\\")
                .replace("$bar", "|");

    }

    public static String getTranslatedType(String type) {

        if (type.equals("boolean")) {
            return "Boolean";
        }
        if (type.equals("byte")) {
            return "Byte";
        }
        if (type.equals("char")) {
            return "Char";
        }
        if (type.equals("double")) {
            return "Double";
        }
        if (type.equals("int")) {
            return "Int";
        }
        if (type.equals("long")) {
            return "Long";
        }
        if (type.equals("float")) {
            return "Float";
        }
        if (type.equals("short")) {
            return "Short";
        }
        if (type.equals("void")) {
            return "Unit";
        }
        if (type.equals("java.lang.Object")) {
            return "Any";
        }
        if (type.equals("java.lang.String")) {
            return "String";
        }

        return type;
    }

    public static String getTranslatedArgument(String argumentName) {
        if (argumentName.startsWith("arg")) {
            String restArg = argumentName.substring(3, argumentName.length());
            try {

                int number = Integer.parseInt(restArg) + 1;
                return "x$" + number;
            } catch (NumberFormatException e) {
            }
        }

        return argumentName;
    }

    public static String getCompletionItemMethodText(final Method method) {
        StringBuilder sbParameter = new StringBuilder();
        String methodName = ScalaTemplateLanguageHelper.getTranslatedMemberName(method.getName());
        String nameReturnType = getTranslatedType(method.getReturnType().getName());

        List<String> parametersString = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            String parameterName = getTranslatedArgument(parameter.getName());

            String parameterTypeName = ScalaTemplateLanguageHelper
                    .getTranslatedType(parameter.getType().getName());
            sbParameter.append(parameterName).append(": ").append(parameterTypeName);
            parametersString.add(sbParameter.toString());
            sbParameter.setLength(0);
        }

        StringJoiner stringJoiner = new StringJoiner(", ", "(", ")");
        parametersString.stream().forEach((parameter) -> {
            stringJoiner.add(parameter);
        });

        String result;
        if (parametersString.isEmpty()) {
            result = methodName + ": " + nameReturnType;
        } else {
            result = methodName + stringJoiner.toString() + ": " + nameReturnType;
        }

        return result;
    }

    public static String getCompletionItemMemberText(Field field) {
        String fieldName = ScalaTemplateLanguageHelper.getTranslatedMemberName(field.getName());
        String fieldReturnType = getTranslatedType(field.getType().getName());

        return fieldName + ": " + fieldReturnType;
    }

    public static Optional<String> getFirstClassFromLine(String line) {
        try {

            char[] chars = line.toCharArray();
            int firstUppercaseIndex = 0;
            for (int i = 0; i < chars.length; i++) {
                if (Character.isUpperCase(chars[i])) {
                    firstUppercaseIndex = i;
                    break;
                }
            }

            String firstClassFromLine = line.substring(0, line.indexOf(".", firstUppercaseIndex));
            boolean anyUpperCaseCharacter = firstClassFromLine.chars().anyMatch(ch -> Character.isUpperCase(ch));
            if (anyUpperCaseCharacter) {
                return Optional.of(firstClassFromLine);
            }
        } catch (Exception e) {
        }

        return Optional.empty();
    }

    public static Optional<Class> getClassToProvideCompletion(String fileContent, String line,
            ClassLoader cl, Language languageConfigured) throws ClassNotFoundException {
        Optional<String> firstClassFromLineOptional = getFirstClassFromLine(line);
        if (firstClassFromLineOptional.isPresent()) {
            String firstClassFromLine = firstClassFromLineOptional.get();
            Class<?> currentClazz = null;
            try {
                currentClazz = cl.loadClass(firstClassFromLine);
            } catch (ClassNotFoundException ex) {
                Optional<Class> classFromImportsOptional = getClassFromImports(fileContent, cl, firstClassFromLine);
                if (classFromImportsOptional.isPresent()) {
                    currentClazz = classFromImportsOptional.get();
                } else if (languageConfigured.equals(JAVA)) {
                    currentClazz = cl.loadClass("java.util." + firstClassFromLine);
                }
            }
            int indexAfterFirstClassFromLine = line.indexOf(firstClassFromLine) + firstClassFromLine.length();
            String lineWithOutFirstType = line.substring(indexAfterFirstClassFromLine, line.length());

            return getClassToProvideCompletion(lineWithOutFirstType, currentClazz);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Class> getClassToProvideCompletionForConstructorParameter(
            String line, String variableName, Class currentClazz) throws ClassNotFoundException {
        int indexAfterVariableName = line.indexOf(variableName) + variableName.length();
        String lineWithOutFirstType = line.substring(indexAfterVariableName, line.length());

        return getClassToProvideCompletion(lineWithOutFirstType, currentClazz);
    }

    private static Optional<Class> getClassToProvideCompletion(String lineWithOutFirstType, Class currentClazz) {
        List<String> parts = Arrays.asList(lineWithOutFirstType.split("\\.")).stream()
                .filter(part -> !part.trim().isEmpty())
                .map(part -> {
                    if (part.contains("(")) {
                        return part.substring(0, part.indexOf("("));
                    } else {
                        return part;
                    }
                })
                .collect(Collectors.toList());

        for (String part : parts) {
            Optional<Field> fieldOptional = Arrays.asList(currentClazz.getFields()).stream().filter(m -> m.getName().equals(part)).findFirst();

            if (fieldOptional.isPresent()) {
                currentClazz = fieldOptional.get().getType();
            } else {
                Optional<Method> methodOptional = Arrays.asList(currentClazz.getMethods()).stream().filter(m -> m.getName().equals(part)).findFirst();

                if (methodOptional.isPresent()) {
                    currentClazz = methodOptional.get().getReturnType();
                }
            }

        }

        //TODO verify this code is working well
        return Optional.ofNullable(currentClazz);
    }

    public static Optional<TemplateParameter> getBlockParameter(String fileContent, int caretPosition) {
        try {
            int openedAmountBracketsAtCaret = ScalaTemplateUtil.getOpenedAmountBracketsAtCaret(fileContent, caretPosition);
            if (openedAmountBracketsAtCaret > 0) {
                Optional<String> blockDefinitionOptional = ScalaTemplateUtil.getBlockDefinition(fileContent, caretPosition);
                if (blockDefinitionOptional.isPresent()) {
                    String blockDefinition = blockDefinitionOptional.get();
                    if (blockDefinition.startsWith("for") && !blockDefinition.startsWith("form")
                            && !blockDefinition.contains("zipWithIndex")) {
                        String variableName = blockDefinition.substring(blockDefinition.indexOf("(") + 1, blockDefinition.indexOf("<-")).trim();
                        String cpName = blockDefinition.substring(blockDefinition.indexOf("<-") + 2, blockDefinition.indexOf(")")).trim();

                        Optional<TemplateParameter> optionalCP = ScalaTemplateLanguageHelper.getConstructorParameters(fileContent)
                                .stream().filter(cp -> cp.variableName.equals(cpName)).findFirst();

                        if (optionalCP.isPresent()) {
                            TemplateParameter cp = optionalCP.get();
                            TemplateParameter tp = new TemplateParameter();
                            tp.variableName = variableName;
                            tp.variableType = cp.variableType.substring(cp.variableType.indexOf("[") + 1, cp.variableType.indexOf("]"));

                            return Optional.of(tp);
                        }
                    } else {
                        Optional<String> valDefinitionOptional = ScalaTemplateUtil.getValDefinition(fileContent, caretPosition);

                        if (valDefinitionOptional.isPresent()) {
                            String variableName = valDefinitionOptional.get();
                            int indexOfDot = blockDefinition.indexOf(".");
                            int indexOfSpace = blockDefinition.indexOf(" ");
                            int minIndex = Math.min(indexOfDot < 0 ? 10000 : indexOfDot, indexOfSpace < 0 ? 10000 : indexOfSpace);
                            String consParamName = blockDefinition.substring(0, minIndex);

                            Optional<TemplateParameter> optionalCP = ScalaTemplateLanguageHelper.getConstructorParameters(fileContent)
                                    .stream().filter(cp -> cp.variableName.equals(consParamName)).findFirst();

                            if (optionalCP.isPresent()) {
                                TemplateParameter cp = optionalCP.get();
                                TemplateParameter tp = new TemplateParameter();
                                tp.variableName = variableName;
                                tp.variableType = cp.variableType.substring(cp.variableType.indexOf("[") + 1, cp.variableType.indexOf("]"));

                                return Optional.of(tp);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ExceptionManager.logException(ex);
        }

        return Optional.empty();
    }

    public static boolean isTemplateParameter(String fileContent, int caretPosition, String variableName) {
        List<TemplateParameter> constructorParameters = getConstructorParameters(fileContent);

        boolean isAConstructorParameter = constructorParameters
                .stream().anyMatch(cp -> cp.variableName.equals(variableName));

        if (isAConstructorParameter) {
            return true;
        } else {
            Optional<TemplateParameter> blockParameterOptional = getBlockParameter(fileContent, caretPosition);
            if (blockParameterOptional.isPresent()) {
                if (blockParameterOptional.get().variableName.equals(variableName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getClassFullName(String className, Language languageConfigured) {
        if (className.contains("[")) {
            className = className.substring(0, className.indexOf("[")); //removing generics part
        }
        if (languageConfigured.equals(Language.SCALA)) {
            switch (className) {
                case "List":
                    return "scala.collection.immutable.List";
                case "String":
                    return "java.lang.String";
                case "Map":
                    return "scala.collection.immutable.Map";
                case "Seq":
                    return "scala.collection.immutable.Seq";
                default:
                    return "scala." + className;

            }
        } else {
            switch (className) {
                case "Collection":
                    return "java.util.Collection";
                case "List":
                    return "java.util.List";
                case "Map":
                    return "java.util.Map";
                case "Set":
                    return "java.util.Set";
                case "SortedMap":
                    return "java.util.SortedMap";
                case "SortedSet":
                    return "java.util.SortedSet";
                default:
                    return "java.lang." + className;
            }
        }

    }

    public static List<String> getImports(String fileContent) {
        try {
            List<String> imports = new ArrayList<>();
            List<String> listLines = MiscUtil.getLinesFromFileContent(fileContent);
            listLines.stream().forEach(line -> {
                if (line.trim().startsWith("@import")) {
                    String identificator = "@import ";
                    int indexOfStart = line.indexOf(identificator);
                    int indexOfEnd = line.indexOf("._");

                    imports.add(line.substring(indexOfStart + identificator.length(), indexOfEnd));
                }
            });

            return imports;
        } catch (Exception ex) {

        }
        return Collections.EMPTY_LIST;
    }

    public static Optional<Class> getClassFromImports(String fileContent, ClassLoader classLoader,
            String className) {
        List<String> imports = getImports(fileContent);
        for (String imp : imports) {
            try {
                return Optional.of(classLoader.loadClass(imp + "." + className));
            } catch (ClassNotFoundException ex) {

            }
        }

        return Optional.empty();
    }

}
