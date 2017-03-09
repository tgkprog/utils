package org.s2n.ddt.util.https;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

//TODO support real key stores, right now accepts all
public class UtlTrustManager implements X509TrustManager {

	private KeyStore ks;

	public UtlTrustManager(KeyStore ks) {
		this.ks = ks;
	}

	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		if (ks == null) {
			return null;
		}

		// TODO return ks.
		return null;
	}
}
