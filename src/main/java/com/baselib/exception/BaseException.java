package com.baselib.exception;

public abstract class BaseException extends RuntimeException {
	
	private String sender = null;

	public BaseException(String sender, String message, Throwable cause) {
		super(message, cause);
		this.sender = sender;
	}

	public BaseException(String sender, String message) {
		super(message);
		this.sender = sender;
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}
	
}
