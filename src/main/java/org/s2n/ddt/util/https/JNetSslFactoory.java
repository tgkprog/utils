package org.s2n.ddt.util.https;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;

import javax.net.ssl.SSLSocketFactory;

;

public class JNetSslFactoory extends SSLSocketFactory {
	private SSLContext sslContext = null;
	private SSLSocketFactory fact = null;

	public JNetSslFactoory() {

	}

	public JNetSslFactoory(KeyStore truststore, boolean acceptAll) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException,
			UnrecoverableKeyException {
		super();
		sslContext = SSLContext.getInstance("TLS");
		UtlTrustManager tm = new UtlTrustManager(null);
		

		sslContext.init(null, new TrustManager[] { tm }, null);
		fact = (SSLSocketFactory) SSLSocketFactory.getDefault();

		if (acceptAll) {
			// Object aa;
			// setHostnameVerifier(aa);
		}
	}

	public Socket createSocket(Socket sock, String arg1, int arg2, boolean arg3) throws IOException {

		return fact.createSocket(sock, arg1, arg2, arg3);
	}

	public String[] getDefaultCipherSuites() {

		return fact.getDefaultCipherSuites();
	}

	public String[] getSupportedCipherSuites() {
		return fact.getSupportedCipherSuites();
	}

	public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException {
		return fact.createSocket(arg0, arg1);
	}

	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {

		return fact.createSocket(arg0, arg1);
	}

	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3) throws IOException, UnknownHostException {
		return fact.createSocket(arg0, arg1, arg2, arg3);
	}

	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
		return fact.createSocket(arg0, arg1, arg2, arg3);
	}

}
