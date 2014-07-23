/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */
package org.tfennelly.httpanalyse.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * Argument assertion utilities.
 *
 * @author tfennelly
 */
public abstract class AssertArgument {

	/**
	 * Assert that the argument is not null.
	 *
	 * @param arg
	 *            Argument.
	 * @param argName
	 *            Argument name.
	 * @throws IllegalArgumentException
	 *             Argument is null.
	 */
	public static void isNotNull(Object arg, String argName)
			throws IllegalArgumentException {
		if (arg == null) {
			throw new IllegalArgumentException("null '" + argName
					+ "' arg in method call.");
		}
	}

	/**
	 * Assert that the argument is not empty.
	 *
	 * @param arg
	 *            Argument.
	 * @param argName
	 *            Argument name.
	 * @throws IllegalArgumentException
	 *             Argument is not null, but is empty.
	 */
	public static void isNotEmpty(String arg, String argName)
			throws IllegalArgumentException {
		if (arg != null && arg.trim().length() == 0) {
			throw new IllegalArgumentException("Not null, but empty '"
					+ argName + "' arg in method call.");
		}
	}

	/**
	 * Assert that the argument is neither null nor empty.
	 *
	 * @param arg
	 *            Argument.
	 * @param argName
	 *            Argument name.
	 * @throws IllegalArgumentException
	 *             Argument is null or empty.
	 */
	public static void isNotNullAndNotEmpty(String arg, String argName)
			throws IllegalArgumentException {
		if (arg == null || arg.trim().length() == 0) {
			throw new IllegalArgumentException("null or empty '" + argName
					+ "' arg in method call.");
		}
	}

	/**
	 * Assert that the argument is neither null nor empty.
	 *
	 * @param arg Argument.
	 * @param argName Argument name.
	 * @throws IllegalArgumentException Argument is null or empty.
	 */
	public static void isNotNullAndNotEmpty(Object[] arg, String argName) throws IllegalArgumentException {
		if (arg == null || arg.length == 0) {
			throw new IllegalArgumentException("null or empty '" + argName + "' arg in method call.");
		}
	}

	/**
	 * Assert that the argument is neither null nor empty.
	 *
	 * @param arg Argument.
	 * @param argName Argument name.
	 * @throws IllegalArgumentException Argument is null or empty.
	 */
	public static void isNotNullAndNotEmpty(Collection<?> arg, String argName) throws IllegalArgumentException {
		if (arg == null || arg.isEmpty()) {
			throw new IllegalArgumentException("null or empty '" + argName + "' arg in method call.");
		}
	}

	/**
	 * Assert that the argument is neither null nor empty.
	 *
	 * @param arg Argument.
	 * @param argName Argument name.
	 * @throws IllegalArgumentException Argument is null or empty.
	 */
	public static void isNotNullAndNotEmpty(Map<?, ?> arg, String argName) throws IllegalArgumentException {
		if (arg == null || arg.isEmpty()) {
			throw new IllegalArgumentException("null or empty '" + argName + "' arg in method call.");
		}
	}

	/**
	 * Assert that the argument is an instance of the specified class.
	 *
	 * @param arg Argument.
	 * @param clazz The Class type to check.
	 * @param argName Argument name.
	 * @throws IllegalArgumentException Argument is null or empty.
	 */
	public static void isInstanceOf(Object arg, Class clazz, String argName) throws IllegalArgumentException {
		if (clazz.isAssignableFrom(arg.getClass())) {
			throw new IllegalArgumentException("Argument '" + argName + "' is not an instance of '" + clazz.getName() + "'.");
		}
	}
}
