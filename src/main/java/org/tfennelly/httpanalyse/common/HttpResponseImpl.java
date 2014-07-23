/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */
package org.tfennelly.httpanalyse.common;

import org.tfennelly.httpanalyse.common.util.StreamUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;

/**
 * A concrete implementation of {@link HttpResponse}.
 * 
 * @author <a href="mailto:daniel.bevenius@gmail.com">daniel.bevenius@gmail.com</a>
 *
 */
public class HttpResponseImpl implements HttpResponse {

    private final int statusCode;
    private String statusMessage;
    private byte[] content;
    private String contentType;
    private Map<String, List<String>> headerFields;
    private Certificate[] serverCerts;

    /**
     * Sole constructors that collects information from the provided {@link java.net.HttpURLConnection}
     * and populates this instance.
     *
     * The {@link java.net.HttpURLConnection} is not stored and only read by this object instance.
     *
     * @param con the {@link java.net.HttpURLConnection} to retrieve information from.
     * @throws java.io.IOException If an error occurs while retrieving information from {@link java.net.HttpURLConnection}.
     */
    public HttpResponseImpl(final HttpURLConnection con) throws IOException {
        con.connect();
        statusCode = con.getResponseCode();
        statusMessage = con.getResponseMessage();
        headerFields = con.getHeaderFields();
        content = getContent(con);
        contentType = con.getContentType();
    }
    
    /* (non-Javadoc)
     * @see com.foxweave.http.HttpResponse#statusCode()
     */
    public int statusCode() {
        return statusCode;
    }

    /* (non-Javadoc)
     * @see com.foxweave.http.HttpResponse#statusMessage()
     */
    public String statusMessage() {
        return statusMessage;
    }

    /* (non-Javadoc)
     * @see com.foxweave.http.HttpResponse#content()
     */
    public byte[] content() {
        return content;
    }
    
    @Override
    public boolean hasContent() {
        return content != null && content.length != 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public String contentType() {
        return contentType;
    }

    /* (non-Javadoc)
    * @see com.foxweave.http.HttpResponse#headerFields()
    */
    public Map<String, List<String>> headerFields() {
        return headerFields;
    }

    private byte[] getContent(final HttpURLConnection con) throws IOException {
        byte[] content = null;

        try {
            InputStream inputStream = con.getInputStream();

            if (inputStream != null) {
                try {
                    content = StreamUtils.readStream(inputStream);
                    if (statusCode >= 200 && statusCode < 300 && con instanceof HttpsURLConnection) {
                        try {
                            serverCerts = ((HttpsURLConnection) con).getServerCertificates();
                        } catch (Exception e) {
                            // This happens on  the Linux JVM on Rackspace... need to figure out why!!!
                            System.out.println("Failed to get Server Certs from HTTPS connection.");
                            e.printStackTrace();
                        }
                    }
                } finally {
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            // No content...
        }

        return content;
    }

    public Certificate[] getServerCerts() {
        return serverCerts;
    }

    @Override
    public String contentAsString() {
        if (content != null) {
            try {
                return new String(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }
}
