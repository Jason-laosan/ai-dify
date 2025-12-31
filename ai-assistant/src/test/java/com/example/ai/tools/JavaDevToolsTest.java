package com.example.ai.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JavaDevToolsTest {

    private JavaDevTools javaDevTools;

    @BeforeEach
    void setUp() {
        javaDevTools = new JavaDevTools();
    }

    @Test
    void testSearchJavaDoc_ArrayList() {
        String result = javaDevTools.searchJavaDoc("ArrayList");
        assertNotNull(result);
        assertTrue(result.contains("ArrayList"));
    }

    @Test
    void testSearchJavaDoc_HashMap() {
        String result = javaDevTools.searchJavaDoc("HashMap");
        assertNotNull(result);
        assertTrue(result.contains("HashMap"));
    }

    @Test
    void testGetSpringBootProperty_ServerPort() {
        String result = javaDevTools.getSpringBootProperty("server.port");
        assertNotNull(result);
        assertTrue(result.contains("8080"));
    }

    @Test
    void testAnalyzeCode_WithIssues() {
        String code = "System.out.println(obj); if(obj == null) {}";
        String result = javaDevTools.analyzeCode(code);
        assertNotNull(result);
        assertTrue(result.contains("日志框架"));
    }

    @Test
    void testGenerateMavenDependency_Lombok() {
        String result = javaDevTools.generateMavenDependency("lombok");
        assertNotNull(result);
        assertTrue(result.contains("org.projectlombok"));
    }

    @Test
    void testCalculate_SimpleExpression() {
        String result = javaDevTools.calculate("100 + 200");
        assertNotNull(result);
        assertTrue(result.contains("300"));
    }

    @Test
    void testCalculate_ComplexExpression() {
        String result = javaDevTools.calculate("(10 + 20) * 3");
        assertNotNull(result);
        assertTrue(result.contains("90"));
    }
}
