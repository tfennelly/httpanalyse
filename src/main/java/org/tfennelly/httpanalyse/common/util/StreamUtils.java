/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

package org.tfennelly.httpanalyse.common.util;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * Stream Utilities.
 *
 * @author tfennelly
 */
public abstract class StreamUtils {

    private StreamUtils() {
    }

    /**
	 * Read the supplied InputStream and return as a byte array.
	 *
	 * @param stream
	 *            The stream to read.
	 * @return byte array containing the Stream data.
	 * @throws java.io.IOException
	 *             Exception reading from the stream.
	 */
	public static byte[] readStream(InputStream stream) throws IOException {
        AssertArgument.isNotNull(stream, "stream");

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		byte[] byteBuf = new byte[1024];
		int readCount = 0;

		while ((readCount = stream.read(byteBuf)) != -1) {
			bytesOut.write(byteBuf, 0, readCount);
		}

		return bytesOut.toByteArray();
	}

    /**
     * Read the supplied InputStream and return as a byte array.
     *
     * @param stream
     *            The stream to read.
     * @return A String containing the Stream data.
     * @throws java.io.IOException
     *             Exception reading from the stream.
     */
    public static String readStreamAsString(InputStream stream) throws IOException {
        AssertArgument.isNotNull(stream, "stream");

        return new String(readStream(stream), CharsetUtils.UTF8);
    }

    public static String readStream(Reader stream) throws IOException {
        AssertArgument.isNotNull(stream, "stream");

        StringBuffer streamString = new StringBuffer();
        char[] readBuffer = new char[256];
        int readCount = 0;

        while ((readCount = stream.read(readBuffer)) != -1) {
            streamString.append(readBuffer, 0, readCount);
        }

        return streamString.toString();
    }

    /**
     * Compares the 2 streams.
     * <p/>
     * Calls {@link #trimLines(java.io.InputStream)} on each stream before comparing.
     * @param s1 Stream 1.
     * @param s2 Stream 2.
     * @return True if the streams are equal not including leading and trailing
     * whitespace on each line and blank lines, otherwise returns false.
     */
    public static boolean compareCharStreams(InputStream s1, InputStream s2) {
        StringBuffer s1Buf, s2Buf;

        try {
            s1Buf = trimLines(s1);
            s2Buf = trimLines(s2);

            return s1Buf.toString().equals(s2Buf.toString());
        } catch (IOException e) {
            // fail the comparison
        }

        return false;
    }

    /**
     * Compares the 2 streams.
     * <p/>
     * Calls {@link #trimLines(java.io.Reader)} on each stream before comparing.
     * @param s1 Stream 1.
     * @param s2 Stream 2.
     * @return True if the streams are equal not including leading and trailing
     * whitespace on each line and blank lines, otherwise returns false.
     */
    public static boolean compareCharStreams(Reader s1, Reader s2) {
        StringBuffer s1Buf, s2Buf;

        try {
            s1Buf = trimLines(s1);
            s2Buf = trimLines(s2);

            return s1Buf.toString().equals(s2Buf.toString());
        } catch (IOException e) {
            // fail the comparison
        }

        return false;
    }


    /**
     * Compares the 2 streams.
     * <p/>
     * Calls {@link #trimLines(java.io.Reader)} on each stream before comparing.
     * @param s1 Stream 1.
     * @param s2 Stream 2.
     * @return True if the streams are equal not including leading and trailing
     * whitespace on each line and blank lines, otherwise returns false.
     */
    public static boolean compareCharStreams(String s1, String s2) {
        return compareCharStreams(new StringReader(s1), new StringReader(s2));
    }

    /**
     * Read the lines lines of characters from the stream and trim each line
     * i.e. remove all leading and trailing whitespace.
     * @param charStream Character stream.
     * @return StringBuffer containing the line trimmed stream.
     * @throws java.io.IOException
     */
    public static StringBuffer trimLines(Reader charStream) throws IOException {
        StringBuffer stringBuf = new StringBuffer();
        BufferedReader reader = new BufferedReader(charStream);
        String line;

        while((line = reader.readLine()) != null) {
            stringBuf.append(line.trim());
        }

        return stringBuf;
    }

    /**
     * Read the lines lines of characters from the supplied string, trim each line (optional)
     * and add a single newline character.
     * @param string The String.
     * @param trim Trim each line i.e. to ignore leading and trailing whitespace.
     * @return String containing the line trimmed stream.
     * @throws java.io.IOException
     */
    public static String normalizeLines(String string, boolean trim) throws IOException {
	return normalizeLines(new StringReader(string), trim);
    }

    /**
     * Read the lines lines of characters from the stream, trim each line (optional)
     * and add a single newline character.
     * @param charStream Character stream.
     * @param trim Trim each line i.e. to ignore leading and trailing whitespace.
     * @return String containing the line trimmed stream.
     * @throws java.io.IOException
     */
    public static String normalizeLines(Reader charStream, boolean trim) throws IOException {
        StringBuffer stringBuf = new StringBuffer();
        BufferedReader reader = new BufferedReader(charStream);
        String line;

        while((line = reader.readLine()) != null) {
            if(trim) {
                stringBuf.append(line.trim());
            } else {
                stringBuf.append(line);
            }
            stringBuf.append('\n');
        }

        return stringBuf.toString();
    }

    /**
     * Read the lines lines of characters from the stream and trim each line
     * i.e. remove all leading and trailing whitespace.
     * @param charStream Character stream.
     * @return StringBuffer containing the line trimmed stream.
     * @throws java.io.IOException
     */
    public static StringBuffer trimLines(InputStream charStream) throws IOException {
        return trimLines(new InputStreamReader(charStream, "UTF-8"));
    }

    /**
     * Read the lines lines of characters from the stream and trim each line
     * i.e. remove all leading and trailing whitespace.
     * @param charStream Character stream.
     * @return String containing the line trimmed stream.
     * @throws java.io.IOException
     */
    public static String trimLines(String charStream) throws IOException {
        return trimLines(new StringReader(charStream)).toString();
    }

    /**
     * Close the {@link java.io.Closeable}.
     * <p/>
     * Also flushes if it's {@link java.io.Flushable}, before closing.
     *
     * @param closeable The closeable object.
     */
    public static void safeClose(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        if (closeable instanceof Flushable) {
            try {
                ((Flushable) closeable).flush();
            } catch (Throwable t) {
            }
        }
        try {
            closeable.close();
        } catch (Throwable t) {
        }
    }
}
