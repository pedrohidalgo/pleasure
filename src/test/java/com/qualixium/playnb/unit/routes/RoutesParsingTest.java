package com.qualixium.playnb.unit.routes;

import com.qualixium.playnb.filetype.routes.parser.RoutesParsingError;
import com.qualixium.playnb.filetype.routes.parser.RoutesParsingError.RoutesErrorEnum;
import com.qualixium.playnb.filetype.routes.parser.RoutesValidator;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoutesParsingTest {

    static String goodRoutesFileContent;

    @BeforeClass
    public static void setUpClass() throws URISyntaxException, IOException {
        URI uri = RoutesParsingTest.class.getClassLoader().getResource("routes").toURI();
        goodRoutesFileContent = new String(Files.readAllBytes(Paths.get(uri)));
    }

    @Test
    public void validateHttpMethod() {
        String httpMethod = "GET";

        List<RoutesParsingError> listErrors = RoutesValidator.validateHttpMethod(goodRoutesFileContent, "", httpMethod);

        assertTrue(listErrors.isEmpty());
    }

    @Test
    public void validateBadHttpMethod() {
        String httpMethod = "DFD";

        List<RoutesParsingError> listErrors = RoutesValidator.validateHttpMethod(goodRoutesFileContent, "", httpMethod);

        boolean containsHttpMethodError = listErrors.stream()
                .anyMatch(error -> error.getDisplayName().equals(RoutesErrorEnum.HTTP_METHOD_ERROR.description));

        assertTrue(containsHttpMethodError);
    }

    @Test
    public void validateGoodURL() {
        String url = "/employee/save";

        List<RoutesParsingError> listErrors = RoutesValidator.validateURL(goodRoutesFileContent, "", url);

        assertTrue(listErrors.isEmpty());
    }

    @Test
    public void validateURLStartIncorrect() {
        String url = "employee/save";

        List<RoutesParsingError> listErrors = RoutesValidator.validateURL(goodRoutesFileContent, "", url);

        boolean containsError = listErrors.stream()
                .anyMatch(error -> error.getDisplayName().equals(RoutesErrorEnum.URL_START_INCORRECT_ERROR.description));

        assertTrue(containsError);
    }

    @Test
    public void validateMethodStartInvalidCharacter() {
        final String method = "&controllers.MainController.index";

        Optional<RoutesParsingError> errorOptional = RoutesValidator.validateMethodStartWithInvalidCharacter(goodRoutesFileContent, "", method);

        assertTrue(errorOptional.isPresent());
        assertEquals(RoutesErrorEnum.METHOD_START_WITH_INVALID_CHAR.description, errorOptional.get().getDisplayName());
    }

    @Test
    public void validateMethodStartCorrect() {
        final String method = "controllers.MainController.index";

        Optional<RoutesParsingError> errorOptional = RoutesValidator.validateMethodStartWithInvalidCharacter(goodRoutesFileContent, "", method);

        assertFalse(errorOptional.isPresent());
    }

}
