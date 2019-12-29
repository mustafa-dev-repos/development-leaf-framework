package com.leaf.framework.jdbc.exception;

import com.baselib.exception.TechnicalException;

public class ConnectionException extends TechnicalException {

	private static final long serialVersionUID = -4566422819023961882L;

	public ConnectionException(String message) {
		super(message);
	}
	
	public ConnectionException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}


