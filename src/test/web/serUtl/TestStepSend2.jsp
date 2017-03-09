<%@ 
page import="java.util.Date"
%><%@ page import="org.apache.log4j.*"%><%@ 
page import="com.exilant.tfw.util.SerUtils"%><%@ 
page import="com.exilant.tfw.util.Base64"%><%@ 
page import="java.net.*"%><%@ 
page import="com.exilant.tfw.util.http.*"%><%@ 
page import="java.util.*"%><%@ 
page import="java.io.*"%><%!

private static Logger logger = Logger.getLogger("com.exilant.tfw.webJsps.serUtl.TestStepSend");

private static int timeout = 1000*30; //30 sec



Object process(String typ, Object o){
	StringBuilder result = new StringBuilder().append("k ");
	result.append(new java.util.Date().toString());
	Map map = (Map)o;
	Object s = map.get(1);
	return result.toString();
}


%>send <%
		
		try{
			int timeOut = 120000;
			int options = Base64.GZIP;
			java.util.Map dataToSend = new java.util.HashMap();
			dataToSend.put("when", new java.util.Date());
			dataToSend.put("1", "2m6");
			String urlTo = "http://localhost:8180/serUtl/TestStepGet.jsp";
			HttpData hDat = new HttpData(urlTo, options, null);
			Object rtn = NetSend.sendObj((Serializable)dataToSend, hDat, "1");
			
			out.println("SENT <BR>GOT :" + rtn + "<br><br> " );
			
			options = Base64.GZIP;
			
			dataToSend.put("vdf", "jsbdck ksdn ksncksncksk 2m6");
			String s = Base64.encodeObject((Serializable)dataToSend, options);
			out.println( "<br><br>gz len : " + s.length() );
			
			options = 0;
			
			s = Base64.encodeObject((Serializable)dataToSend, options);
			out.println( "<br><br>0 len : " + s.length() );
				
			
		} catch(Exception e){
			out.print("errr=" + e);	
				
			e.printStackTrace();
			logger.fatal(" err r " + e, e);
		}
%>
<br>DONE