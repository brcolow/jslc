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

package com.sun.scenario.effect.compiler.backend.hw;

import java.util.HashMap;
import java.util.Map;

import com.sun.scenario.effect.compiler.JSLParser;
import com.sun.scenario.effect.compiler.model.BaseType;
import com.sun.scenario.effect.compiler.model.Function;
import com.sun.scenario.effect.compiler.model.Qualifier;
import com.sun.scenario.effect.compiler.model.Type;
import com.sun.scenario.effect.compiler.model.Types;
import com.sun.scenario.effect.compiler.model.Variable;
import com.sun.scenario.effect.compiler.tree.Expr;
import com.sun.scenario.effect.compiler.tree.FuncDef;
import com.sun.scenario.effect.compiler.tree.JSLCVisitor;
import com.sun.scenario.effect.compiler.tree.VarDecl;

/**
 */
public class HLSLBackend extends SLBackend {

    private final ShaderModel shaderModel;
    private final Map<String, String> typeMap = new HashMap<>();
    private final Map<String, String> qualMap = new HashMap<>();
    private final Map<String, HLSLFunction> funcMap = new HashMap<>();
    private final Map<String, String> varMap = new HashMap<>();

    public HLSLBackend(JSLParser parser, JSLCVisitor visitor, ShaderModel shaderModel) {
        super(parser, visitor);
        this.shaderModel = shaderModel;
        initTypeMap();
        initQualMap();
        initFuncMap();
    }

    private void initTypeMap() {
        if (shaderModel.supports(ShaderModel.SM3)) {
            typeMap.put("void", "void");
            typeMap.put("float", "float");
            typeMap.put("float2", "float2");
            typeMap.put("float3", "float3");
            typeMap.put("float4", "float4");
            typeMap.put("int", "int");
            typeMap.put("int2", "int2");
            typeMap.put("int3", "int3");
            typeMap.put("int4", "int4");
            typeMap.put("bool", "bool");
            typeMap.put("bool2", "bool2");
            typeMap.put("bool3", "bool3");
            typeMap.put("bool4", "bool4");
            typeMap.put("sampler", "sampler2D");
            typeMap.put("lsampler", "sampler2D");
            typeMap.put("fsampler", "sampler2D");
        }
        if (shaderModel.supports(ShaderModel.SM4_0)) {
            // A constant buffer is a specialized buffer resource that is accessed like a buffer. Each constant buffer
            // can hold up to 4096 vectors; each vector contains up to four 32-bit values. You can bind up to 14
            // constant buffers per pipeline stage (2 additional slots are reserved for internal use).
            typeMap.put("cbuffer", "cbuffer");

            // A texture buffer is a specialized buffer resource that is accessed like a texture. Texture access
            // (as compared with buffer access) can have better performance for arbitrarily indexed data. You can bind
            // up to 128 texture buffers per pipeline stage.
            typeMap.put("tbuffer", "tbuffer");
        }
        if (shaderModel.supports(ShaderModel.SM5_0)) {

        }
        if (shaderModel.supports(ShaderModel.SM5_1)) {

        }
    }

    private void initQualMap() {
        if (shaderModel.supports(ShaderModel.SM3)) {
            qualMap.put("const", "");
            qualMap.put("param", "");
        }
        if (shaderModel.supports(ShaderModel.SM5_0)) {

        }
        if (shaderModel.supports(ShaderModel.SM5_1)) {

        }
    }

