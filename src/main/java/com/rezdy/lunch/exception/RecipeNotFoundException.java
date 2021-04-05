package com.rezdy.lunch.exception;

/**
 * Throws when there is no recipe found in DB for given title
 */
public class RecipeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RecipeNotFoundException(String message) {
		super(message);
	}
}
