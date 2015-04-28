package edu.banda.coel;

/**
 * @author Peter Banda
 * @since 2011
 */
public class CoelRuntimeException extends RuntimeException {

	public CoelRuntimeException() {
		super();
	}

	public CoelRuntimeException(String message) {
		super(message);
	}

	public CoelRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
