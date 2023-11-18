package net.zeeraa.novacore.spigot.utils.definedarea;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class DefinedBoxArea extends DefinedArea {
	@Nullable
	protected World world;
	protected Vector lowerPoint;
	protected Vector upperPoint;

	public DefinedBoxArea(@Nonnull Vector position1, @Nonnull Vector position2) {
		this(null, position1, position2);
	}

	public DefinedBoxArea(@Nullable World world, @Nonnull Vector position1, @Nonnull Vector position2) {
		this.world = world;
		this.lowerPoint = new Vector((Math.min(position1.getX(), position2.getX())), (Math.min(position1.getY(), position2.getY())), (Math.min(position1.getZ(), position2.getZ())));
		this.upperPoint = new Vector((Math.max(position1.getX(), position2.getX())), (Math.max(position1.getY(), position2.getY())), (Math.max(position1.getZ(), position2.getZ())));
	}

	@Override
	public boolean isInside(Location location, boolean ignoreWorld, boolean useBlockPosition) {
		boolean shouldIgnoreWorld = ignoreWorld;
		boolean shouldUseBlock = useBlockPosition;

		if (flags.contains(DefinedAreaFlag.ALWAYS_IGNORE_WORLD)) {
			shouldIgnoreWorld = true;
		} else if (flags.contains(DefinedAreaFlag.NEVER_IGNORE_WORLD)) {
			shouldIgnoreWorld = false;
		}

		if (flags.contains(DefinedAreaFlag.ALWAYS_USE_BLOCK_POSITION)) {
			shouldUseBlock = true;
		} else if (flags.contains(DefinedAreaFlag.NEVER_USE_BLOCK_POSITION)) {
			shouldUseBlock = false;
		}

		if (excludedAreas.stream().anyMatch(a -> a.isInside(location, ignoreWorld))) {
			return false;
		}

		if (includedAreas.stream().anyMatch(a -> a.isInside(location, ignoreWorld))) {
			return true;
		}

		if (!shouldIgnoreWorld) {
			if (!this.world.equals(location.getWorld())) {
				return false;
			}
		}

		if (shouldUseBlock) {
			int x1 = lowerPoint.getBlockX();
			int y1 = lowerPoint.getBlockY();
			int z1 = lowerPoint.getBlockZ();

			int x2 = upperPoint.getBlockX();
			int y2 = upperPoint.getBlockY();
			int z2 = upperPoint.getBlockZ();

			int x = location.getBlockX();
			int y = location.getBlockY();
			int z = location.getBlockZ();

			if (x >= x1 && x <= x2) {
				if (y >= y1 && y <= y2) {
					return z >= z1 && z <= z2;
				}
			}
		} else {
			double x1 = lowerPoint.getX();
			double y1 = lowerPoint.getY();
			double z1 = lowerPoint.getZ();

			double x2 = upperPoint.getX();
			double y2 = upperPoint.getY();
			double z2 = upperPoint.getZ();

			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();

			if (x >= x1 && x <= x2) {
				if (y >= y1 && y <= y2) {
					return z >= z1 && z <= z2;
				}
			}
		}
		return false;
	}

}