    private void initFuncMap() {
        // All HLSL builtin functions must be added here (not just one's with different names).
        funcMap.put("abs",        new HLSLFunction("abs", ShaderModel.SM3));
        funcMap.put("acos",       new HLSLFunction("acos", ShaderModel.SM3));
        funcMap.put("all",        new HLSLFunction("all", ShaderModel.SM3));
        funcMap.put("any",        new HLSLFunction("any", ShaderModel.SM3));
        funcMap.put("asin",       new HLSLFunction("asin", ShaderModel.SM3));
        funcMap.put("atan",       new HLSLFunction("atan", ShaderModel.SM3));
        funcMap.put("atan2",      new HLSLFunction("atan2", ShaderModel.SM3));
        funcMap.put("ceil",       new HLSLFunction("ceil", ShaderModel.SM3));
        funcMap.put("clamp",      new HLSLFunction("clamp", ShaderModel.SM3));
        // clip
        funcMap.put("cos",        new HLSLFunction("cos", ShaderModel.SM3));
        funcMap.put("cosh",       new HLSLFunction("cosh", ShaderModel.SM3));
        funcMap.put("cross",      new HLSLFunction("cross", ShaderModel.SM3));
        funcMap.put("ddx",        new HLSLFunction("ddx", ShaderModel.SM3));
        funcMap.put("ddy",        new HLSLFunction("ddy", ShaderModel.SM3));
        funcMap.put("degrees",    new HLSLFunction("degrees", ShaderModel.SM3));
        // determinant
        funcMap.put("distance",   new HLSLFunction("distance", ShaderModel.SM3));
        funcMap.put("dot",        new HLSLFunction("dot", ShaderModel.SM3));
        funcMap.put("exp",        new HLSLFunction("exp", ShaderModel.SM3));
        // exp2
        // faceforward
        funcMap.put("floor",      new HLSLFunction("floor", ShaderModel.SM3));
        funcMap.put("mod",        new HLSLFunction("fmod", ShaderModel.SM3));
        funcMap.put("fract",      new HLSLFunction("frac", ShaderModel.SM3));
        // frexp https://github.com/jythontools/jython/blob/master/src/org/python/modules/math.java
        // fwidth
        funcMap.put("intcast",    new HLSLFunction("int", ShaderModel.SM3));
        funcMap.put("isFinite",   new HLSLFunction("isfinite", ShaderModel.SM3));
        funcMap.put("isInfinite", new HLSLFunction("isinf", ShaderModel.SM3));
        funcMap.put("isNaN",      new HLSLFunction("isnan", ShaderModel.SM3));
        // ldexp https://github.com/jythontools/jython/blob/master/src/org/python/modules/math.java
        funcMap.put("length",     new HLSLFunction("length", ShaderModel.SM3));
        funcMap.put("mix",        new HLSLFunction("lerp", ShaderModel.SM3));
        // lit
        // log
        // log10
        // log2
        funcMap.put("max",        new HLSLFunction("max", ShaderModel.SM3));
        funcMap.put("min",        new HLSLFunction("min", ShaderModel.SM3));
        // modf
        // mul
        // noise
        funcMap.put("normalize",  new HLSLFunction("normalize", ShaderModel.SM3));
        funcMap.put("pow",        new HLSLFunction("pow", ShaderModel.SM3));
        funcMap.put("radians",    new HLSLFunction("radians", ShaderModel.SM3));
        // reflect
        // refract
        // round
        // rsqrt
        // saturate
        funcMap.put("sign",       new HLSLFunction("sign", ShaderModel.SM3));
        funcMap.put("sin",        new HLSLFunction("sin", ShaderModel.SM3));
        // sincos
        funcMap.put("sinh",       new HLSLFunction("sinh", ShaderModel.SM3));
        funcMap.put("smoothstep", new HLSLFunction("smoothstep", ShaderModel.SM3));
        funcMap.put("sqrt",       new HLSLFunction("sqrt", ShaderModel.SM3));
        // step
        funcMap.put("tan",        new HLSLFunction("tan", ShaderModel.SM3));
        funcMap.put("tanh",       new HLSLFunction("tanh", ShaderModel.SM3));
        // tex1D/3D/CUBE
        funcMap.put("sample",     new HLSLFunction("tex2D", ShaderModel.SM3));
        // transpose
        // trunc

        funcMap.put("fma", new HLSLFunction("fma", ShaderModel.SM5_0));
    }

    @Override
    protected String getType(Type t) {
        return typeMap.get(t.toString());
    }

    @Override
    protected String getQualifier(Qualifier q) {
        return qualMap.get(q.toString());
    }

