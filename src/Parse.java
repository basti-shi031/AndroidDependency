import config.Config;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.SourceUnit;
import util.L;

import java.util.List;

public class Parse {
    public static void main(String args[]) {
        String source = "COMPILE_SDK_VERSION=23\n" +
                "BUILD_TOOLS_VERSION=22.0.1\n" +
                "MIN_SDK_VERSION=11\n" +
                "TARGET_SDK_VERSION=23\n" +
                "VERSION_CODE=11\n" +
                "VERSION_NAME=1.6.0\n" +
                "SUPPORT_TEST_VERSION=0.4.1\n" +
                "HAMCREST_VERSION=1.3\n" +
                "ESPRESSO_VERSION=2.2.1\n" +
                "KOTLIN_VERSION=1.0.0\n" +
                "\n" +
                "SUPPORT_APP_COMPAT_VERSION=23.2.1\n" +
                "\n" +
                "GROUP=com.github.aakira\n" +
                "ARTIFACT_ID=expandable-layout\n" +
                "\n" +
                "POM_DESCRIPTION=An android library that brings the expandable layout with various animation.\\\n" +
                "   You can include optional contents and use everywhere.\n" +
                "POM_URL=https://github.com/aakira/ExpandableLayout\n" +
                "POM_SCM_URL=git@github.com:aakira/ExpandableLayout.git\n" +
                "POM_SCM_CONNECTION=scm:git@github.com:aakira/ExpandableLayout.git\n" +
                "POM_SCM_DEV_CONNECTION=scm:git@github.com:aakira/ExpandableLayout.git\n" +
                "POM_LICENSE_NAME=The Apache Software License, Version 2.0\n" +
                "POM_LICENSE_URL=http://www.apache.org/licenses/LICENSE-2.0.txt\n" +
                "POM_LICENSE_DIST=repo\n" +
                "POM_DEVELOPER_ID=aakira\n" +
                "POM_DEVELOPER_NAME=aakira\n" +
                "POM_DEVELOPER_EMAIL=developer.a.akira@gmail.com\n" +
                "ISSUE_URL=https://github.com/aakira/ExpandableLayout/issues";

        String source1 = "import com.app.plugin.AspectjPlugin\n" +
                "import com.app.plugin.JavassistPlugin\n" +
                "\n" +
                "apply plugin: 'com.android.application'\n" +
                "apply plugin: 'realm-android'\n" +
                "apply plugin: 'me.tatarka.retrolambda'\n" +
                "\n" +
                "android {\n" +
                "    compileSdkVersion 25\n" +
                "    buildToolsVersion '25.0.2'\n" +
                "\n" +
                "    defaultConfig {\n" +
                "        applicationId \"cn.com.app\"\n" +
                "        minSdkVersion 21\n" +
                "        targetSdkVersion 25\n" +
                "        versionCode 1\n" +
                "        versionName \"1.0\"\n" +
                "        renderscriptTargetApi 20\n" +
                "        renderscriptSupportModeEnabled true\n" +
                "        vectorDrawables.useSupportLibrary = true\n" +
                "    }\n" +
                "    buildTypes {\n" +
                "        release {\n" +
                "            minifyEnabled false\n" +
                "            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    compileOptions {\n" +
                "        sourceCompatibility 1.8\n" +
                "        targetCompatibility 1.8\n" +
                "    }\n" +
                "\n" +
                "    lintOptions {\n" +
                "        abortOnError false\n" +
                "    }\n" +
                "    dexOptions {\n" +
                "        preDexLibraries = false\n" +
                "    }\n" +
                "    dataBinding {\n" +
                "        enabled = true\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "dependencies {\n" +
                "    compile 'com.github.todou:appbarspring:1.0.3'\n" +
                "    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'\n" +
                "\n" +
                "    compile \"io.reactivex.rxjava2:rxjava:$rootProject.ext.rxjava2Version\"\n" +
                "\n" +
                "\n" +
                "    compile \"com.squareup.retrofit2:retrofit:$rootProject.ext.retrofit2Version\"\n" +
                "    compile \"com.squareup.retrofit2:adapter-rxjava2:$rootProject.ext.retrofit2Version\"\n" +
                "    compile \"com.squareup.retrofit2:converter-gson:$rootProject.ext.retrofit2Version\"\n" +
                "    compile \"com.google.code.gson:gson: $rootProject.ext.gsonVersion\"\n" +
                "    compile 'com.squareup.okhttp3:logging-interceptor:3.6.0'\n" +
                "    compile 'com.github.bumptech.glide:glide:3.7.0'\n" +
                "    compile 'org.aspectj:aspectjrt:1.8.9'\n" +
                "\n" +
                "    compile 'com.android.support:design:25.3.1'\n" +
                "    compile 'com.android.support:cardview-v7:25.3.1'\n" +
                "    compile 'com.android.support:recyclerview-v7:25.3.1'\n" +
                "    compile 'com.android.support:support-vector-drawable:25.3.1'\n" +
                "    compile 'com.android.support:appcompat-v7:25.3.1'\n" +
                "    compile 'com.android.support:percent:25.3.1'\n" +
                "\n" +
                "    compile project(':lib')\n" +
                "    annotationProcessor project(':apt')\n" +
                "}\n" +
                "\n" +
                "apply plugin: AspectjPlugin\n" +
                "apply plugin: JavassistPlugin";
        SourceUnit unit = SourceUnit.create("gradle", source);
        unit.parse();
        unit.completePhase();
        unit.convert();
        visitScriptCode(unit, new parseBuildGradle());
    }


    private static void visitScriptCode(SourceUnit source, GroovyCodeVisitor transformer) {
        source.getAST().getStatementBlock().visit(transformer);

        for (Object method : source.getAST().getMethods()) {
            MethodNode methodNode = (MethodNode) method;
            L.l("123");
            // System.out.println(methodNode.getName());
            //System.out.println(methodNode.getText());
            // System.out.println(methodNode.toString());
            methodNode.getCode().visit(transformer);
        }
    }

    static class parseBuildGradle extends CodeVisitorSupport {

        @Override
        public void visitMethodCallExpression(MethodCallExpression call) {
            ConstantExpression method = (ConstantExpression) call.getMethod();
            L.l(method.getValue().toString());
            L.l(call.getMethodAsString());
            L.l(call.getArguments().getText());
            super.visitMethodCallExpression(call);
       /*     if (isDependencyKey(call.getMethodAsString())) {
                L.l(call.getArguments().getText());
            }*/

        }

        @Override
        public void visitArgumentlistExpression(ArgumentListExpression ale) {
            L.l(ale.getText());
            int size = ale.getExpressions().size();
            L.l(String.valueOf(size));
            if (size >= 1) {
                List<Statement> statements = ((BlockStatement) ((ClosureExpression) ale.getExpression(0)).getCode()).getStatements();
                for (Statement statement : statements) {
                    Expression left = ((BinaryExpression) ((ExpressionStatement) statement).getExpression()).getLeftExpression();
                    Expression right = ((BinaryExpression) ((ExpressionStatement) statement).getExpression()).getRightExpression();
                    String leftValue = left.getText();
                    String rightValue = "";
                    if (right instanceof ConstantExpression) {
                        rightValue = ((ConstantExpression) right).getValue().toString();
                        L.l(leftValue, rightValue);

                    } else if (right instanceof MapExpression) {
                        List<MapEntryExpression> expressions = ((MapExpression) right).getMapEntryExpressions();
                        for (MapEntryExpression mapEntryExpression : expressions) {
                            leftValue = ((ConstantExpression)mapEntryExpression.getKeyExpression()).getValue().toString();
                            rightValue = (mapEntryExpression.getValueExpression()).getText();
                            L.l(leftValue, rightValue);
                        }
                    }
                }
            }
            super.visitArgumentlistExpression(ale);
        }

        private boolean isDependencyKey(String methodAsString) {
            return Config.DEPENDENCY_TAG.contains(methodAsString.toLowerCase());
        }
    }


}


