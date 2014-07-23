/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

package org.tfennelly.httpanalyse.common.util;

import java.nio.charset.Charset;

/**
 * Character set utilities.
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class CharsetUtils {

    public static final Charset UTF8 = Charset.forName("UTF-8");

    private CharsetUtils() {
    }
}
