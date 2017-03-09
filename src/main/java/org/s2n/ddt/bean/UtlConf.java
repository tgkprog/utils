package org.s2n.ddt.bean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

import org.s2n.ddt.util.LangUtils;

/**
 * Load properties from common file system file, merge with data base and then load this node specific properties from data base and file.
 * 
 * This class is used to instantiate the data source using the properties file. Pooled connection creation, closing implementation is provided with this
 * class.
 */
public class UtlConf {
	private static final Logger logger = LogManager.getLogger(UtlConf.class);
	private static final Properties mainProps = new Properties();
	private static String appDataRoot;
	private static String appVerifierHomeDir;
	private static File ddtHomeDir;

	static {
		initialize();
		// java.lang.reflect.Method m;
		// m.setAccessible(arg0)
	}

	/**
	 * Method to initialize the properties and the data source by using the database properties configured in user defined file
	 */
	public static void initialize() {
		String filePath = getConfigFilePath();
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(filePath));
			Properties p = new Properties();
			p.load(is);
			updateProps(p, true);
			String sddtHomeDir = mainProps.getProperty("ddtHomeDir", "/data/ddt");
			setDdtHomeDir(new File(sddtHomeDir));
			appVerifierHomeDir = mainProps.getProperty("verifiers.home", appVerifierHomeDir);// {r}
			String second = mainProps.getProperty("props.2", null);
			String secondE = mainProps.getProperty("props.2.enabled", null);
			if(second != null && LangUtils.isTrue(secondE, false)){
				logger.warn("INFO : loading more props (will be added or replaced) from :" + second + ".");
				is.close();
				is = new BufferedInputStream(new FileInputStream(second));
				p.clear();
				p.load(is);
				updateProps(p, false);
			}
		} catch (Exception e) {
			logger.warn("UtlConf Init " + e, e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
					logger.warn("UtlConf Init close is :" + e2, e2);
				}
			}
		}
	}

	/**
	 * Gets the configuration file path
	 * 
	 * @return String
	 */
	private static String getConfigFilePath() {
		appDataRoot = System.getProperty("DDT_MAIN_PROPERTIES_FILE_PATH");
		System.out.println("appDataRoot from sys prop [DDT_MAIN_PROPERTIES_FILE_PATH]:" + appDataRoot);
		if (appDataRoot == null) {
			appDataRoot = "/data/ddt/config/main.properties";
			appVerifierHomeDir = "/data/ddt/config";
		}
		File f = new File(appDataRoot);
		System.out.println("appDataRoot exists:" + f.exists());
		if ((appDataRoot == null) || !f.exists()) {
			URL url = UtlConf.class.getClassLoader().getResource("main.properties");
			if (url != null) {
				appDataRoot = url.getFile();
			}
		}
		System.out.println("appDataRoot final:" + appDataRoot);
		return appDataRoot;
	}

	/**
	 * Replaces the <,>,'," entities with HTML encoded string
	 */
	public static String replaceHtmlEnt(String in) {
		return in.replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
	}

	/**
	 * Get property from main property file of DDT
	 * 
	 * @param name
	 *            non null property name
	 * @return property value as string or null if no such property defined
	 */
	public static String getProperty(String name) {
		return mainProps.getProperty(name);
	}

	/**
	 * 
	 * @param name
	 *            - name of property non null.
	 * @param defalt
	 *            - can be null
	 * @return
	 */
	public static String getProperty(String name, String defalt) {
		return mainProps.getProperty(name, defalt);
	}

	/**
	 * replaces special strings like  {conf} {home} entry of the properties' values with ddtHome data
	 * and ~ with user.home
	 */
	private static void updateProps(final Properties in, boolean clr) {
		//mainProps,mainProps.getProperty("ddtHomeDir", "/data/ddt")
		final String homeDir = mainProps.getProperty("ddtHomeDir", "/data/ddt");
		Enumeration e = in.propertyNames();
		String userHome1 = System.getProperty("user.home");
		logger.info("From system userHome :" + userHome1);
		if(userHome1 == null || userHome1.length() == 0){
			userHome1 = "./";
		}
		final String userHome =  userHome1;
		logger.warn("Final using userHome :" + userHome1);
		System.out.println("Final using userHome :" + userHome1);
		logger.warn("Final homeDir :" + homeDir);
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = in.getProperty(key);
			//whats the point of the if contains, if its there replace it.
			//if (value.contains("{conf}")) {
			//	value = value.replace("{conf}", replacementString);
			//}
			value = value.replace("{conf}", homeDir);
			value = value.replace("{home}", homeDir);
			value = value.replace("~", userHome1);
			in.put(key, value);
		}
		// for re init, only discard current if new set was loaded successfully.
		if(clr){
			mainProps.clear();
		}
		mainProps.putAll(in);
	}

	/*
	 * Random number generator for DB Id, when threads run parallely
	 */
	public static String randomNum() {
		String rand = new Long(System.nanoTime()).toString();
		return rand.substring(rand.length() - 7);
	}

	public static String getRoot() {
		return appDataRoot;
	}

	public static String getApiConfigsRoot(String project) {
		return mainProps.getProperty("apiConf." + project, appVerifierHomeDir + "/apiPro");
	}

	public static File getDdtHomeDir() {
		return ddtHomeDir;
	}

	public static void setDdtHomeDir(File ddtHomeDir) {
		UtlConf.ddtHomeDir = ddtHomeDir;
	}
	
	public static Properties getProperties(){
		return mainProps;
	}
	
	public static void main(String[] args) {
		System.out.println(mainProps);
		String a = "~/sd";
		a = a.replace("~", "ll");
		System.out.println("a " + a);
	}
}
