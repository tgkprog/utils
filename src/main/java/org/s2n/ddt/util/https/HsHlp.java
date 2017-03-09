package org.s2n.ddt.util.https;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

public class HsHlp {
	private static final Logger logger = LogManager.getLogger(HsHlp.class);

	public synchronized static SSLSocketFactory jksLoadForSsl(char[] passphrase, String certLocation) {
		if (certLocation == null || "".equals(certLocation)){
			return null;
		}
		if (passphrase == null || "".equals(passphrase)){
			return null;
		}
		try {
			logger.debug("Jks load start ");

			KeyStore ts = KeyStore.getInstance("JKS");
			ts.load(new FileInputStream(certLocation), passphrase);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ts);
			logger.debug("trust loaded");
			SSLContext ctx = SSLContext.getInstance("SSLv3");
			ctx.init(null, tmf.getTrustManagers(), null);
			SSLSocketFactory ssLSocketFactory = (SSLSocketFactory) ctx.getSocketFactory();
			logger.debug("Done");
			return ssLSocketFactory;
		} catch (Exception e) {
			logger.fatal("jksLoadForSsl:" + e, e);
			return null;
		}
	}
}