    @Override
    protected String getVar(String v) {
        String s = varMap.get(v);
        return (s != null) ? s : v;
    }

    @Override
    protected String getFuncName(String funcName) {
        HLSLFunction hlslFunction = funcMap.get(funcName);
        if (hlslFunction != null) {
            // This is a builtin (intrinsic) function, check that it is supported in this shader model.
            if (!shaderModel.supports(hlslFunction.minSupportedModel)) {
                throw new IllegalArgumentException("builtin function \"" + funcName +
                        "\" is not supported for shader model: " + shaderModel);
            }

            return hlslFunction.name;
        } else {
            return funcName;
        }
    }

    @Override
    public void visitFuncDef(FuncDef d) {
        Function func = d.getFunction();
        if (func.getName().equals("main")) {
            output(getType(func.getReturnType()) + " " + func.getName() + "(");
            // TODO: it would be better if we scanned the whole JSL program
            // to see if pos0 or pos1 are used anywhere, but for now there
            // doesn't seem to be any harm in blindly declaring both here...
            for (int i = 0; i < 2; i++) {
                output("in float2 pos" + i + " : TEXCOORD" + i + ",\n");
            }
            // TODO: only need this if pixcoord is referenced somewhere
            // in the JSL program...
            output("in float2 pixcoord : VPOS,\n");
            output("in float4 jsl_vertexColor : COLOR0,\n");
            output("out float4 color : COLOR0");
            output(") ");
            scan(d.getStmt());
        } else {
            super.visitFuncDef(d);
        }
    }

    @Override
    public void visitVarDecl(VarDecl d) {
        Variable var = d.getVariable();
        Type type = var.getType();
        Qualifier qual = var.getQualifier();
        if (qual == Qualifier.PARAM && type.getBaseType() == BaseType.INT) {
            // TODO: It seems that constant integer registers have limitations
            // in SM 3.0... For example, the max number of integer registers
            // (those specified with i#) is 16; in PS 2.0 these were limited
            // to flow control instructions only, but according to MSDN this
            // restriction went away with PS 3.0.  However, bad things happen
            // at runtime if we output:
            //     int variableName : register(c0);
            // (not sure what the problem is, but bad values seem to be
            // uploaded if we use SetPixelShaderConstantI() in this case), and
            // if we use i# instead:
            //     int variableName : register(i0);
            // the compiler will say this is invalid (it won't complain if
            // we actually used it in a loop expression though).  Until this
            // problem is better understood, we can work around it by
            // declaring these params as float variants, e.g.:
            //     float variableName : register(c0);
            // and using SetPixelShaderConstantF() instead.
            String t;
            if (type == Types.INT) {
                t = "float";
            } else if (type == Types.INT2) {
                t = "float2";
            } else if (type == Types.INT3) {
                t = "float3";
            } else if (type == Types.INT4) {
                t = "float4";
            } else {
                throw new InternalError();
            }
            output(t + " " + var.getName());
        } else if (qual == Qualifier.CONST) {
            // use #define-style definition
            output("#define " + var.getName());
        } else {
            output(getType(type) + " " + var.getName());
        }
        Expr init = d.getInit();
        if (init != null) {
            if (qual == Qualifier.CONST) {
                // use #define-style definition (no '=', wrap in
                // parens for safety)
                output(" (");
                scan(init);
                output(")");
            } else {
                output(" = ");
                scan(init);
            }
        }
        if (var.isArray()) {
            output("[" + var.getArraySize() + "]");
        }
        if (qual == Qualifier.PARAM) {
            char c = (type.getBaseType() == BaseType.SAMPLER) ? 's' : 'c';
            output(" : register(" + c + var.getReg() + ")");
        }
        if (qual == Qualifier.CONST) {
            // use #define-style definition (no closing ';')
            output("\n");
        } else {
            output(";\n");
        }
    }

    private static final class HLSLFunction {
        private final String name;
        private final ShaderModel minSupportedModel;

        HLSLFunction(String name, ShaderModel minSupportedModel) {
            this.name = name;
            this.minSupportedModel = minSupportedModel;
        }
    }
}
