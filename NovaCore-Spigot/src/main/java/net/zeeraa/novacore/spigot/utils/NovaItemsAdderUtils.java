package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;

public class NovaItemsAdderUtils {
	public static ItemStack getItemStack(@Nonnull String namespace) {
		CustomStack stack = CustomStack.getInstance(namespace);
		return stack.getItemStack();
	}

	public static String getFontImage(@Nonnull String namespace) {
		return new FontImageWrapper(namespace).getString();
	}
}