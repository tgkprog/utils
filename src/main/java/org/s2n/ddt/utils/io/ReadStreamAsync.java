package org.s2n.ddt.utils.io;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

import org.s2n.ddt.util.threads.DdtPools;

public class ReadStreamAsync implements Runnable{
	private static final Logger logger = LogManager.getLogger(ReadStreamAsync.class);
	
	
	private InputStream is;
	private int logLevel;
	private boolean saveStr;
	private String resp;
	private boolean done;
	private String encoding = null;
	
	public ReadStreamAsync(InputStream is, String pool, boolean ownThread, int logLevel, boolean saveStr, String encoding){
		if(is instanceof BufferedInputStream){
			this.is = is;
		}else{
			this.is = new BufferedInputStream(is);
		}
		this.saveStr = saveStr;
		this.logLevel = logLevel;
		this.encoding = encoding;
		//if(ownThread)//todo
		DdtPools.offer(pool, this);
	}

	
	public void run() {
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(is, writer, encoding);//TODO check encoding
			String theString = writer.toString();
			if(logLevel == 1){
				logger.info("Response :" + theString);
			}else{
				logger.debug("Response :" + theString);
			}
			if(saveStr){
				resp = theString;
			}
		} catch (Throwable e) {
			logger.warn("run err " + e, e);
		}
		
	}

	public String getResponse() {
		return resp;
	}

	

	public boolean isDone() {
		return done;
	}

	
	

}
