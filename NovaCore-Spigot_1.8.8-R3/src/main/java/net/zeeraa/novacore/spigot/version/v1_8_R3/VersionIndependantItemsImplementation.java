package net.zeeraa.novacore.spigot.version.v1_8_R3;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class VersionIndependantItemsImplementation extends net.zeeraa.novacore.spigot.abstraction.VersionIndependentItems {
	@Override
	public ItemStack getPlayerSkull() {
		return new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isPlayerSkull(ItemStack item) {
		if (item.getType() == Material.SKULL_ITEM) {
			MaterialData data = item.getData();

			return data.getData() == 3;
		}

		return false;
	}
}