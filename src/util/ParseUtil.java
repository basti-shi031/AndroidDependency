package util;

import config.Config;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.control.SourceUnit;

import java.util.*;

public class ParseUtil {

    public static void parse(String gradleString) {
        SourceUnit unit = SourceUnit.create("gradle", gradleString);
        unit.parse();
        unit.completePhase();
        unit.convert();
        visitScriptCode(unit, new parseBuildGradle());
        replaceDependencyValue();
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

    /**
     * 替代依赖中的值
     * compile project.depWearableSupport
     * project.ext.set('depWearableSupport', 'com.google.android.support:wearable:1.3.0')
     */
    private static void replaceDependencyValue() {
        List<String> dependencies = getDependencies();
        Map<String, String> value = getDependencyValue();

        int index = 0;
        //特殊处理 [Rajawali-Rajawali]
        for (String dependency : dependencies) {
            if (dependency.startsWith("project.")) {
                dependencies.set(index, value.get(dependency.split("\\.")[1].trim()));
            }
            index++;
        }

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
            super.visitMethodCallExpression(call);
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

