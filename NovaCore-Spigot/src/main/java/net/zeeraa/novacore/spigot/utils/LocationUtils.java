package net.zeeraa.novacore.spigot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.zeeraa.novacore.spigot.abstraction.commons.EntityBoundingBox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.utils.RandomGenerator;
import net.zeeraa.novacore.commons.utils.Rotation;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.module.modules.multiverse.MultiverseWorld;

/**
 * Utils to compare and modify locations
 * 
 * @author Zeeraa
 */
public class LocationUtils {
	public static boolean isOutsideOfBorder(@Nonnull Location location) {
		WorldBorder border = location.getWorld().getWorldBorder();
		double size = border.getSize() / 2;
		Location center = border.getCenter();
		double x = location.getX() - center.getX(), z = location.getZ() - center.getZ();
		return ((x > size || (-x) > size) || (z > size || (-z) > size));
	}

	/**
	 * Check if 2 locations have matching {@link Location#getWorld()},
	 * {@link Location#getBlockX()}, {@link Location#getBlockY()} and
	 * {@link Location#getBlockZ()}
	 * 
	 * @param location1 Location 1 to check
	 * @param location2 Location 2 to check
	 * @return <code>true</code> if matching
	 */
	public static boolean isSameBlock(@Nonnull Location location1, @Nonnull Location location2) {
		if (location1.getWorld().equals(location2.getWorld())) {
			if (location1.getBlockX() == location2.getBlockX()) {
				if (location1.getBlockY() == location2.getBlockY()) {
					return location1.getBlockZ() == location2.getBlockZ();
				}
			}
		}
		return false;
	}

	/**
	 * Check if a location and vector have matching block X, Y and Z values
	 * 
	 * @param location1 Location 1 to check
	 * @param location2 Location 2 to check
	 * @return <code>true</code> if matching
	 */
	public static boolean isSameBlock(@Nonnull Location location1, @Nonnull Vector location2) {
		if (location1.getBlockX() == location2.getBlockX()) {
			if (location1.getBlockY() == location2.getBlockY()) {
				return location1.getBlockZ() == location2.getBlockZ();
			}
		}
		return false;
	}

	/**
	 * Check if 2 locations have matching {@link Location#getBlockX()},
	 * {@link Location#getBlockY()} and {@link Location#getBlockZ()}
	 * 
	 * @param location1 Location 1 to check
	 * @param location2 Location 2 to check
	 * @return <code>true</code> if matching
	 */
	public static boolean isSameVector(@Nonnull Location location1, @Nonnull Location location2) {
		if (location1.getBlockX() == location2.getBlockX()) {
			if (location1.getBlockY() == location2.getBlockY()) {
				return location1.getBlockZ() == location2.getBlockZ();
			}
		}
		return false;
	}

	public static double blockCenter(int block) {
		/*
		 * if (block >= 0) { return ((double) block) + 0.5; } if (block < 0) { return
		 * ((double) block) - 0.5; } return block;
		 */

		// return ((double) block) + (block < 0 ? -0.5D : 0.5D);

		return ((double) block) + 0.5D;
	}

	public static JSONObject toJSONObject(@Nonnull Location location) {
		JSONObject result = new JSONObject();

		result.put("x", location.getX());
		result.put("y", location.getY());
		result.put("z", location.getZ());

		result.put("yaw", location.getYaw());
		result.put("pitch", location.getPitch());

		result.put("world", location.getWorld().getName());

		return result;
	}

	public static int getHighestYAtLocation(@Nonnull Location location) {
		return LocationUtils.getHighestYAtLocation(location, false, false);
	}

	public static int getHighestYAtLocation(@Nonnull Location location, boolean ignoreNonSolid) {
		return LocationUtils.getHighestYAtLocation(location, ignoreNonSolid, false);
	}

	public static int getHighestYAtLocation(@Nonnull Location location, boolean ignoreNonSolid, boolean ignoreLiquid) {
		int max = location.getWorld().getMaxHeight() - 1;
		int min = VersionIndependentUtils.get().getMinY();

		for (int y = max; y >= min; y--) {
			Block block = location.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ());

			if (block.getType() == Material.AIR) {
				continue;
			}

			if (block.isLiquid() && ignoreLiquid) {
				continue;
			}

			if (!block.getType().isSolid() && ignoreNonSolid) {
				continue;
			}

			return y;
		}
		Log.trace("Failure. Use default");

