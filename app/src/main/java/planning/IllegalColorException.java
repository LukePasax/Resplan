package planning;

/**
 * Class that represents a basic exception for a non-valid color
 */
public class IllegalColorException extends RuntimeException {

	private static final long serialVersionUID = 7322020293412776324L;

	public IllegalColorException(String message) {
		super(message);
	}
}
