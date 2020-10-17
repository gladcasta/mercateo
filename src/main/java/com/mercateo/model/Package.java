package com.mercateo.model;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Package {
	@Min(value = 0, message = "must be greater than 0")
	@Max(value = 100, message = "must be less than or equal to 100")
	private Double totalWeight;

	@Size(max=15, message = "package can only hold 15 items")
	@Valid
	private List<Item> itemList;

	public Package() {
		this.itemList = new ArrayList<>();
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	@Override public String toString() {
		return new StringJoiner(", ", "[", "]")
				.add("totalWeight=" + totalWeight)
				.add("itemList=" + itemList)
				.toString();
	}
}
