package com.mercateo;

import com.mercateo.model.Package;
import com.mercateo.service.PackageReader;
import com.mercateo.service.PackageService;
import com.mercateo.service.ValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.validation.ConstraintViolationException;
import java.util.*;

@SpringBootApplication
public class Main implements CommandLineRunner {
    private static Logger LOG = LoggerFactory.getLogger(Main.class);
    @Autowired PackageReader packageReader;
    @Autowired ValidatorService validatorService;
    @Autowired PackageService packageService;
    @Autowired ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override public void run(String... args) {
        if (args == null || args.length != 1) {
            LOG.info("Please enter file path for input.");
            return;
        }
        List<Package> packages = packageReader.readFile(args[0]);
        StringBuilder sb = new StringBuilder("\n");

        for (int i = 0; i<packages.size(); i++ ) {
            try {
                validatorService.validatePackage(packages.get(i));
                String result = packageService.findHighestItemsInPackage(packages.get(i));
                sb.append(result);
                sb.append("\n\n");

            } catch (ConstraintViolationException e) {
                sb.append("Invalid input for line:" + e.getMessage());
                sb.append("\n\n");
            }
        }
        LOG.info(sb.toString());
        context.close();
    }
}
