package com.mercateo.service;

import com.mercateo.model.Package;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
public class ValidatorService {
	private Validator validator;

	public ValidatorService(Validator validator) {
		this.validator = validator;
	}

	public boolean validatePackage(Package myPackage) {
		if (myPackage == null) {
			throw new ConstraintViolationException("Invalid input", null);
		}
		Set<ConstraintViolation<Package>> violations = validator.validate(myPackage);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return true;
	}

}
