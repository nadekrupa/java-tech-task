package com.rezdy.lunch.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rezdy.lunch.persitence.entity.Ingredient;
import com.rezdy.lunch.persitence.entity.Recipe;

public class LunchTestHelper {
	
	public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
	
	public static List<Recipe> getTestRecipes() {
		List<Recipe> recipes = new ArrayList<>();
		
		//Start -- build recipe1
		Recipe recipe1 = new Recipe();
		recipe1.setTitle("Ham and Cheese Toastie");
		
		Ingredient ingredient1 = new Ingredient();
		ingredient1.setTitle("Ham");
		ingredient1.setBestBefore(LocalDate.parse("2030-12-31", dateFormatter));
		ingredient1.setUseBy(LocalDate.parse("2025-01-01", dateFormatter));
		
		Ingredient ingredient2 = new Ingredient();
		ingredient2.setTitle("Cheese");
		ingredient2.setBestBefore(LocalDate.parse("1999-01-01", dateFormatter));
		ingredient2.setUseBy(LocalDate.parse("2030-01-01", dateFormatter));
		
		Set<Ingredient> ingredients1 = new HashSet<>();
		ingredients1.add(ingredient1);
		ingredients1.add(ingredient2);
		recipe1.setIngredients(ingredients1);
		//End -- build recipe1
		
		//Start -- build recipe2
		Recipe recipe2 = new Recipe();
		recipe2.setTitle("Salad");
		
		Ingredient ingredient3 = new Ingredient();
		ingredient3.setTitle("Cucumber");
		ingredient3.setBestBefore(LocalDate.parse("2030-12-31", dateFormatter));
		ingredient3.setUseBy(LocalDate.parse("2030-01-01", dateFormatter));
		
		Ingredient ingredient4 = new Ingredient();
		ingredient4.setTitle("Beetroot");
		ingredient4.setBestBefore(LocalDate.parse("2025-01-01", dateFormatter));
		ingredient4.setUseBy(LocalDate.parse("2030-01-01", dateFormatter));
		
		Set<Ingredient> ingredients2 = new HashSet<>();
		ingredients2.add(ingredient3);
		ingredients2.add(ingredient4);
		recipe2.setIngredients(ingredients2);
		//End -- build recipe2
		
		recipes.add(recipe1);
		recipes.add(recipe2);
		return recipes;
	}
}
