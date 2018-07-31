package util;

import config.Config;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.SourceUnit;

import java.io.*;
import java.util.*;

public class ParseUtil {

    static String path1;

    public static void parse(String gradleString, String path) {
        path1 = path;
        SourceUnit unit = SourceUnit.create("gradle", gradleString);
        unit.parse();
        unit.completePhase();
        try {
            unit.convert();
        } catch (Exception e) {
            return;
        }

        visitScriptCode(unit, new parseBuildGradle());
    }

    private static void visitScriptCode(SourceUnit source, GroovyCodeVisitor transformer) {
        source.getAST().getStatementBlock().visit(transformer);
        for (Object method : source.getAST().getMethods()) {
            MethodNode methodNode = (MethodNode) method;
            // System.out.println(methodNode.getName());
            //System.out.println(methodNode.getText());
            // System.out.println(methodNode.toString());
            methodNode.getCode().visit(transformer);
        }
    }

    public static void parseProperties(String path) {
        path1 = path;

        L.l("path1", path1);
        try {
            String temp[] = path.split("\\.properties");
            int size = temp.length;
            copy(path, temp[0] + "_2" + ".properties");
            path1 = temp[0] + "_2" + ".properties";
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(path1));
                Iterator it = properties.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    getDependencyValue().put(key.toString(), value.toString());
                }
            } catch (IOException e) {
                L.l(path);
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替代依赖中的值
     * compile project.depWearableSupport
     * project.ext.set('depWearableSupport', 'com.google.android.support:wearable:1.3.0')
     */
    public static void replaceDependencyValue() {
        List<String> dependencies = getDependencies();
        Map<String, String> value = getDependencyValue();
        int index = -1;
        for (String dependency : dependencies) {
            if (dependency.contains("org.springframework.cloud:spring-cloud-starter-eureka-server")) {
                int a = 1;
            }
            //     L.l(dependency);
            index++;

            if (dependency.contains(", ") && dependency.split(", ").length == 2) {
                dependency = dependency.split(", ")[0].trim();
                dependencies.set(index, dependency);
            }

            if (dependency == null) {
                continue;
            }
            // L.l("////", dependency);
            if (dependency.contains("org.springframework:spring-test")) {
                int a = 1;
            }
//            compile "org.springframework:spring-core:${springVersion}", optional
            if (dependency.contains("\",")) {
                dependency = dependency.split(",")[0].trim();
                dependencies.set(index, dependency);
            }
            if (dependency.contains("$antlr4Version")) {

            }
            if (dependency.contains(": + ")) {
                //((com.fasterxml.jackson.core:jackson-databind: + jacksonVersion))
                dependency = dependency.replaceAll(" + ", "&");
            }

            if (dependency.contains("()")) {
                //files(org.gradle.internal.jvm.Jvm.current().getToolsJar())
                dependencies.set(index, "");
            }
            if (dependency.startsWith("project.")) {
                //特殊处理 [Rajawali-Rajawali]
                dependency = dependency.split("\\.")[1].trim();
                if (value.get(dependency) != null) {
                    dependency = value.get(dependency);
                    dependencies.set(index, dependency);
                }

            }
            if (dependency.startsWith("featureDependencies.") || dependency.startsWith("aptDependencies.")
                    || dependency.startsWith("libs.") || dependency.startsWith("deps.")
                    || dependency.startsWith("libraries.") || dependency.startsWith("supportLib.")
                    || dependency.startsWith("archLib.") || dependency.startsWith("widgets.")
                    || dependency.startsWith("externalDependency.") || dependency.startsWith("lib.")
                    || dependency.startsWith("libs.") || dependency.startsWith("depends.")) {
                //[Piasy-AndroidTDDBootStrap] featureDependencies.cardViewV7
                dependency = value.get(dependency.split("\\.")[1].trim());//valueDependency = cardViewV7 stetho
                if (dependency != null) {
                    dependencies.set(index, dependency);
                }
            }
            if (dependency == null) {
                continue;
            }
            if (!dependency.startsWith("[")) {
                if (dependency.split(":").length >= 3) {
                    //com.fasterxml.jackson.core:jackson-databind:jacksonVersion
                    String[] temps = dependency.split(":");
                    int tempIndex = 0;
                    for (String temp : temps) {
                        boolean shouldReplace = false;
                        String fakeVersionKey = dependency.split(":")[tempIndex].trim();
                        if (fakeVersionKey.startsWith("$")) {
                            shouldReplace = true;
                            fakeVersionKey = fakeVersionKey.substring(1);
                        } else if (fakeVersionKey.contains("$") || tempIndex == 2) {
                            shouldReplace = true;
                            fakeVersionKey = fakeVersionKey.substring(fakeVersionKey.indexOf("$") + 1);
                        }
                        if (shouldReplace && value.get(fakeVersionKey) != null) {
                            String fakeVersion = value.get(fakeVersionKey);
                            dependency = dependency.replace("$" + fakeVersionKey, fakeVersion);
                            dependency = dependency.replace(fakeVersionKey, fakeVersion);
                            dependencies.set(index, dependency);
                        }
                        tempIndex++;
                    }
                }
            }
            if (dependency.startsWith("group:") || dependency.startsWith("name:")) {
                dependency = "[" + dependency + "]";
            }
            if (dependency != null) {
                if (dependency.startsWith("[") && dependency.endsWith("]")) {
                    L.l(dependency);
                    //[group:io.dropwizard, name:dropwizard-jdbi, version:1.0.0-rc2]
                    dependency = dependency.substring(1, dependency.length() - 1);
                    String splitGroups[] = dependency.split(",");
                    Map<String, String> maps = new HashMap<>();
                    for (String splitGroup : splitGroups) {
                        String[] keyValue = splitGroup.trim().split(":");
                        if (keyValue.length <= 1) {
                            continue;
                        }
                        maps.put(keyValue[0], keyValue[1]);
                    }
                    //拼接成com.google.android.support:wearable:1.3.0
                    String groupName = maps.get("group");
                    String moduleName = maps.get("name");
                    String version = maps.get("version");
                    if (value.get(version) != null) {
                        version = value.get(version);
                    }
                    dependency = "";
                    if (groupName != null) {
                        dependency = groupName + ":";
                    }
                    if (moduleName != null) {
                        dependency = dependency + moduleName + ":";
                    }
                    if (version != null) {
                        dependency = dependency + version;
                    }
                    if (dependency.endsWith(":")) {
                        dependency = dependency.substring(0, dependency.length() - 1);
                    }
                    dependencies.set(index, dependency);
                }
                if (dependency.contains("$")) {
                    //$version
                    //$project.version
                    String[] tempFakeVersion = dependency.split("\\$");
                    if (tempFakeVersion.length <= 1) {
                        continue;
                    }
                    String fakeVersion = dependency.split("\\$")[1].trim();
                    String tempVersion = fakeVersion;
                    if (tempVersion.contains(".")) {
                        String[] temp = fakeVersion.split("\\.");
                        int size = temp.length;
                        tempVersion = temp[size - 1];
                    }
                    if (value.get(tempVersion) != null) {
                        dependency = dependency.replace("$" + fakeVersion, value.get(tempVersion));
                        dependencies.set(index, dependency);
                    }
                }
                if (dependency.startsWith("rootProject") || dependency.startsWith("rootproject")) {
                    String[] tempDependencies = dependency.split("\\.");
                    if (tempDependencies.length >= 1) {
                        dependency = tempDependencies[tempDependencies.length - 1];
                        dependency = value.get(dependency);
                        if (dependency != null) {
                            dependencies.set(index, dependency);
                        }
                    }
                }
            }
            if (dependency != null) {
                dependency = simpleResolve(dependency);
                if (dependency != null) {
                    dependencies.set(index, dependency);
                }
            }


            if (dependency != null && getVersion(dependency) != null &&
                    (getVersion(dependency).startsWith("project.") ||
                            getVersion(dependency).startsWith("parent.") ||
                            getVersion(dependency).startsWith("identityParent.") ||
                            getVersion(dependency).startsWith("versions."))) {
                //org.springframework.security:spring-security-config:project.spring-security.version
                String[] versionTemp = getVersion(dependency).split("\\.");
                String fakeVersion = versionTemp[versionTemp.length - 1];
                if (value.get(fakeVersion.trim()) != null) {
                    dependency = dependency.replace(getVersion(dependency), value.get(fakeVersion.trim()));
                    dependencies.set(index, dependency);
                }
            }

            if (dependency == null) {
                continue;
            }


            if (value.get(dependency) != null) {
                dependency = value.get(dependency);
                dependencies.set(index, dependency);
            }

            if (dependency.startsWith("(")) {
                dependency = dependency.substring(1);
                dependencies.set(index, dependency);
            }
        }

    }

    /**
     * 分割出版本号
     *
     * @param dependency
     * @return
     */
    private static String getVersion(String dependency) {
        String[] temp = dependency.split(":");
        if (temp.length >= 3) {
            return temp[2];
        }
        return null;
    }

    public static Set<String> getMethodSet() {
        return parseBuildGradle.getMethodSet();
    }

    public static List<String> getDependencies() {
        return parseBuildGradle.getDependencies();
    }

    public static void clearDependencies() {
        parseBuildGradle.clearDependencies();
    }

    private static Map<String, String> getDependencyValue() {
        return parseBuildGradle.getDependencyValue();
    }

    public static void clearDependencyValue() {
        parseBuildGradle.clearDependencyValue();
    }

    static class parseBuildGradle extends CodeVisitorSupport {
        static Set<String> methodSet = new HashSet<>();
        static List<String> dependencies = new ArrayList<>();//存放依赖
        static Map<String, String> dependencyValue = new HashMap();//存放键值对，用于替换


        @Override
        public void visitMapEntryExpression(MapEntryExpression expression) {
            super.visitMapEntryExpression(expression);
            Expression keyExpression = expression.getKeyExpression();
            Expression valueExpression = expression.getValueExpression();
            String leftValue = "";
            String rightValue = "";
            if (keyExpression instanceof ConstantExpression) {
                leftValue = keyExpression.getText();
                if (leftValue.contains("antlr")) {
                    int a = 1;
                }
            }
            if (valueExpression instanceof ConstantExpression) {
                rightValue = valueExpression.getText();
            } else if (valueExpression instanceof GStringExpression) {
                rightValue = valueExpression.getText();
            }
            if (leftValue.length() != 0 && rightValue.length() != 0) {
                dependencyValue.put(leftValue, rightValue);
            }
        }

        @Override
        public void visitMethodCallExpression(MethodCallExpression call) {
            //My code

            String method = call.getMethodAsString();
           /* if (method != null) {
                if (isDependencyKey(call.getMethodAsString())) {

                    Expression expression = call.getArguments();
                    String dependency = expression.getText();
                    dependency = simpleResolve(dependency);
                    if (dependency == null || dependency.length() == 0) {
                        return;
                    }
                    dependencies.add(dependency);
                } else */
            if (call != null && call.getMethodAsString() != null) {
                if (call.getMethodAsString().equals("set")) {
                    String text = call.getArguments().getText();
                    //(depAppCompat, com.android.support:appcompat-v7:23.2.0)
                    //去除括号
                    L.l(text);
                    text = removeBracket(text);
                    if (text.split(",").length >= 2) {
                        dependencyValue.put(text.split(",")[0].trim(), text.split(",")[1].trim());
                    }

                }
                methodSet.add(call.getMethodAsString());
            }
            super.visitMethodCallExpression(call);
            // }
        }

        @Override
        public void visitBlockStatement(BlockStatement block) {
            List<Statement> statements = block.getStatements();
            for (Statement statement : statements) {
                if (statement instanceof ExpressionStatement) {
                    Expression expression = ((ExpressionStatement) statement).getExpression();
                    if (expression instanceof MethodCallExpression) {
                        if (isDependencyKey(((MethodCallExpression) expression).getMethodAsString())) {
//                            L.l("dependency:",((MethodCallExpression) expression).getArguments().getText());
                            Expression arguements = ((MethodCallExpression) expression).getArguments();
                            if (arguements instanceof ArgumentListExpression) {
                                List<Expression> expressions = ((ArgumentListExpression) arguements).getExpressions();
                                for (Expression e : expressions) {
                                    if (e instanceof PropertyExpression) {
                                        L.l("property", ((PropertyExpression) e).getPropertyAsString());
                                        dependencies.add(((PropertyExpression) e).getPropertyAsString());
                                    } else {
                                        dependencies.add(e.getText());
                                    }
                                }
                            } else if (arguements instanceof TupleExpression) {
                                dependencies.add(((MethodCallExpression) expression).getArguments().getText());
                            }

                        }
                    }
                }
            }
            super.visitBlockStatement(block);
        }

        @Override
        public void visitBinaryExpression(BinaryExpression expression) {
            Expression leftExpression = expression.getLeftExpression();
            Expression rightExpression = expression.getRightExpression();
            String leftValue = "";
            String rightValue = "";
            if (leftExpression instanceof PropertyExpression) {
                leftValue = ((PropertyExpression) leftExpression).getProperty().getText();
            } else if (leftExpression instanceof VariableExpression) {
                leftValue = leftExpression.getText();
            }
            if (leftValue.contains("sparkVersion")) {
                int a = 1;
            }
            if (rightExpression instanceof ConstantExpression) {
                Object value = ((ConstantExpression) rightExpression).getValue();
                if (value != null) {
                    if (value.toString().contains("4.1.22.Final")) {
                        int a = 1;
                    }
                    rightValue = value.toString();
                }
            } else if (rightExpression instanceof GStringExpression) {
                rightValue = rightExpression.getText();
            } else if (rightExpression instanceof VariableExpression) {
                rightValue = rightExpression.getText();
            }
            if (leftValue.length() != 0 && rightValue.length() != 0) {
                dependencyValue.put(leftValue, rightValue);
            }
            super.visitBinaryExpression(expression);
        }

        @Override
        public void visitArgumentlistExpression(ArgumentListExpression ale) {
            int size = ale.getExpressions().size();
            L.l("====", path1);
            // L.l(String.valueOf(size));
            if (size >= 2) {
                if (ale.getExpressions() instanceof LinkedList) {
                    Expression key = ale.getExpression(0);
                    Expression value = ale.getExpression(1);
                    String leftValue = "";
                    String rightValue = "";
                    if (key instanceof ConstantExpression) {
                        leftValue = key.getText();
                    }
                    if (value instanceof ConstantExpression) {
                        rightValue = value.getText();
                    }
                    if (leftValue.length() != 0 && rightValue.length() != 0) {
                        dependencyValue.put(leftValue, rightValue);
                    }
                }
            } else if (size >= 1) {
                if (ale.getExpression(0) instanceof ClosureExpression) {
                    List<Statement> statements = ((BlockStatement) ((ClosureExpression) ale.getExpression(0)).getCode()).getStatements();
                    for (Statement statement : statements) {
                        if (statement instanceof ExpressionStatement && ((ExpressionStatement) statement).getExpression() instanceof BinaryExpression) {
                            Expression left = ((BinaryExpression) ((ExpressionStatement) statement).getExpression()).getLeftExpression();
                            Expression right = ((BinaryExpression) ((ExpressionStatement) statement).getExpression()).getRightExpression();
                            String leftValue = left.getText();
                            String rightValue = "";
                            if (right instanceof ConstantExpression) {
                                if (((ConstantExpression) right).getValue() != null) {
                                    rightValue = ((ConstantExpression) right).getValue().toString();
                                    // L.l(leftValue, rightValue);
                                    dependencyValue.put(leftValue, rightValue);
                                }
                            } else if (right instanceof MapExpression) {
                                List<MapEntryExpression> expressions = ((MapExpression) right).getMapEntryExpressions();
                                for (MapEntryExpression mapEntryExpression : expressions) {
                                    Expression tempExpression = mapEntryExpression.getKeyExpression();
                                    if (tempExpression instanceof ConstantExpression) {
                                        leftValue = ((ConstantExpression) tempExpression).getValue().toString();
                                    } else if (tempExpression instanceof GStringExpression) {
                                        leftValue = tempExpression.getText();
                                    }

                                    rightValue = (mapEntryExpression.getValueExpression()).getText();
                                    //   L.l(leftValue, rightValue);
                                    dependencyValue.put(leftValue, rightValue);
                                }
                            }
                        }
                    }
                } else if (ale.getExpression(0) instanceof PropertyExpression) {

                }
            }
            super.visitArgumentlistExpression(ale);
        }


        private boolean isDependencyKey(String methodAsString) {
            if (methodAsString == null) {
                return false;
            }
            return Config.DEPENDENCY_TAG.contains(methodAsString.toLowerCase());
        }

        public static List<String> getDependencies() {
            return dependencies;
        }

        public static void clearDependencies() {
            dependencies.clear();
        }

        public static Set<String> getMethodSet() {
            return methodSet;
        }

        public static void clearDependencyValue() {
            dependencyValue.clear();
        }

        public static Map<String, String> getDependencyValue() {
            return dependencyValue;
        }
    }

    /**
     * 对dependency进行简单的处理
     *
     * @param dependency
     * @return
     */
    private static String simpleResolve(String dependency) {
        String result = removeBracket(dependency);//移除括号

        result = removeAdd(result);//移除加号
        //this.files(libs/butterknife-7.0.1.jar)
        //this.project([path::swipemenu])
        if (result.startsWith("this.")) {
            result = result.substring("this.".length());
        }
        //  compile gradleApi()此类排除
        if (result.matches("^[a-zA-z0-9]+\\(\\)$")) {
            return null;
        }

        //compile fileTree([dir:libs, include:[*.jar]]) 此类排除
        if (result.startsWith("fileTree")) {
            return null;
        }

        //com.android.support.test.espresso:espresso-core:2.2.2, { -> ... }
        if (result.endsWith("}")) {
            //[group:com.facebook.presto, name:presto-client, version:prestoVersion], { -> ... }
            result = result.split(", \\{ -> \\.\\.\\. \\}")[0];
        }
        return result;
    }

    /**
     * 移除加号
     *
     * @param result
     * @return
     */
    private static String removeAdd(String result) {
        String[] temp = result.split("\\+");
        if (temp.length <= 1) {
            return result.trim();
        } else {
            result = "";
            for (String s : temp) {
                result += s.trim();
            }
            return result.trim();
        }
    }

    /**
     * 去除括号
     *
     * @param text
     * @return
     */
    private static String removeBracket(String text) {
        //去除括号和头尾空格
        while (text.trim().startsWith("(") && text.trim().endsWith(")")) {
            text = text.trim().substring(1, text.length() - 1);
        }
        return text;
    }

    private static void copy(String of, String wf) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(of));
        BufferedWriter wr = new BufferedWriter(new FileWriter(wf));
        String tmp = null;
        StringBuffer sb = new StringBuffer();
        tmp = br.readLine();
        String cc = "\\\\";
        String dd = "/";
        while (tmp != null) {
            if (tmp.indexOf("\\") != -1)
                tmp = tmp.replaceAll(cc, dd);
            wr.write(tmp);
            wr.newLine();
            tmp = br.readLine();
        }
        wr.close();
        br.close();
    }

}

