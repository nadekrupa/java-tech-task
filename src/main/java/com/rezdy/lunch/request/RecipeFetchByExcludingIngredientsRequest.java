package com.rezdy.lunch.request;

import java.util.List;

/**
 * Class representing request body of fetch the recipes by excluding given ingredients.
 */
public class RecipeFetchByExcludingIngredientsRequest {
	private List<String> excludedIngredients;

	public RecipeFetchByExcludingIngredientsRequest(List<String> excludedIngredients) {
		this.excludedIngredients = excludedIngredients;
	}
	
	public RecipeFetchByExcludingIngredientsRequest() {
	}

	public List<String> getExcludedIngredients() {
		return excludedIngredients;
	}

	public void setExcludedIngredients(List<String> excludedIngredients) {
		this.excludedIngredients = excludedIngredients;
	}
}
