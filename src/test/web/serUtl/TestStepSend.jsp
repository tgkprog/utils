<%@ 
page import="java.util.Date"
%><%@ page import="org.apache.log4j.*"%><%@ 
page import="com.exilant.tfw.util.SerUtils"%><%@ 
page import="com.exilant.tfw.util.Base64"%><%@ 
page import="java.net.*"%><%@ 
page import="java.io.*"%><%@ 
page import="java.util.*"%><%@ 
page import="javax.net.ssl.*"%><%!

private static Logger logger = Logger.getLogger("com.exilant.tfw.webJsps.serUtl.TestStepSend");

private static int timeout = 1000*30; //30 sec
	private static String authProxyURL = "";
	private static String contentType = "application/x-www-form-urlencoded; charset=utf-8";
	private static SSLSocketFactory ssLSocketFactory = null;

	private static String location = "/data/config/exilant_trust.jks";
	private static String password = "get-in9w";	

Object process(String typ, Object o){
	StringBuilder result = new StringBuilder().append("k ");
	result.append(new java.util.Date().toString());
	Map map = (Map)o;
	Object s = map.get(1);
	return result.toString();
}

//move to Utls

//TODO

public synchronized static void loadSSLContext(char[] passphrase, String certLocation) {
		/*if(certLocation==null || "".equals(certLocation)) return;
		if(passphrase==null || "".equals(passphrase)) return;

		try {
			logger.debug("Loading SSLContext.. ");
			
			KeyStore ts = KeyStore.getInstance("JKS");
			ts.load(new FileInputStream(certLocation), passphrase);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");			
			tmf.init(ts);
			logger.debug("trust loaded");	
			SSLContext ctx = SSLContext.getInstance("SSLv3");
			ctx.init(null, tmf.getTrustManagers(), null);																		
			ssLSocketFactory = (SSLSocketFactory) ctx.getSocketFactory();
			logger.debug("Done");
		} catch(Exception e) {
			logger.fatal("Error:" + e);
			e.printStackTrace();
		} */
	}	

	public static String connectAndSend(String serverURL, String xml, int timeout) 
			throws IOException {
		Socket door = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			URL url = new URL(serverURL);
			int port = url.getPort()==-1?443:url.getPort();
			//logger.debug("Connecting :");
			String error = null;
			for(int i=0;i<3;i++) {
				try {
					if(serverURL.startsWith("https://")) {
						//logger.debug("sSL...........");
						door = (SSLSocket) ssLSocketFactory.createSocket(url.getHost(), port);
					} else {
						logger.debug("Gateway-url"+serverURL+xml);
						door = new Socket(url.getHost(), port);
					}
					door.setSoTimeout(timeout);
					break;
				} catch(ConnectException e) {
					//logger.debug("ConnectException failed: "+e);
					error = ""+e;
					continue;
				}	      		
			}
			
			if(door==null) {
				throw new ConnectException("Can not connect! "+error);
			}
	
			InputStream in = door.getInputStream();
			OutputStream out = door.getOutputStream();
			BufferedInputStream bin = new  BufferedInputStream(in);
			logger.debug("Connected!");
	
			byte toSend[] = null;
			if(url.getQuery()!=null && url.getQuery().length()!=0) {
				xml = url.getQuery()+"&"+xml;
			}
			
			sb.append("POST ").append(url.getPath()).append(" HTTP/1.0\r\n");
			sb.append("Content-type: ").append(contentType).append("\r\n");
			sb.append("User-Agent: ACCOSA3DS-ACS\r\n");
			sb.append("Host: ").append(url.getHost()).append(":").append(port).append("\r\n");
			sb.append("Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n");
			//sb.append("Connection: keep-alive\r\n");
			toSend = xml.getBytes("UTF-8");
			//logger.debug("-----encode 1----"+new String(toSend, "utf-8"));
			
			//toSend =  java.net.URLEncoder.encode(xml.getBytes());
			//logger.debug("-----encode 2----"+toSend);
			
			sb.append("Content-Length: ").append(toSend.length).append("\r\n");
			sb.append("\r\n");
			byte headerToSend[] = sb.toString().getBytes("UTF-8");
			//logger.debug("Header To be sent: "+sb.toString());//for debug
			
			out.write(headerToSend,0,headerToSend.length);			
			out.write(toSend,0,toSend.length);
			out.flush();
			//logger.debug("Data sent!");
			
			byte temp[] = null;
			sb.setLength(0);
			String t1 = null;
			do {
				temp = readInputStream(bin);//br.readLine();
				if(temp!=null) {
					t1 = new String(temp, "UTF-8");
					sb.append(t1);
				} else {
					//logger.debug("read was null ");
					if(sb.length()==0 && temp==null) {
						throw new IOException("Lost Connection - App");
					}
				}
				
				//check if full data was read.. but server wont close connection
				String fullHttp = null;
				do {
					fullHttp = sb.toString();
					//logger.debug("FullHttp: "+fullHttp);
					String sep = "\r\n\r\n";
					int i = fullHttp.indexOf(sep);
					if(i!=-1) {
						//logger.debug("Got header....");
						String header = fullHttp.substring(0, i);
						//logger.debug("Only header ->"+header+"<-");
						fullHttp = fullHttp.substring(i+sep.length());
						//logger.debug("rest data ->"+fullHttp+"<-");				
						if(fullHttp.startsWith("HTTP/ ") && fullHttp.indexOf(sep)!=-1) {
							//logger.debug("second header was found!");
							sb.setLength(0);
							sb.append(fullHttp);
							continue;//re-process
						}
						i = header.toUpperCase().indexOf("\r\nCONTENT-LENGTH:");
						if(i!=-1) {
							//logger.debug("got Content-Length!");
							int j = header.indexOf("\r\n", i+2+15);
							if(j!=-1) {
								String len = header.substring(i+2+15,j).trim();
								//logger.debug("len: "+len);
								//logger.debug("data len: "+fullHttp.length());
								int lenCL = Integer.parseInt(len);
								//logger.debug("lenCL: "+lenCL);
								if(lenCL<=fullHttp.length()) {
									temp=null;//so it breaks out of main loop
									//logger.debug("break of main loop");
									break;
								}
							}
						}
						break;
					}
				} while(true);
			} while(temp!=null);	
		} catch(ConnectException e) {
			logger.fatal("ConnectException: "+e);
			throw new IOException(e.getMessage());
		} catch(Exception e) {
			logger.error("Error: "+e);
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} finally {
			if(door!=null) {
				try {
					door.close();
				} catch(Exception ee) {}
			}
		}
	
		//check for HTTP/1.1 200 OK
		String header = sb.toString();
		if(header!=null && header.length()!=0) {
			int i = header.indexOf("\r\n");
			if(i!=-1) {
				header = header.substring(0,i);
				if(header.endsWith("200 OK")==false) {
					throw new IOException("Bad HTTP Response! "+header);
				}
			}
		}
	
		return sb.toString();
	}
	
	private static byte[] readInputStream(BufferedInputStream bin) 
			throws IOException {
		if(bin==null) {
			logger.warn("BufferedInputStream was null ! ");
			return null;
		}
		byte data[] = null;
		//logger.debug("Waiting for read...");
		int s = bin.read();
		if(s==-1)
			return null; //Connection lost
		int alength = bin.available();
		//logger.debug("Data available: "+alength);
		if(alength > 0) {
			data = new byte[alength+1];
			data[0] = (byte)s;
			bin.read(data, 1, alength);
		} else {
			data = new byte[1];
	        data[0] = (byte)s;
		}
		return data;
	}
	
	public static String removeHttpHeader(String data) {
		if(data==null) return null;
	
		int i = data.indexOf("\r\n\r\n");
		return data.substring(i+4);
	}
	
%>send <%
		
		try{
			int timeOut = 120000;
			java.util.Map map = new java.util.HashMap();
			map.put("when", new java.util.Date());
			map.put("1", "2");
			String urlTo = "http://localhost:8180/serUtl/TestStepGet.jsp";
			byte objBytes[] = SerUtils.getObjectSerialized(map);
			//data = new String(Base64.encode(bytes));
			String encoded = new String( Base64.encodeBytes( objBytes, Base64.GZIP));//Bytes Base64.GZIP
			
			String data = "type=1&obj="+ java.net.URLEncoder.encode(encoded);
			String reply1 = connectAndSend(urlTo, data, timeOut);
			
			out.println("SENT <BR>GOT :" + reply1 + "<br><br>");
			
			String reply = removeHttpHeader(reply1);
				
			String objDataFromServletParam = reply.substring(4);				
			byte dataBytes[] = Base64.decode(objDataFromServletParam, Base64.GZIP);//Base64.GZIP
			Object obj = SerUtils.retriveObject(dataBytes);//cast`
			
						
			
			
			out.print("rsp=" + obj);
		} catch(Exception e){
			out.print("errr=" + e);	
				
			e.printStackTrace();
			logger.fatal(" err r " + e, e);
		}
%>
<br>DONE