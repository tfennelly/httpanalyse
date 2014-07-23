/*
	Milyn - Copyright (C) 2006 - 2010

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License (version 2.1) as published by the Free Software
	Foundation.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	See the GNU Lesser General Public License for more details:
	http://www.gnu.org/licenses/lgpl.txt
*/
package org.tfennelly.httpanalyse.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility methods to aid in class/resource loading.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class ClasspathUtil {

    private static final Map<String, Class> primitives;

    static {
        primitives = new HashMap<String, Class>();
        primitives.put("int", Integer.TYPE);
        primitives.put("long", Long.TYPE);
        primitives.put("boolean", Boolean.TYPE);
        primitives.put("float", Float.TYPE);
        primitives.put("double", Double.TYPE);
        primitives.put("char", Character.TYPE);
        primitives.put("byte", Byte.TYPE);
        primitives.put("short", Short.TYPE);
    }

    /**
     * Load the specified class.
     *
     * @param className The name of the class to load.
     * @param caller    The class of the caller.
     * @return The specified class.
     * @throws ClassNotFoundException If the class cannot be found.
     */
    public static Class forName(final String className, final Class caller) throws ClassNotFoundException {
        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();

        Class primitiveClass = primitives.get(className);
        if (primitiveClass != null) {
            return primitiveClass;
        }

        if (threadClassLoader != null) {
            try {
                return threadClassLoader.loadClass(className);
            } catch (final ClassNotFoundException cnfe) {
            } // ignore
        }

        ClassLoader classLoader = caller.getClassLoader();
        if (classLoader != null) {
            try {
                return classLoader.loadClass(className);
            } catch (final ClassNotFoundException cnfe) {
            } // ignore
        }

        return Class.forName(className, true, ClassLoader.getSystemClassLoader());
    }

    /**
     * Get the specified resource as a stream.
     *
     * @param resourceName The name of the class to load.
     * @param caller       The class of the caller.
     * @return The input stream for the resource or null if not found.
     */
    public static InputStream getResourceAsStream(final String resourceName, final Class caller) {
        final String resource;

        if (!resourceName.startsWith("/")) {
            final Package callerPackage = caller.getPackage();
            if (callerPackage != null) {
                resource = callerPackage.getName().replace('.', '/') + '/'
                        + resourceName;
            } else {
                resource = resourceName;
            }

            return getResourceAsStream(resource, caller.getClassLoader());
        } else {
            return getResourceAsStream(resourceName, caller.getClassLoader());
        }
    }

    /**
     * Get the specified resource as a stream.
     *
     * @param resourceName The name of the class to load.
     * @param classLoader  The ClassLoader to use, if the resource is not located via the
     *                     Thread context ClassLoader.
     * @return The input stream for the resource or null if not found.
     */
    public static InputStream getResourceAsStream(final String resourceName, final ClassLoader classLoader) {
        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        final String resource;

        if (resourceName.startsWith("/")) {
            resource = resourceName.substring(1);
        } else {
            resource = resourceName;
        }

        if (threadClassLoader != null) {
            final InputStream is = threadClassLoader.getResourceAsStream(resource);
            if (is != null) {
                return is;
            }
        }

        if (classLoader != null) {
            final InputStream is = classLoader.getResourceAsStream(resource);
            if (is != null) {
                return is;
            }
        }

        return ClassLoader.getSystemResourceAsStream(resource);
    }

    public static List<URL> getResources(String resourcePath, Class<?> caller) throws IOException {
        return getResources(resourcePath, caller.getClassLoader());
    }

    public static List<URL> getResources(String resourcePath, ClassLoader callerClassLoader) throws IOException {
        Set<URL> resources = new LinkedHashSet<URL>();

        if (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            resources.addAll(CollectionsUtil.toList(contextClassLoader.getResources(resourcePath)));
        }

        if (callerClassLoader != null) {
            resources.addAll(CollectionsUtil.toList(callerClassLoader.getResources(resourcePath)));
        }

        return new ArrayList<URL>(resources);
    }
}
