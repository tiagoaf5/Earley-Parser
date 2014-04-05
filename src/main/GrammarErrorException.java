package main;

public class GrammarErrorException extends Exception {

	private static final long serialVersionUID = 1L;

	public GrammarErrorException(String string) {
		super(string);
	}
}
