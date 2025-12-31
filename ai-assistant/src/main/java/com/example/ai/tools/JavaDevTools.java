package com.example.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JavaDevTools {

    @Tool("搜索Java类或方法的官方文档说明")
    public String searchJavaDoc(String className) {
        log.info("Searching Java doc for: {}", className);
        
        return switch (className.toLowerCase()) {
            case "arraylist" -> """
                    java.util.ArrayList<E>
                    - 可调整大小的数组实现
                    - 允许null元素
                    - 非线程安全
                    - 常用方法: add(), get(), remove(), size(), clear()
                    - 时间复杂度: get O(1), add O(1)摊销, remove O(n)
                    """;
            case "hashmap" -> """
                    java.util.HashMap<K,V>
                    - 基于哈希表的Map实现
                    - 允许null键和null值
                    - 非线程安全，使用ConcurrentHashMap替代
                    - 常用方法: put(), get(), remove(), containsKey()
                    - 时间复杂度: 平均O(1)
                    """;
            case "stream" -> """
                    java.util.stream.Stream<T>
                    - Java 8引入的函数式编程接口
                    - 支持顺序和并行操作
                    - 常用方法: filter(), map(), collect(), reduce()
                    - 注意: Stream只能消费一次
                    """;
            default -> String.format("找到类 %s 的基本信息，建议查阅官方文档获取更多详情", className);
        };
    }

    @Tool("获取Spring Boot配置属性的说明和使用示例")
    public String getSpringBootProperty(String propertyName) {
        log.info("Getting Spring Boot property info: {}", propertyName);
        
        return switch (propertyName.toLowerCase()) {
            case "server.port" -> """
                    server.port=8080
                    说明: 配置Spring Boot应用的HTTP端口
                    默认值: 8080
                    示例: server.port=9090
                    """;
            case "spring.datasource.url" -> """
                    spring.datasource.url=jdbc:mysql://localhost:3306/mydb
                    说明: 配置数据库连接URL
                    支持: MySQL, PostgreSQL, Oracle, H2等
                    示例: jdbc:postgresql://localhost:5432/mydb
                    """;
            case "logging.level" -> """
                    logging.level.root=INFO
                    logging.level.com.example=DEBUG
                    说明: 配置日志级别
                    可选值: TRACE, DEBUG, INFO, WARN, ERROR
                    """;
            default -> String.format("属性 %s 请参考Spring Boot官方文档", propertyName);
        };
    }

    @Tool("分析Java代码并提供改进建议")
    public String analyzeCode(String code) {
        log.info("Analyzing code snippet");
        
        StringBuilder suggestions = new StringBuilder();
        suggestions.append("代码分析结果：\n");
        
        if (code.contains("System.out.println")) {
            suggestions.append("- 建议使用日志框架(如SLF4J)替代System.out.println\n");
        }
        if (code.contains("new ArrayList()") && !code.contains("<")) {
            suggestions.append("- 建议使用泛型避免类型安全问题\n");
        }
        if (code.contains("catch (Exception e)")) {
            suggestions.append("- 建议捕获更具体的异常类型\n");
        }
        if (code.contains("== null")) {
            suggestions.append("- 建议使用Optional或Objects.isNull()处理空值\n");
        }
        if (!code.contains("@Override") && code.contains("public ") && code.contains("()")) {
            suggestions.append("- 如果是重写方法，建议添加@Override注解\n");
        }
        
        if (suggestions.toString().equals("代码分析结果：\n")) {
            suggestions.append("- 代码看起来不错！\n");
        }
        
        return suggestions.toString();
    }

    @Tool("生成Maven依赖配置")
    public String generateMavenDependency(String library) {
        log.info("Generating Maven dependency for: {}", library);
        
        return switch (library.toLowerCase()) {
            case "lombok" -> """
                    <dependency>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>1.18.30</version>
                        <scope>provided</scope>
                    </dependency>
                    """;
            case "spring-boot-starter-web" -> """
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                    </dependency>
                    """;
            case "mysql" -> """
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.33</version>
                        <scope>runtime</scope>
                    </dependency>
                    """;
            case "junit" -> """
                    <dependency>
                        <groupId>org.junit.jupiter</groupId>
                        <artifactId>junit-jupiter</artifactId>
                        <version>5.10.1</version>
                        <scope>test</scope>
                    </dependency>
                    """;
            default -> String.format("请访问 https://mvnrepository.com 搜索 %s 获取最新依赖配置", library);
        };
    }

    @Tool("执行简单的数学计算")
    public String calculate(String expression) {
        log.info("Calculating: {}", expression);
        try {
            String sanitized = expression.replaceAll("[^0-9+\\-*/().\\s]", "");
            double result = evaluateExpression(sanitized);
            return String.format("计算结果: %s = %.2f", expression, result);
        } catch (Exception e) {
            return "计算错误: " + e.getMessage();
        }
    }

    private double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s", "");
        return parseAddSub(expression, new int[]{0});
    }

    private double parseAddSub(String expr, int[] pos) {
        double result = parseMulDiv(expr, pos);
        while (pos[0] < expr.length()) {
            char op = expr.charAt(pos[0]);
            if (op != '+' && op != '-') break;
            pos[0]++;
            double right = parseMulDiv(expr, pos);
            result = op == '+' ? result + right : result - right;
        }
        return result;
    }

    private double parseMulDiv(String expr, int[] pos) {
        double result = parseNumber(expr, pos);
        while (pos[0] < expr.length()) {
            char op = expr.charAt(pos[0]);
            if (op != '*' && op != '/') break;
            pos[0]++;
            double right = parseNumber(expr, pos);
            result = op == '*' ? result * right : result / right;
        }
        return result;
    }

    private double parseNumber(String expr, int[] pos) {
        if (pos[0] < expr.length() && expr.charAt(pos[0]) == '(') {
            pos[0]++;
            double result = parseAddSub(expr, pos);
            pos[0]++;
            return result;
        }
        int start = pos[0];
        while (pos[0] < expr.length() && 
               (Character.isDigit(expr.charAt(pos[0])) || expr.charAt(pos[0]) == '.')) {
            pos[0]++;
        }
        return Double.parseDouble(expr.substring(start, pos[0]));
    }
}
