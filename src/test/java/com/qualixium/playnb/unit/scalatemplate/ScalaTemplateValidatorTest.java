package com.qualixium.playnb.unit.scalatemplate;

import com.qualixium.playnb.filetype.scalatemplate.parser.ScalaTemplateValidator;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ScalaTemplateValidatorTest {

    @Test
    public void testGetScalaTemplateResourceNameV24() {
        String expected = "javascripts/hello.js";
        String line = "<script src=\"@routes.Assets.versioned(\"javascripts/hello.js\")\" type=\"text/javascript\"></script>";
        
        Optional<String> actual = ScalaTemplateValidator.getScalaTemplateResourceName(line);
        
        assertTrue("the resource name could not be determined", actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testGetScalaTemplateResourceNameV23() {
        String expected = "lib/jquery/jquery.js";
        String line = "<script src=\"@routes.Assets.at(\"lib/jquery/jquery.js\")\" type=\"text/javascript\"></script>";
        
        Optional<String> actual = ScalaTemplateValidator.getScalaTemplateResourceName(line);
        
        assertTrue("the resource name could not be determined", actual.isPresent());
        assertEquals(expected, actual.get());
    }

}
