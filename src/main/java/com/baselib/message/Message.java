package com.baselib.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message {

	
	
	private static final String NO_ARG_NAME = "";

	private String messageKey;
	
	private String defaultMessage;

	private Map<String, Object> args = new HashMap<String, Object>(5, 1f);
	
	private Map<String, Object> translatedArgs = new HashMap<String, Object>(5, 1f);

	private List<String> argKeys = new ArrayList<String>(5);
	
	public Message() {}
	
	public Message(String messageKey) {
		this.messageKey = messageKey;
	}

	public Message(String messageKey, String defaultMessage) {
		this.messageKey = messageKey;
		this.defaultMessage = defaultMessage;
	}

	public Map<String, Object> getArgs() {
		return args;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public Map<String, Object> getTranslatedArgs() {
		return translatedArgs;
	}
	
	public Message addNamedArg( String arg, Object value){
		getArgs().put(arg,value);
		argKeys.add(arg);
		return this;
	}
	public Message addArg(Object... value){
//TODO		
		return null;
	}

	
	public Message addNamedTranslatedArg( String arg, Object value ){
		getTranslatedArgs().put(arg,value);
		argKeys.add(arg);
		return this;
	}

	
}
