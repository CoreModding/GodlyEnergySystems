package info.coremodding.gps.exceptions;

/**
 * @author James USE TEH UDDER FRACKING METHOD
 */
public class UseTheOtherMethodException extends Exception {

	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -3448721880248208589L;

	/**
	 * A constructor using a default message
	 */
	public UseTheOtherMethodException() {
		super("Use teh OTHER METHOD >,<");
	}

	/**
	 * A constructor
	 * 
	 * @param message
	 *            The exception message
	 */
	public UseTheOtherMethodException(String message) {
		super(message);
	}

	/**
	 * A constructor
	 * 
	 * @param message
	 *            The exception message
	 * @param throwable
	 *            Used for chaining exceptions
	 */
	public UseTheOtherMethodException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * A constructor
	 * 
	 * @param throwable
	 *            Used for chaining exceptions
	 */
	public UseTheOtherMethodException(Throwable throwable) {
		super(throwable);
	}
}
