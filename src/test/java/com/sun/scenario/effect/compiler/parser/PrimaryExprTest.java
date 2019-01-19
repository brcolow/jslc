/*
 * Copyright (c) 2008, 2019, Oracle and/or its affiliates. All rights reserved.
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
import com.sun.scenario.effect.compiler.model.Types;
import com.sun.scenario.effect.compiler.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Before;
import org.junit.Test;

import static com.sun.scenario.effect.compiler.parser.Expressions.SIMPLE_EXPRESSION;
import static com.sun.scenario.effect.compiler.parser.Expressions.SIMPLE_EXPRESSION_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PrimaryExprTest extends ParserBase {

    private String primary;

    @Before
    public void setUp() {
        this.primary = primary();
    }

    @Test
    public void variable() throws Exception {
        Expr tree = parseTreeFor("foo");
        assertTrue(tree instanceof VariableExpr);
        assertEquals("foo", ((VariableExpr) tree).getVariable().getName());
    }

    @Test
    public void intLiteral() throws Exception {
        Expr tree = parseTreeFor("123");
        assertTrue(tree instanceof LiteralExpr);
        assertEquals(123, ((LiteralExpr) tree).getValue());
    }

    @Test
    public void floatLiteral() throws Exception {
        Expr tree = parseTreeFor("1.234");
        assertTrue(tree instanceof LiteralExpr);
        assertEquals(1.234f, ((LiteralExpr) tree).getValue());
    }

    @Test
    public void boolLiteralT() throws Exception {
        Expr tree = parseTreeFor("true");
        assertTrue(tree instanceof LiteralExpr);
        assertEquals(Boolean.TRUE, ((LiteralExpr) tree).getValue());
    }

    @Test
    public void boolLiteralF() throws Exception {
        Expr tree = parseTreeFor("false");
        assertTrue(tree instanceof LiteralExpr);
        assertEquals(Boolean.FALSE, ((LiteralExpr) tree).getValue());
    }

    @Test
    public void bracketed() throws Exception {
        Expr tree = parseTreeFor("(" + primary + ")");
        assertTrue(tree instanceof ParenExpr);
        Expr expr = ((ParenExpr) tree).getExpr();
        assertTrue(expr instanceof LiteralExpr);
        assertEquals(5, ((LiteralExpr) expr).getValue());
    }

    @Test(expected = ParseCancellationException.class)
    public void notAPrimaryExpression() throws Exception {
        parseTreeFor("!(@&#");
    }

    private Expr parseTreeFor(String text) throws Exception {
        JSLParser parser = parserOver(text);
        JSLCVisitor visitor = new JSLCVisitor();
        visitor.getSymbolTable().declareVariable("foo", Types.INT, null);
        return visitor.visitPrimary_expression(parser.primary_expression());
    }

    protected String primary() {
        return SIMPLE_EXPRESSION;
    }

    protected int primaryValue() {
        return SIMPLE_EXPRESSION_VALUE;
    }
}
