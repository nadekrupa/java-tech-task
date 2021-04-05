package com.rezdy.lunch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rezdy.lunch.persitence.entity.Recipe;

import java.time.LocalDate;
import java.util.List;

/**
 * Implements CRUD operations for the Recipes.
 */
@Repository
@Transactional
public interface LunchRepository extends JpaRepository<Recipe, String> {

	@Query(value = "select distinct recipe1.* from  recipe recipe1 where recipe1.title not in\n"
			+ "(select distinct recipe2.* from (select * from ingredient i where i.use_by < (:date)) a\n"
			+ "inner join recipe_ingredient ri on a.title = ri.ingredient\n"
			+ "inner join recipe recipe2 on recipe2.title = ri.recipe)", nativeQuery = true)
	List<Recipe> findNonExpiredRecipeList(@Param("date") LocalDate date);
	
    @Query("select distinct recipe from Recipe as recipe " +
            "where recipe.id not in (select distinct recipe2.id from Recipe as recipe2 " +
            "inner join recipe2.ingredients as ingredient2 where ingredient2.title in (:ingredients))")
    List<Recipe> findRecipesWithoutSpecifiedIngredients(@Param("ingredients") List<String> ingredients);
}
