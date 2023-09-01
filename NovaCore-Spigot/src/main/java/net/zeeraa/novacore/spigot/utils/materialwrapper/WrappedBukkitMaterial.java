package net.zeeraa.novacore.spigot.utils.materialwrapper;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.spigot.utils.ItemBuilder;

public class WrappedBukkitMaterial extends WrappedMaterial {
	private Material material;

	public WrappedBukkitMaterial(@Nonnull Material material) {
		this.material = material;
	}
	
	@Override
	public ItemStack getItemStack() {
		return new ItemStack(material, 1);
	}

	@Override
	public ItemBuilder getItemBuilder() {
		return ItemBuilder.newInstance(material);
	}

	@Override
	public void setBlock(Block block) {
		block.setType(material);
	}
}