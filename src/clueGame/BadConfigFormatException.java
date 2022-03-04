package clueGame;

public class BadConfigFormatException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadConfigFormatException() {
	}

	public BadConfigFormatException(String message) {
		super(message);
	}
}
