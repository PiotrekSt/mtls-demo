package com.piotrekst.client;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyStore;

@Configuration
class ClientInjector {

    @Value("classpath:clientkeystore.p12")
    private Resource keyStoreResource;

    @Value("${key-store-password}")
    private String keyStorePassword;

    @Value("classpath:clienttruststore.jks")
    private Resource trustStoreResource;

    @Value("${trust-store-password}")
    private String trustStorePassword;

    @Bean("externalServiceRestTemplate")
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .requestFactory(this::requestFactory)
                .build();
    }

    private ClientHttpRequestFactory requestFactory() {
        try {
            final KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(keyStoreResource.getInputStream(), keyStorePassword.toCharArray());
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(trustStoreResource.getInputStream(), trustStorePassword.toCharArray());

            final SSLConnectionSocketFactory socketFactory =
                    new SSLConnectionSocketFactory(
                            new SSLContextBuilder()
                                    .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                                    .build(),
                            new String[]{"TLSv1.2", "TLSv1.3"},
                            null,
                            NoopHostnameVerifier.INSTANCE);

            final HttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(socketFactory)
                    .build();
            final ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            return requestFactory;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create client request factory");
        }
    }
}
