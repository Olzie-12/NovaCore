package net.zeeraa.novacore.spigot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.utils.RandomGenerator;

public class VectorArea {
	protected Vector position1;
	protected Vector position2;

	/**
	 * Create a {@link VectorArea} from a {@link JSONObject}. The {@link JSONObject}
	 * needs to have the following properties: x1, y1, z1, x2, y2, z2
	 * 
	 * @since 1.1
	 * @param json Input {@link JSONObject}
	 * @return the {@link VectorArea} loaded from json
	 */
	public static VectorArea fromJSON(@Nonnull JSONObject json) {
		Vector p1 = new Vector(json.getDouble("x1"), json.getDouble("y1"), json.getDouble("z1"));
		Vector p2 = new Vector(json.getDouble("x2"), json.getDouble("y2"), json.getDouble("z2"));

		return new VectorArea(p1, p2);
	}

	public VectorArea(@Nonnull Vector position1, @Nonnull Vector position2) {
		this.position1 = new Vector((Math.min(position1.getX(), position2.getX())), (Math.min(position1.getY(), position2.getY())), (Math.min(position1.getZ(), position2.getZ())));
		this.position2 = new Vector((Math.max(position1.getX(), position2.getX())), (Math.max(position1.getY(), position2.getY())), (Math.max(position1.getZ(), position2.getZ())));
	}

	public VectorArea(int x1, int y1, int z1, int x2, int y2, int z2) {
		this(new Vector(x1, y1, z1), new Vector(x2, y2, z2));
	}

	public VectorArea(double x1, double y1, double z1, double x2, double y2, double z2) {
		this(new Vector(x1, y1, z1), new Vector(x2, y2, z2));
	}

	public VectorArea(float x1, float y1, float z1, float x2, float y2, float z2) {
		this(new Vector(x1, y1, z1), new Vector(x2, y2, z2));
	}

	public VectorArea clone() {
		return new VectorArea(this.getPosition1().clone(), this.getPosition2().clone());
	}

	/**
	 * Apply an offset to both position 1 and position 2 of this vector area
	 * 
	 * @param offset The {@link Vector} to add
	 * @return instance of this vector area
	 */
	public VectorArea applyOffset(@Nonnull Vector offset) {
		this.position1.add(offset);
		this.position2.add(offset);
		return this;
	}

	public VectorArea addOffset(@Nonnull Vector offset) {
		return this.applyOffset(offset);
	}

	public VectorArea subtractOffset(@Nonnull Vector offset) {
		this.position1.subtract(offset);
		this.position2.subtract(offset);
		return this;
	}

	public VectorArea addOffset(@Nonnull Location location) {
		return this.applyOffset(location.toVector());
	}

	public VectorArea subtractOffset(@Nonnull Location location) {
		return this.subtractOffset(location.toVector());
	}

	/**
	 * Apply an offset to both position 1 and position 2 of this vector area
	 * 
	 * @param offset The {@link Location} to add
	 * @return instance of this vector area
	 */
	public VectorArea applyOffset(@Nonnull Location offset) {
		return this.applyOffset(offset.toVector());
	}

	/**
	 * Get a {@link JSONObject} from this {@link VectorArea}
	 * 
	 * @return {@link JSONObject}
	 */
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();

		json.put("x1", position1.getX());
		json.put("y1", position1.getY());
		json.put("z1", position1.getZ());

		json.put("x2", position2.getX());
		json.put("y2", position2.getY());
		json.put("z2", position2.getZ());

