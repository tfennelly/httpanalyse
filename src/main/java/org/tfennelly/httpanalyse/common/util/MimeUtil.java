/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */
package org.tfennelly.httpanalyse.common.util;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class MimeUtil {

    private static final Set<String> XML_MIME_TYPE = CollectionsUtil.toSet("text/xml", "application/xml", "application/soap+xml", "text/html");
    private static final Set<String> JSON_MIME_TYPE = CollectionsUtil.toSet("application/json");
    private static final Set<String> HTML_MIME_TYPE = CollectionsUtil.toSet("text/html");
    private static final Set<String> TEXT_MIME_TYPE = new HashSet<String>();

    static {
        TEXT_MIME_TYPE.addAll(XML_MIME_TYPE);
        TEXT_MIME_TYPE.addAll(JSON_MIME_TYPE);
        TEXT_MIME_TYPE.addAll(HTML_MIME_TYPE);
    }

    private MimeUtil() {
    }

    public static boolean isXML(String type) {
        return XML_MIME_TYPE.contains(type);
    }

    public static boolean isJSON(String type) {
        return JSON_MIME_TYPE.contains(type);
    }

    public static boolean isHTML(String type) {
        return HTML_MIME_TYPE.contains(type);
    }

    public static boolean isText(String type) {
        return TEXT_MIME_TYPE.contains(type);
    }
}
