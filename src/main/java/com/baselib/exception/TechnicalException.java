package com.baselib.exception;

public class TechnicalException extends BaseException {

	private static final long serialVersionUID = 2628134000298402549L;

	public TechnicalException(Class<?> clazz, String property) {
		this("Could not find property " + property + " in class " + clazz.getName());
	}
	
	public TechnicalException(String sender, String message, Throwable cause) {

		super(sender,message, cause);

	}

	public TechnicalException(String sender, String message) {
		super(sender, message);

	}

	public TechnicalException(String message, Throwable cause) {
		super(message, cause);
	}


	public TechnicalException(String message) {
		super(message);
	}

	public TechnicalException(Throwable cause) {
		super(cause);
	}

}
