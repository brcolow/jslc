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

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class JSLCTest {

    static void compileBlend() throws Exception {
        File tmpfile = File.createTempFile("foo", null);
        File tmpdir = tmpfile.getParentFile();
        JSLC.JSLCInfo jslcinfo = new JSLC.JSLCInfo();
        jslcinfo.shaderName = "Blend";
        String path = JSLCTest.class.getResource("Blend.jsl").getPath();
        jslcinfo.outDir = "target/gensrc";
        jslcinfo.srcDirs = List.of(path.substring(0, path.lastIndexOf('/')));
        jslcinfo.parseArgs(new String[]{"-all", "-t"});
        File mainFile = jslcinfo.getJSLFile();
        String main = CompileJSL.readFile(mainFile);
        for (Blend.Mode mode : Blend.Mode.values()) {
            String funcname = mode.name().toLowerCase(Locale.ENGLISH);
            String modename = jslcinfo.shaderName + "_" + mode.name();
            File funcFile = jslcinfo.getJSLFile(modename);
            String func = CompileJSL.readFile(funcFile);
            String source = String.format(main, func, funcname);
            jslcinfo.peerName = modename;
            JSLC.compile(jslcinfo, source, Long.MAX_VALUE);
        }
    }

    @Test
    public void testSse() throws Exception {
        compileBlend();
    }

    private static class Blend {

        public enum Mode {
            SRC_OVER,
            SRC_IN,
            SRC_OUT,
            SRC_ATOP,
            ADD,
            MULTIPLY,
            SCREEN,
            OVERLAY,
            DARKEN,
            LIGHTEN,
            COLOR_DODGE,
            COLOR_BURN,
            HARD_LIGHT,
            SOFT_LIGHT,
            DIFFERENCE,
            EXCLUSION,
            RED,
            GREEN,
            BLUE,
        }
    }
}
