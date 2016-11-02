package com.qualixium.playnb.unit.routes;

import com.qualixium.playnb.filetype.routes.RoutesLanguageHelper;
import java.util.Optional;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoutesMethodPartTest {

    private static String routesFileContent;

    @BeforeClass
    public static void setUpClass() {
        routesFileContent = "# Routes\n"
                + "# This file defines all application routes (Higher priority routes first)\n"
                + "# ~~~~\n"
                + "\n"
                + "# Home page\n"
                + "GET        /                    controllers.Application.index()\n"
                + "GET        /scala               controllers.MainController.index()\n"
                + "\n"
                + "\n"
                + "# Map static resources from the /public folder to the /assets URL path\n"
                + "GET        /assets/*file        controllers.Assets.versioned(path=\"/public\", file: Asset)";
    }

    @Test
    public void getOnlyClassAndMethodNameFromCompleteMethodSignatureTest() {
        String completeSignatureMethod = "controllers.Assets.versioned(path=\"/public\", file: Asset)";

        String methodName = RoutesLanguageHelper.getOnlyClassAndMethodNameFromCompleteMethodSignature(completeSignatureMethod);

        assertEquals("controllers.Assets.versioned", methodName);
    }

    @Test
    public void getOnlyClassNameFromCompleteMethodSignatureTest() {
        String completeSignatureMethod = "controllers.Assets.versioned(path=\"/public\", file: Asset)";

        String methodName = RoutesLanguageHelper.getOnlyClassNameFromCompleteMethodSignature(completeSignatureMethod);

        assertEquals("controllers.Assets", methodName);
    }

    @Test
    public void getOnlyMethodNameFromCompleteMethodSignatureTest() {
        String completeSignatureMethod = "controllers.Assets.versioned(path=\"/public\", file: Asset)";

        String methodName = RoutesLanguageHelper.getOnlyMethodNameFromCompleteMethodSignature(completeSignatureMethod);

        assertEquals("versioned", methodName);
    }

    @Test
    public void getMethodNameApplicationIndexTest() {
        int offset = 140;

        Optional<String> methodNameOptional = RoutesLanguageHelper.getRouteMethod(routesFileContent, offset);

        assertEquals("controllers.Application.index", methodNameOptional.get());
    }

    @Test
    public void getMethodNameMainIndexTest() {
        int offset = 220;

        Optional<String> methodNameOptional = RoutesLanguageHelper.getRouteMethod(routesFileContent, offset);

        assertEquals("controllers.MainController.index", methodNameOptional.get());
    }

    @Test
    public void thereIsNoMethodInOffetTest() {
        int offset = 49;

        Optional<String> methodNameOptional = RoutesLanguageHelper.getRouteMethod(routesFileContent, offset);

        assertFalse(methodNameOptional.isPresent());
    }

}
