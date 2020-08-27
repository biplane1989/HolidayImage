package com.example.holidayimage.core.server.tls;

import android.os.Build;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

public class SSLHelper {

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client, InputStream... certificates) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            TLSSocketFactory tlsSocketFactory = new TLSSocketFactory();

            if (tlsSocketFactory.getTrustManager() != null) {
                client.sslSocketFactory(tlsSocketFactory, tlsSocketFactory.getTrustManager());
            } else {
                client.sslSocketFactory(tlsSocketFactory);
            }
            ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();

            List<ConnectionSpec> specs = new ArrayList<>();
            specs.add(cs);
            specs.add(ConnectionSpec.COMPATIBLE_TLS);
            specs.add(ConnectionSpec.CLEARTEXT);

            client.connectionSpecs(specs);


        }
        return client;
    }
}