package com.rezdy.lunch.response;

import java.time.LocalDate;

/**
 * Class representing request body of get recipe request.
 */
public class IngredientItem {
    private final String title;

    private final LocalDate bestBefore;

    private final LocalDate useBy;

	public IngredientItem(String title, LocalDate bestBefore, LocalDate useBy) {
		this.title = title;
		this.bestBefore = bestBefore;
		this.useBy = useBy;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getBestBefore() {
		return bestBefore;
	}

	public LocalDate getUseBy() {
		return useBy;
	}
}
