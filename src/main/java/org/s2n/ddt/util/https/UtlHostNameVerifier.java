package org.s2n.ddt.util.https;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
public class UtlHostNameVerifier implements HostnameVerifier{

	public boolean verify(String hostname, SSLSession session) {

		return true;
	}

}
