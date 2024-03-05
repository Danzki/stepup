package com.danzki.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
public class CustomRestTemplateConfiguration {
   @Value("${server.ssl.trust-store}")
   private Resource trustStore;
   @Value("${server.ssl.trust-store-password}")
   private String trustStorePassword;

   @Bean
   public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, MalformedURLException, IOException {
      SSLContext sslContext = (new SSLContextBuilder()).loadTrustMaterial(this.trustStore.getURL(), this.trustStorePassword.toCharArray()).build();
      SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
      HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(sslConFactory).build();
      CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
      ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
      return new RestTemplate(requestFactory);
   }
}
