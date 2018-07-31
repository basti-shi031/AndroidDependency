import config.Config;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.classgen.BytecodeExpression;
import org.codehaus.groovy.control.SourceUnit;
import util.L;

import java.util.List;

public class Parse {
    public static void main(String args[]) {
        try {
//             String source = FileUtil.read("D:\\cs\\projects_last\\gradle500\\VaughnVernon__fdse__IDDD_Samples\\build.gradle ");
/*            String source = "dependencies {\n" +
                    "    compile depends.antlr_runtime,\n" +
                    "            depends.stringtemplate,\n" +
                    "            depends.jcommander,\n" +
                    "            depends.mockito,\n" +
                    "            depends.guava,\n" +
                    "            depends.findbugs,\n" +
                    "            depends.ST4\n" +
                    "\n" +
                    "    dx    depends.dx\n" +
                    "\n" +
                    "    accessorTestGenerator project('accessorTestGenerator')\n" +
                    "}";*/
String source = "dependencies {\n" +
        "    compile 'com.google.guava:guava:[10.+,)'\n" +
        "    compile 'com.google.code.findbugs:jsr305:2.0.2'\n" +
        "\n" +
        "    // junit testing\n" +
        "    testCompile 'junit:junit:4.11'\n" +
        "    testCompile 'org.mockito:mockito-all:1.9.5'\n" +
        "}";
            SourceUnit unit = SourceUnit.create("gradle", source);
            unit.parse();
            unit.completePhase();
            unit.convert();
            visitScriptCode(unit, new parseBuildGradle());
        } catch (Exception e) {
            L.l("123");
            e.printStackTrace();
        }

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
        /*    L.l("visitBlockStatement");
            List<Statement> statements = block.getStatements();
            for (Statement statement : statements) {
                if (statement instanceof ExpressionStatement) {
                    Expression expression = ((ExpressionStatement) statement).getExpression();
                    if (expression instanceof MethodCallExpression) {
                        if (isDependencyKey(((MethodCallExpression) expression).getMethodAsString())) {
                            L.l("dependency:",((MethodCallExpression) expression).getArguments().getText());
                        }
                    }
                }
            }
            super.visitBlockStatement(block);*/
            L.l("-+-+");
            List<Statement> statements = block.getStatements();
            for (Statement statement : statements) {
                if (statement instanceof ExpressionStatement) {
                    Expression expression = ((ExpressionStatement) statement).getExpression();
                    if (expression instanceof MethodCallExpression) {
                        if (isDependencyKey(((MethodCallExpression) expression).getMethodAsString())) {
                            List<Expression> expressions = ((ArgumentListExpression) ((MethodCallExpression) expression).getArguments()).getExpressions();
                            for (Expression e : expressions) {
                                if (e instanceof PropertyExpression) {
                                    L.l("property",((PropertyExpression) e).getPropertyAsString());
                                }
                            }
                            L.l("dependency:", ((MethodCallExpression) expression).getArguments().getText());
                        }
                    }
                }
            }
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
            Expression expression = statement.getExpression();
            if (expression instanceof MethodCallExpression) {
                ((MethodCallExpression) statement.getExpression()).getMethodAsString();
            }
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
            } else if (leftExpression instanceof VariableExpression) {
                leftValue = leftExpression.getText();
            }
            if (rightExpression instanceof ConstantExpression) {
                rightValue = ((ConstantExpression) rightExpression).getValue().toString();
            }
            L.l(leftValue, rightValue);
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
            L.l("===", call.getArguments().getText());
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

    private boolean isDependencyKey(String methodAsString) {
        return Config.DEPENDENCY_TAG.contains(methodAsString.toLowerCase());
    }
}


