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

package com.sun.scenario.effect.compiler.backend.sw.java;

import com.sun.scenario.effect.compiler.JSLC;
import com.sun.scenario.effect.compiler.JSLC.JSLCInfo;
import org.junit.Assert;
import org.junit.Test;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static javax.tools.JavaFileObject.Kind.SOURCE;

/**
 * Tests that verify the JSWBackend creates Java code with the expected
 * results. We do this by actually compiling the generated Java code and
 * asserting that the result is as expected.
 */
public class JSWTest {

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
    public void testAny() throws Exception {
        assertEquals("bool res = any(true);", Boolean.class, true);
        assertEquals("bool res = any(false);", Boolean.class, false);

        assertEquals("bool res = any(bool2(true, true));", Boolean.class, true);
        assertEquals("bool res = any(bool2(true, false));", Boolean.class, true);
        assertEquals("bool res = any(bool2(false, true));", Boolean.class, true);
        assertEquals("bool res = any(bool2(false, false));", Boolean.class, false);
    }

    @Test
    public void testAll() throws Exception {
        assertEquals("bool res = all(true);", Boolean.class, true);
        assertEquals("bool res = all(false);", Boolean.class, false);

        assertEquals("bool res = all(bool2(true, true));", Boolean.class, true);
        assertEquals("bool res = all(bool2(true, false));", Boolean.class, false);
        assertEquals("bool res = all(bool2(false, true));", Boolean.class, false);
        assertEquals("bool res = all(bool2(false, false));", Boolean.class, false);
    }

    @Test
    public void testCross() throws Exception {
        assertEquals("float3 res = cross(float3(0.1, 0.2, 0.3), float3(0.2, 0.4, 0.6));",
                Float.class, 0f, 0f, 0f);
        assertEquals("float3 res = cross(float3(1.0, 2.0, 3.0), float3(0.4, 0.6, 0.8));",
                Float.class, -0.2f, 0.4f, -0.2f);
        assertEquals("float3 res = cross(float3(0.0, 0.0, 1.0), float3(0.5, 0.5, 0.5));",
                Float.class, -0.5f, 0.5f, 0f);
    }

    @Test
    public void testDot() throws Exception {
        assertEquals("float res = dot(float3(0.1, 0.2, 0.3), float3(0.2, 0.4, 0.6));",
                Float.class, 0.28f);
        assertEquals("float res = dot(float3(1.0, 2.0, 3.0), float3(0.4, 0.6, 0.8));",
                Float.class, 4f);
        assertEquals("float res = dot(float3(0.0, 0.0, 1.0), float3(0.5, 0.5, 0.5));",
                Float.class, 0.5f);
    }

    @Test
    public void testDegrees() throws Exception {
        assertEquals("float res = degrees(0.0);", Float.class, 0f);
        assertEquals("float res = degrees(1.0);", Float.class, 57.29578f);
        assertEquals("float res = degrees(2.0);", Float.class, 114.59156f);
        assertEquals("float res = degrees(3.14159265359);", Float.class, 180f);

        assertEquals("float2 res = degrees(float2(1.0, 1.0));", Float.class, 57.29578f, 57.29578f);
    }

    @Test
    public void testRadians() throws Exception {
        assertEquals("float res = radians(0.0);", Float.class, 0f);
        assertEquals("float res = radians(57.29578);", Float.class, 1f);
        assertEquals("float res = radians(114.59156);", Float.class, 2f);
        assertEquals("float res = radians(180.0);", Float.class, 3.14159265359f);

        assertEquals("float2 res = radians(float2(57.29578, 57.29578));", Float.class, 1f, 1f);
    }

    @Test
    public void testIsFinite() throws Exception {
        assertEquals("bool res = isFinite(1.0);", Boolean.class, true);
        assertEquals("bool res = isFinite(1.0/0.0);", Boolean.class, false);

        assertEquals("bool2 res = isFinite(float2(1.0, 1.0));", Boolean.class, true, true);
        assertEquals("bool2 res = isFinite(float2(1.0, 1.0/0.0));", Boolean.class, true, false);
        assertEquals("bool2 res = isFinite(float2(1.0/0.0, 1.0));", Boolean.class, false, true);
        assertEquals("bool2 res = isFinite(float2(1.0/0.0, 1.0/0.0));", Boolean.class, false, false);
    }

    @Test
    public void testIsInfinite() throws Exception {
        assertEquals("bool res = isInfinite(1.0/0.0);", Boolean.class, true);
        assertEquals("bool res = isInfinite(1.0);", Boolean.class, false);

        assertEquals("bool2 res = isInfinite(float2(1.0/0.0, 1.0/0.0));", Boolean.class, true, true);
        assertEquals("bool2 res = isInfinite(float2(1.0/0.0, 1.0));", Boolean.class, true, false);
        assertEquals("bool2 res = isInfinite(float2(1.0, 1.0/0.0));", Boolean.class, false, true);
        assertEquals("bool2 res = isInfinite(float2(1.0, 1.0));", Boolean.class, false, false);
    }

