package net.zeeraa.novacore.spigot.version.v1_16_R3;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VersionIndependantItemsImplementation extends net.zeeraa.novacore.spigot.abstraction.VersionIndependentItems {
	@Override
	public ItemStack getPlayerSkull() {
		return new ItemStack(Material.PLAYER_HEAD, 1);
	}

	@Override
	public boolean isPlayerSkull(ItemStack item) {
		return item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD;
	}
}