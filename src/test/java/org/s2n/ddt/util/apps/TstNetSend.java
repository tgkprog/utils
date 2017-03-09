package org.s2n.ddt.util.apps;

import java.util.Map;

import org.s2n.ddt.util.LangUtils;
import org.s2n.ddt.util.http.HttpData;
import org.s2n.ddt.util.http.NetSend;

public class TstNetSend {
	public static void main(String[] args) {
		LangUtils.log4Default(false);
		int compreOptions = 0;
		String url = "http://sel2in.com/up6.php?df=f";
		Map<String, Object> params = null;
		//(String url, int compreOptions, Map<String, String> params)
		HttpData hDat = new HttpData(url , compreOptions , params );
		String dat = "k=a";
		try {
			System.out.print("sending to :" + url + "]");
			String s = NetSend.send(hDat, dat);
			System.out.print("Got :" + s + "]");
		} catch (Exception e) {
			System.out.print("err :" + e + "]");
			e.printStackTrace();
		}
	}
}
