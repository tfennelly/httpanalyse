/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

package org.tfennelly.httpanalyse.common.security;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 */
public class PrivateX509TrustManager implements X509TrustManager {

    private X509Certificate cerverCert;

    public PrivateX509TrustManager(X509Certificate trustedCert) {
        this.cerverCert = trustedCert;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        if (!x509Certificates[0].equals(cerverCert)) {
            throw new CertificateException("Untrusted Client certificate: " + x509Certificates[0].getSubjectDN());
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        if (!x509Certificates[0].equals(cerverCert)) {
            throw new CertificateException("Untrusted Server certificate: " + x509Certificates[0].getSubjectDN());
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