		return max;
	}

	public static @Nullable Block getHighestBlockAtLocation(@Nonnull Location location) {
		return LocationUtils.getHighestBlockAtLocation(location, false, false);
	}

	public static @Nullable Block getHighestBlockAtLocation(@Nonnull Location location, boolean ignoreNonSolid) {
		return LocationUtils.getHighestBlockAtLocation(location, ignoreNonSolid, false);
	}

	public static @Nullable Block getHighestBlockAtLocation(@Nonnull Location location, boolean ignoreNonSolid, boolean ignoreLiquid) {
		int max = location.getWorld().getMaxHeight() - 1;
		int min = VersionIndependentUtils.get().getMinY();

		for (int y = max; y >= min; y--) {
			Block block = location.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ());

			if (block.getType() == Material.AIR) {
				continue;
			}

			if (block.isLiquid() && ignoreLiquid) {
				continue;
			}

			if (!block.getType().isSolid() && ignoreNonSolid) {
				continue;
			}

			return block;
		}

		return null;
	}

	public static Location fromJSONObject(@Nonnull JSONObject json) {
		World world = Bukkit.getServer().getWorld(json.getString("world"));

		if (world == null) {
			world = LocationUtils.getFirstAvaliableWorld();
		}

		double x = json.getDouble("x");
		double y = json.getDouble("y");
		double z = json.getDouble("z");

		float yaw = 0;

		if (json.has("yaw")) {
			yaw = json.getFloat("yaw");
		}

		float pitch = 0;

		if (json.has("pitch")) {
			pitch = json.getFloat("pitch");
		}

		return new Location(world, x, y, z, yaw, pitch);
	}

	public static Location fromJSONObject(@Nonnull JSONObject json, World world) {
		double x = json.getDouble("x");
		double y = json.getDouble("y");
		double z = json.getDouble("z");

		float yaw = 0;

		if (json.has("yaw")) {
			yaw = json.getFloat("yaw");
		}

		float pitch = 0;

		if (json.has("pitch")) {
			pitch = json.getFloat("pitch");
		}

		return new Location(world, x, y, z, yaw, pitch);
	}

	public static World getFirstAvaliableWorld() {
		return Bukkit.getServer().getWorlds().get(0);
	}

	/**
	 * Construct a location from a {@link Vector}
	 * 
	 * @param world  The {@link World}
	 * @param vector The {@link Vector}
	 * @return The {@link Location}
	 */
	public static Location getLocation(@Nonnull World world, @Nonnull Vector vector) {
		return LocationUtils.getLocation(world, vector, 0, 0);
	}

	/**
	 * Construct a location from a {@link Vector}
	 * 
	 * @param world  The {@link World}
	 * @param vector The {@link Vector}
	 * @param yaw    The yaw
	 * @return The {@link Location}
	 */
	public static Location getLocation(@Nonnull World world, @Nonnull Vector vector, float yaw) {
		return LocationUtils.getLocation(world, vector, yaw, 0);
	}

	/**
	 * Construct a location from a {@link Vector}
	 * 
	 * @param world    The {@link World}
	 * @param vector   The {@link Vector}
	 * @param rotation The {@link Rotation}
	 * @return The {@link Location}
	 */
	public static Location getLocation(@Nonnull World world, @Nonnull Vector vector, @Nonnull Rotation rotation) {
		return LocationUtils.getLocation(world, vector, rotation.getYaw(), rotation.getPitch());
	}

	/**
	 * Construct a location from a {@link Vector}
	 * 
	 * @param world  The {@link World}
	 * @param vector The {@link Vector}
	 * @param yaw    The yaw
	 * @param pitch  The pitch
	 * @return The {@link Location}
	 */
	public static Location getLocation(@Nonnull World world, @Nonnull Vector vector, float yaw, float pitch) {
		return new Location(world, vector.getX(), vector.getY(), vector.getZ(), yaw, pitch);
	}

	/**
	 * Construct a location from a {@link Vector}
	 * 
	 * @param world  The {@link MultiverseWorld}
	 * @param vector The {@link Vector}
	 * @return The {@link Location}
	 */
	public static Location getLocation(@Nonnull MultiverseWorld world, @Nonnull Vector vector) {
		return LocationUtils.getLocation(world, vector, 0, 0);
	}

	/**
	 * Construct a location from a {@link Vector}
	 * 
	 * @param world  The {@link MultiverseWorld}
	 * @param vector The {@link Vector}
	 * @param yaw    The yaw
	 * @return The {@link Location}
	 */
	public static Location getLocation(@Nonnull MultiverseWorld world, @Nonnull Vector vector, float yaw) {
		return LocationUtils.getLocation(world, vector, yaw, 0);
	}

	/**
	 * Construct a location from a {@link Vector}
	 * 
	 * @param world    The {@link MultiverseWorld}
	 * @param vector   The {@link Vector}
	 * @param rotation The {@link Rotation}
	 * @return The {@link Location}
	 */
	public static Location getLocation(@Nonnull MultiverseWorld world, @Nonnull Vector vector, @Nonnull Rotation rotation) {
		return LocationUtils.getLocation(world, vector, rotation.getYaw(), rotation.getPitch());
	}

	/**
	 * Construct a location from a {@link Vector}
	 * 
	 * @param world  The {@link MultiverseWorld}
	 * @param vector The {@link Vector}
	 * @param yaw    The yaw
	 * @param pitch  The pitch
	 * @return The {@link Location}
	 */
	public static Location getLocation(@Nonnull MultiverseWorld world, @Nonnull Vector vector, float yaw, float pitch) {
		return LocationUtils.getLocation(world.getWorld(), vector, yaw, pitch);
	}

	/**
	 * Get a copy of the provided location with its x and z centered. This will not
	 * modify the provided {@link Location} object
	 * 
	 * @param location The location to get a centered copy of
	 * @return New location with centered x and z
	 */
	public static Location centerLocation(@Nonnull Location location) {
		Location newLocation = location.clone();

		newLocation.setX(LocationUtils.blockCenter(newLocation.getBlockX()));
		newLocation.setZ(LocationUtils.blockCenter(newLocation.getBlockZ()));

		return newLocation;
	}

	/**
	 * Get a copy of the provided location with its x, y and z centered. This will
	 * not modify the provided {@link Location} object
	 * 
	 * @param location The location to get a centered copy of
	 * @return New location with centered x, y and z
	 */
	public static Location fullyCenterLocation(@Nonnull Location location) {
		Location newLocation = location.clone();

		newLocation.setX(LocationUtils.blockCenter(newLocation.getBlockX()));
		newLocation.setY(LocationUtils.blockCenter(newLocation.getBlockY()));
		newLocation.setZ(LocationUtils.blockCenter(newLocation.getBlockZ()));

		return newLocation;
	}

	/**
	 * Modify a location by adding the values from a {@link BlockFace}
	 * 
	 * @param location  The {@link Location} to modify
	 * @param blockFace The {@link BlockFace} to apply
	 * @return Modified location
	 */
	public static Location addFaceMod(@Nonnull Location location, @Nonnull BlockFace blockFace) {
		return location.add(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
	}

	/**
	 * Check if 2 {@link Location}s has the same X and Z block location
	 * 
	 * @param location1 First location to compare
	 * @param location2 Second location to compare
	 * @return <code>true</code> if block x and z is matching
	 */
	public static boolean isBlockXZMatching(@Nonnull Location location1, @Nonnull Location location2) {
		return location1.getBlockX() == location2.getBlockX() && location1.getBlockZ() == location2.getBlockZ();
	}

	/**
	 * Check if 2 {@link Vector}s has the same X and Z block location
	 * 
	 * @param vector1 First vector to compare
	 * @param vector2 Second vector to compare
	 * @return <code>true</code> if block x and z is matching
	 */
	public static boolean isBlockXZMatching(@Nonnull Vector vector1, @Nonnull Vector vector2) {
		return vector1.getBlockX() == vector2.getBlockX() && vector1.getBlockZ() == vector2.getBlockZ();
	}

	/**
	 * Add a random value to the X, Y and Z coordinate of a location. The input
	 * instance will have its location changed, if you dont want that make sure you
	 * use {@link Location#clone()} and use the return value from this function
	 * instead
	 * 
	 * @param location The {@link Location} to modify
	 * @param x        The X value range
	 * @param y        The Y value range
	 * @param z        The Z value range
	 * @return Instance of the modified location
	 */
	public static Location addRandomValue(@Nonnull Location location, double x, double y, double z) {
		return LocationUtils.addRandomValue(location, x, y, z, new Random());
	}

	/**
	 * Add a random value to the X, Y and Z coordinate of a location. The input
	 * instance will have its location changed, if you dont want that make sure you
	 * use {@link Location#clone()} and use the return value from this function
	 * instead
	 * 
	 * @param location The {@link Location} to modify
	 * @param x        The X value range
	 * @param y        The Y value range
	 * @param z        The Z value range
	 * @param random   The Random instance to use
	 * @return Instance of the modified location
	 */
	public static Location addRandomValue(@Nonnull Location location, double x, double y, double z, @Nonnull Random random) {
		double newX = RandomGenerator.generateDouble(x * -1, x, random);
		double newY = RandomGenerator.generateDouble(y * -1, y, random);
		double newZ = RandomGenerator.generateDouble(z * -1, z, random);

		return location.add(newX, newY, newZ);
	}

	/**
	 * Checks if the player is inside a block location, since it needs to count for
	 * the players hitbox
	 *
	 * @param block  The block to be checked
	 * @param entity The entity to be checked
	 * @return If player is inside the block location
	 */
	public static boolean entityIsInsideBlockLocation(@Nonnull Block block, @Nonnull Entity entity) {
		EntityBoundingBox bb = VersionIndependentUtils.get().getEntityBoundingBox(entity);
		Block blockTop = block.getLocation().clone().add(bb.getWidth(), bb.getHeight(), bb.getWidth()).getBlock();
		Block blockBottom = block.getLocation().clone().add(-bb.getWidth(), 0, -bb.getWidth()).getBlock();

		List<Block> avaliableBlocks = new ArrayList<>();

		for (int x = blockBottom.getX(); x <= blockTop.getX(); x++) {
			for (int y = blockBottom.getY(); y <= blockTop.getY(); y++) {
				for (int z = blockBottom.getZ(); z <= blockTop.getZ(); z++) {
					avaliableBlocks.add(entity.getWorld().getBlockAt(x, y, z));
				}
			}
		}

		boolean isInside = false;

		for (Block found : avaliableBlocks) {
			if (blockLocationsEqual(found, block)) {
				isInside = true;
				break;
			}
		}
		return isInside;
	}

	public static boolean blockLocationsEqual(@Nonnull Block block, @Nonnull Block other) {
		if (block.getWorld() == other.getWorld()) {
			return block.getX() == other.getX() && block.getY() == other.getY() && other.getZ() == other.getZ();
		} else {
			return false;
		}
	}

	public static Rotation locationToRotation(@Nonnull LocationData locationData) {
		return new Rotation(locationData.getYaw(), locationData.getPitch());
	}

	public static Rotation locationToRotation(@Nonnull Location location) {
		return new Rotation(location.getYaw(), location.getPitch());
	}

	/**
	 * Same as {@link Location#distance(Location)} but this version does not check
	 * if the locations are in the same world
	 * 
	 * @param location1 the first location
	 * @param location2 the second location to check distance for
	 * @return distance
	 */
	public static double distanceNoWorldCheck(@Nonnull Location location1, @Nonnull Location location2) {
		return Math.sqrt(distanceSquaredNoWorldCheck(location1, location2));
	}

	/**
	 * Same as {@link Location#distanceSquared(Location)} but this version does not
	 * check if the locations are in the same world
	 * 
	 * @param location1 the first location
	 * @param location2 the second location to check distance for
	 * @return distance
	 */
	public static double distanceSquaredNoWorldCheck(@Nonnull Location location1, @Nonnull Location location2) {
		return NumberConversions.square(location1.getX() - location2.getX()) + NumberConversions.square(location1.getY() - location2.getY()) + NumberConversions.square(location1.getZ() - location2.getZ());
	}
}