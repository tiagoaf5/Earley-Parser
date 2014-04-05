package main;

public class FileError extends Exception {
	private static final long serialVersionUID = 1L;

	String msg;
	public FileError(String string) {
		msg = string;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}



}
