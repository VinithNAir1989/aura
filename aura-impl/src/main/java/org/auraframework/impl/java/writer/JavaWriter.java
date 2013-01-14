/*
 * Copyright (C) 2012 salesforce.com, inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.auraframework.impl.java.writer;

import java.io.IOException;
import java.io.Writer;

import org.auraframework.def.AttributeDef;
import org.auraframework.def.ComponentDef;
import org.auraframework.def.DefDescriptor;
import org.auraframework.def.Definition;
import org.auraframework.system.Source;
import org.auraframework.throwable.AuraRuntimeException;
import org.auraframework.throwable.quickfix.QuickFixException;
import org.auraframework.util.AuraTextUtil;

/**
 */
public class JavaWriter extends SourceWriterImpl {

    private static final JavaWriter instance = new JavaWriter();

    public static JavaWriter getInstance() {
        return instance;
    }

    @Override
    public <D extends Definition> void write(D def, Source<?> source) {
        Writer writer = null;
        ComponentDef componentDef = (ComponentDef) def;
        try {
            writer = source.getWriter();
            writeHeader(writer);
            writePackage(writer, componentDef);
            writeLineBreaks(writer, 2);
            writeImports(writer);
            writeLineBreaks(writer, 2);
            writeClassBegin(writer, componentDef);
            writeLineBreaks(writer, 2);
            writeMethods(writer, componentDef);
            writeBlockEnd(writer);
        } catch (Exception e) {
            throw new AuraRuntimeException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                throw new AuraRuntimeException(e);
            }
        }
    }

    private void writeHeader(Writer writer) throws IOException {
        writer.write("/*" + NL + " * ");
        writeCopyright(writer);
        writer.write(NL + " * All Rights Reserved" + NL);
        writer.write(" * Company Confidential" + NL);
        writer.write(" *" + NL);
        writer.write(" * DO NOT MODIFY. DO NOT CHECK INTO PERFORCE." + NL);
        writer.write(" * THIS FILE IS GENERATED BY Aura" + NL);
        writer.write(" */" + NL);
    }

    private void writePackage(Writer writer, ComponentDef def) throws IOException {
        write(writer, "package cmp.%s;", def.getDescriptor().getNamespace());
    }

    private void writeImports(Writer writer) throws IOException {
        write(writer, "import org.auraframework.instance.Component;");
    }

    private void writeClassBegin(Writer writer, ComponentDef def) throws IOException {
        write(writer, "public class %s%s", AuraTextUtil.initCap(def.getDescriptor().getName()), "Cmp");
        DefDescriptor<ComponentDef> extendsDesc = def.getExtendsDescriptor();
        if (extendsDesc != null) {
            write(writer, " extends cmp.%s.%s%s", extendsDesc.getNamespace(),
                    AuraTextUtil.initCap(extendsDesc.getName()), "Cmp");
        }
        writer.write(" implements Component");
        writeBlockBegin(writer);
    }

    private void writeBlockBegin(Writer writer) throws IOException {
        writer.write("{");
        writeLineBreaks(writer, 1);
    }

    private void writeBlockEnd(Writer writer) throws IOException {
        writer.write("}");
        writeLineBreaks(writer, 1);
    }

    private void writeMethods(Writer writer, ComponentDef def) throws IOException, QuickFixException {
        for (AttributeDef attributeDef : def.getDeclaredAttributeDefs().values()) {
            writeGetter(writer, attributeDef);
            writeLineBreaks(writer, 2);
        }
    }

    private void writeGetter(Writer writer, AttributeDef attributeDef) throws IOException, QuickFixException {
        String name = attributeDef.getName();
        if (name.equals("class")) {
            name = "Clazz";
        }
        write(writer, "\tpublic /*%s*/Object get%s()", attributeDef.getTypeDef().getName(), AuraTextUtil.initCap(name));
        writeBlockBegin(writer);
        write(writer, "\t\treturn null;");
        writeLineBreaks(writer, 1);
        writer.write("\t");
        writeBlockEnd(writer);
    }
}
