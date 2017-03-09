package org.s2n.ddt.util.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

import org.s2n.ddt.util.Base64;
import org.s2n.ddt.util.https.JNetSslFactoory;
import org.s2n.ddt.util.https.UtlHostNameVerifier;

//import javax.net.ssl.*;

/**
 * Send data to other JVM using HTTP post or Http Get or other custom data send over ssl or plain Socket. Ligther version of Apache HttpComponents which is
 * has more features. We can add support to send via HttpComponents as the send method specified in HttpData.
 * 
 * @author Tushar Kapila
 * 
 */
public class NetSend {
	private static final Logger logger = LogManager.getLogger(NetSend.class);

	private static String contentType = "application/x-www-form-urlencoded; charset=utf-8";
	private static JNetSslFactoory utlSsl = null;
	private static UtlHostNameVerifier nmV = new UtlHostNameVerifier();
	static {
		try {
			utlSsl = new JNetSslFactoory(null, true);
		} catch (Exception e) {
			logger.warn("init :" + e, e);
		}
	}

	public static Object sendObj(Serializable srlzblObj, HttpData hDat, String appObjTyp) {
		return sendObjects(appObjTyp, hDat, srlzblObj);
	}

	/*
	 * common.http.NetSend, HttpParam, SslParam, factory GenericParameters .java and CSR jsp common.threads TheadPool - 4 classes
	 */

	public static Object sendObjects(String appObjTyp, HttpData hDat, Serializable... srlzblObjs) {
		int options = 0;
		if (hDat == null) {
			logger.log(Level.WARN, "sendObj no http");
			return null;
		}
		// if(hDat.getSendType() == SendMethod.)
		try {

			StringBuilder data = new StringBuilder().append("typ=");
			data.append(appObjTyp);
			String dat = null;
			int i = 1;
			String addIndex = "";
			String reply;
			for (Serializable sro : srlzblObjs) {
				if (sro == null) {
					dat = "";
				} else {
					dat = Base64.encodeObject(sro, options);
				}
				addIndex = "" + i;
				data.append("&obj" + addIndex + "=").append(java.net.URLEncoder.encode(dat, "UTF-8"));
				i++;
			}

			reply = send(hDat, data.toString());
			if (reply != null && reply.length() > 4) {
				reply = removeHttpHeader(reply);
				if (reply != null && reply.length() > 4) {
					String objDataFromServletParam = reply.substring(4);
					Object obj = Base64.decodeToObject(objDataFromServletParam, options, null);
					return obj;
				}
			}
		} catch (IOException e) {
			logger.warn("obj send:" + e, e);
		} catch (ClassNotFoundException e) {
			logger.warn("obj send:" + e, e);
			logger.fatal("obj send srlzblObj:" + srlzblObjs);
		} catch (Exception e) {
			logger.warn("obj send:" + e, e);
			logger.fatal("obj send srlzblObj:" + srlzblObjs);
		}

		return null;
	}

	public static String send(HttpData hDat, String dat) throws Exception {
		return send(hDat, dat, null);
	}

