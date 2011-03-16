package it.drwolf.iscrizioni.util;

import it.drwolf.iscrizioni.entity.Iscritto;

import java.util.Random;

import javax.persistence.EntityManager;

public class IdGenerator {

	private static String charFor(long n) {
		if (n > 9) {
			n += 7;
		}
		n += 48;
		return Character.valueOf((char) n).toString().toLowerCase();
	}

	private static String convert(long number) {
		int base = 36;
		if (number < base) {
			return IdGenerator.charFor(number);
		} else {
			return IdGenerator.convert(number / base)
					+ IdGenerator.charFor(number % base);
		}
	}

	public static String newId(EntityManager entityManager) {
		String generated = IdGenerator
				.convert(new Random().nextInt() + 2778007459l);
		while (entityManager.find(Iscritto.class, generated) != null) {
			generated = IdGenerator
					.convert(new Random().nextInt() + 2778007459l);
		}

		return generated;
	}
}
