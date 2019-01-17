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

package com.sun.scenario.effect.compiler.parser;

import com.sun.scenario.effect.compiler.JSLParser;
import com.sun.scenario.effect.compiler.model.BinaryOpType;
import com.sun.scenario.effect.compiler.model.Qualifier;
import com.sun.scenario.effect.compiler.model.SymbolTable;
import com.sun.scenario.effect.compiler.model.Type;
import com.sun.scenario.effect.compiler.tree.BinaryExpr;
import com.sun.scenario.effect.compiler.tree.LiteralExpr;
import com.sun.scenario.effect.compiler.tree.VariableExpr;
import com.sun.scenario.effect.compiler.tree.VectorCtorExpr;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AssignmentExprTest extends ParserBase {

    @Test
    public void userVar() throws Exception {
        BinaryExpr tree = parseTreeFor("foo = 32.0");
        assertEquals(Type.FLOAT, tree.getResultType());
        assertEquals(BinaryOpType.EQ, tree.getOp());
        assertEquals(VariableExpr.class, tree.getLeft().getClass());
        assertEquals("foo", ((VariableExpr) tree.getLeft()).getVariable().getName());
        assertEquals(Type.FLOAT, tree.getRight().getResultType());
        assertEquals(LiteralExpr.class, tree.getRight().getClass());
        assertEquals(32.0f, ((LiteralExpr) tree.getRight()).getValue());
    }

    @Test(expected = RuntimeException.class)
    public void userROVar() throws Exception {
        BinaryExpr tree = parseTreeFor("readonly = 32.0");
    }

    @Test
    public void coreVar() throws Exception {
        BinaryExpr tree = parseTreeFor("color = float4(1.0)");
        assertEquals(Type.FLOAT4, tree.getResultType());
        assertEquals(BinaryOpType.EQ, tree.getOp());
        assertEquals(VariableExpr.class, tree.getLeft().getClass());
        assertEquals("color", ((VariableExpr) tree.getLeft()).getVariable().getName());
        assertEquals(Type.FLOAT4, tree.getRight().getResultType());
        assertEquals(VectorCtorExpr.class, tree.getRight().getClass());
        assertEquals(4, ((VectorCtorExpr) tree.getRight()).getParams().size());
        assertEquals(Type.FLOAT, ((VectorCtorExpr) tree.getRight()).getParams().get(0).getResultType());
        assertEquals(1.0f, ((LiteralExpr) ((VectorCtorExpr) tree.getRight()).getParams().get(0)).getValue());

        assertEquals(Type.FLOAT, ((VectorCtorExpr) tree.getRight()).getParams().get(1).getResultType());
        assertEquals(1.0f, ((LiteralExpr) ((VectorCtorExpr) tree.getRight()).getParams().get(1)).getValue());

        assertEquals(Type.FLOAT, ((VectorCtorExpr) tree.getRight()).getParams().get(2).getResultType());
        assertEquals(1.0f, ((LiteralExpr) ((VectorCtorExpr) tree.getRight()).getParams().get(2)).getValue());

        assertEquals(Type.FLOAT, ((VectorCtorExpr) tree.getRight()).getParams().get(3).getResultType());
        assertEquals(1.0f, ((LiteralExpr) ((VectorCtorExpr) tree.getRight()).getParams().get(3)).getValue());
    }

    @Test
    public void coreVarField() throws Exception {
        BinaryExpr tree = parseTreeFor("color.r = 3.0");
    }

    @Test(expected = RuntimeException.class)
    public void coreROVar() throws Exception {
        BinaryExpr tree = parseTreeFor("pos0 = float2(1.0)");
        assertEquals(Type.FLOAT2, tree.getResultType());
        assertEquals(BinaryOpType.EQ, tree.getOp());
        assertEquals(Type.FLOAT2, tree.getRight().getResultType());
        assertEquals(1.0, ((LiteralExpr) tree.getRight()).getValue());

    }

    @Test(expected = RuntimeException.class)
    public void coreROVarField() throws Exception {
        BinaryExpr tree = parseTreeFor("pos0.x = 1.0");
    }

    @Test(expected = RecognitionException.class)
    public void notAnAssignment() throws Exception {
        parseTreeFor("const foo");
    }

    private BinaryExpr parseTreeFor(String text) throws RecognitionException {
        JSLParser parser = parserOver(text);
        SymbolTable st = parser.getSymbolTable();
        st.declareVariable("foo", Type.FLOAT, null);
        st.declareVariable("readonly", Type.FLOAT, Qualifier.CONST);
        // trick test into thinking main() function is currently in
        // scope so that we can test core variables such as color and pos0
        st.enterFrame();
        st.declareFunction("main", Type.VOID, null);
        return (BinaryExpr)parser.assignment_expression();
    }
}
