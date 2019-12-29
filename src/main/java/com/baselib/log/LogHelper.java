package com.baselib.log;

import java.lang.reflect.Array;
import java.util.Iterator;

import org.apache.commons.logging.LogFactory;

public class LogHelper {

	private org.apache.commons.logging.Log delegate;

	private LogHelper(org.apache.commons.logging.Log delegate) {
		
		this.delegate = delegate;
	}

	public static LogHelper getLog() {
		
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		
		String logName = "unspecified";
		
		for (int i = 2; i < stackTrace.length; i++) {

			String stackNode = stackTrace[i].getClassName();

			if (!stackNode.startsWith("java.") && !stackNode.startsWith("com.baselib.log")) {
				
				logName = stackNode;
				
				break;
			}
		}
		
		return new LogHelper(LogFactory.getLog(logName));
	}
	
	public static LogHelper getLog(Class<?> clazz) {
		
		return new LogHelper(LogFactory.getLog(clazz));
	}
	
	public boolean isTraceEnabled() {
		
		return delegate.isTraceEnabled();
	}

	public boolean isDebugEnabled() {
		
		return delegate.isDebugEnabled();
	}
	
	public boolean isInfoEnabled() {
		
		return delegate.isInfoEnabled();
	}
	
	public boolean isWarnEnabled() {
		
		return delegate.isWarnEnabled();
	}
	
	public boolean isErrorEnabled() {
		
		return delegate.isErrorEnabled();
	}
	
	public final void fatal(Object... messages) {
		
		if (delegate.isFatalEnabled()) 
			delegate.fatal(prepareMessage(messages));
	}

	public final void error(Object... messages) {
		
		if (delegate.isErrorEnabled()) 
			delegate.error(prepareMessage(messages));
	}

	public final void warn(Object... messages) {
		
		if (delegate.isWarnEnabled()) 
			delegate.warn(prepareMessage(messages));
	}

	public final void info(Object... messages) {
		
		if (delegate.isInfoEnabled()) 
			delegate.info(prepareMessage(messages));
	}

	public final void debug(Object... messages) {
		
		if (delegate.isDebugEnabled()) 
			delegate.debug(prepareMessage(messages));
	}

	public final void trace(Object... messages) {
		
		if (delegate.isTraceEnabled()) 
			delegate.trace(prepareMessage(messages));
	}

	public final void fatal(Throwable throwable, Object... messages) {
		
		if (delegate.isFatalEnabled()) 
			delegate.fatal(prepareMessage(messages), throwable);
	}

	public final void error(Throwable throwable, Object... messages) {
		
		if (delegate.isErrorEnabled()) 
			delegate.error(prepareMessage(messages), throwable);
	}

	public final void warn(Throwable throwable, Object... messages) {
		
		if (delegate.isWarnEnabled()) 
			delegate.warn(prepareMessage(messages), throwable);
	}

	public final void info(Throwable throwable, Object... messages) {
		
		if (delegate.isInfoEnabled()) 
			delegate.info(prepareMessage(messages), throwable);
	}

	public final void debug(Throwable throwable, Object... messages) {
		
		if (delegate.isDebugEnabled()) 
			delegate.debug(prepareMessage(messages), throwable);
	}

	public final void trace(Throwable throwable, Object... messages) {
		
		if (delegate.isTraceEnabled()) 
			delegate.trace(prepareMessage(messages), throwable);
	}

	public org.apache.commons.logging.Log getDelegate() {
		
		return delegate;
	}

	private String prepareMessage(Object[] parts) {
		
		StringBuilder builder = new StringBuilder();
		
		for (Object object : parts) {
			
			if (object != null && object.getClass().isArray())
				
				prepareMessageFromArray(builder, object);
			
			else if (object instanceof Iterable) {
				
				prepareMessageFromIterable(builder, (Iterable<?>) object);
			} else
				builder.append(object);
		}
		
		return builder.toString();
	}

	private void prepareMessageFromIterable(StringBuilder builder, Iterable<?> iterable) {
		
		builder.append("[ ");
		
		for (Iterator<?> i = iterable.iterator(); i.hasNext(); ) {
			
			builder.append(i.next());
			
			if (i.hasNext())
				builder.append(", ");
		}
		
		builder.append(" ]");
	}
	
	private void prepareMessageFromArray(StringBuilder builder, Object object) {
		
		builder.append("[ ");
		
		int length = Array.getLength(object);
		
		for (int i = 0; i < length; i++) {
			
			builder.append(Array.get(object, i));
			
			if (i < length - 1)
				builder.append(", ");
		}
		
		builder.append(" ]");
	}

}
