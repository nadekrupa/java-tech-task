package com.rezdy.lunch.exception;

/**
 * Throws when the input provided is having invalid/missing parameters
 */
public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidInputException(String message) {
		super(message);
	}
}
