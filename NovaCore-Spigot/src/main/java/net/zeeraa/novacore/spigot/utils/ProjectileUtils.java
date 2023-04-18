package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

/**
 * Functions used for projectiles
 * 
 * @author Zeeraa
 */
public class ProjectileUtils {
	/**
	 * Check if a a entity is a {@link Projectile}
	 * 
	 * @param entity The {@link Entity} to check
	 * @return <code>true</code> if the {@link Entity} is a {@link Projectile}
	 */
	public static boolean isProjectile(@Nonnull Entity entity) {
		return entity instanceof Projectile;
	}

	/**
	 * Try to get the {@link Entity} that fired a {@link Projectile} {@link Entity}
	 * 
	 * @param projectile A {@link Entity} that might be a {@link Projectile}
	 * @return The entity that fired the {@link Projectile} or <code>null</code> if
	 *         there was no shooter or if the {@link Projectile} {@link Entity} is
	 *         not a {@link Projectile}
	 */
	public static Entity getProjectileShooterEntity(@Nonnull Entity projectile) {
		if (isProjectile(projectile)) {
			return (Entity) ((Projectile) projectile).getShooter();
		}

		return null;
	}

	/**
	 * Get the {@link Location} hit by a {@link Projectile}. This is intended to be
	 * used with the {@link ProjectileHitEvent}
	 * 
	 * @param projectile The {@link Projectile}
	 * @return The estimated hit {@link Location}
	 * @since 2.0.0
	 */
	public static Location getEstimatedHitLocation(@Nonnull Projectile projectile) {
		Location loc = projectile.getLocation();
		Vector vec = projectile.getVelocity();
		return new Location(loc.getWorld(), loc.getX() + vec.getX(), loc.getY() + vec.getY(), loc.getZ() + vec.getZ());
	}

	/**
	 * Get the {@link Block} hit by a {@link Projectile}. This is intended to be
	 * used with the {@link ProjectileHitEvent}
	 * 
	 * @param projectile The {@link Projectile}
	 * @return The estimated hit {@link Block}
	 * @since 2.0.0
	 */
	public static Block getEstimatedHitBlock(@Nonnull Projectile projectile) {
		return ProjectileUtils.getEstimatedHitLocation(projectile).getBlock();
	}
}