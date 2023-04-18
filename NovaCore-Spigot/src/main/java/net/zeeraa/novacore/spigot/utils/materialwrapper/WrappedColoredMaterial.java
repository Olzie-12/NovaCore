package net.zeeraa.novacore.spigot.utils.materialwrapper;

import javax.annotation.Nonnull;

import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.ColoredBlockType;
import net.zeeraa.novacore.spigot.utils.ItemBuilder;

public class WrappedColoredMaterial extends WrappedMaterial {
	private DyeColor color;
	private ColoredBlockType type;

	public WrappedColoredMaterial(@Nonnull DyeColor color, @Nonnull ColoredBlockType type) {
		this.color = color;
		this.type = type;
	}

	public void setType(@Nonnull ColoredBlockType type) {
		this.type = type;
	}

	@Override
	public ItemStack getItemStack() {
		return getItemBuilder().build();
	}

	@Override
	public ItemBuilder getItemBuilder() {
		return new ItemBuilder(type, color);
	}

	@Override
	public void setBlock(Block block) {
		VersionIndependentUtils.get().setColoredBlock(block, color, type);
	}
}