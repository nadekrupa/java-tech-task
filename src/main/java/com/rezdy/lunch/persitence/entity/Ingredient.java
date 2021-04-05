package com.rezdy.lunch.persitence.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents Ingredient Entity in the database
 */
@Entity
public class Ingredient {

    @Id
    private String title;

    @Column(name = "best_before")
    private LocalDate bestBefore;

    @Column(name = "use_by")
    private LocalDate useBy;

    public String getTitle() {
        return title;
    }

    public Ingredient setTitle(String title) {
        this.title = title;
        return this;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    public Ingredient setBestBefore(LocalDate bestBefore) {
        this.bestBefore = bestBefore;
        return this;
    }

    public LocalDate getUseBy() {
        return useBy;
    }

    public Ingredient setUseBy(LocalDate useBy) {
        this.useBy = useBy;
        return this;
    }

	@Override
	public int hashCode() {
		return Objects.hash(bestBefore, title, useBy);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredient other = (Ingredient) obj;
		return Objects.equals(bestBefore, other.bestBefore) && Objects.equals(title, other.title)
				&& Objects.equals(useBy, other.useBy);
	}

	@Override
	public String toString() {
		return "Ingredient [title=" + title + ", bestBefore=" + bestBefore + ", useBy=" + useBy + "]";
	}
}
