/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

package org.tfennelly.httpanalyse.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Properties utilities.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class PropertiesUtil {

    private static Charset utf8Charset = Charset.forName("UTF-8");

    private PropertiesUtil() {
    }

    public static Properties newProperties(File propertiesFile) throws IOException {
        InputStream propertyStream = new FileInputStream(propertiesFile);
        try {
            return newProperties(propertyStream);
        } finally {
            propertyStream.close();
        }
    }

    public static Properties newProperties(InputStream propertyStream) throws IOException {
        return newProperties(new InputStreamReader(propertyStream, utf8Charset));
    }

    public static Properties newProperties(Reader propertyStream) throws IOException {
        Properties properties = new Properties();
        properties.load(propertyStream);
        return properties;
    }

    public static Properties newProperties(String properties) throws IOException {
        return newProperties(new StringReader(properties));
    }

    public static Properties newProperties(byte[] bytes) throws IOException {
        return newProperties(new ByteArrayInputStream(bytes));
    }

    public static void storeProperties(Properties properties, File file, String comments) throws IOException {
        if (file.isDirectory()) {
            throw new IllegalStateException("Cannot persist Properties to file '" + file.getAbsolutePath() + "'.  It is a directory.");
        }

        // create the dir structure for the file...
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Writer fileWriter = new OutputStreamWriter(fileOutputStream, utf8Charset);

        try {
            properties.store(fileWriter, comments);
        } finally {
            StreamUtils.safeClose(fileWriter);
        }
    }

    public static String propertiesToString(Properties properties, String comments) {
        StringWriter writer = new StringWriter();

        try {
            properties.store(writer, comments);
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected exception writing Properties to a StringWriter.", e);
        } finally {
            StreamUtils.safeClose(writer);
        }
    }

    public static Properties fromSystemProperties(String... propertyNames) {
        Properties properties = new Properties();
        for (String propertyName : propertyNames) {
            String value = System.getProperty(propertyName);
            if (value != null){
                properties.setProperty(propertyName, value);
            }
        }
        return properties;
    }
}