    @Test
    public void testIsNaN() throws Exception {
        assertEquals("bool res = isNaN(0.0/0.0);", Boolean.class, true);
        assertEquals("bool res = isNaN(0.0);", Boolean.class, false);

        assertEquals("bool2 res = isNaN(float2(0.0/0.0, 0.0/0.0));", Boolean.class, true, true);
        assertEquals("bool2 res = isNaN(float2(0.0/0.0, 1.0));", Boolean.class, true, false);
        assertEquals("bool2 res = isNaN(float2(1.0, 0.0/0.0));", Boolean.class, false, true);
        assertEquals("bool2 res = isNaN(float2(1.0, 1.0));", Boolean.class, false, false);
    }

    private static <T> void assertEquals(String jslCode, Class<T> resultType, T... expected) throws Exception {
        JSLC.ParserInfo pinfo = compile(jslCode);
        JSWBackend javaBackend = new JSWBackend(pinfo.parser, pinfo.visitor, pinfo.program);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        ClassFileManager manager = new ClassFileManager(
                compiler.getStandardFileManager(null, null, null));
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        String body = javaBackend.getBody();
        printWriter.write(getClassCode(body, expected.length));
        String className = "com.sun.scenario.effect.compiler.backend.sw.java.Test";
        List<ClassFileManager.CharSequenceJavaFileObject> files = new ArrayList<>();
        files.add(new ClassFileManager.CharSequenceJavaFileObject(className, getClassCode(body, expected.length)));
        compiler.getTask(null, manager, null, null, null, files).call();
        Class<?> testClass;
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class<?> caller = StackWalker.getInstance(RETAIN_CLASS_REFERENCE)
                .walk(s -> s.skip(2).findFirst().get().getDeclaringClass());
        if (className.startsWith(caller.getPackageName())) {
            testClass = MethodHandles.privateLookupIn(caller, lookup).defineClass(manager.jfo.getBytes());
        } else {
            testClass = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) {
                    byte[] b = manager.jfo.getBytes();
                    int len = b.length;
                    return defineClass(className, b, 0, len);
                }
            }.loadClass(className);
        }
        if (expected.length == 1) {
            T actual = resultType.cast(testClass.getMethod("getRes").invoke(null));
            if (resultType.equals(Float.class)) {
                Assert.assertEquals((float) expected[0], (float) actual, 0.0001);
            } else {
                Assert.assertEquals(expected[0], actual);
            }
        } else {
            for (int i = 0; i < expected.length; i++) {
                T actual = resultType.cast(testClass.getMethod("getRes" + JSWBackend.getSuffix(i)).invoke(null));
                if (resultType.equals(Float.class)) {
                    Assert.assertEquals("Expected " + i + "th component of res to be equal within 0.0001",
                            (float) expected[i], (float) actual, 0.0001);
                } else {
                    Assert.assertEquals("Expected " + i + "th component of res to be equal",
                            expected[i], actual);
                }
            }
        }
    }

    private static String getClassCode(String body, int numResults) {
        StringBuilder classCode = new StringBuilder();
        classCode.append("package com.sun.scenario.effect.compiler.backend.sw.java;\n\n")
                .append("public class Test {\n");
        for (int i = 0; i < numResults; i++) {
            String optionalSuffix = numResults == 1 ? "" : JSWBackend.getSuffix(i);
            classCode.append("  public static Object getRes").append(optionalSuffix).append("() {\n");
            classCode.append(body).append("\n");
            classCode.append("    return res").append(optionalSuffix).append(";\n");
            classCode.append("  }\n");
        }
        classCode.append("}");
        return classCode.toString();
    }

    static class JavaFileObject extends SimpleJavaFileObject {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        JavaFileObject(String name, JavaFileObject.Kind kind) {
            super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        }

        byte[] getBytes() {
            return baos.toByteArray();
        }

        @Override
        public OutputStream openOutputStream() {
            return baos;
        }
    }

    static class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        JavaFileObject jfo;

        ClassFileManager(StandardJavaFileManager m) {
            super(m);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location,
                                                   String className,
                                                   JavaFileObject.Kind kind,
                                                   FileObject sibling) {
            return jfo = new JavaFileObject(className, kind);
        }

        static class CharSequenceJavaFileObject extends SimpleJavaFileObject {
            CharSequence content;

            private CharSequenceJavaFileObject(String className, CharSequence content) {
                super(URI.create("string:///" + className.replace('.', '/') + SOURCE.extension),
                        SOURCE);
                this.content = content;
            }

            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return content;
            }
        }
    }

}
