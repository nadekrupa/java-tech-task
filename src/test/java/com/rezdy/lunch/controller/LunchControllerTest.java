package com.rezdy.lunch.controller;

import static com.rezdy.lunch.helper.LunchTestHelper.dateFormatter;
import static com.rezdy.lunch.helper.LunchTestHelper.getTestRecipes;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.rezdy.lunch.persitence.entity.Ingredient;
import com.rezdy.lunch.persitence.entity.Recipe;
import com.rezdy.lunch.repository.LunchRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class LunchControllerTest {

	@MockBean
	private LunchRepository lunchRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void shouldFailIfInputDateIsInvalid() throws Exception {
		// given
		Mockito.doReturn(Collections.emptyList()).when(lunchRepository)
				.findNonExpiredRecipeList(Mockito.any(LocalDate.class));

		// then
        mockMvc.perform(MockMvcRequestBuilders
        		.get("/lunch")
        		.param("date", "2029/01-01"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.errorMessage", Matchers.is("Text '2029/01-01' could not be parsed at index 4")));
	}

	@Test
	void shouldNotProvideRecipesIfNoEntriesInDB() throws Exception {
		// given
		Mockito.doReturn(Collections.emptyList()).when(lunchRepository)
				.findNonExpiredRecipeList(Mockito.any(LocalDate.class));

		// then
        mockMvc.perform(MockMvcRequestBuilders.get("/lunch").param("date", "2029-01-01"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()", Matchers.is(0)));
	}

	@Test
	void shouldProvideRecipesIfEntriesInDB() throws Exception {
		// given
		Mockito.doReturn(getTestRecipes()).when(lunchRepository).findNonExpiredRecipeList(Mockito.any(LocalDate.class));

		// then
        mockMvc.perform(MockMvcRequestBuilders.get("/lunch").param("date", "2029-01-01"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()", Matchers.is(2)))
        .andExpect(jsonPath("$.[*].title",
                Matchers.hasItems("Salad", "Ham and Cheese Toastie")
        ));
	}

	@Test
	void shouldFailIfNoTitleProvided() throws Exception {
		// given
		String requestBody = "{\"title\":\"\"}";

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/lunch/find/by-title")
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldFailIfNoRecipePresentForGivenTitle() throws Exception {
		
		// given
		Mockito.doReturn(getTestRecipes()).when(lunchRepository)
				.findRecipesWithoutSpecifiedIngredients(Mockito.any());
		String requestBody = "{\"title\":\"test\"}";

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/lunch/find/by-title")
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestBody))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldProvideRecipeByTitleIfEntriesInDB() throws Exception {
		// given
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

		Mockito.doReturn(Optional.of(recipe1)).when(lunchRepository).findById(Mockito.any(String.class));
		
		// given
		Mockito.doReturn(getTestRecipes()).when(lunchRepository)
				.findRecipesWithoutSpecifiedIngredients(Mockito.any());
		String requestBody = "{\"title\":\"test\"}";

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/lunch/find/by-title")
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title", Matchers.is("Ham and Cheese Toastie")));
	}

	@Test
	void shouldFailIfNoIngridentsProvided() throws Exception {
		/*
		 * assertEquals(exception.getMessage(),
		 * "Please provide a ingredients to be excluded!");
		 */
		
		String requestBody = "{\"excludedIngredients\":[]}";

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/lunch/find/by-excluding-ingredient")
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorMessage", Matchers.is("Please provide a ingredients to be excluded!")));
	}

	@Test
	void shouldReturnRecipesIfExcludedIngridentsAreDefined() throws Exception {
		// given
		Mockito.doReturn(getTestRecipes()).when(lunchRepository)
				.findRecipesWithoutSpecifiedIngredients(Mockito.any());
		String requestBody = "{\"excludedIngredients\":[\"Spinach\",\"Potato\"]}";

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/lunch/find/by-excluding-ingredient")
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestBody))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", Matchers.is(2)));
	}
}
