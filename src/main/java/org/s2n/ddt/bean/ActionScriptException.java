package org.s2n.ddt.bean;

public class ActionScriptException extends Exception {
	
	/**
	 * ActionScript exception
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private String msg;

	public ActionScriptException() {
		super();
	}

	public ActionScriptException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public ActionScriptException(String msg, Exception e) {
		super(msg, e);
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ActionScriptException [msg=" + msg + "]";
	}

}
