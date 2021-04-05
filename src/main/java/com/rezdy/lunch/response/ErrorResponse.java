package com.rezdy.lunch.response;

/**
 * 
 * This is response object for update password operation.
 *
 */
public class ErrorResponse {
	
	/**
	 * The error message in case the update password operation fails
	 */
	private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorResponse setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
