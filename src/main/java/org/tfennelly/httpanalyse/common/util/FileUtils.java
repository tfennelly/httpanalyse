/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */
package org.tfennelly.httpanalyse.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * File Utilities.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class FileUtils {

    public static final Set<Character> reservedFilenameChars = CollectionsUtil.toSet('/', '\\', '?', '%', '*', ':', '|', '"', '<', '>', '.', ' ');

    private FileUtils() {
    }

    /**
     * Build a "normalized" file listing of the specified path, relative to the specified rootDirectory.
     * @param path The listing path.
     * @param rootDirectory The relative root directory.
     * @return A list of file paths normalized relative to the specified rootDirectory.
     */
    public static Set<File> normalizedFileList(final File path, final File rootDirectory) {
        final File[] listfiles = path == null ? rootDirectory.listFiles() : path.listFiles();
        if (listfiles == null || listfiles.length == 0) {
            return Collections.emptySet();
        }

        final HashSet<File> filesSet = new LinkedHashSet<File>();
        final String rootPath = rootDirectory.getPath();
        final int rootPathLength = rootDirectory.getPath().length();
        for (File file : listfiles) {
            String filePath = file.getPath();
            filePath = filePath.substring(filePath.indexOf(rootPath) + rootPathLength + 1);
            filesSet.add(new File(filePath));
        }
        return filesSet;
    }

    public static String toFileName(String string) {
        StringBuilder unameFileBuilder = new StringBuilder();

        int usernameLen = string.length();
        for (int i = 0; i < usernameLen; i++) {
            char c = string.charAt(i);

            if (reservedFilenameChars.contains(c)) {
                unameFileBuilder.append("_" + (int)c + "_");
            } else {
                unameFileBuilder.append(c);
            }
        }

        return unameFileBuilder.toString();
    }

    public static List<File> tokenize(File path) {
        List<File> tokens = new ArrayList<File>();
        File token = path;

        while (token != null) {
            tokens.add(0, token);
            token = token.getParentFile();
        }

        return tokens;
    }

    public static byte[] read(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            return StreamUtils.readStream(fileInputStream);
        } finally {
            fileInputStream.close();
        }
    }

    public static void write(byte[] data, File file) throws IOException {
        file.getParentFile().mkdirs();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            fileOutputStream.write(data);
            fileOutputStream.flush();
        } finally {
            fileOutputStream.close();
        }
    }

    public static boolean delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subfile : files) {
                delete(subfile);
            }
        }

        return file.delete();
    }

    public static String addTrailingSlash(String path) {
        if (path == null) {
            return null;
        }

        if (!path.endsWith("/")) {
            return path + "/";
        } else {
            return path;
        }
    }

    public static void move(File from, File to) throws IOException {
        if (from.isFile()) {
            File toTargetFile = to;
            if (to.isFile() && to.exists()) {
                to.delete();
                toTargetFile = to;
            } else if (to.isDirectory()) {
                toTargetFile = new File(to, from.getName());
            }

            toTargetFile.getParentFile().mkdirs();
            from.renameTo(toTargetFile);

            // If renameTo is not supported, then physically "move" the file data...
            if (from.exists() && !toTargetFile.exists()) {
                copy(from, toTargetFile);
                from.delete();
            }
        } else if (from.isDirectory()) {
            if (!to.exists()) {
                to.mkdirs();
            }

            if (to.isDirectory()) {
                String fromDirAbsPath = from.getAbsolutePath();
                for (File fromFile : from.listFiles()) {
                    String relativePath = fromFile.getAbsolutePath().substring(fromDirAbsPath.length());
                    File moveTo = new File(to, relativePath);

                    move(fromFile, moveTo);
                    FileUtils.delete(fromFile);
                }
            }
        }
    }

    public static void copy(File from, File to) throws IOException {
        byte[] fromBytes = read(from);
        write(fromBytes, to);
    }

    public static void sortByDateASC(File[] files) {
	Arrays.sort(files, new SortASCComparator());
    }

    public static void sortByDateDEC(File[] files) {
	Arrays.sort(files, new SortDECComparator());
    }

    private static class SortASCComparator implements Comparator<File> {
	@Override
	public int compare(File file1, File file2) {
	    return (int) (file1.lastModified() - file2.lastModified());
	}
    }

    private static class SortDECComparator implements Comparator<File> {
	@Override
	public int compare(File file1, File file2) {
	    return (int) (file2.lastModified() - file1.lastModified());
	}
    }
}
