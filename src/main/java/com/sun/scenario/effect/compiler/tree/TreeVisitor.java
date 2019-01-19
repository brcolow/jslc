/*
 * Copyright (c) 2008, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.scenario.effect.compiler.tree;

/**
 */
public class TreeVisitor {

    public final void visit(Tree node) {
        if (node != null) {
            node.accept(this);
        }
    }

    public void visitBinaryExpr(BinaryExpr e) {
    }

    // visitUnary_expression
    public void visitUnaryExpr(UnaryExpr e) {
    }

    public void visitLiteralExpr(LiteralExpr e) {
    }

    public void visitVariableExpr(VariableExpr e) {
    }

    public void visitVectorCtorExpr(VectorCtorExpr e) {
    }

    public void visitParenExpr(ParenExpr e) {
    }

    public void visitFieldSelectExpr(FieldSelectExpr e) {
    }

    public void visitArrayAccessExpr(ArrayAccessExpr e) {
    }

    // visitFunction_call
    public void visitCallExpr(CallExpr e) {
    }

    // visitJump_statement
    public void visitContinueStmt(ContinueStmt s) {
    }

    // visitJump_statement
    public void visitBreakStmt(BreakStmt s) {
    }

    // visitJump_statement
    public void visitDiscardStmt(DiscardStmt s) {
    }

    // visitJump_statement
    public void visitReturnStmt(ReturnStmt s) {
    }

    public void visitSelectStmt(SelectStmt s) {
    }

    // visitIteration_statement
    public void visitWhileStmt(WhileStmt s) {
    }

    // visitIteration_statement
    public void visitDoWhileStmt(DoWhileStmt s) {
    }

    // visitIteration_statement
    public void visitForStmt(ForStmt s) {
    }

    public void visitExprStmt(ExprStmt s) {
    }

    // visitDeclaration
    public void visitDeclStmt(DeclStmt s) {
    }

    public void visitCompoundStmt(CompoundStmt s) {
    }

    // visitFunction_definition
    public void visitFuncDef(FuncDef d) {
    }

    // visitSingle_declaration
    public void visitVarDecl(VarDecl d) {
    }

    // visitGlue_block
    public void visitGlueBlock(GlueBlock b) {
    }

    // visitTranslation_unit
    public void visitProgramUnit(ProgramUnit p) {
    }
}
