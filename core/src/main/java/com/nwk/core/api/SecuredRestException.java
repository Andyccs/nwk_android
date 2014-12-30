/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package com.nwk.core.api;

/**
 * A special class made to specify exceptions that are thrown by our
 * SecuredRestBuilder.
 * 
 * A more robust implementation would probably have fields for tracking
 * the type of exception (e.g., bad password, etc.).
 * 
 * @author jules
 *
 */
public class SecuredRestException extends RuntimeException {

    int status;
	public SecuredRestException(String message,int status) {
		super(message);
        this.status = status;
	}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}