package com.rezdy.lunch.request;

/**
 * Class representing request body of fetching the recipes by title.
 */
public class RecipeFetchByTitleRequest {
	private String title;

	public RecipeFetchByTitleRequest(String title) {
		this.title = title;
	}
	
	public RecipeFetchByTitleRequest() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
