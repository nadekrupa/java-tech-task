package com.rezdy.lunch.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rezdy.lunch.exception.RecipeNotFoundException;
import com.rezdy.lunch.persitence.entity.Ingredient;
import com.rezdy.lunch.persitence.entity.Recipe;
import com.rezdy.lunch.repository.LunchRepository;
import com.rezdy.lunch.response.IngredientItem;
import com.rezdy.lunch.response.RecipeItem;

/**
 * Implementation of the <code>LunchService</code> service. Which provides non
 * expired recipes on date.
 */
@Service
public class LunchServiceImpl implements LunchService {

	private LunchRepository lunchRepository;

	@Autowired
	public LunchServiceImpl(LunchRepository lunchRepository) {
		this.lunchRepository = lunchRepository;
	}

	@Override
	public List<RecipeItem> getNonExpiredRecipesOnDate(LocalDate date) {
		List<Recipe> nonExpiredRecipes = lunchRepository.findNonExpiredRecipeList(date);

		List<RecipeItem> recipes = getRecipeItems(nonExpiredRecipes);

		removeEntriesHavingNoIngredients(recipes);

		sortRecipes(recipes, date);

		return recipes;
	}

	/**
	 * If any recipe is not having any ingredients then remove it
	 * 
	 * @param recipes
	 */
	private void removeEntriesHavingNoIngredients(List<RecipeItem> recipes) {
		if (!isEmpty(recipes)) {
			recipes.removeIf(r -> isEmpty(r.getIngredients()));
		}
	}

	private List<RecipeItem> getRecipeItems(List<Recipe> recipes) {
		if (!isEmpty(recipes)) {
			return recipes.stream().map(r -> {
				return getRecipeItem(r);
			}).collect(Collectors.toList());
		}
		return emptyList();
	}

	private RecipeItem getRecipeItem(Recipe recipe) {
		Set<Ingredient> ingredients = recipe.getIngredients();
		Set<IngredientItem> ingredientItems = emptySet();

		if (!isEmpty(ingredients)) {
			ingredientItems = ingredients.stream().map(i -> {
				return new IngredientItem(i.getTitle(), i.getBestBefore(), i.getUseBy());
			}).collect(Collectors.toSet());
		}
		return new RecipeItem(recipe.getTitle(), ingredientItems);
	}

	private void sortRecipes(List<RecipeItem> recipes, LocalDate date) {
		if (recipes != null && !recipes.isEmpty()) {
			recipes.sort((recipe1, recipe2) -> compareRecipes(date, recipe1, recipe2));
		}
	}

	/**
	 * recipes with an ingredient past its "best before" at the bottom of the list,
	 * sorted by "best before" descending, recipe title ascending
	 * 
	 * @param date
	 * @param recipe1
	 * @param recipe2
	 * @return
	 */
	private int compareRecipes(LocalDate date, RecipeItem recipe1, RecipeItem recipe2) {
		Optional<LocalDate> minExpiredDateForRecipe1 = getMinExpiredBestBeforeDateForRecipe(date, recipe1);
		Optional<LocalDate> minExpiredDateForRecipe2 = getMinExpiredBestBeforeDateForRecipe(date, recipe2);

		// Try to prepare the recipe the best before date for which is still valid (Given
		// date or future)
		if (minExpiredDateForRecipe1.isPresent() && minExpiredDateForRecipe2.isEmpty()) {
			return 1;
		} else if (minExpiredDateForRecipe1.isEmpty() && minExpiredDateForRecipe2.isPresent()) {
			return -1;
		} else if (minExpiredDateForRecipe1.isPresent() && minExpiredDateForRecipe2.isPresent()) {
			int dateComparison = minExpiredDateForRecipe1.get().compareTo(minExpiredDateForRecipe2.get());

			// Cook the recipe for which the best before date is earliest
			if (dateComparison != 0) {
				return dateComparison;
			}
		}
		return recipe1.getTitle().compareTo(recipe2.getTitle());
	}

	/**
	 * 
	 * @param date
	 * @param recipe
	 * @return
	 */
	private Optional<LocalDate> getMinExpiredBestBeforeDateForRecipe(LocalDate currentDate, RecipeItem recipe) {
		return recipe.getIngredients().stream().map(IngredientItem::getBestBefore)
				.filter(bestBefore -> bestBefore.isBefore(currentDate)).min(LocalDate::compareTo);
	}

	/**
	 * Finds a recipe for a given title
	 * @param title - title of the recipe
	 * @return RecipeItem
	 * @throws RecipeNotFoundException if no recipe is found
	 */
	@Override
	public RecipeItem getRecipeByTitle(String title) {
		Optional<Recipe> recipe = lunchRepository.findById(title);
		
		if (recipe.isEmpty()) {
			throw new RecipeNotFoundException("No recipe found for title " + title);
		}
		return getRecipeItem(recipe.get());
	}

	/**
	 * Finds the list of recipes not having given ingredients
	 * @param ingredients - ingredients to be excluded
	 * @return list of recipes
	 */
	@Override
	public List<RecipeItem> findRecipesWithoutSpecifiedIngredients(List<String> ingredients) {
		List<Recipe> recipesFromDb = lunchRepository.findRecipesWithoutSpecifiedIngredients(ingredients);
		
		List<RecipeItem> recipes = getRecipeItems(recipesFromDb);
		
		removeEntriesHavingNoIngredients(recipes);
		
		return recipes;
	}
}
