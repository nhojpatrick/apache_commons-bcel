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
package org.apache.bcel.generic;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Utility;

/**
 * Denotes reference such as {@link String}.
 */
public class ObjectType extends ReferenceType {

    /**
     * Constructs a new instance.
     *
     * @param className fully qualified class name, for example java.lang.String
     * @return a new instance.
     * @since 6.0
     */
    public static ObjectType getInstance(final String className) {
        return new ObjectType(className);
    }

    private final String className; // Class name of type

    /**
     * Constructs a new instance.
     *
     * @param className fully qualified class name, for example {@link String}
     */
    public ObjectType(final String className) {
        super(Const.T_REFERENCE, "L" + Utility.packageToPath(className) + ";");
        this.className = Utility.pathToPackage(className);
    }

    /**
     * Java Virtual Machine Specification edition 2, � 5.4.4 Access Control
     *
     * @throws ClassNotFoundException if the class referenced by this type can't be found
     */
    public boolean accessibleTo(final ObjectType accessor) throws ClassNotFoundException {
        final JavaClass jc = Repository.lookupClass(className);
        if (jc.isPublic()) {
            return true;
        }
        final JavaClass acc = Repository.lookupClass(accessor.className);
        return acc.getPackageName().equals(jc.getPackageName());
    }

    /**
     * @return true if both type objects refer to the same class.
     */
    @Override
    public boolean equals(final Object type) {
        return type instanceof ObjectType && ((ObjectType) type).className.equals(className);
    }

    /**
     * @return name of referenced class
     */
    @Override
    public String getClassName() {
        return className;
    }

    /**
     * @return a hash code value for the object.
     */
    @Override
    public int hashCode() {
        return className.hashCode();
    }

    /**
     * If "this" doesn't reference a class, it references an interface or a non-existant entity.
     * @deprecated (since 6.0) this method returns an inaccurate result if the class or interface referenced cannot be
     *             found: use referencesClassExact() instead
     */
    @Deprecated
    public boolean referencesClass() {
        try {
            final JavaClass jc = Repository.lookupClass(className);
            return jc.isClass();
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Return true if this type references a class, false if it references an interface.
     *
     * @return true if the type references a class, false if it references an interface
     * @throws ClassNotFoundException if the class or interface referenced by this type can't be found
     */
    public boolean referencesClassExact() throws ClassNotFoundException {
        final JavaClass jc = Repository.lookupClass(className);
        return jc.isClass();
    }

    /**
     * If "this" doesn't reference an interface, it references a class or a non-existant entity.
     *
     * @deprecated (since 6.0) this method returns an inaccurate result if the class or interface referenced cannot be
     *             found: use referencesInterfaceExact() instead
     */
    @Deprecated
    public boolean referencesInterface() {
        try {
            final JavaClass jc = Repository.lookupClass(className);
            return !jc.isClass();
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Return true if this type references an interface, false if it references a class.
     *
     * @return true if the type references an interface, false if it references a class
     * @throws ClassNotFoundException if the class or interface referenced by this type can't be found
     */
    public boolean referencesInterfaceExact() throws ClassNotFoundException {
        final JavaClass jc = Repository.lookupClass(className);
        return !jc.isClass();
    }

    /**
     * Return true if this type is a subclass of given ObjectType.
     *
     * @throws ClassNotFoundException if any of this class's superclasses can't be found
     */
    public boolean subclassOf(final ObjectType superclass) throws ClassNotFoundException {
        if (referencesInterfaceExact() || superclass.referencesInterfaceExact()) {
            return false;
        }
        return Repository.instanceOf(this.className, superclass.className);
    }
}
