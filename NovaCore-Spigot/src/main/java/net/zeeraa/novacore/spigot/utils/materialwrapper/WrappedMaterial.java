package net.zeeraa.novacore.spigot.utils.materialwrapper;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.spigot.utils.ItemBuilder;

/**
 * A wrapper class for special materials
 * 
 * @author Zeeraa
 */
public abstract class WrappedMaterial {
	public abstract ItemStack getItemStack();

	public abstract ItemBuilder getItemBuilder();

	public abstract void setBlock(Block block);
}