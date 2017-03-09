package org.s2n.ddt.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionCommand {

	/** commenting these two variables due to compilation errors */
	// private AcInput oAcInput;
	// private AcOutput oAcOutput;
	private String objectid1 = "";
	private String objectid2 = "";
	private ArrayList<String> values = new ArrayList<String>();
	private String condition = "";
	private boolean result = false;
	private String comment = "";
	private String snapShot = " ";
	private String dateFormat = "";
	private String action = "";
	private String detailMsgs = "";
	private String response ="";
	private String request ="";
	private String stepParam = "";
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public HashMap<String, String> getReqMap() {
		return reqMap;
	}

	public void setReqMap(HashMap<String, String> reqMap) {
		this.reqMap = reqMap;
	}

	public HashMap<String, String> getSaveResult() {
		return saveResult;
	}

	public void setSaveResult(HashMap<String, String> saveResult) {
		this.saveResult = saveResult;
	}

	private HashMap<String,String> reqMap = new HashMap<String,String>(); 
	private HashMap<String,String> saveResult = new HashMap<String,String>();

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateForamt) {
		this.dateFormat = dateForamt;
	}

	public String getSnapShot() {
		return snapShot;
	}

	public void setSnapShot(String snapShot) {
		if (snapShot != null) {
			this.snapShot = snapShot;
		}
	}

	public String getObjectid1() {
		return objectid1;
	}

	public void setObjectid1(String objectid1) {
		if (objectid1 != null) {
			this.objectid1 = objectid1;
		}
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		if (condition != null) {
			this.condition = condition;
		}
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		if (comment != null) {
			this.comment = comment;
		}
	}

	public String getDetailMsgs() {
		return detailMsgs;
	}

	public void setDetailMsgs(String detailMsgs) {
		this.detailMsgs = detailMsgs;
	}
	
	public String getObjectid2() {
		return objectid2;
	}

	public void setObjectid2(String objectid2) {
		if (objectid2 != null) {
			this.objectid2 = objectid2;
		}
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}
	
	public String getStepParam() {
		return stepParam;
	}

	public void setStepParam(String stepParam) {
		this.stepParam = stepParam;
	}

	/**
	 * commenting these two property setters & getters due to compilation errors
	 */
	// public AcInput getoAcInput() {
	// return oAcInput;
	// }
	//
	// public void setoAcInput(AcInput oAcInput) {
	// this.oAcInput = oAcInput;
	// }
	//
	// public AcOutput getoAcOutput() {
	//
	// return oAcOutput;
	// }
	//
	// public void setoAcOutput(AcOutput oAcOutput) {
	// this.oAcOutput = oAcOutput;
	// }
}
