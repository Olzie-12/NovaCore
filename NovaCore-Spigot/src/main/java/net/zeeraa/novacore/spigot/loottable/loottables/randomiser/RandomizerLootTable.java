package net.zeeraa.novacore.spigot.loottable.loottables.randomiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.loottable.LootTable;

public class RandomizerLootTable extends LootTable {
	private Map<String, Integer> lootTables;

	public Map<String, Integer> getLootTables() {
		return lootTables;
	}

	public RandomizerLootTable(String name, int minItems, int maxItems, Map<String, Integer> lootTables) {
		super(name, 1, 1);
		this.lootTables = lootTables;
	}

	@Override
	public List<ItemStack> generate(Random random, int count) {
		List<String> pool = new ArrayList<String>();
		lootTables.forEach((name, chance) -> {
			if (!NovaCore.getInstance().getLootTableManager().hasLootTable(name)) {
				Log.warn("RandomizerLootTable", "Missing loot table " + name);
			}
			for (int i = 0; i < chance; i++) {
				pool.add(name);
			}
		});

		if (pool.size() == 0) {
			Log.error("RandomizerLootTable", "No loot tables in pool. Please check than cance is larger than 1 and that you have provided at leas 1 loot table");
			return new ArrayList<>();
		}

		String toUse = pool.get(random.nextInt(pool.size()));
		LootTable lootTable = NovaCore.getInstance().getLootTableManager().getLootTable(toUse);

		if (lootTable == null) {
			Log.error("RandomizerLootTable", "Could not find loot table " + toUse);
			return new ArrayList<>();
		}

		return lootTable.generateLoot(random);
	}
}