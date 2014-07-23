/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */
package org.tfennelly.httpanalyse.common;

import org.tfennelly.httpanalyse.common.security.PrivateX509TrustManager;
import org.tfennelly.httpanalyse.common.security.TrustManagerUtil;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A concrete implementation of {@link HttpClient} which uses the jdk
 * {@link java.net.HttpURLConnection} perform post, get, update, and delete operations.
 *
 * @author <a href="mailto:daniel.bevenius@gmail.com">daniel.bevenius@gmail.com</a>
 */
public class HttpClientImpl implements HttpClient {

    private SSLSocketFactory secureSocketFactory;
    private String basicAuthCredentials;

    public void setBasicAuthCredentials(String basicAuthCredentials) {
        this.basicAuthCredentials = basicAuthCredentials;
    }

    /* (non-Javadoc)
    * @see com.foxweave.http.HttpClient#post(byte[], java.lang.String, java.net.URL)
    */
    public HttpResponse post(final byte[] content, final String contentType, final URL url) throws MalformedURLException, IOException, ProtocolException {
        assertUrlAndContentType(url, contentType);
        return send("POST", content, contentType, url);
    }

    public HttpResponse post(final byte[] content, final String contentType, final URL url, final Map<String, String> headers) throws MalformedURLException, IOException, ProtocolException {
        assertUrlAndContentType(url, contentType);
        return send("POST", content, contentType, url, headers);
    }

    public HttpResponse put(final byte[] content, final String contentType, final URL url) throws MalformedURLException, IOException, ProtocolException {
        assertUrlAndContentType(url, contentType);
        return send("PUT", content, contentType, url);
    }

    /* (non-Javadoc)
    * @see com.foxweave.http.HttpClient#get(java.net.URL)
    */
    public HttpResponse get(final URL url) throws IOException {
        return send("GET", null, null, url);
    }

    /* (non-Javadoc)
     * @see com.foxweave.http.HttpClient#delete(java.net.URL)
     */
    public HttpResponse delete(URL url) throws Exception {
        return send("DELETE", null, null, url);
    }

    public void setTrustedCert(final X509Certificate trustedCert) throws GeneralSecurityException {
        TrustManager[] trustManagers = TrustManagerUtil.getDefaultTrustManagersWithX509Fallback(new PrivateX509TrustManager(trustedCert));

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustManagers, new java.security.SecureRandom());
        secureSocketFactory = sc.getSocketFactory();
    }

    private void assertUrlAndContentType(final URL url, final String contentType) {
    }

    private HttpResponse send(final String method, final byte[] content, final String contentType, final URL url) throws IOException {
        final Map<String, String> headers = Collections.emptyMap();
        return send(method, content, contentType, url, headers);
    }

    public HttpResponse send(final String method, final byte[] content, final String contentType, final URL url, final Map<String, String> headers) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        if (secureSocketFactory != null && conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setSSLSocketFactory(secureSocketFactory);
        }

        try {
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            for (Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            if (contentType != null) {
                conn.setRequestProperty("Content-Type", contentType);
            }
            if (content != null && (method.equals("POST") || method.equals("PUT"))) {
                conn.setRequestProperty("Content-Length", String.valueOf(content.length));
                final OutputStream os = conn.getOutputStream();
                try {
                    os.write(content);
                    os.flush();
                } finally {
                    os.close();
                }
            }

            return new HttpResponseImpl(conn);
        } finally {
            conn.disconnect();
        }
    }
}
