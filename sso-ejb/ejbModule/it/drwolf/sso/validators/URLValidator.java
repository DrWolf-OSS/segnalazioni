package it.drwolf.sso.validators;

import java.net.URL;

import org.hibernate.mapping.Property;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Validator;

public class URLValidator implements Validator<ValidURL>, PropertyConstraint {

	public void apply(Property arg0) {
		// TODO Auto-generated method stub

	}

	public void initialize(ValidURL arg0) {
		// TODO Auto-generated method stub

	}

	public boolean isValid(Object value) {
		try {
			new URL(value.toString());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
