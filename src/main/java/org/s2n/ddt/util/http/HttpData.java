package org.s2n.ddt.util.http;

import java.util.Map;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

public class HttpData {
	private static final Logger logger = LogManager.getLogger(HttpData.class);
	private String url;
	private int compreOptions;
	private Map<String, Object> params;
	private SendVia sendType = SendVia.DIRECT;

	private HttpSendMethod sendMethod = HttpSendMethod.POST;
	private int timeoutMilli = 30000;
	
	private Map<String, String> headersToSave;
	private Map<String, String> savedHeaders;
	private Map<String, String> headersForSubsequntRequests;
	
	private boolean sendSavedHeaders;
	
	private int readByteCnt;

	public HttpData() {

	}

	public HttpData(String url) {
		super();
		this.url = url;
	}

	public HttpData(String url, Map<String, Object> params) {
		super();
		this.url = url;
		this.params = params;
	}

	public HttpData(String url, int compreOptions, Map<String, Object> params) {
		super();
		this.url = url;
		this.compreOptions = compreOptions;
		this.params = params;
	}



	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCompreOptions() {
		return compreOptions;
	}

	public void setCompreOptions(int compreOptions) {
		this.compreOptions = compreOptions;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public SendVia getSendType() {
		return sendType;
	}

	public void setSendType(SendVia sendType) {
		this.sendType = sendType;
	}

	public HttpSendMethod getSendMethod() {
		return sendMethod;
	}

	public void setSendMethod(HttpSendMethod sendMethod) {
		this.sendMethod = sendMethod;
	}

	public void setTimeoutMilli(int timeout) {
		this.timeoutMilli = timeout;
	}

	public int getTimeoutMilli() {

		return timeoutMilli;
	}

	public final Map<String, String> getHeadersToSave() {
		return headersToSave;
	}
	
	public final String getHeaderToSave(String s) {
		return headersToSave.get(s);
	}
	
	public final void setHeaderToSave(String name, String vl) {
		this.headersToSave.put(name, vl);
	}

	public final void setHeadersToSave(Map<String, String> headersToSave) {
		this.headersToSave = headersToSave;
	}

	public final Map<String, String> getSavedHeaders() {
		return savedHeaders;
	}
	
	public final String getSavedHeader(String s) {
		return savedHeaders.get(s);
	}
	
	public final void setSavedHeader(String name, String vl) {
		this.savedHeaders.put(name, vl);
	}

	public final void setSavedHeaders(Map<String, String> savedHeaders) {
		this.savedHeaders = savedHeaders;
	}

	public final String getHeaderForSubsequntRequests(String s) {
		return savedHeaders.get(s);
	}
	
	public final void setHeaderForSubsequntRequests(String name, String vl) {
		this.savedHeaders.put(name, vl);
	}
	public final Map<String, String> getHeadersForSubsequntRequests() {
		return headersForSubsequntRequests;
	}

	public final void setHeadersForSubsequntRequests(Map<String, String> headersForSubsequntRequests) {
		this.headersForSubsequntRequests = headersForSubsequntRequests;
	}

	public final boolean isSendSavedHeaders() {
		return sendSavedHeaders;
	}

	public final void setSendSavedHeaders(boolean sendSavedHeaders) {
		this.sendSavedHeaders = sendSavedHeaders;
	}

	public int getReadByteCnt() {
		return readByteCnt;
	}

	public void setReadByteCnt(int readByteCnt) {
		this.readByteCnt = readByteCnt;
	}

}
