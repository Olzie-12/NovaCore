package net.zeeraa.novacore.spigot.module.modules.lootdrop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.loottable.LootTable;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.lootdrop.event.LootDropSpawnEvent;
import net.zeeraa.novacore.spigot.module.modules.lootdrop.message.DefaultLootDropSpawnMessage;
import net.zeeraa.novacore.spigot.module.modules.lootdrop.message.LootDropSpawnMessage;
import net.zeeraa.novacore.spigot.module.modules.lootdrop.particles.LootdropParticleEffect;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;
import net.zeeraa.novacore.spigot.utils.LocationUtils;

public class LootDropManager extends NovaModule implements Listener {
	private static LootDropManager instance;

	private List<LootDrop> chests;
	private List<LootDropEffect> dropEffects;

	private Map<UUID, LootdropParticleEffect> particleEffects;

	private LootDropSpawnMessage spawnMessage;

	private Task particleTask;

	private int taskId;

	public static LootDropManager getInstance() {
		return instance;
	}

	@Override
	public String getName() {
		return "LootDropManager";
	}

	@Override
	public void onLoad() {
		LootDropManager.instance = this;
		chests = new ArrayList<LootDrop>();
		dropEffects = new ArrayList<LootDropEffect>();
		spawnMessage = new DefaultLootDropSpawnMessage();
		particleEffects = new HashMap<UUID, LootdropParticleEffect>();
		taskId = -1;

		this.particleTask = new SimpleTask(NovaCore.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (UUID uuid : particleEffects.keySet()) {
					particleEffects.get(uuid).update();
				}
			}
		}, 2L);
	}

	@Override
	public void onEnable() {
		taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(NovaCore.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (int i = dropEffects.size(); i > 0; i--) {
					if (dropEffects.get(i - 1).isCompleted()) {
						dropEffects.remove(i - 1);
					}
				}
			}
		}, 20L, 20L);

		particleTask.start();
	}

	@Override
	public void onDisable() {
		Task.tryStopTask(particleTask);
		destroy();
	}

	public void destroy() {
		if (taskId != -1) {
			Bukkit.getScheduler().cancelTask(taskId);
		}

		for (LootDropEffect effect : dropEffects) {
			effect.undoBlocks();
		}

		particleEffects.clear();

		for (int i = chests.size(); i > 0; i--) {
			removeChest(chests.get(i - 1));
		}
	}

	public void removeFromWorld(World world) {
		for (LootDropEffect effect : dropEffects) {
			if (effect.getWorld().equals(world)) {
				effect.undoBlocks();
			}
		}

		List<UUID> removeParticles = new ArrayList<UUID>();

		for (UUID uuid : particleEffects.keySet()) {
			if (particleEffects.get(uuid).getLocation().getWorld().equals(world)) {
				removeParticles.add(uuid);
			}
		}

		for (UUID uuid : removeParticles) {
			particleEffects.remove(uuid);
		}

		for (int i = chests.size(); i > 0; i--) {
			if (chests.get(i).getWorld().equals(world)) {
				removeChest(chests.get(i - 1));
			}
		}
	}

	/**
	 * Set the spawn message when a loot drop spawns
	 * 
	 * @param spawnMessage The custom {@link LootDropSpawnMessage} to use
	 */
	public void setSpawnMessage(LootDropSpawnMessage spawnMessage) {
		this.spawnMessage = spawnMessage;
	}

	/**
	 * Spawn a loot drop
	 * 
	 * @param location  The {@link Location} to spawn the loot drop at
	 * @param lootTable The name of the {@link LootTable} to use
	 * @return <code>true</code> on success, <code>false</code> if can't spawn or if
	 *         the event is canceled
	 */
	public boolean spawnDrop(Location location, String lootTable) {
		return spawnDrop(location, lootTable, true);
	}

	/**
	 * Spawn a loot drop
	 * 
	 * @param location  The {@link Location} to spawn the loot drop at
	 * @param lootTable The name of the {@link LootTable} to use
	 * @param announce  <code>true</code> to announce that a loot drop is spawning
	 * @return <code>true</code> on success, <code>false</code> if can't spawn or if
	 *         the event is canceled
	 */
	public boolean spawnDrop(Location location, String lootTable, boolean announce) {
		if (canSpawnAt(location)) {
			LootDropSpawnEvent event = new LootDropSpawnEvent(location, lootTable);
			Bukkit.getServer().getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				LootDropEffect effect = new LootDropEffect(location, lootTable);
				dropEffects.add(effect);
				if (announce) {
					spawnMessage.showLootDropSpawnMessage(effect);
				}
				return true;
			}
		}
		return false;
	}

	public boolean canSpawnAt(Location location) {
		for (LootDropEffect effect : dropEffects) {
			if (effect.getLocation().getBlockX() == location.getBlockX()) {
				if (effect.getLocation().getBlockZ() == location.getBlockZ()) {
					return false;
				}
			}
		}

		if (location.getBlock().getType() == Material.SKULL) {
			return false;
		}

		if (LocationUtils.isOutsideOfBorder(location)) {
			return false;
		}

		return true;
	}

	public void spawnChest(Location location, String lootTable) {
		LootDrop drop = new LootDrop(location, lootTable);
		chests.add(drop);

		Location particleLocation = new Location(location.getWorld(), LocationUtils.blockCenter(location.getBlockX()), location.getY() + 0.8, LocationUtils.blockCenter(location.getBlockZ()));

		particleEffects.put(drop.getUuid(), new LootdropParticleEffect(particleLocation));
	}

	public LootDrop getChestAtLocation(Location location) {
		for (LootDrop chest : chests) {
			if (chest.getLocation().getWorld() == location.getWorld()) {
				if (chest.getLocation().getBlockX() == location.getBlockX()) {
					if (chest.getLocation().getBlockY() == location.getBlockY()) {
						if (chest.getLocation().getBlockZ() == location.getBlockZ()) {
							return chest;
						}
					}
				}
			}
		}

		return null;
	}

	public void removeChest(LootDrop chest) {
		chests.remove(chest);
		chest.remove();
		if (particleEffects.containsKey(chest.getUuid())) {
			particleEffects.remove(chest.getUuid());
		}
	}

	public LootDrop getChestByUUID(UUID uuid) {
		for (LootDrop chest : chests) {
			if (chest.getUuid() == uuid) {
				return chest;
			}
		}

		return null;
	}

	private boolean isInventoryEmpty(Inventory inventory) {
		for (ItemStack i : inventory.getContents()) {
			if (i == null) {
				continue;
			}

			if (i.getType() != Material.AIR) {
				return false;
			}
		}

		return true;
	}

	public boolean isDropActiveAt(World world, int x, int z) {
		for (LootDropEffect e : dropEffects) {
			if (e.getWorld().getName().equalsIgnoreCase(world.getName())) {
				if (e.getLocation().getBlockX() == x) {
					if (e.getLocation().getBlockZ() == z) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClose(InventoryCloseEvent e) {
		if (e.getInventory().getHolder() instanceof LootDropInventoryHolder) {
			if (isInventoryEmpty(e.getInventory())) {
				UUID uuid = ((LootDropInventoryHolder) e.getInventory().getHolder()).getUuid();

				LootDrop chest = this.getChestByUUID(uuid);

				if (chest != null) {
					if (chest.isRemoved()) {
						return;
					}

					removeChest(chest);
					chest.getWorld().playSound(chest.getLocation(), Sound.WITHER_HURT, 1F, 1F);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.SKULL) {
				LootDrop chest = this.getChestAtLocation(e.getClickedBlock().getLocation());

				if (chest != null) {
					e.getPlayer().openInventory(chest.getInventory());
					e.setCancelled(true);
				}
			} else if (e.getClickedBlock().getType() == Material.BEACON) {
				for (LootDropEffect effect : dropEffects) {
					for (Location location : effect.getRemovedBlocks().keySet()) {
						if (location.equals(e.getClickedBlock().getLocation())) {
							Log.trace("Preventing player from interacting with loot drop beacon");
							e.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent e) {
		for (LootDropEffect effect : dropEffects) {
			for (Location location : effect.getRemovedBlocks().keySet()) {
				if (location.equals(e.getBlock().getLocation())) {
					Log.trace("Preventing player from breaking loot drop");
					e.setCancelled(true);
					return;
				}
			}
		}

		if (e.getBlock().getType() == Material.SKULL) {
			LootDrop chest = this.getChestAtLocation(e.getBlock().getLocation());

			if (chest != null) {
				e.setCancelled(true);
				return;
			}
		}
	}
}