
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.exilant.tfw.utils.io.FileNameEndsWithFilter"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileFilter"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
	out.print("hi");
		String sourceDirectory = "/Users/muktar/Documents/JBoss/jboss-5.1.0.GA/server/default/deploy/ROOT.war/reports/r/";
		File f = new File(sourceDirectory);
		File[] paths;
		String[] fileNames = f.list();
		File[] fileObjects = f.listFiles();

	//	String sample = "/Users/muktar/Documents/JBoss/jboss-5.1.0.GA/server/default/deploy/ROOT.war/reports/r/2013aug05run1_DFJE/index.html";
	//	String myDir = "/Users/muktar/Documents/JBoss/jboss-5.1.0.GA/server/default/deploy/ROOT.war/reports/";

		FileNameEndsWithFilter fileFilter = new FileNameEndsWithFilter("index.html", true);
		String name = fileFilter.getSubString("hi", "hello");
	%>


	<!--  <a href="file://///SERVER/directory/file.ext">file.ext</a>-->

	<%-- <a href="<%="hi"%>"><%=name%></a> --%>
	<%
		
	%>
	</ul>
</body>
</html>