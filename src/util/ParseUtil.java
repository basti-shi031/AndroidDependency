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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ParseUtil {

    static String path1;

    public static void parse(String gradleString, String path) {
        path1 = path;
        SourceUnit unit = SourceUnit.create("gradle", gradleString);
        unit.parse();
        unit.completePhase();
        unit.convert();
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
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
            Iterator it = properties.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                getDependencyValue().put(key.toString(), value.toString());
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
        int index = 0;
        for (String dependency : dependencies) {
            //     L.l(dependency);

            if (dependency.startsWith("project.")) {
                //特殊处理 [Rajawali-Rajawali]
                dependencies.set(index, value.get(dependency.split("\\.")[1].trim()));
            } else if (dependency.startsWith("featureDependencies.") || dependency.startsWith("aptDependencies.")) {
                //[Piasy-AndroidTDDBootStrap] featureDependencies.cardViewV7
                String valueDependency = value.get(dependency.split("\\.")[1].trim());//valueDependency = cardViewV7 stetho
                if (valueDependency != null) {
                    dependencies.set(index, valueDependency);
                }
            }else  if (dependency.contains("$")) {
                String fakeVersion = dependency.split("\\$")[1].trim();
                dependency = dependency.replace("$" + fakeVersion, value.get(fakeVersion));
                dependencies.set(index, dependency);
            }else if (dependency.startsWith("[")&&dependency.endsWith("]")){
                //[group:io.dropwizard, name:dropwizard-jdbi, version:1.0.0-rc2]
                dependency = dependency.substring(1,dependency.length()-1);

            }
            index++;
        }


        clearDependencyValue();

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

    private static void clearDependencyValue() {
        parseBuildGradle.clearDependencyValue();
    }

    static class parseBuildGradle extends CodeVisitorSupport {
        static Set<String> methodSet = new HashSet<>();
        static List<String> dependencies = new ArrayList<>();//存放依赖
        static Map<String, String> dependencyValue = new HashMap();//存放键值对，用于替换

        @Override
        public void visitMethodCallExpression(MethodCallExpression call) {
            //My code

            String method = call.getMethodAsString();
            if (method != null) {
                if (isDependencyKey(call.getMethodAsString())) {
                    Expression expression = call.getArguments();
                    String dependency = expression.getText();
                    dependency = simpleResolve(dependency);
                    if (dependency == null || dependency.length() == 0) {
                        return;
                    }
                    dependencies.add(dependency);
                } else if (call.getMethodAsString().equals("set")) {
                    String text = call.getArguments().getText();
                    //(depAppCompat, com.android.support:appcompat-v7:23.2.0)
                    //去除括号
                    text = removeBracket(text);
                    dependencyValue.put(text.split(",")[0].trim(), text.split(",")[1].trim());
                }
                methodSet.add(call.getMethodAsString());
                super.visitMethodCallExpression(call);
            }
        }

        @Override
        public void visitArgumentlistExpression(ArgumentListExpression ale) {
            int size = ale.getExpressions().size();
            // L.l(String.valueOf(size));
            if (size >= 1) {
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
                                    leftValue = ((ConstantExpression) mapEntryExpression.getKeyExpression()).getValue().toString();
                                    rightValue = (mapEntryExpression.getValueExpression()).getText();
                                    //   L.l(leftValue, rightValue);
                                    dependencyValue.put(leftValue, rightValue);
                                }
                            }
                        }
                    }
                } else if (ale.getExpression(0) instanceof PropertyExpression) {

                }
                super.visitArgumentlistExpression(ale);
            }
        }

        /**
         * 去除括号
         *
         * @param text
         * @return
         */
        private String removeBracket(String text) {
            //去除括号和头尾空格
            return text.trim().substring(1, text.length() - 1);
        }

        /**
         * 对dependency进行简单的处理
         *
         * @param dependency
         * @return
         */
        private String simpleResolve(String dependency) {
            String result = removeBracket(dependency);
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
                result = result.split(",")[0];
            }
            return result;
        }

        private boolean isDependencyKey(String methodAsString) {
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
}

