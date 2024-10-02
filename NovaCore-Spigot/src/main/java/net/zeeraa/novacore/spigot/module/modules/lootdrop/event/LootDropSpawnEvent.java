package net.zeeraa.novacore.spigot.module.modules.lootdrop.event;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LootDropSpawnEvent extends Event implements Cancellable {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private Location location;
	private String lootTable;

	private String type;

	private boolean cancel;
	private boolean hideMessage;

	public LootDropSpawnEvent(Location location, String lootTable, String type, boolean hideMessage) {
		this.location = location;
		this.lootTable = lootTable;
		this.type = type;

		this.cancel = false;
		this.hideMessage = hideMessage;
	}

	/**
	 * Get the location the loot table is spawning at
	 * 
	 * @return {@link Location} for the loot table
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Get the name of the loot table
	 * 
	 * @return The name string of the loot table
	 */
	public String getLootTable() {
		return lootTable;
	}

	/**
	 * @return String with the name of the loot drop type
	 */
	public String getType() {
		return type;
	}

	public boolean isHideMessage() {
		return hideMessage;
	}

	public void setHideMessage(boolean hideMessage) {
		this.hideMessage = hideMessage;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}