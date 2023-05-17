package net.zeeraa.novacore.spigot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.abstraction.enums.PlayerDamageReason;

/**
 * Functions to do things with players
 * 
 * @author Zeeraa
 *
 */
public class PlayerUtils {
	/**
	 * Clear the players inventory including armor slots
	 * 
	 * @param player The {@link Player} to clear
	 */
	public static void clearPlayerInventory(@Nonnull Player player) {
		player.getInventory().clear();
		for (int i = 0; i < player.getInventory().getSize(); i++) {
			player.getInventory().setItem(i, new ItemStack(Material.AIR));
		}
		player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
		player.getItemOnCursor().setType(Material.AIR);
	}

	/**
	 * Remove all the potion effects from a player
	 * 
	 * @param player The {@link Player} to remove potion effects from
	 */
	public static void clearPotionEffects(@Nonnull Player player) {
		player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
	}

	/**
	 * Set the player level and xp to 0
	 * 
	 * @param player The {@link Player} to reset xp for
	 */
	public static void resetPlayerXP(@Nonnull Player player) {
		player.setExp(0);
		player.setLevel(0);
	}

	/**
	 * Reset the max heath of a {@link Player}
	 * 
	 * @param player The player to reset the max health of
	 */
	public static void resetMaxHealth(@Nonnull Player player) {
		NovaCore.getInstance().getVersionIndependentUtils().resetEntityMaxHealth(player);
	}

	/**
	 * Set the max heath of a {@link Player}
	 * 
	 * @param player The player to reset the set health of
	 * @param health The new health value
	 */
	public static void setMaxHealth(@Nonnull Player player, double health) {
		NovaCore.getInstance().getVersionIndependentUtils().setEntityMaxHealth(player, health);
	}

	/**
	 * Get the max heath of a {@link Player}
	 * 
	 * @param player The player to get the max health from
	 * 
	 * @return The max health of the player
	 */
	public static double getPlayerMaxHealth(@Nonnull Player player) {
		return NovaCore.getInstance().getVersionIndependentUtils().getEntityMaxHealth(player);
	}

	/**
	 * Fully heal a player and return the new health
	 * 
	 * @param player The player to heal
	 * @return The new player health
	 */
	public static double fullyHealPlayer(@Nonnull Player player) {
		double maxHealth = PlayerUtils.getPlayerMaxHealth(player);

		player.setHealth(maxHealth);

		return maxHealth;
	}

	public static void damagePlayer(@Nonnull Player player, @Nonnull PlayerDamageReason reason, float damage) {
		NovaCore.getInstance().getVersionIndependentUtils().damagePlayer(player, reason, damage);
	}

	/**
	 * Check if a player is online and exists
	 * <p>
	 * This also accepts null but will return <code>false</code>
	 * 
	 * @param player The player to check
	 * @return <code>true</code> if the player is online and exists, this will also
	 *         return <code>false</code> if the player is <code>null</code>
	 */
	public static boolean existsAndIsOnline(@Nullable Player player) {
		if (player != null) {
			return player.isOnline();
		}
		return false;
	}

	public static boolean isOnline(UUID uuid) {
		return Bukkit.getPlayer(uuid) != null;
	}

	/**
	 * Deprecated: turns out player.isOnline() is unnecessary since
	 * Bukkit.getPlayer() always return null if player is offline
	 * <p>
	 * Check if a player is online and exists
	 * <p>
	 * This also accepts null but will return <code>false</code>
	 * 
	 * @param uuid The UUID of the player to check
	 * @return <code>true</code> if the player is online and exists, this will also
	 *         return <code>false</code> if the player is <code>null</code>
	 */
	@Deprecated
	public static boolean existsAndIsOnline(@Nonnull UUID uuid) {
		return PlayerUtils.existsAndIsOnline(Bukkit.getServer().getPlayer(uuid));
	}

	/**
	 * Convert a list of players to a list of names
	 * 
	 * @param players The list of players
	 * @return The list on names
	 */
	public static List<String> getNames(@Nonnull List<Player> players) {
		List<String> names = new ArrayList<String>();

		players.forEach(player -> names.add(player.getName()));

		return names;
	}

	/**
	 * Try to send a message to a player by their {@link UUID}
	 * 
	 * @param uuid    The {@link UUID} of the player to message
	 * @param message The message to send
	 * @return <code>true</code> if the player is online and received the message,
	 *         <code>false</code> if the player is offline
	 */
	public static boolean tryMessagePlayer(@Nonnull UUID uuid, @Nonnull String message) {
		Player player = Bukkit.getServer().getPlayer(uuid);
		if (player != null) {
			player.sendMessage(message);
			return true;
		}
		return false;
	}

	/**
	 * Check if the player is at full health
	 * 
	 * @param player The {@link Player} to check
	 * @return <code>true</code> if the player is at full health
	 */
	public static boolean isAtMaxHealth(@Nonnull Player player) {
		return player.getHealth() == PlayerUtils.getPlayerMaxHealth(player);
	}

	/**
	 * Check if a player is online and if so call a {@link Consumer} of type
	 * {@link Player}
	 * 
	 * @param uuid     The {@link UUID} of the player to check
	 * @param consumer The {@link Consumer} to use
	 * @return <code>true</code> if the player is online
	 */
	public static boolean ifOnline(@Nonnull UUID uuid, @Nonnull Consumer<Player> consumer) {
		Player player = Bukkit.getServer().getPlayer(uuid);
		if (player != null) {
			consumer.accept(player);
			return true;
		}
		return false;
	}
}