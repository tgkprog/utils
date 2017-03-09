<%@ 
page import="java.util.Date"
%><%@ page import="org.apache.log4j.*"%><%@ 
page import="com.exilant.tfw.util.SerUtils"%><%@ 
page import="com.exilant.tfw.util.Base64"%><%@ 
page import="java.util.*"%><%@ 
page import="java.io.*"%><%!

Serializable process(String typ, Object o){
	StringBuilder result = new StringBuilder().append("k ");
	result.append(new java.util.Date().toString());
	Map map = (Map)o;
	Object s = map.get("1");
	result.append("\n<br> y2 <br>s :");
	result.append(s);
	return (Serializable)result.toString();
	
	
}
%><%
		
		try{
			
			String typ = request.getParameter("typ");
			int options = Base64.GZIP;
			String objData = request.getParameter("obj");					
			/*byte dataBytes[] = Base64.decode(objData, Base64.GZIP);
			Object obj = SerUtils.retriveObject(dataBytes);//cast`
			*/
			//Object decodeToObject(  String encodedObject, int options, final ClassLoader loader )
			Object obj = Base64.decodeToObject(objData, options, null);
						
			
			
			Serializable result = process(typ, obj);
			
			System.out.println("typ :" +  typ + "; obj :" + obj);
			System.out.println("result :" +  result + ";");
			//byte replyData[] = SerUtils.getObjectSerialized(result);
			String reply = null;
			//reply = new String(Base64.encodeBytes(replyData, Base64.GZIP));
			reply = Base64.encodeObject(result, options);
			out.print("rsp=" + reply);
		} catch(Exception e){		
			e.printStackTrace();
			out.print("ERR=" + e);
		}
%>