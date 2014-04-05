package main;

public class GrammarError extends Exception {
	private static final long serialVersionUID = 1L;

	String msg;
	public GrammarError(String string) {
		msg = string;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}



}
