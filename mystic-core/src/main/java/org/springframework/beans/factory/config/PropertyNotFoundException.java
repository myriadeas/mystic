package org.springframework.beans.factory.config;

public class PropertyNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PropertyNotFoundException(String name){
		super(name);
	}
}
