package com.standardkim.kanban.global.util;

import java.util.Random;

public class AlphanumericGenerator {
	private static final char[] charset = "abcdefghijklnmopqrstuvwxyz0123456789".toCharArray();
	private Random random;

	public AlphanumericGenerator(Random random) {
		this.random = random;
	}

	public String generate(int length) {
		char[] result = new char[length];

		for (int i = 0; i < result.length; i++) {
			int charsetIndex = random.nextInt(charset.length);
			result[i] = charset[charsetIndex];
		}

		return new String(result);
	}
}