		return json;
	}

	/**
	 * Check if a vectors position is inside this area. To check block location use
	 * {@link VectorArea#isInsideBlock(Vector)}
	 * 
	 * @since 1.1
	 * @param vector The vector to check
	 * @return <code>true</code> if the vectors location is inside this area
	 */
	public boolean isInside(@Nonnull Vector vector) {
		double x1 = position1.getX();
		double y1 = position1.getY();
		double z1 = position1.getZ();

		double x2 = position2.getX();
		double y2 = position2.getY();
		double z2 = position2.getZ();

		double x = vector.getX();
		double y = vector.getY();
		double z = vector.getZ();

		if (x >= x1 && x <= x2) {
			if (y >= y1 && y <= y2) {
				return z >= z1 && z <= z2;
			}
		}

		return false;
	}

	/**
	 * Check if a vectors block position is inside this area. To check location use
	 * {@link VectorArea#isInside(Vector)}
	 * 
	 * @param vector The vector to check
	 * @return <code>true</code> if the vectors block location is inside this area
	 */
	public boolean isInsideBlock(@Nonnull Vector vector) {
		int x1 = position1.getBlockX();
		int y1 = position1.getBlockY();
		int z1 = position1.getBlockZ();

		int x2 = position2.getBlockX();
		int y2 = position2.getBlockY();
		int z2 = position2.getBlockZ();

		int x = vector.getBlockX();
		int y = vector.getBlockY();
		int z = vector.getBlockZ();

		if (x >= x1 && x <= x2) {
			if (y >= y1 && y <= y2) {
				return z >= z1 && z <= z2;
			}
		}

		return false;
	}

	/**
	 * Check if a {@link Location} is inside this vector area
	 * 
	 * @param location The {@link Location} to check
	 * @return <code>true</code> if the location is inside
	 */
	public boolean isInside(@Nonnull Location location) {
		return this.isInside(location.toVector());
	}

	/**
	 * Check if a {@link Block} is inside this vector areas block location
	 * 
	 * @param block The {@link Block} to check
	 * @return <code>true</code> if the location is inside
	 */
	public boolean isInsideBlock(@Nonnull Block block) {
		return this.isInsideBlock(block.getLocation());
	}

	/**
	 * Check if a {@link Location} is inside this vector areas block location
	 * 
	 * @param location The {@link Location} to check
	 * @return <code>true</code> if the location is inside
	 */
	public boolean isInsideBlock(@Nonnull Location location) {
		return this.isInsideBlock(location.toVector());
	}

	/**
	 * Check if an {@link Entity} is inside this vector area
	 * 
	 * @param entity The {@link Entity} to check
	 * @return <code>true</code> if the entity is inside
	 */
	public boolean isInside(@Nonnull Entity entity) {
		return this.isInside(entity.getLocation());
	}

	/**
	 * Check if an {@link Entity} is inside this vector areas block location
	 * 
	 * @param entity The {@link Entity} to check
	 * @return <code>true</code> if the entity is inside
	 */
	public boolean isInsideBlock(@Nonnull Entity entity) {
		return this.isInsideBlock(entity.getLocation());
	}

	public Vector getDifferential() {
		return VectorUtils.getDifferential(position1, position2);
	}

	public List<Vector> getOutline(double distance) {
		List<Vector> result = new ArrayList<>();
		double minX = position1.getBlockX();
		double minY = position1.getBlockY();
		double minZ = position1.getBlockZ();
		double maxX = position2.getBlockX();
		double maxY = position2.getBlockY();
		double maxZ = position2.getBlockZ();

		for (double x = minX; x <= maxX; x = Math.round((x + distance) * 1e2) / 1e2) {
			for (double y = minY; y <= maxY; y = Math.round((y + distance) * 1e2) / 1e2) {
				for (double z = minZ; z <= maxZ; z = Math.round((z + distance) * 1e2) / 1e2) {
					int components = 0;
					if (x == minX || x == maxX)
						components++;
					if (y == minY || y == maxY)
						components++;
					if (z == minZ || z == maxZ)
						components++;
					if (components >= 2) {
						result.add(new Vector(x, y, z));
					}
				}
			}
		}
		return result;
	}

	public List<Vector> getOutline() {
		return this.getOutline(1);
	}

	/**
	 * Get a list of all block within the {@link VectorArea} as a {@link List} of
	 * {@link Vector}s
	 * 
	 * @return {@link List} with {@link Vector}s within
	 */
	public List<Vector> getAllVectors() {
		List<Vector> vectors = new ArrayList<>();

		for (int x = position1.getBlockX(); x <= position2.getBlockX(); x++) {
			for (int y = position1.getBlockY(); y <= position2.getBlockY(); y++) {
				for (int z = position1.getBlockZ(); z <= position2.getBlockZ(); z++) {
					vectors.add(new Vector(x, y, z));
				}
			}
		}

		return vectors;
	}

	/**
	 * Get the amount of blocks this {@link VectorArea} contains. This inludes air
	 * blocks
	 * 
	 * @return The amount of blocks in this area
	 */
	public int getArea() {
		int xLenght = position1.getBlockX() - position2.getBlockX();
		int yLenght = position1.getBlockY() - position2.getBlockY();
		int zLenght = position1.getBlockZ() - position2.getBlockZ();

		return xLenght * yLenght * zLenght;
	}

	/**
	 * Get the position with the lowest x, y and z values
	 * 
	 * @return The {@link Vector} with the lowest values
	 */
	public Vector getPosition1() {
		return position1;
	}

	/**
	 * Get the position with the highest x, y and z values
	 * 
	 * @return The {@link Vector} with the highest values
	 */
	public Vector getPosition2() {
		return position2;
	}

	public Vector getRandomVectorWithin() {
		return this.getRandomVectorWithin(new Random());
	}

	public Vector getRandomVectorWithin(@Nonnull Random random) {
		double x = RandomGenerator.generateDouble(position1.getX(), position2.getX(), random);
		double y = RandomGenerator.generateDouble(position1.getY(), position2.getY(), random);
		double z = RandomGenerator.generateDouble(position1.getZ(), position2.getZ(), random);

		return new Vector(x, y, z);
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " lower: " + position1.toString() + " upper: " + position2.toString();
	}
}