package net.zeeraa.novacore.spigot.module.modules.chestloot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentSound;
import net.zeeraa.novacore.spigot.language.LanguageManager;
import net.zeeraa.novacore.spigot.loottable.LootTable;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.chestloot.events.ChestFillEvent;
import net.zeeraa.novacore.spigot.module.modules.chestloot.events.ChestRefillEvent;
import net.zeeraa.novacore.spigot.utils.definedarea.DefinedArea;

public class ChestLootManager extends NovaModule implements Listener {
	private static ChestLootManager instance;

	private ArrayList<Location> chests;
	private HashMap<Location, Inventory> enderChests;

	private BlockFace chestBlockFaces[] = { BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH };

	private String chestLootTable;
	private String enderChestLootTable;

	private ChestLootMode mode;
	private List<DefinedArea> lootAreas;
	private List<UUID> worlds;

	private boolean clearOldItems;

	private Random random;

	public static ChestLootManager getInstance() {
		return instance;
	}

	public ChestLootManager() {
		super("NovaCore.ChestLootManager");
		ChestLootManager.instance = this;
		this.enderChests = new HashMap<Location, Inventory>();
		this.chests = new ArrayList<Location>();
		this.chestLootTable = null;
		this.enderChestLootTable = null;
		this.mode = ChestLootMode.GLOBAL;
		this.lootAreas = new ArrayList<>();
		this.worlds = new ArrayList<>();
		this.clearOldItems = true;
		this.random = new Random();
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public ChestLootMode getMode() {
		return mode;
	}

	public void setMode(ChestLootMode mode) {
		this.mode = mode;
	}

	public List<UUID> getWorldUIDs() {
		return worlds;
	}

	public boolean addWorld(World world) {
		UUID uid = world.getUID();
		if (!worlds.contains(uid)) {
			return true;
		}
		return false;
	}

	public boolean removeWorld(World world) {
		UUID uid = world.getUID();
		return worlds.remove(uid);
	}

	public List<DefinedArea> getLootAreas() {
		return lootAreas;
	}

	public void addLootArea(DefinedArea area) {
		this.lootAreas.add(area);
	}

	public boolean isClearOldItems() {
		return clearOldItems;
	}

	public void setClearOldItems(boolean clearOldItems) {
		this.clearOldItems = clearOldItems;
	}

	public void refillChests() {
		refillChests(false);
	}

	public void refillChests(boolean announce) {
		ChestRefillEvent event = new ChestRefillEvent(announce);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			enderChests.clear();
			chests.clear();
			if (event.isShowMessage()) {
				Bukkit.getOnlinePlayers().forEach(player -> {
					player.sendMessage(LanguageManager.getString(player, "novacore.game.modules.chestloot.refill"));
					VersionIndependentUtils.get().playSound(player, player.getLocation(), VersionIndependentSound.NOTE_PLING, 1F, 1F);
				});
			}
		}
	}

	public String getChestLootTable() {
		return chestLootTable;
	}

	public void setChestLootTable(String chestLootTable) {
		this.chestLootTable = chestLootTable;
	}

	public String getEnderChestLootTable() {
		return enderChestLootTable;
	}

	public void setEnderChestLootTable(String enderChestLootTable) {
		this.enderChestLootTable = enderChestLootTable;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (mode == ChestLootMode.DEFINED_AREAS_ONLY) {
				if (lootAreas.stream().noneMatch(a -> a.isInside(e.getClickedBlock(), false))) {
					// Not in defined area. Stop
					return;
				}
			} else if (mode == ChestLootMode.LIMITED_WORLDS) {
				if (!worlds.contains(e.getClickedBlock().getWorld().getUID())) {
					// Not in specified worlds. Stop
					return;
				}
			}

			if (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
				fillChest(e.getClickedBlock(), clearOldItems);
			} else if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
				if (enderChestLootTable != null) {
					e.setCancelled(true);
					Player p = e.getPlayer();

					if (e.getClickedBlock() != null) {
						Log.trace("Filling ender chest at location " + e.getClickedBlock().getLocation().toString());

						if (!enderChests.containsKey(e.getClickedBlock().getLocation())) {
							Inventory inventory = Bukkit.createInventory(new EnderChestHolder(), 27, "Ender chest");

							LootTable lootTable = NovaCore.getInstance().getLootTableManager().getLootTable(enderChestLootTable);

							if (lootTable == null) {
								Log.warn("Missing loot table " + enderChestLootTable);
								return;
							}

							ChestFillEvent event = new ChestFillEvent(e.getClickedBlock(), lootTable, ChestType.ENDERCHEST);

							Bukkit.getServer().getPluginManager().callEvent(event);

							if (event.isCancelled()) {
								return;
							}

							if (event.hasLootTableChanged()) {
								lootTable = event.getLootTable();
							}

							if (clearOldItems) {
								inventory.clear();
							}

							List<ItemStack> loot = lootTable.generateLoot();

							List<Integer> slots = getAvailableSlots(inventory);

							while (loot.size() > slots.size()) {
								loot.remove(0);
							}

							Collections.shuffle(slots, random);

							while (loot.size() > 0) {
								int slot = slots.remove(0);
								ItemStack item = loot.remove(0);

								inventory.setItem(slot, item);
							}

							enderChests.put(e.getClickedBlock().getLocation(), inventory);
						}

						p.openInventory(enderChests.get(e.getClickedBlock().getLocation()));
					}
				}
			}
		}
	}

	private List<Integer> getAvailableSlots(Inventory inventory) {
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) == null) {
				result.add(i);
			}
		}
		return result;
	}

	private void fillChest(Block block, boolean clearOldItems) {
		if (block.getState() instanceof Chest) {
			if (chestLootTable != null) {
				if (!chests.contains(block.getLocation())) {
					Log.trace("Filling chest at location " + block.getLocation().toString());

					LootTable lootTable = NovaCore.getInstance().getLootTableManager().getLootTable(chestLootTable);

					if (lootTable == null) {
						Log.warn("Missing loot table " + chestLootTable);
						return;
					}

					chests.add(block.getLocation());

					Chest chest = (Chest) block.getState();

					Inventory inventory = chest.getBlockInventory();

					ChestFillEvent event = new ChestFillEvent(block, lootTable, ChestType.CHEST);

					Bukkit.getServer().getPluginManager().callEvent(event);

					if (event.isCancelled()) {
						return;
					}

					if (event.hasLootTableChanged()) {
						lootTable = event.getLootTable();
					}

					if (clearOldItems) {
						inventory.clear();
					}

					List<ItemStack> loot = lootTable.generateLoot();

					List<Integer> slots = getAvailableSlots(inventory);

					while (loot.size() > slots.size()) {
						loot.remove(0);
					}

					Collections.shuffle(slots, random);

					while (loot.size() > 0) {
						int slot = slots.remove(0);
						ItemStack item = loot.remove(0);

						inventory.setItem(slot, item);
					}

					for (BlockFace face : chestBlockFaces) {
						Block nextBlock = block.getRelative(face);

						if (nextBlock.getType() == Material.CHEST || nextBlock.getType() == Material.TRAPPED_CHEST) {
							if (!chests.contains(nextBlock.getLocation())) {
								Log.trace("Executing recursive fill to chest at location " + nextBlock.getLocation().toString());
								this.fillChest(nextBlock, clearOldItems);
							}
						}
					}
				}
			}
		}
	}
}