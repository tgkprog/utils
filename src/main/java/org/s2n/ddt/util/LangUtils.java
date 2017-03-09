package org.s2n.ddt.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LangUtils {
	private static final Logger logger = LogManager.getLogger(LangUtils.class);
	private static Map<String, SimpleDateFormat> sdfFormats = new HashMap<String,SimpleDateFormat>();
	
	private static NumberFormat nmbrFormat = NumberFormat.getIntegerInstance ();
	
	//private static ThreadLocal<Map<String, SimpleDateFormat>> sdf = null;//new ThreadLocal(new HashMap<String,SimpleDateFormat>());

	
	public static boolean isTrue(String s, boolean nullOrEmptyIsTrue){
		if(s == null || s.length() == 0){
			return nullOrEmptyIsTrue;
		}
		char char1 =s.substring(0,1).toLowerCase().charAt(0);
		return 'y' == char1 || '1' == char1 || Boolean.parseBoolean(s);
	}
	
	/*
	 * DO NOT USE THESE FOR sdf.parse(String) as that function is not thread safe. Will need a cache of thread local sdf Map for that.
	 */
	public static final SimpleDateFormat getSdfForFormat(String pattern){
		SimpleDateFormat sdf = sdfFormats.get(pattern);
		if(sdf == null){
			sdf = new SimpleDateFormat(pattern);
			sdfFormats.put(pattern, sdf);
		}
		return sdf;
	}
	
	public static final SimpleDateFormat getSdfForParse(String pattern){
		SimpleDateFormat sdf = sdfFormats.get(pattern);
		if(sdf == null){
			sdf = new SimpleDateFormat(pattern);
			sdfFormats.put(pattern, sdf);
		}
		return sdf;
	}
	
	public static final long getLong(String s, long def, String msg) {
		long i = def;
		try {
			Number n = nmbrFormat.parse(s);
			if(n != null){
				i = n.longValue();
			}
		} catch (ParseException e) {
			logger.warn("getInt err " + e + ", input :" + s + "; msg :" + msg);
		}
		return i;
	}
	
	public static final int getInt(String s, int def, String msg) {
		int i = def;
		try {
			Number n = nmbrFormat.parse(s);
			if(n != null){
				i = n.intValue();
			}
		} catch (ParseException e) {
			logger.warn("getInt err " + e + ", input :" + s + "; msg :" + msg);
		}
		return i;
	}

	public static void sleep(int delayMilli) {

		try {
			Thread.sleep(delayMilli);
		} catch (Exception e) {
			logger.error("Error in LangUtils: " + e);
		}

	}
	
	/**
	 * Default URLDecoder.decode without char-set.
	 * Kept in separate function so can suppress deprecation locally
	 * */

	@SuppressWarnings("deprecation")
	public static String urlDecode(String in) {

		return URLDecoder.decode(in);
	}


	public static double getCpuLoad() {
		logger.trace("calling cpu load 2");
		final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		logger.trace("os bean :" + osBean + ".");
		double load = osBean.getSystemLoadAverage();
		return load;
	}

	public static final String getVersion() {
		return "1.6.10";
	}
	
	public static void log4Default(boolean force) {
        /*
         * boolean noLog = true; org.apache.logging.log4j.Logger rootLogger = org.apache.logging.log4j.Logger.getRootLogger(); Enumeration
         * appenders = rootLogger.getAllAppenders(); if (!appenders.hasMoreElements()) { System.out.println("LOG4J config file is missing");
         * } else { System.out.println("appender found " + ((Appender) appenders.nextElement()).getName()); noLog = false;
         * 
         * }
         * 
         * if (noLog || force) { System.out.println("no log4j"); Layout layout = new PatternLayout(" %-5p %t %d [%t][%F:%L] : %m%n");
         * Appender ap = new ConsoleAppender(layout, ConsoleAppender.SYSTEM_OUT); Logger.getRootLogger().setLevel(Level.ALL); //
         * Logger.getRootLogger().addAppender(new ConsoleAppender(layout, ConsoleAppender.SYSTEM_ERR));
         * Logger.getRootLogger().addAppender(ap); }
         */

	}
	
	

}
