package net.zeeraa.novacore.spigot.utils;

import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentMaterial;

import javax.annotation.Nonnull;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PotionBuilder extends ItemBuilder {
	public PotionBuilder(@Nonnull Material material) {
		super(material);
	}

	public PotionBuilder(@Nonnull Material material, int ammount) {
		super(material, ammount);
	}

	public PotionBuilder(@Nonnull VersionIndependentMaterial material) {
		super(material);
	}

	public PotionBuilder(@Nonnull VersionIndependentMaterial material, int ammount) {
		super(material, ammount);
	}

	public PotionBuilder(@Nonnull ItemStack itemStack) {
		super(itemStack);
	}

	public PotionBuilder(@Nonnull ItemStack itemStack, boolean clone) {
		super(itemStack, clone);
	}

	public PotionBuilder setPotionEffect(@Nonnull PotionEffect effect, boolean color) {
		VersionIndependentUtils.get().setPotionEffect(item, meta, effect, color);
		return this;
	}

	public PotionBuilder setPotionColor(@Nonnull Color color) {
		VersionIndependentUtils.get().setPotionColor(meta, color);
		return this;
	}
}