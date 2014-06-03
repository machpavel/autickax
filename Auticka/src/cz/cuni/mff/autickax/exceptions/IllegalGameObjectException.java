package cz.cuni.mff.autickax.exceptions;

@SuppressWarnings("serial")
public class IllegalGameObjectException extends RuntimeException {
	public IllegalGameObjectException() {
		super("Unrecognized game object");
	}
	public IllegalGameObjectException(String message) {
		super("Unrecognized game object: " + message);
	}

}
