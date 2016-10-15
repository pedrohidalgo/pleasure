package com.qualixium.playnb.unit.nodes;

import com.qualixium.playnb.nodes.testfile.TestFileFilterNode;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestFileTest {

    @Test
    public void testGetTestFileClassNameJava() {
        String projectDir = "/home/pedro/NetBeansProjects/another2";
        String path = "/home/pedro/NetBeansProjects/another2/test/ApplicationTest.java";
        String expected = "ApplicationTest";

        String actual = TestFileFilterNode.getFullyQualifyClassName(projectDir, path);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTestFileClassNameScala() {
        String projectDir = "/home/pedro/NetBeansProjects/another2";
        String path = "/home/pedro/NetBeansProjects/another2/test/department/superior/DepartmentSpec.scala";
        String expected = "department.superior.DepartmentSpec";

        String actual = TestFileFilterNode.getFullyQualifyClassName(projectDir, path);

        assertEquals(expected, actual);
    }

}
