package cz.cuni.mff.xcars.exceptions;

@SuppressWarnings("serial")
public class IllegalCommandException extends RuntimeException {
	public IllegalCommandException() {
		super("Unrecognized command");
	}
	public IllegalCommandException(String message) {
		super("Unrecognized command: " + message);
	}

}
