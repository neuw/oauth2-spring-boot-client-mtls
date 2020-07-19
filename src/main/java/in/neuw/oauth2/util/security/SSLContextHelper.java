package in.neuw.oauth2.util.security;

import in.neuw.oauth2.exception.AppBootTimeException;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.FingerprintTrustManagerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;

/**
 * @author Karanbir Singh on 07/19/2020
 */
public class SSLContextHelper {

    public static SslContext sslContext(final KeyManagerFactory keyManagerFactory,
                                        final TrustManagerFactory trustManagerFactory) throws AppBootTimeException {
        try {
            SslContext sslContext = SslContextBuilder.forClient()
                    .clientAuth(ClientAuth.REQUIRE)
                    .keyManager(keyManagerFactory)
                    // the following line is not recommended and commented out
                    // .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .trustManager(trustManagerFactory)
                    .build();
            return sslContext;
        } catch (SSLException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        }
    }

    public static KeyManagerFactory getKeyStore(final String keystoreContent,
                                                final String keyStorePassword) throws AppBootTimeException {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(keystoreContent));
            keyStore.load(inputStream, keyStorePassword.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, keyStorePassword.toCharArray());
            return kmf;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        } catch (CertificateException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        }
    }

    /*
     * Create the Trust Store.
     */
    public static TrustManagerFactory getTrustStore(final String truststoreContent,
                                                    final String trustStorePassword) throws AppBootTimeException {
        try {
            KeyStore trustStore = KeyStore.getInstance("JKS");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(truststoreContent));
            trustStore.load(inputStream, trustStorePassword.toCharArray());
            TrustManagerFactory tmf = FingerprintTrustManagerFactory.getInstance(FingerprintTrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            return tmf;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        } catch (CertificateException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppBootTimeException(e.getMessage(), e);
        }
    }

}
