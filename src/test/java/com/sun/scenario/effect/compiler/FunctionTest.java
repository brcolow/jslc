/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.scenario.effect.compiler;

import java.io.File;
import com.sun.scenario.effect.compiler.JSLC.JSLCInfo;
import com.sun.scenario.effect.compiler.backend.sw.java.JSWBackend;
import org.junit.Test;

/**
 */
public class FunctionTest {

    public FunctionTest() {
    }

    static JSLC.ParserInfo compile(String s) throws Exception {
        File tmpfile = File.createTempFile("foo", null);
        File tmpdir = tmpfile.getParentFile();
        JSLCInfo jslcinfo = new JSLCInfo();
        jslcinfo.outDir = tmpdir.getAbsolutePath();
        jslcinfo.shaderName = "Effect";
        jslcinfo.peerName = "Foo";
        jslcinfo.outTypes = JSLC.OUT_D3D11;
        return JSLC.compile(jslcinfo, s, Long.MAX_VALUE);
    }

    @Test
    public void fma() throws Exception {
        String s =
                "float myFunc() {\n" +
                "   float funcres = fma(1.5, 3.0, 5.0);\n" +
                "   return funcres;\n" +
                "}\n";
        compile(s);
    }

    @Test
    public void cross() throws Exception {
        String s =
                "float3 myFunc() {\n" +
                "   float3 a = float3(0.30, 0.59, 0.11);\n" +
                "   float3 b = float3(0.15, 0.44, 0.66);\n" +
                "   float3 funcres = cross(a, b);\n" +
                "   return funcres;\n" +
                "}\n";
        compile(s);
    }
}
