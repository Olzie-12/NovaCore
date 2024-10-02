package net.zeeraa.novacore.spigot.loottable.event;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.spigot.loottable.LootTable;

public class LootTableGeneratedEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();

	private LootTable lootTable;
	private List<ItemStack> generatedContent;

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public LootTableGeneratedEvent(LootTable lootTable, List<ItemStack> generatedContent) {
		this.lootTable = lootTable;
		this.generatedContent = generatedContent;
	}

	public LootTable getLootTable() {
		return lootTable;
	}

	public List<ItemStack> getGeneratedContent() {
		return generatedContent;
	}
}