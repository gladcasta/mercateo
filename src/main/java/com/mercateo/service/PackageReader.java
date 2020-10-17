package com.mercateo.service;

import com.mercateo.model.Item;
import com.mercateo.model.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PackageReader {
	private static final Pattern pattern = Pattern.compile("\\((\\d*),(\\d.*),€(\\d.*)\\)");
	private static final Logger LOGGER = LoggerFactory.getLogger(PackageReader.class);

	/**
	 * Generates a list of package based on the input file
	 * @param fileInput full path of where to read the input file
	 * @return list of package generated
	 */
	public List<Package> readFile(String fileInput) {
		List<Package> packages = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File(fileInput),  "UTF-8")) {
			//read the numbers
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				LOGGER.debug(line);
				if (!StringUtils.isEmpty(line)) {
					Package aPackage = generatePackage(line);
					packages.add(aPackage);
				}
			}
		} catch (FileNotFoundException fileException) {
			LOGGER.error("Cannot open file: " + fileInput, fileException);
		}
		return packages;
	}

	/**
	 * Checks if the line follows the correct format and creates package object based on it.
	 * Format is: totalWeight : (item) (item) ....
	 * where item is (index,weight,cost)
	 * @param line
	 * @return package if it follows the correct format, otherwise returns null
	 */
	public Package generatePackage(String line) {
		try {
			String[] allSplits = line.split(" ");
			Package aPackage = new Package();
			aPackage.setTotalWeight(Double.parseDouble(allSplits[0]));

			for (int i = 2; i <allSplits.length; i++) {
				Matcher matcher = pattern.matcher(allSplits[i]);
				if (matcher.matches()) {
					Item item = new Item();
					item.setIndex(Integer.parseInt(matcher.group(1)));
					item.setWeight(Double.parseDouble(matcher.group(2)));
					item.setCost(Double.parseDouble(matcher.group(3)));
					aPackage.getItemList().add(item);
				} else {
					LOGGER.warn("Invalid value encountered for {}, package does not follow the format = {}", line, allSplits[i]);
					return null;
				}
			}
			return aPackage;
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			LOGGER.warn("Invalid value encountered for {}", line, e.getMessage());
		}
		return null;
	}

}
