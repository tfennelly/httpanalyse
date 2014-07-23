package org.tfennelly.httpanalyse;

import org.tfennelly.httpanalyse.common.HttpClientImpl;
import org.tfennelly.httpanalyse.common.HttpResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * SSL Analyse
 */
public class SSLAnalyse {

    public static void main(String[] args) throws IOException {
        HttpClientImpl httpClient = new HttpClientImpl();
        HttpResponse response = httpClient.get(new URL("https://appservice.foxweave.io?operation=getTargets&com.foxweave.servlet.login.Login=cloudbees_tomfennelly"));

        System.out.println(response.statusCode());
        System.out.println(response.contentAsString());
    }
}
