package org.s2n.ddt.bean;

public class VerifyResult {
	
	private String comment;
	
	private boolean result;
	
	private String detailMsgs;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getDetailMsgs() {
		return detailMsgs;
	}

	public void setDetailMsgs(String detailMsgs) {
		this.detailMsgs = detailMsgs;
	}

}