	public static String send(HttpData hDat, String dat, Map<String, String> headers) throws Exception {
		Socket sockt = null;
		StringBuffer sb = new StringBuffer();
		InputStream in = null;
		OutputStream out = null;

		try {
			String sUrl = hDat.getUrl();
			int timeout = hDat.getTimeoutMilli();
			URL url = new URL(sUrl);
			final boolean sslReq = sUrl.toLowerCase().startsWith("https");
			int port = url.getPort() == -1 ? (sslReq ? 443 : 80) : url.getPort();
			// logger.trace("Connecting :");
			//String error = null;
			SSLSocketFactory ssLSocketFactory = utlSsl;
			Exception connEx = null;
			for (int i = 0; i < 3; i++) {
				try {
					if (sslReq) {
						logger.trace("sSL...........");
						// sockt = (SSLSocket) ssLSocketFactory.createSocket(url.getHost(), port);

						HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
						conn.setHostnameVerifier(nmV);
						conn.setConnectTimeout(timeout);
						conn.setReadTimeout(timeout);
						conn.setSSLSocketFactory(ssLSocketFactory);
						conn.setDoOutput(true);
						out = conn.getOutputStream();
						in = conn.getInputStream();
					} else {
						logger.trace("send url :" + sUrl + "; d:" + dat + ".");
						sockt = new Socket(url.getHost(), port);

						in = sockt.getInputStream();
						out = sockt.getOutputStream();
						sockt.setSoTimeout(timeout);
					}

					break;
				} catch (ConnectException e) {
					logger.info("Connect Exception failed: "+ e + ", try :" + (i + 1));
					connEx = e;
					continue;
				}
			}
			if(in == null){
				throw connEx;
			}

			logger.trace("Connected!");

			byte toSend[] = null;
			if (url.getQuery() != null && url.getQuery().length() != 0) {
				dat = url.getQuery() + "&" + dat;
			}

			sb.append("POST ").append(url.getPath()).append(" HTTP/1.1\r\n");
			if (headers == null) {
				headers = Collections.emptyMap();
			}
			final String USR_AGNT_H = "User-Agent";
			final String CON_TYP_H = "Content-type";
			String ctype = null;
			if (headers.containsKey(CON_TYP_H)) {
				ctype = headers.get(CON_TYP_H);
				headers.remove(CON_TYP_H);
			}
			if (ctype == null) {
				// if not set or val is null take default
				ctype = contentType;
			}
			String usrAg = null;
			if (headers.containsKey(USR_AGNT_H)) {
				usrAg = headers.get(USR_AGNT_H);
				headers.remove(USR_AGNT_H);
			}
			if (usrAg == null) {
				usrAg = "DDT Exilant";
			}

			final String Accept_H = "Accept";
			String saccpt = null;
			if (headers.containsKey(Accept_H)) {
				saccpt = headers.get(Accept_H);
				headers.remove(Accept_H);
			}
			if (saccpt == null) {
				saccpt = "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
			}

			sb.append(CON_TYP_H).append(": ").append(ctype).append("\r\n");

			sb.append(USR_AGNT_H).append(": ").append(usrAg).append("\r\n");
			sb.append("Host: ").append(url.getHost()).append(":").append(port).append("\r\n");
			sb.append(Accept_H).append(": ").append(saccpt).append("\r\n");

			// sb.append("Connection: keep-alive\r\n");
			toSend = dat.getBytes("UTF-8");
			// logger.trace("-----encode 1----"+new String(toSend, "utf-8"));

			// toSend = java.net.URLEncoder.encode(xml.getBytes());
			// logger.trace("-----encode 2----"+toSend);//CONTENT-LENGTH:
			final String contLen = "Content-Length: ";
			sb.append(contLen).append(toSend.length).append("\r\n");

			Set<Map.Entry<String, String>> headerS = headers.entrySet();
			for (Map.Entry<String, String> en : headerS) {
				sb.append(en.getKey()).append(": ").append(en.getValue()).append("\r\n");
			}

			sb.append("\r\n");
			byte headerToSend[] = sb.toString().getBytes("UTF-8");
			logger.trace("Header To be sent: "+sb.toString());//for debug

			out.write(headerToSend, 0, headerToSend.length);
			out.write(toSend, 0, toSend.length);
			out.flush();
			logger.trace("Data sent!");
			BufferedInputStream bin = new BufferedInputStream(in);
			byte temp[] = null;
			sb.setLength(0);
			String t1 = null;
			int looper =0;
			do {
				temp = readInputStream(bin, hDat);// br.readLine();
				if (temp != null) {
					t1 = new String(temp, "UTF-8");
					sb.append(t1);
				} else {
					// logger.trace("read was null ");
					if (sb.length() == 0 && temp == null) {
						throw new IOException("Lost Connection - App");
					}
				}

				// all data read?
				String fullHttp = null;
		
				//do
				{
					fullHttp = sb.toString();
					logger.trace("Outer k Http: " + fullHttp.length() + ", looper" + looper + ", read :" + hDat.getReadByteCnt());
					String sep = "\r\n\r\n";
					int i = fullHttp.indexOf(sep);
					looper++;
					if(looper % 10 == 0){
						logger.trace("fullHttp is :" + fullHttp + "\n---");
					}
					if (i != -1 && looper < 110) {
						// logger.trace("Got header....");
						String header = fullHttp.substring(0, i);
						// logger.trace("Only header ->"+header+"<-");
						fullHttp = fullHttp.substring(i + sep.length());
						// logger.trace("rest data ->"+fullHttp+"<-");
						if (fullHttp.startsWith("HTTP/ ") && fullHttp.indexOf(sep) != -1 && looper < 100) {
							// logger.trace("second header was found!");
							sb.setLength(0);
							sb.append(fullHttp);
							looper++;
							continue;// re-process
						}
						i = header.toUpperCase().indexOf(contLen);
						logger.trace("read some");
						if (i != -1) {
							logger.trace("got Content-Length :" + header);
							int j = header.indexOf("\r\n", i + 2 + 15);
							if (j != -1) {
								String len = header.substring(i + 2 + 15, j).trim();
								logger.trace("hlen: " + len);
								logger.trace("data len: " + fullHttp.length());
								int lenCL = Integer.parseInt(len);
								logger.trace("lenCL: " + lenCL);
								if (lenCL <= fullHttp.length()) {
									temp = null;// so it breaks out of main loop
									logger.trace("break of main loop");
									break;
								}
							}
						}
						break;
					}
				}// while (looper < 110 && hDat.getReadByteCnt() > 0);
			} while (temp != null && hDat.getReadByteCnt() > 0 );
		} catch (ConnectException e) {
			logger.fatal("ConnectException: " + e);
			throw e;
		} catch (Exception e) {
			logger.error("Error: " + e, e);
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ee) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception ee) {
				}
			}
			if (sockt != null) {
				try {
					sockt.close();
				} catch (Exception ee) {
				}
			}
		}

		// check for HTTP/1.1 200 OK
		final String reply =  sb.toString();
		
		if (reply != null && reply.length() != 0) {
			int i = reply.indexOf("\r\n");
			if (i != -1) {
				String header = reply.substring(0, i);
				if (header.endsWith("200 OK") == false) {
					throw new IOException("Bad HTTP Response! " + header);
				}
			}
		}
		final String reply1 = removeHttpHeader(reply);
		
		return reply1;
	}

	public static byte[] readInputStream(BufferedInputStream bis, HttpData hp) throws IOException {
		hp.setReadByteCnt(0);
		if (bis == null) {
			logger.warn("readInputStream bis was null ! ");
			return null;
		}
		byte data[] = null;
		logger.trace("readIs Waiting for read...");
		int s = bis.read();
		if (s == -1) {
			return null; // Connection lost
		}
		hp.setReadByteCnt(1);
		int alength = bis.available();
		logger.trace("readIs Data available: " + alength);
		if (alength > 0) {
			data = new byte[alength + 1];
			data[0] = (byte) s;
			int read = bis.read(data, 1, alength);
			if(read != alength){
				byte[] data2 = new byte[read + 1];
				System.arraycopy(data, 0, data2, 0, read + 1);
				data = data2;
			}
			hp.setReadByteCnt(read + 1);
		} else {
			data = new byte[1];
			data[0] = (byte) s;
		}
		logger.trace("readIs2: ");
		return data;
	}

	public static String removeHttpHeader(String data) {
		if (data == null) {
			return null;
		}
		int i = data.indexOf("\r\n\r\n");
		if(i > -1){
			return data.substring(i + 4);
		}
		return data;
	}

}
