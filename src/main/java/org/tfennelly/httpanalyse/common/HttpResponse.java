/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */
package org.tfennelly.httpanalyse.common;

import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;

/**
 * Represents a response populated with information from a {@link java.net.HttpURLConnection}.
 *
 * @author <a href="mailto:daniel.bevenius@gmail.com">daniel.bevenius@gmail.com</a>
 */
public interface HttpResponse {

    /**
     * The {@link java.net.HttpURLConnection} status returned from a connection.
     *
     * @return {@code int} the status code of the connection.
     */
    int statusCode();

    /**
     * The response status message from the {@link java.net.HttpURLConnection}.
     *
     * @return {@code String} the status message.
     */
    String statusMessage();

    /**
     * Get the returned content, if any, from the {@link java.net.HttpURLConnection}.
     *
     * @return {@code byte} the content returned as a byte[]. Can be null if nothing was retured.
     */
    byte[] content();

    /**
     * Determines if this response contains any content.
     *
     * @return {@code true} if this response content is null or empty.
     */
    boolean hasContent();

    /**
     * Get the content type of the returned content.
     *
     * @return The content type of the returned content.
     */
    String contentType();

    /**
     * Gets the header fields from the {@link java.net.HttpURLConnection}.
     * 
     * @return {@code Map<String, List<String>>} A map keyed by the header name and a list of the values
     * set for that header name.
     */
    Map<String, List<String>> headerFields();

    /**
     * Get the server certificates.
     * <p/>
     * Only applies to https.
     * @return The server certificates, or null if it's not https.
     */
    Certificate[] getServerCerts();

    /**
     * Get the response content as a String.
     * @return The response content as a String, or null if there is no response content.
     */
    String contentAsString();
}