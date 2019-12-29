package com.leaf.framework.dab.exception;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class SQLInvocationException extends RuntimeException {

	private static final long serialVersionUID = 2409475954778896524L;

	public SQLInvocationException() {

		super();

	}

	public SQLInvocationException(String message, Throwable cause) {

		super(message, cause);

	}

	public SQLInvocationException(String message) {

		super(message);

	}

	public SQLInvocationException(Throwable cause) {

		super(cause);

	}

}
