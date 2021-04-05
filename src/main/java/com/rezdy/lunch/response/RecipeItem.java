package com.rezdy.lunch.response;

import java.util.Set;

/**
 * Class representing request body of get recipe request.
 */
public class RecipeItem {
	private final String title;
	
	private final Set<IngredientItem> ingredients;

	public RecipeItem(String title, Set<IngredientItem> ingredients) {
		this.title = title;
		this.ingredients = ingredients;
	}

	public String getTitle() {
		return title;
	}

	public Set<IngredientItem> getIngredients() {
		return ingredients;
	}
}
