package com.mercateo.service;

import com.mercateo.model.Item;
import com.mercateo.model.Package;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ValidatorServiceTest {
	@Autowired ValidatorService validatorService;
	private Package aPackage;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Before
	public void setUp() {
		aPackage = new Package();
		aPackage.setTotalWeight(90);
		aPackage.getItemList().add(new Item());
	}

	@Test
	public void testPackageMaxWeight() {
		aPackage.setTotalWeight(120);
		exceptionRule.expect(ConstraintViolationException.class);
		exceptionRule.expectMessage("totalWeight: must be less than or equal to 100");
		validatorService.validatePackage(aPackage);
	}

	@Test
	public void testPackageMaxItems() {
		IntStream.range(1, 20).forEach(i -> aPackage.getItemList().add(new Item()));

		exceptionRule.expect(ConstraintViolationException.class);
		exceptionRule.expectMessage("itemList: package can only hold 15 items");

		validatorService.validatePackage(aPackage);
	}

	@Test
	public void testItemMaxWeight() {
		Item item = aPackage.getItemList().get(0);
		item.setWeight(150d);
		exceptionRule.expect(ConstraintViolationException.class);
		exceptionRule.expectMessage("itemList[0].weight: must be less than or equal to 100");

		validatorService.validatePackage(aPackage);
	}

	@Test
	public void testItemMaxCost() {
		Item item = aPackage.getItemList().get(0);
		item.setCost(150d);

		exceptionRule.expect(ConstraintViolationException.class);
		exceptionRule.expectMessage("itemList[0].cost: must be less than or equal to 100");

		validatorService.validatePackage(aPackage);
	}


	@Test
	public void testValidPackage() {
		assertTrue(validatorService.validatePackage(aPackage));
	}
}
