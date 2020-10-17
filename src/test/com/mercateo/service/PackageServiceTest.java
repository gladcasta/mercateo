package com.mercateo.service;

import com.mercateo.model.Package;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PackageServiceTest {
	@Autowired PackageService packageService;
	@Autowired PackageReader packageReader;
	private Package aPackage;

	@Test
	public void testPackage_whenItemIsOverTotal_thenOutputShouldDash() {
		Package myPackage = packageReader.generatePackage("10 : (1,25,€34)");
		assertEquals("-", packageService.findHighestItemsInPackage(myPackage));
	}

	@Test
	public void testPackage_whenItemsHaveSameWeightDiffCost_thenOutputShouldBeHighestPair() {
		Package myPackage = packageReader.generatePackage("100 : (1,25,€10) (2,25,€20) (3,25,€40) (4,25,€30)");
		assertEquals("3,4", packageService.findHighestItemsInPackage(myPackage));
	}

	@Test
	public void testPackage_whenItemHasSameWeightSameCost_thenOutputShouldBeHighestPair() {
		Package myPackage = packageReader.generatePackage("100 : (1,25,€10) (2,25,€10) (3,25,€10) (4,25,€10)");
		assertEquals("1,2", packageService.findHighestItemsInPackage(myPackage));
	}

	@Test
	/**
	 * 145.0=[[index=4, weight=11.2, cost=100.0], [index=1, weight=10.0, cost=45.0]],
	 * 110.0=[[index=4, weight=11.2, cost=100.0], [index=2, weight=25.0, cost=10.0]]}
	 */
	public void testPackage_whenItemsHaveDiffValues_thenOutputShouldBeHighestPair() {
		Package myPackage = packageReader.generatePackage("100 : (1,10,€45) (2,25,€10) (3,25,€45) (4,11.2,€100) (5,160,€1000)");
		assertEquals("1,4", packageService.findHighestItemsInPackage(myPackage));
	}


}
