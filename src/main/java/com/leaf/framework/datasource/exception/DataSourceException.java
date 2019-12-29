package com.leaf.framework.datasource.exception;

import com.baselib.exception.TechnicalException;

public class DataSourceException extends TechnicalException {

		private static final long serialVersionUID = -300770773018778915L;

		public DataSourceException(String message) {
			super(message);
		}
		
		public DataSourceException(String message, Throwable throwable) {
			super(message, throwable);
		}
		
		public DataSourceException(String sender, String message, Throwable throwable) {
			super(sender, message, throwable);
		}

}

