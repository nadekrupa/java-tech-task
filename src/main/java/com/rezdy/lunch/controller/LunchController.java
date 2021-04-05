package com.rezdy.lunch.controller;

import static org.springframework.util.StringUtils.isEmpty;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rezdy.lunch.exception.InvalidInputException;
import com.rezdy.lunch.request.RecipeFetchByExcludingIngredientsRequest;
import com.rezdy.lunch.request.RecipeFetchByTitleRequest;
import com.rezdy.lunch.response.RecipeItem;
import com.rezdy.lunch.service.LunchService;

/**
 * 
 * Rest controller with entry points to perform lunch service operation.
 *
 */
@RestController
public class LunchController {

	private LunchService lunchService;

	@Autowired
	public LunchController(LunchService lunchService) {
		this.lunchService = lunchService;
	}

	@GetMapping("/lunch")
	public List<RecipeItem> getNonExpiredRecipesOnDate(@RequestParam(value = "date") String date) {
		return lunchService.getNonExpiredRecipesOnDate(LocalDate.parse(date));
	}

	@PostMapping("/lunch/find/by-title")
	public RecipeItem getRecipeByTitle(@RequestBody RecipeFetchByTitleRequest request) {
		if (isEmpty(request.getTitle())) {
			throw new InvalidInputException("Please provide a valid recipe title!");
		}
		return lunchService.getRecipeByTitle(request.getTitle());
	}

	@PostMapping("/lunch/find/by-excluding-ingredient")
	public List<RecipeItem> findRecipesWithoutSpecifiedIngredients(
			@RequestBody RecipeFetchByExcludingIngredientsRequest request) {
		if (CollectionUtils.isEmpty(request.getExcludedIngredients())) {
			throw new InvalidInputException("Please provide a ingredients to be excluded!");
		}
		List<RecipeItem> recipes = lunchService
				.findRecipesWithoutSpecifiedIngredients(request.getExcludedIngredients());
		return recipes;
	}
}