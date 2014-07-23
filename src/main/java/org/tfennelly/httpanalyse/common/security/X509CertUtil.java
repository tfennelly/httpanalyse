/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

package org.tfennelly.httpanalyse.common.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class X509CertUtil {

    public static X509Certificate loadCertificate(InputStream certStream) throws IOException, CertificateException {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(certStream);
        } finally {
            certStream.close();
        }
    }
}
