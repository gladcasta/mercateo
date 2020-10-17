package com.mercateo.service;

import com.mercateo.model.Item;
import com.mercateo.model.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PackageService {
	private static Logger LOG = LoggerFactory.getLogger(PackageService.class);

	public String findHighestItemsInPackage(Package myPackage) {
		Map<Double, List<Item>> costPerPackage = findCostPerItem(myPackage);
		LOG.debug("costPerPackage: {}", costPerPackage);
		return costPerPackage.keySet().stream()
				.sorted((k1, k2) -> k1.compareTo(k2) * -1)
				.findFirst() // get the highest cost
				.map(key -> costPerPackage.get(key))
				.map(item -> item.stream()
						.map(Item::getIndex)
						.sorted(Integer::compareTo)
						.map(Objects::toString)
						.collect(Collectors.joining(",")))
				.orElse("-");
	}

	/**
	 * Removes items that are over the total weight and sorts the items by cost then by weight
	 * @param myPackage
	 * @return sorted items that have weight less than the package's total weight
	 */
	private List<Item> filterAndSort(Package myPackage) {
		return  myPackage.getItemList()
				.stream()
				// remove items bigger than the total weight
				.filter(item -> item.getWeight() < myPackage.getTotalWeight())
				// sort by higher cost, and if the costs are the same, sort by weight
				.sorted(Comparator.comparing(Item::getCost).reversed().thenComparing(Item::getWeight))
				.collect(Collectors.toList());
	}

	/**
	 * First: remove items that are over the total weight and sort the item by cost, and then by weight.
	 * Since we sorted the items, we're guaranteed that the item(s) with the highest cost will always fit the package.
	 * Second: For each item, check if there is other item that can be combined within the total weight.
	 * If so, put the cost in the map, and the 2 items.
	 *
	 * @param itemList
	 * @return map of sum of cost, and the item or pair of items.
	 */
	private Map<Double,  List<Item>> findCostPerItem(Package myPackage) {
		List<Item> sortedItems = filterAndSort(myPackage);
		LOG.debug("Sorted list: {}", sortedItems);

		if (sortedItems.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Double, List<Item>> listMap = new HashMap<>(); //cost, item/pair

		final double totalWeight = myPackage.getTotalWeight();
		double maxCost = Integer.MIN_VALUE;
		for (int i = 0; i < sortedItems.size(); i++) {
			Item item = sortedItems.get(i);
			double currentWeight = item.getWeight();

			double currentItemCost = item.getCost();

			// if maxCost from previous iteration is already greater than the currentItemCost,
			// we can already return the map, since there'll be no item that can result to higher cost.
			if (maxCost > currentItemCost) {
				return listMap;
			}

			for (int j = i+1; j < sortedItems.size(); j++) {
				Item nextItem = sortedItems.get(j);
				if (currentWeight + nextItem.getWeight() <= totalWeight) {
					List<Item> itemPair = new ArrayList<>(2);
					itemPair.add(item);
					itemPair.add(nextItem);
					listMap.putIfAbsent(currentItemCost + nextItem.getCost(), itemPair);
					maxCost = Math.max(maxCost, currentItemCost + nextItem.getCost());
				}
			}

			if (listMap.isEmpty()) {
				listMap.put(currentItemCost, Collections.singletonList(item));
			}
		}

		return listMap;
	}

}
