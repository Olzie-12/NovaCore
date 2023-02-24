package net.zeeraa.novacore.commons.utils;

import java.util.Random;

/**
 * Functions to generate random data
 * 
 * @author Zeeraa
 */
public class RandomGenerator {
	private static final Random DEFAULT_RANDOM_INSTACE = new Random();

	/**
	 * This is used to generate numbers in a certain range
	 * <p>
	 * The min and max number will be included in the possible results
	 * 
	 * @param min The minimum number to generate
	 * @param max The maximum number to generate
	 * @return A random number between min and max
	 */
	public static int generate(int min, int max) {
		return RandomGenerator.generate(min, max, DEFAULT_RANDOM_INSTACE);
	}

	/**
	 * This is used to generate numbers in a certain range
	 * <p>
	 * The min and max number will be included in the possible results
	 * 
	 * @param min    The minimum number to generate
	 * @param max    The maximum number to generate
	 * @param random The {@link Random} instance to use
	 * @return A random number between min and max
	 */
	public static int generate(int min, int max, Random random) {
		return random.nextInt((max - min) + 1) + min;
	}

	/**
	 * This is used to generate numbers in a certain range
	 * <p>
	 * This will generate a value between the 2 numbers
	 * 
	 * @param min The minimum number to generate
	 * @param max The maximum number to generate
	 * @return A random number between min and max
	 */
	public static double generateDouble(double min, double max) {
		return RandomGenerator.generateDouble(min, max, DEFAULT_RANDOM_INSTACE);
	}

	/**
	 * This is used to generate numbers in a certain range
	 * <p>
	 * This will generate a value between the 2 numbers
	 * 
	 * @param min    The minimum number to generate
	 * @param max    The maximum number to generate
	 * @param random The {@link Random} instance to use
	 * @return A random number between min and max
	 */
	public static double generateDouble(double min, double max, Random random) {
		return (random.nextDouble() * (max - min)) + min;
	}

	/**
	 * This is used to generate numbers in a certain range
	 * <p>
	 * The min and max number will be included in the possible results
	 *
	 * @param min The minimum number to generate
	 * @param max The maximum number to generate
	 * @return A random number between min and max
	 */
	public static float generateFloat(float min, float max) {
		return RandomGenerator.generateFloat(min, max, DEFAULT_RANDOM_INSTACE);
	}

	/**
	 * This is used to generate numbers in a certain range
	 * <p>
	 * The min and max number will be included in the possible results
	 *
	 * @param min    The minimum number to generate
	 * @param max    The maximum number to generate
	 * @param random The {@link Random} instance to use
	 * @return A random number between min and max
	 */
	public static float generateFloat(float min, float max, Random random) {
		return (random.nextFloat() * (max - min)) + min;
	}

	/**
	 * This is used to generate numbers in a certain range
	 * <p>
	 * The min and max number will be included in the possible results
	 *
	 * @param min The minimum number to generate
	 * @param max The maximum number to generate
	 * @return A random number between min and max
	 */
	public static long generateLong(long min, long max) {
		return RandomGenerator.generateLong(min, max, DEFAULT_RANDOM_INSTACE);
	}

	/**
	 * This is used to generate numbers in a certain range
	 * <p>
	 * The min and max number will be included in the possible results
	 *
	 * @param min    The minimum number to generate
	 * @param max    The maximum number to generate
	 * @param random The {@link Random} instance to use
	 * @return A random number between min and max
	 */
	public static long generateLong(long min, long max, Random random) {
		return (long) (random.nextDouble() * (max - min) + min);
	}

}