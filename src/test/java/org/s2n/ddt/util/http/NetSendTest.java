package org.s2n.ddt.util.http;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.junit.Test;

import org.s2n.ddt.util.LangUtils;

//import javax.net.ssl.*;

/**
 * Test Send data to other JVM .
 * 
 * @author Tushar Kapila
 * 
 */
public class NetSendTest {
	private static final Logger logger = LogManager.getLogger(NetSendTest.class);
	static{
		LangUtils.log4Default(false);
	}
	

	@Test
	public void sendSSl(){
		String dat = "ok=s";
		Map<String, String> headers = new HashMap<String, String>();
		String exp = "Read data, done-fine";
		//exp ="pickerElementDisplay.style.display";
		HttpData hDat = new HttpData();
		hDat.setTimeoutMilli(12000);
		//hDat.setUrl("https://exis.exilant.com/SSO/index.htm");
		hDat.setUrl("https://sel2in.com/up6.php");
		try {
			boolean send1 = send( hDat , dat,  headers, exp);
			Assert.assertEquals(true, send1);
		} catch (Exception e) {
			logger.warn("ssl " + e, e);
		}
	}
	
	
	@Test
	public void send80(){
		String dat = "ok=s";
		Map<String, String> headers = new HashMap<String, String>();
		String exp = "Read data, done-fine";
		//exp ="pickerElementDisplay.style.display";
		HttpData hDat = new HttpData();
		hDat.setTimeoutMilli(12000);
		//hDat.setUrl("https://exis.exilant.com/SSO/index.htm");
		hDat.setUrl("http://sel2in.com/up6.php");
		try {
			boolean send1 = send( hDat , dat,  headers, exp);
			Assert.assertEquals(true, send1);
		} catch (Exception e) {
			logger.warn("ssl " + e, e);
		}
	}


	public static boolean send(HttpData hDat, String dat, Map<String, String> headers, String expext) throws Exception {
		
		boolean b = false;
		String rtn = NetSend.send( hDat, dat,  headers);
		b = rtn != null && rtn.indexOf(expext) > -1 ;
		logger.info("RTN :" + rtn);
		return b;
	}

}
