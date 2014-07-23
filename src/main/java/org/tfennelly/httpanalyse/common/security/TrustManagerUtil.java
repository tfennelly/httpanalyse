/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

package org.tfennelly.httpanalyse.common.security;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 */
public class TrustManagerUtil {

    private TrustManagerUtil() {
    }

    public static TrustManager[] getDefaultTrustManagers() throws GeneralSecurityException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore)null);
        return trustManagerFactory.getTrustManagers();
    }

    public static TrustManager[] getDefaultTrustManagersWithX509Fallback(final X509TrustManager x509Fallback) throws GeneralSecurityException {
        TrustManager[] defaultTrustManagers = getDefaultTrustManagers();
        int defaultTrustManagerMatchIndex = -1;

        for (int i = 0; i < defaultTrustManagers.length; i++) {
            if (defaultTrustManagers[i] instanceof X509TrustManager) {
                defaultTrustManagerMatchIndex = i;
                break;
            }
        }

        final TrustManager[] clonedTrustManagers;

        if (defaultTrustManagerMatchIndex != -1) {
            // OK, so there is a default trust manager of this type... we need to wrap both
            // the existing default and the supplied x509Fallback together so
            clonedTrustManagers = new TrustManager[defaultTrustManagers.length];
            System.arraycopy(defaultTrustManagers, 0, clonedTrustManagers, 0, defaultTrustManagers.length);

            final X509TrustManager defaultX509TrustManager = (X509TrustManager) clonedTrustManagers[defaultTrustManagerMatchIndex];
            X509TrustManager wrapperTrustManager = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    try {
                        defaultX509TrustManager.checkClientTrusted(x509Certificates, s);
                    } catch (CertificateException e) {
                        x509Fallback.checkClientTrusted (x509Certificates, s);
                    }
                }
                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    try {
                        defaultX509TrustManager.checkServerTrusted(x509Certificates, s);
                    } catch (CertificateException e) {
                        x509Fallback.checkServerTrusted(x509Certificates, s);
                    }
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // Only using default for this....
                    return defaultX509TrustManager.getAcceptedIssuers();
                }
            };
            clonedTrustManagers[defaultTrustManagerMatchIndex] = wrapperTrustManager;
        } else {
            // There isn't a TrustManager of this type, so just add it onto the list....
            clonedTrustManagers = new TrustManager[defaultTrustManagers.length + 1];
            System.arraycopy(defaultTrustManagers, 0, clonedTrustManagers, 0, defaultTrustManagers.length);
            clonedTrustManagers[clonedTrustManagers.length - 1] = x509Fallback;
        }

        return clonedTrustManagers;
    }
}
