package net.zeeraa.novacore.spigot.world;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;

public class WorldUtils {
	public static Entity getEntityByUUID(@Nonnull World world, @Nonnull UUID uuid) {
		for (Entity entity : world.getEntities()) {
			if (entity.getUniqueId().toString().equalsIgnoreCase(uuid.toString())) {
				return entity;
			}
		}

		return null;
	}

	public static void setGameRule(@Nonnull World world, @Nonnull String rule, @Nonnull String value) {
		VersionIndependentUtils.get().setGameRule(world, rule, value);
	}
}