/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bcel.verifier.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Utility;

public abstract class TestCreator {

    // Common package base name for generated test classes
    protected static final String TEST_PACKAGE = TestCreator.class.getPackage().getName();

    public void create() throws IOException {
        final File classFile = new File(getPackageFolder(), getClassName());
        try (FileOutputStream out = new FileOutputStream(classFile)) {
            create(out);
        }
    }

    public abstract void create(OutputStream out) throws IOException;

    private File getClassesFolder() throws IOException {
        try {
            return new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (final URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private String getClassName() {
        final String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1).replace("Creator", JavaClass.EXTENSION);
    }

    private File getPackageFolder() throws IOException {
        return new File(getClassesFolder(), getPackageName());
    }

    protected String getPackageName() {
        return Utility.packageToPath(getClass().getPackage().getName());
    }
}
