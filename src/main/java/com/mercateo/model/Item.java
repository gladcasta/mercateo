package com.mercateo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.StringJoiner;

public class Item {
	private int index;

	@Min(value = 0, message = "must be greater than 0")
	@Max(value = 100, message = "must be less than or equal to 100")
	private Double weight;

	@Min(value = 0, message = "must be greater than 0")
	@Max(value = 100, message = "must be less than or equal to 100")
	private Double cost;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Override public String toString() {
		return new StringJoiner(", ", "[", "]")
				.add("index=" + index)
				.add("weight=" + weight)
				.add("cost=" + cost)
				.toString();
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Item item = (Item) o;

		if (index != item.index)
			return false;
		if (weight != null ? !weight.equals(item.weight) : item.weight != null)
			return false;
		return cost != null ? cost.equals(item.cost) : item.cost == null;
	}

	@Override public int hashCode() {
		int result = index;
		result = 31 * result + (weight != null ? weight.hashCode() : 0);
		result = 31 * result + (cost != null ? cost.hashCode() : 0);
		return result;
	}
}
