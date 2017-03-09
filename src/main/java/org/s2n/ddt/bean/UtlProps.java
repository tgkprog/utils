package org.s2n.ddt.bean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

/**
 * This class is used to instantiate the data source using the properties file.
 * Pooled connection creation, closing implementation is provided with this
 * class.
 */
public class UtlProps {
	private static final Logger logger = LogManager.getLogger(UtlProps.class);
	
	private  Properties mainProps;
	
	public UtlProps(){
		mainProps = new Properties();
	}
	
	public UtlProps(File f){
		mainProps = new Properties();
		initialize(f);
	}
	
	public  String getProperty(String name) {
		return mainProps.getProperty(name);
	}

	
	public  String getProperty(String name, String defalt) {
		return mainProps.getProperty(name, defalt);
	}

	public  void setMainProps(Properties mainProps) {
		this.mainProps = mainProps;
	}

	/**
	 * Method to initialize the properties and the data source by using the
	 * database properties configured in user defined file
	 */
	public  final  void initialize(File filePath ) {
	
		InputStream is = null;
		if(filePath == null){
			logger.warn("No file path to url props. Will not load/ re load");
			return;
		}
		try {
			is = new BufferedInputStream(new FileInputStream(filePath));
			Properties p = new Properties();
			p.load(is);
			mainProps.clear();//for re init, only discard current if new set was loaded successfully.
			mainProps.putAll(p);
		} catch (Exception e) {
			logger.warn("UtlConf Init " + e, e);
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (Exception e2) {
					logger.warn("UtlConf Init close is :" + e2, e2);
				}
			}
		}
		
	}
	
	
	public Properties getProps() {
		return mainProps;
	}
}

