package in.neuw.oauth2.config;

import in.neuw.oauth2.exception.AppBootTimeException;
import in.neuw.oauth2.util.security.SSLContextHelper;
import io.netty.handler.ssl.SslContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Karanbir Singh on 07/19/2020
 */
@ConfigurationProperties(prefix = "oauth2.client")
public class OAuth2ClientSSLPropertiesConfigurer {

    private Map<String, SslConfiguration> registration = new HashMap();

    private Map<String, TrustManagerFactory> trustManagerFactories = new HashMap();

    private Map<String, KeyManagerFactory> keyManagerFactories = new HashMap();

    private Map<String, SslContext> sslContexts = new HashMap();

    public Map<String, SslConfiguration> getRegistration() {
        return registration;
    }

    public Map<String, SslContext> getConstructedSslContexts() {
        return sslContexts;
    }

    @PostConstruct
    public void validate() {
        registration.forEach((k, v) -> {
            validateRegistration(k, v);
        });
    }

    private void validateRegistration(String registrationKey, SslConfiguration registration) {
        if (registration.getSslEnabled() != null && registration.getSslEnabled()) {
            // validate the presence of the other properties
            if (!StringUtils.hasText(registration.getKeystore())) {
                throw new IllegalStateException("keystore must not be empty.");
            }

            if (!StringUtils.hasText(registration.getKeystore())) {
                throw new IllegalStateException("keystore-password must not be empty.");
            }

            if (!StringUtils.hasText(registration.getKeystore())) {
                throw new IllegalStateException("truststore must not be empty.");
            }

            if (!StringUtils.hasText(registration.getKeystore())) {
                throw new IllegalStateException("truststore-password must not be empty.");
            }
            // configure the trustManagerFactories, keyManagerFactories & the sslContexts
            sslContext(registrationKey, registration);
        }
    }

    public static class SslConfiguration {

        private Boolean sslEnabled = false;

        private String keystore;

        private String keystorePassword;

        private String truststore;

        private String truststorePassword;

        public Boolean getSslEnabled() {
            return sslEnabled;
        }

        public void setSslEnabled(Boolean sslEnabled) {
            this.sslEnabled = sslEnabled;
        }

        public String getKeystore() {
            return keystore;
        }

        public void setKeystore(String keystore) {
            this.keystore = keystore;
        }

        public String getKeystorePassword() {
            return keystorePassword;
        }

        public void setKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
        }

        public String getTruststore() {
            return truststore;
        }

        public void setTruststore(String truststore) {
            this.truststore = truststore;
        }

        public String getTruststorePassword() {
            return truststorePassword;
        }

        public void setTruststorePassword(String truststorePassword) {
            this.truststorePassword = truststorePassword;
        }
    }

    private SslContext sslContext(final String registrationKey, final SslConfiguration sslConfiguration) throws AppBootTimeException {

        KeyManagerFactory keyManagerFactory = SSLContextHelper.getKeyStore(registrationKey, sslConfiguration.keystore, sslConfiguration.keystorePassword);
        keyManagerFactories.put(registrationKey, keyManagerFactory);

        TrustManagerFactory trustManagerFactory = SSLContextHelper.getTrustStore(registrationKey, sslConfiguration.truststore, sslConfiguration.truststorePassword);
        trustManagerFactories.put(registrationKey, trustManagerFactory);

        SslContext sslContext = SSLContextHelper.sslContext(keyManagerFactory, trustManagerFactory);
        sslContexts.put(registrationKey, sslContext);
        return sslContext;
    }

}
