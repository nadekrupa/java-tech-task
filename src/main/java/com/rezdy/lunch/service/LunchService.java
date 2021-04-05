package com.rezdy.lunch.service;

import java.time.LocalDate;
import java.util.List;

import com.rezdy.lunch.response.RecipeItem;

/**
 * Provides functions for finding recipes
 */
public interface LunchService {
	
	/**
	 * Finds all the recipes which can be cooked for a given date
	 * @param date
	 * @return list of recipes in sorted order
	 */
	public List<RecipeItem> getNonExpiredRecipesOnDate(LocalDate date);
	
	/**
	 * Finds a recipe for a given title
	 * @param title - title of the recipe
	 * @return RecipeItem
	 * @throws RecipeNotFoundException if no recipe is found
	 */
	public RecipeItem getRecipeByTitle(String title);

	/**
	 * Finds the list of recipes not having given ingredients
	 * @param ingredients - ingredients to be excluded
	 * @return list of recipes
	 */
	public List<RecipeItem> findRecipesWithoutSpecifiedIngredients(List<String> ingredients);

}
