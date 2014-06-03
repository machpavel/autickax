package cz.cuni.mff.xcars.exceptions;

@SuppressWarnings("serial")
public class IllegalDifficultyException extends RuntimeException {
	public IllegalDifficultyException() {
		super("Unrecognized difficulty");
	}
	public IllegalDifficultyException(String message) {
		super("Unrecognized difficulty: " + message);
	}

}
