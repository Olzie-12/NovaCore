package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.util.Vector;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.utils.Pair;

/**
 * Utilities for {@link Vector}s
 * 
 * @author Zeeraa
 */
public class VectorUtils {
	/**
	 * Convert a {@link Vector} to a {@link JSONObject}.
	 * <p>
	 * The output json should look like this
	 * <code>{"x:" 0.0, "y:" 0.0, "z:" 0.0}</code>
	 * 
	 * @param vector The {@link Vector} to convert
	 * @return A {@link JSONObject} with the x, y and z values of the vector
	 */
	public static JSONObject toJSONObject(@Nonnull Vector vector) {
		JSONObject json = new JSONObject();

		json.put("x", vector.getX());
		json.put("y", vector.getY());
		json.put("z", vector.getZ());

		return json;
	}

	/**
	 * Convert a {@link JSONObject} to a {@link Vector}
	 * <p>
	 * The input json should look like this
	 * <code>{"x:" 0.0, "y:" 0.0, "z:" 0.0}</code>
	 * 
	 * @param json The {@link JSONObject} with the x, y and z values of the vector
	 * @return The {@link Vector}
	 */
	public static Vector fromJSONObject(@Nonnull JSONObject json) {
		double x = json.getDouble("x");
		double y = json.getDouble("y");
		double z = json.getDouble("z");

		return new Vector(x, y, z);
	}

	/**
	 * Get a {@link Pair} of {@link Vector}s from a json object.
	 * <p>
	 * The input json should look like this
	 * <code>{"x1:" 0.0, "y1:" 0.0, "z1:" 0.0, "x2:" 0.0, "y2:" 0.0, "z2:" 0.0}</code>
	 * 
	 * @param json The {@link JSONObject} to read
	 * @return {@link Pair} with {@link Vector}s
	 */
	public static Pair<Vector> vectorPairFromJSON(@Nonnull JSONObject json) {
		double x1 = json.getDouble("x1");
		double y1 = json.getDouble("y1");
		double z1 = json.getDouble("z1");

		double x2 = json.getDouble("x2");
		double y2 = json.getDouble("y2");
		double z2 = json.getDouble("z2");

		return new Pair<Vector>(new Vector(x1, y1, z1), new Vector(x2, y2, z2));
	}

	/**
	 * @return A vector with all values set to 0
	 */
	public static Vector getEmptyVector() {
		return new Vector(0D, 0D, 0D);
	}

	public static Vector getDifferential(@Nonnull Vector vector1, @Nonnull Vector vector2) {
		return new Vector(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
	}
}