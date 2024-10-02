package net.zeeraa.novacore.spigot.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LoreBuilder {
	private List<String> lore;

	public LoreBuilder() {
		this.lore = new ArrayList<>();
	}

	public LoreBuilder(List<String> lore) {
		this.lore = new ArrayList<>(lore);
	}

	public LoreBuilder(ItemMeta meta) {
		this(meta.hasLore() ? meta.getLore() : new ArrayList<>());
	}

	public LoreBuilder(ItemStack item) {
		this(item.getItemMeta());
	}

	public LoreBuilder(ItemBuilder builder) {
		this(builder.getItemMeta());
	}

	public LoreBuilder addLore(@Nonnull String string) {
		lore.add(string);
		return this;
	}

	public LoreBuilder addEmptyLoreLine() {
		return this.addLore(" ");
	}

	public LoreBuilder addLore(@Nonnull String... strings) {
		for (int i = 0; i < strings.length; i++) {
			this.addLore(strings[i]);
		}
		return this;
	}

	public LoreBuilder addLore(@Nonnull List<String> lines) {
		lines.forEach(string -> this.addLore(string));
		return this;
	}

	public List<String> build() {
		return lore;
	}

	public LoreBuilder apply(ItemMeta meta) {
		meta.setLore(new ArrayList<>(lore));
		return this;
	}

	public LoreBuilder apply(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(new ArrayList<>(lore));
		item.setItemMeta(meta);
		return this;
	}

	public LoreBuilder apply(ItemBuilder builder) {
		builder.setLore(new ArrayList<>(lore));
		return this;
	}
}