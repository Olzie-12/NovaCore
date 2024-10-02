package net.zeeraa.novacore.spigot.loottable;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.spigot.loottable.event.LootTableGeneratedEvent;

/**
 * Represents a loot table that can generate random loot
 * 
 * @author Zeeraa
 */
public abstract class LootTable {
	protected String name;
	protected String displayName;
	protected int minItems;
	protected int maxItems;

	public LootTable(String name, int minItems, int maxItems) {
		this(name, name, minItems, maxItems);
	}

	public LootTable(String name, String displayName, int minItems, int maxItems) {
		this.name = name;
		this.displayName = displayName;
		this.minItems = minItems;
		this.maxItems = maxItems;

		if (this.minItems < 0) {
			this.minItems = 0;
		}

		if (this.maxItems < 0) {
			this.maxItems = 0;
		}

		if (this.minItems > this.maxItems) {
			this.minItems = this.maxItems;
		}
	}

	/**
	 * Get name of loot table
	 * 
	 * @return name {@link String}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get display name of loot table
	 * 
	 * @return display name {@link String}
	 */
	public String getDisplayName() {
		return displayName;
	}

	protected void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Get minimum items to add
	 * 
	 * @return minimum items
	 */
	public int getMinItems() {
		return minItems;
	}

	/**
	 * Get max items to add
	 * 
	 * @return max items
	 */
	public int getMaxItems() {
		return maxItems;
	}

	/**
	 * Generate loot using loot table
	 * 
	 * @return {@link List} with {@link ItemStack}
	 */
	public final List<ItemStack> generateLoot() {
		return this.generateLoot(new Random());
	}

	/**
	 * Generate loot using loot table
	 * 
	 * @param random instance of {@link Random} to use
	 * @return {@link List} with {@link ItemStack}
	 */
	public final List<ItemStack> generateLoot(Random random) {
		int count = minItems + random.nextInt((maxItems - minItems) + 1);
		return this.generateLoot(random, count);
	}

	/**
	 * Generate loot using loot table
	 * 
	 * @param count amount of {@link ItemStack} to generate
	 * @return {@link List} with {@link ItemStack}
	 */
	public final List<ItemStack> generateLoot(int count) {
		return this.generateLoot(new Random(), count);
	}

	/**
	 * Generate loot using loot table
	 * 
	 * @param random instance of {@link Random} to use
	 * @param count  amount of {@link ItemStack} to generate
	 * @return {@link List} with {@link ItemStack}
	 */
	public final List<ItemStack> generateLoot(Random random, int count) {
		List<ItemStack> loot = generate(random, count);
		LootTableGeneratedEvent event = new LootTableGeneratedEvent(this, loot);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return event.getGeneratedContent();
	}

	public abstract List<ItemStack> generate(Random random, int count);

	/**
	 * Get the name of a {@link LootTable}. The provided loot table can be
	 * <code>null</code> and if so it will just return the string "null"
	 * 
	 * @param lootTable The {@link LootTable} to get the name off
	 * @return The loot table name
	 */
	public static String getNameSafe(@Nullable LootTable lootTable) {
		if (lootTable == null) {
			return "null";
		}
		return lootTable.getName();
	}
}