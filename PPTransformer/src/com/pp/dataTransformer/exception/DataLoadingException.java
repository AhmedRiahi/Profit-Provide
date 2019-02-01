package com.pp.dataTransformer.exception;

public class DataLoadingException extends Exception{

	private Exception originalException;
	
	public DataLoadingException(Exception originalException){
		this.originalException = originalException;
	}
	
	@Override
	public String getMessage() {
		return this.originalException.getMessage();
	}
	
	@Override
	public StackTraceElement[] getStackTrace() {
		return this.originalException.getStackTrace();
	}
	
	@Override
	public synchronized Throwable getCause() {
		return this.originalException.getCause();
	}
	
	@Override
	public String getLocalizedMessage() {
		return this.originalException.getLocalizedMessage();
	}
}