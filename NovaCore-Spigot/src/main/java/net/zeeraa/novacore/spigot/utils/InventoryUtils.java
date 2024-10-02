package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.spigot.NovaCore;

public class InventoryUtils {
	/**
	 * Remove one item from the players main hand
	 * 
	 * @param player The player to remove the item from
	 * @return The new amount in hand
	 */
	public static int removeOneFromHand(@Nonnull Player player) {
		final ItemStack item = NovaCore.getInstance().getVersionIndependentUtils().getItemInMainHand(player);
		final int a = item.getAmount();
		if (a <= 1) {
			NovaCore.getInstance().getVersionIndependentUtils().setItemInMainHand(player, new ItemStack(Material.AIR));
			return 0;
		} else {
			item.setAmount(a - 1);
			NovaCore.getInstance().getVersionIndependentUtils().setItemInMainHand(player, item);
			return item.getAmount();
		}
	}

	/**
	 * Remove one item from the players offhand
	 *
	 * @param player The player to remove the item from
	 * @return The new amount in offhand, or -1 if item is null
	 */
	public static int removeOneFromOffHand(@Nonnull Player player) {
		final ItemStack item = NovaCore.getInstance().getVersionIndependentUtils().getItemInOffHand(player);
		if (item == null) {
			return -1;
		}
		final int a = item.getAmount();

		if (a <= 1) {
			NovaCore.getInstance().getVersionIndependentUtils().setItemInOffHand(player, new ItemStack(Material.AIR));
			return 0;
		} else {
			item.setAmount(a - 1);
			NovaCore.getInstance().getVersionIndependentUtils().setItemInOffHand(player, item);
			return item.getAmount();
		}
	}

	/**
	 * Count the amount of a specified {@link Material} in an inventory
	 * 
	 * @param inventory The {@link Inventory} to scan
	 * @param material  The {@link Material} to scan for
	 * @return The total amount of the provoded {@link Material} in the inventory
	 */
	public static int countItemsOfType(@Nonnull Inventory inventory, @Nonnull Material material) {
		int result = 0;
		for (ItemStack item : inventory.getContents()) {
			if (item != null) {
				if (item.getType() == material) {
					result += item.getAmount();
				}
			}
		}
		return result;
	}
}