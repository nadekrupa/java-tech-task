package com.rezdy.lunch.service;

import static com.rezdy.lunch.helper.LunchTestHelper.dateFormatter;
import static com.rezdy.lunch.helper.LunchTestHelper.getTestRecipes;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.rezdy.lunch.persitence.entity.Recipe;
import com.rezdy.lunch.repository.LunchRepository;
import com.rezdy.lunch.response.RecipeItem;

@RunWith(SpringRunner.class)
@SpringBootTest
class LunchServiceTest {

	@MockBean
	private LunchRepository lunchRepository;
	
	@Autowired
	private LunchService lunchService;
	
	@Test
	void shouldNotReturnAnyRecepieWhenAllIngrediendsAreExpired() {
		//given
		Mockito.doReturn(Collections.emptyList()).when(lunchRepository).findNonExpiredRecipeList(Mockito.any(LocalDate.class));
		
		//when
		List<RecipeItem> nonExpiredRecipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.now());
		
		//then
		assertEquals(nonExpiredRecipes.size(), 0);
	}
	
	@Test
	void shouldSortCorrectlyWhenBestBeforeDateIsInPastForSomeIngredients() {
		//given
		List<Recipe> recipes = getTestRecipes();
		Mockito.doReturn(recipes).when(lunchRepository).findNonExpiredRecipeList(Mockito.any(LocalDate.class));
		
		//when
		List<RecipeItem> resultRecipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.now());
		
		//then
		assertEquals(resultRecipes.size(), 2);
		assertEquals(resultRecipes.get(0).getTitle(), "Salad");
		assertEquals(resultRecipes.get(1).getTitle(), "Ham and Cheese Toastie");
	}
	
	@Test
	void shouldSortCorrectlyWhenBestBeforeDateIsInFutureForAllIngredients() {
		//given
		List<Recipe> recipes = getTestRecipes();
		
		Recipe recipe1 = recipes.get(0);
		recipe1.getIngredients().stream().filter(i -> i.getBestBefore().isBefore(LocalDate.now())).forEach(i -> {
			i.setBestBefore(LocalDate.parse("2029-01-01", dateFormatter));
		});

		Mockito.doReturn(recipes).when(lunchRepository).findNonExpiredRecipeList(Mockito.any(LocalDate.class));
		
		//when
		List<RecipeItem> resultRecipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.now());
		
		//then
		assertEquals(resultRecipes.size(), 2);
		
		assertEquals(resultRecipes.get(0).getTitle(), "Ham and Cheese Toastie");
		assertEquals(2, resultRecipes.get(0).getIngredients().size());
		
		assertEquals(resultRecipes.get(1).getTitle(), "Salad");
		assertEquals(2, resultRecipes.get(1).getIngredients().size());
	}
	
	@Test
	void shouldSortCorrectlyWhenBestBeforeDateIsInPastForAllIngredients() {
		// given
		List<Recipe> recipes = getTestRecipes();
		
		Recipe recipe1 = recipes.get(0);
		Recipe recipe2 = recipes.get(0);
		
		recipe1.getIngredients().stream().filter(i -> i.getBestBefore().isAfter(LocalDate.now())).forEach(i -> {
			i.setBestBefore(LocalDate.parse("1999-01-01", dateFormatter));
		});
		
		recipe2.getIngredients().stream().filter(i -> i.getBestBefore().isAfter(LocalDate.now())).forEach(i -> {
			i.setBestBefore(LocalDate.parse("2000-01-01", dateFormatter));
		});
		
		Mockito.doReturn(recipes).when(lunchRepository).findNonExpiredRecipeList(Mockito.any(LocalDate.class));
		
		//when
		List<RecipeItem> resultRecipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.now());
		
		//then
		assertEquals(resultRecipes.size(), 2);
		assertEquals(resultRecipes.get(0).getTitle(), "Salad");
		assertEquals(2, resultRecipes.get(0).getIngredients().size());
		
		assertEquals(resultRecipes.get(1).getTitle(), "Ham and Cheese Toastie");
		assertEquals(2, resultRecipes.get(1).getIngredients().size());
	}
	
	@Test
	void shouldFilterRecipeWhenNoIngridientsPresent() {
		//given
		List<Recipe> recipes = getTestRecipes();
		
		Recipe recipe1 = recipes.get(0);
		recipe1.setIngredients(null);
	
		Mockito.doReturn(recipes).when(lunchRepository).findNonExpiredRecipeList(Mockito.any(LocalDate.class));
		
		//when
		List<RecipeItem> resultRecipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.now());
		
		//then
		assertEquals(resultRecipes.size(), 1);
		assertEquals(resultRecipes.get(0).getTitle(), "Salad");
		assertEquals(2, resultRecipes.get(0).getIngredients().size());
	}
}