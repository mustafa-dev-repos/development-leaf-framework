package com.baselib.exception;


import java.util.Map;

import com.baselib.message.Message;

public class BusinessException extends BaseException {

	private static final long serialVersionUID = 9217260569712030659L;

	private Message message;
	
	public BusinessException(String message, String messageKey, Throwable cause) {
		super(message, cause);
		this.message = new Message();
		this.message.setMessageKey(messageKey);
		this.message.addArg("exception", this);
	}
	
	public BusinessException(Message message) {
		super(message.getMessageKey());
		this.message = message;
	}
	
	public BusinessException(String message, String messageKey) {
		this(message,  messageKey, null);
	}
	
	public Message getLocaleAwareMessage() {
		return message;
	}

	public BusinessException addArg(String arg, Object value) {
		message.addArg(arg, value);
		return this;
	}

	public BusinessException addTranslatedArg(String arg, Object value) {
		message.addNamedTranslatedArg(arg, value);
		return this;
	}

	public Map<String, Object> getArgs() {
		return message.getArgs();
	}

	public String getMessageKey() {
		return message.getMessageKey();
	}

	public Map<String, Object> getTranslatedArgs() {
		return message.getTranslatedArgs();
	}

	
	

}

