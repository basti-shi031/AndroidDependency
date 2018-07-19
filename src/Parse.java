import config.Config;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.classgen.BytecodeExpression;
import org.codehaus.groovy.control.SourceUnit;
import util.L;

public class Parse {
    public static void main(String args[]) {
        String source ="allprojects {\n" +
                "    // Top-level build file where you can add configuration options common to all sub-projects/modules.\n" +
                "    ext {\n" +
                "        // Author\n" +
                "        developerId = \"davideas\"\n" +
                "        developerName = \"Davide Steduto\"\n" +
                "        developerEmail = \"dave.dna@gmail.com\"\n" +
                "\n" +
                "        // Library Repository\n" +
                "        libraryName = \"FlexibleAdapter\"\n" +
                "        bintrayRepo = \"maven\"\n" +
                "        packageExt = \"aar\"\n" +
                "        siteUrl = \"https://github.com/davideas/FlexibleAdapter\"\n" +
                "        gitUrl = \"https://github.com/davideas/FlexibleAdapter.git\"\n" +
                "\n" +
                "        // SDK and Build tools version\n" +
                "        minSdk = 14\n" +
                "        targetSdk = 27\n" +
                "        buildTools = \"27.0.3\"\n" +
                "\n" +
                "        // Android Architecture Components\n" +
                "        archLifecycleVer = \"1.1.1\"\n" +
                "        archExtensionsVer = \"1.1.1\"\n" +
                "        roomPersistenceVer = \"1.1.0-beta2\"\n" +
                "        archLib = [\n" +
                "                lifecycle      : \"android.arch.lifecycle:runtime:${archLifecycleVer}\",\n" +
                "                extensions     : \"android.arch.lifecycle:extensions:${archExtensionsVer}\",\n" +
                "                compiler       : \"android.arch.lifecycle:compiler:${archExtensionsVer}\",\n" +
                "                commonJava8    : \"android.arch.lifecycle:common-java8:${archExtensionsVer}\",\n" +
                "                reactiveStreams: \"android.arch.lifecycle:reactivestreams:${archExtensionsVer}\",\n" +
                "                coreTesting    : \"android.arch.core:core-testing:${archExtensionsVer}\",\n" +
                "                room           : \"android.arch.persistence.room:runtime:${roomPersistenceVer}\",\n" +
                "                roomCompiler   : \"android.arch.persistence.room:compiler:${roomPersistenceVer}\",\n" +
                "                roomRxJava2    : \"android.arch.persistence.room:rxjava2:${roomPersistenceVer}\"\n" +
                "        ]\n" +
                "\n" +
                "        // Support Libraries dependencies\n" +
                "        supportVer = \"27.1.1\"\n" +
                "        constraintVer = \"1.1.0\"\n" +
                "        supportLib = [\n" +
                "                design          : \"com.android.support:design:${supportVer}\",\n" +
                "                recyclerView    : \"com.android.support:recyclerview-v7:${supportVer}\",\n" +
                "                cardView        : \"com.android.support:cardview-v7:${supportVer}\",\n" +
                "                appCompat       : \"com.android.support:appcompat-v7:${supportVer}\",\n" +
                "                customTabs      : \"com.android.support:customtabs:${supportVer}\",\n" +
                "                support_v4      : \"com.android.support:support-v4:${supportVer}\",\n" +
                "                support_v13     : \"com.android.support:support-v13:${supportVer}\",\n" +
                "                annotations     : \"com.android.support:support-annotations:${supportVer}\",\n" +
                "                vectorDrawable  : \"com.android.support:support-vector-drawable:${supportVer}\",\n" +
                "                preference_v7   : \"com.android.support:preference-v7:${supportVer}\",\n" +
                "                preference_v14  : \"com.android.support:preference-v14:${supportVer}\",\n" +
                "                constraintLayout: \"com.android.support.constraint:constraint-layout:${constraintVer}\",\n" +
                "        ]\n" +
                "\n" +
                "        // Widgets\n" +
                "        flipViewVer = \"1.1.3@aar\"\n" +
                "        iconicsVer = \"3.0.3@aar\"\n" +
                "        iconicsCmdVer = \"2.0.46.1@aar\"\n" +
                "        widgets = [\n" +
                "                flipView        : \"eu.davidea:flipview:${flipViewVer}\",\n" +
                "                iconicsCore     : \"com.mikepenz:iconics-core:${iconicsVer}\",\n" +
                "                iconicsViews    : \"com.mikepenz:iconics-views:${iconicsVer}\",\n" +
                "                iconicsCommunity: \"com.mikepenz:community-material-typeface:${iconicsCmdVer}\",\n" +
                "        ]\n" +
                "\n" +
                "        // 3rd Libraries\n" +
                "        butterKnifeVer = \"8.8.1\"\n" +
                "        timberVer = \"4.7.0\"\n" +
                "        glideVer = \"4.6.1\"\n" +
                "        javaxInjectVer = \"1\"\n" +
                "        junitPlatformVer = \"1.1.0\"\n" +
                "        junitJupiterVer = \"5.1.0\"\n" +
                "        libraries = [\n" +
                "                butterKnife        : \"com.jakewharton:butterknife:${butterKnifeVer}\",\n" +
                "                butterKnifeCompiler: \"com.jakewharton:butterknife-compiler:${butterKnifeVer}\",\n" +
                "                timber             : \"com.jakewharton.timber:timber:${timberVer}\",\n" +
                "                glide              : \"com.github.bumptech.glide:glide:${glideVer}\",\n" +
                "                glideCompiler      : \"com.github.bumptech.glide:compiler:${glideVer}\",\n" +
                "                javaxInject        : \"javax.inject:javax.inject:${javaxInjectVer}\",\n" +
                "        ]\n" +
                "\n" +
                "        // License stuff\n" +
                "        licenseName = \"The Apache Software License, Version 2.0\"\n" +
                "        licenseUrl = \"http://www.apache.org/licenses/LICENSE-2.0.txt\"\n" +
                "        licenseDist = \"repo\"\n" +
                "        allLicenses = [\"Apache-2.0\"]\n" +
                "    }\n" +
                "\n" +
                "    repositories {\n" +
                "        jcenter()\n" +
                "        google()\n" +
                "        maven { url \"http://dl.bintray.com/davideas/maven\" }\n" +
                "        maven { url \"https://oss.sonatype.org/content/repositories/snapshots/\" }\n" +
                "    }\n" +
                "\n" +
                "    tasks.withType(Javadoc) {\n" +
                "        options.addStringOption('Xdoclint:none', '-quiet')\n" +
                "        options.addStringOption('encoding', 'UTF-8')\n" +
                "    }\n" +
                "}\n";
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
        public void visitBlockStatement(BlockStatement block) {
            L.l("visitBlockStatement");
            super.visitBlockStatement(block);
        }

        @Override
        public void visitForLoop(ForStatement forLoop) {
            L.l("visitForLoop");
            super.visitForLoop(forLoop);
        }

        @Override
        public void visitWhileLoop(WhileStatement loop) {
            L.l("visitWhileLoop");
            super.visitWhileLoop(loop);
        }

        @Override
        public void visitDoWhileLoop(DoWhileStatement loop) {
            L.l("visitDoWhileLoop");
            super.visitDoWhileLoop(loop);
        }

        @Override
        public void visitIfElse(IfStatement ifElse) {
            L.l("visitIfElse");
            super.visitIfElse(ifElse);
        }

        @Override
        public void visitExpressionStatement(ExpressionStatement statement) {
            L.l("visitExpressionStatement");
            super.visitExpressionStatement(statement);
        }

        @Override
        public void visitReturnStatement(ReturnStatement statement) {
            L.l("visitReturnStatement");
            super.visitReturnStatement(statement);
        }

        @Override
        public void visitAssertStatement(AssertStatement statement) {
            L.l("visitAssertStatement");
            super.visitAssertStatement(statement);
        }

        @Override
        public void visitTryCatchFinally(TryCatchStatement statement) {
            L.l("visitTryCatchFinally");
            super.visitTryCatchFinally(statement);
        }

        @Override
        protected void visitEmptyStatement(EmptyStatement statement) {
            L.l("visitEmptyStatement");
            super.visitEmptyStatement(statement);
        }

        @Override
        public void visitSwitch(SwitchStatement statement) {
            L.l("visitSwitch");
            super.visitSwitch(statement);
        }

        @Override
        public void visitCaseStatement(CaseStatement statement) {
            L.l("visitCaseStatement");
            super.visitCaseStatement(statement);
        }

        @Override
        public void visitBreakStatement(BreakStatement statement) {
            L.l("visitBreakStatement");
            super.visitBreakStatement(statement);
        }

        @Override
        public void visitContinueStatement(ContinueStatement statement) {
            L.l("visitContinueStatement");
            super.visitContinueStatement(statement);
        }

        @Override
        public void visitSynchronizedStatement(SynchronizedStatement statement) {
            L.l("visitSynchronizedStatement");
            super.visitSynchronizedStatement(statement);
        }

        @Override
        public void visitThrowStatement(ThrowStatement statement) {
            L.l("visitThrowStatement");
            super.visitThrowStatement(statement);
        }

        @Override
        public void visitStaticMethodCallExpression(StaticMethodCallExpression call) {
            L.l("visitStaticMethodCallExpression");
            super.visitStaticMethodCallExpression(call);
        }

        @Override
        public void visitConstructorCallExpression(ConstructorCallExpression call) {
            L.l("visitConstructorCallExpression");
            super.visitConstructorCallExpression(call);
        }

        @Override
        public void visitBinaryExpression(BinaryExpression expression) {
            L.l("visitBinaryExpression");
            Expression leftExpression = expression.getLeftExpression();
            Expression rightExpression = expression.getRightExpression();
            String leftValue = "";
            String rightValue = "";
            if (leftExpression instanceof PropertyExpression) {
                leftValue = ((PropertyExpression) leftExpression).getProperty().getText();
            }else if (leftExpression instanceof VariableExpression){
                leftValue = leftExpression.getText();
            }
            if (rightExpression instanceof ConstantExpression) {
                rightValue = ((ConstantExpression) rightExpression).getValue().toString();
            }
            L.l(leftValue,rightValue);
            super.visitBinaryExpression(expression);
        }

        @Override
        public void visitTernaryExpression(TernaryExpression expression) {
            L.l("visitTernaryExpression");
            super.visitTernaryExpression(expression);
        }

        @Override
        public void visitShortTernaryExpression(ElvisOperatorExpression expression) {
            L.l("visitShortTernaryExpression");
            super.visitShortTernaryExpression(expression);
        }

        @Override
        public void visitPostfixExpression(PostfixExpression expression) {
            L.l("visitPostfixExpression");
            super.visitPostfixExpression(expression);
        }

        @Override
        public void visitPrefixExpression(PrefixExpression expression) {
            L.l("visitPrefixExpression");
            super.visitPrefixExpression(expression);
        }

        @Override
        public void visitBooleanExpression(BooleanExpression expression) {
            L.l("visitBooleanExpression");
            super.visitBooleanExpression(expression);
        }

        @Override
        public void visitNotExpression(NotExpression expression) {
            L.l("visitNotExpression");
            super.visitNotExpression(expression);
        }

        @Override
        public void visitClosureExpression(ClosureExpression expression) {
            L.l("visitClosureExpression");
            super.visitClosureExpression(expression);
        }

        @Override
        public void visitTupleExpression(TupleExpression expression) {
            L.l("visitTupleExpression");
            super.visitTupleExpression(expression);
        }

        @Override
        public void visitListExpression(ListExpression expression) {
            L.l("visitListExpression");
            super.visitListExpression(expression);
        }

        @Override
        public void visitArrayExpression(ArrayExpression expression) {
            L.l("visitArrayExpression");
            super.visitArrayExpression(expression);
        }

        @Override
        public void visitMapExpression(MapExpression expression) {
            L.l("visitMapExpression");
            super.visitMapExpression(expression);
        }

        @Override
        public void visitMapEntryExpression(MapEntryExpression expression) {
            L.l("visitMapEntryExpression");
            super.visitMapEntryExpression(expression);
        }

        @Override
        public void visitRangeExpression(RangeExpression expression) {
            L.l("visitRangeExpression");
            super.visitRangeExpression(expression);
        }

        @Override
        public void visitSpreadExpression(SpreadExpression expression) {
            L.l("visitSpreadExpression");
            super.visitSpreadExpression(expression);
        }

        @Override
        public void visitSpreadMapExpression(SpreadMapExpression expression) {
            L.l("visitSpreadMapExpression");
            super.visitSpreadMapExpression(expression);
        }

        @Override
        public void visitMethodPointerExpression(MethodPointerExpression expression) {
            L.l("visitMethodPointerExpression");
            super.visitMethodPointerExpression(expression);
        }

        @Override
        public void visitUnaryMinusExpression(UnaryMinusExpression expression) {
            L.l("visitUnaryMinusExpression");
            super.visitUnaryMinusExpression(expression);
        }

        @Override
        public void visitUnaryPlusExpression(UnaryPlusExpression expression) {
            L.l("visitUnaryPlusExpression");
            super.visitUnaryPlusExpression(expression);
        }

        @Override
        public void visitBitwiseNegationExpression(BitwiseNegationExpression expression) {
            L.l("visitBitwiseNegationExpression");
            super.visitBitwiseNegationExpression(expression);
        }

        @Override
        public void visitCastExpression(CastExpression expression) {
            L.l("visitCastExpression");
            super.visitCastExpression(expression);
        }

        @Override
        public void visitConstantExpression(ConstantExpression expression) {
            L.l("visitConstantExpression");
            L.l(expression.getText());
            super.visitConstantExpression(expression);
        }

        @Override
        public void visitClassExpression(ClassExpression expression) {
            L.l("visitClassExpression");
            super.visitClassExpression(expression);
        }

        @Override
        public void visitVariableExpression(VariableExpression expression) {
            L.l("visitVariableExpression");
            L.l(expression.getName());
            super.visitVariableExpression(expression);
        }

        @Override
        public void visitDeclarationExpression(DeclarationExpression expression) {
            L.l("visitDeclarationExpression");
            super.visitDeclarationExpression(expression);
        }

        @Override
        public void visitPropertyExpression(PropertyExpression expression) {
            L.l("visitPropertyExpression");
            super.visitPropertyExpression(expression);
        }

        @Override
        public void visitAttributeExpression(AttributeExpression expression) {
            L.l("visitAttributeExpression");
            super.visitAttributeExpression(expression);
        }

        @Override
        public void visitFieldExpression(FieldExpression expression) {
            L.l("visitFieldExpression");
            super.visitFieldExpression(expression);
        }

        @Override
        public void visitGStringExpression(GStringExpression expression) {
            L.l("visitGStringExpression");
            super.visitGStringExpression(expression);
        }

        @Override
        public void visitCatchStatement(CatchStatement statement) {
            L.l("visitCatchStatement");
            super.visitCatchStatement(statement);
        }

        @Override
        public void visitClosureListExpression(ClosureListExpression cle) {
            L.l("visitClosureListExpression");
            super.visitClosureListExpression(cle);
        }

        @Override
        public void visitBytecodeExpression(BytecodeExpression cle) {
            L.l("visitBytecodeExpression");
            super.visitBytecodeExpression(cle);
        }

        @Override
        public void visitMethodCallExpression(MethodCallExpression call) {
            ConstantExpression method = (ConstantExpression) call.getMethod();
            L.l(method.getValue().toString());
            L.l(call.getMethodAsString());
            L.l(call.getArguments().getText());
            super.visitMethodCallExpression(call);
        }

        @Override
        public void visitArgumentlistExpression(ArgumentListExpression ale) {
            L.l(ale.getText());
            int size = ale.getExpressions().size();
            L.l(String.valueOf(size));
          /*  if (size >= 1) {
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
                            leftValue = ((ConstantExpression) mapEntryExpression.getKeyExpression()).getValue().toString();
                            rightValue = (mapEntryExpression.getValueExpression()).getText();
                            L.l(leftValue, rightValue);
                        }
                    }
                }
            }*/
            super.visitArgumentlistExpression(ale);
        }

        private boolean isDependencyKey(String methodAsString) {
            return Config.DEPENDENCY_TAG.contains(methodAsString.toLowerCase());
        }
    }


}


