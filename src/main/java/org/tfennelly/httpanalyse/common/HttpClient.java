/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */
package org.tfennelly.httpanalyse.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public interface HttpClient {

    public abstract HttpResponse post(final byte[] content, final String contentType, final URL url)
            throws MalformedURLException, IOException, ProtocolException;
    
    public abstract HttpResponse post(final byte[] content, final String contentType, final URL url,
                                      final Map<String, String> headers)
            throws MalformedURLException, IOException, ProtocolException;
    
    public abstract HttpResponse put(final byte[] content, final String contentType, final URL url)
            throws MalformedURLException, IOException, ProtocolException;

    public abstract HttpResponse get(final URL url) throws Exception;

    public abstract HttpResponse delete(URL url) throws Exception;

}