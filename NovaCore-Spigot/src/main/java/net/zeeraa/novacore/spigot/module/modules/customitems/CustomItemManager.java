package net.zeeraa.novacore.spigot.module.modules.customitems;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.zeeraa.novacore.spigot.abstraction.CustomItemAdditionsManager;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentItems;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.utils.ProjectileUtils;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.module.NovaModule;

public class CustomItemManager extends NovaModule implements Listener {
	private static CustomItemManager instance = null;
	private static CustomItemAdditionsManager customItemAdditionsManager;

	private Map<String, CustomItem> customItems = new HashMap<String, CustomItem>();

	public static CustomItemManager getInstance() {
		return instance;
	}

	public static boolean isInitialized() {
		return instance != null;
	}

	public CustomItemManager() {
		super("NovaCore.CustomItemManager");
	}

	@Override
	public void onLoad() {
		CustomItemManager.instance = this;
		customItemAdditionsManager = VersionIndependentUtils.get().getCustomItemAdditionsManager();
		customItems = new HashMap<>();
	}

	@Override
	public void onEnable() throws Exception {
	}

	@Override
	public void onDisable() throws Exception {
	}

	/**
	 * Get a map containing all custom items
	 *
	 * @return {@link Map} with custom items
	 */
	public Map<String, CustomItem> getCustomItems() {
		return customItems;
	}

	/**
	 * Add a custom item by its class
	 *
	 * @param clazz The class of the {@link CustomItem}
	 * @return <code>true</code> on success
	 * @throws InstantiationException    .
	 * @throws IllegalAccessException    .
	 * @throws IllegalArgumentException  .
	 * @throws InvocationTargetException .
	 * @throws NoSuchMethodException     .
	 * @throws SecurityException         .
	 */
	public boolean addCustomItem(Class<? extends CustomItem> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (!hasCustomItem(clazz)) {
			CustomItem customItem = clazz.getConstructor().newInstance();

			customItems.put(customItem.getClass().getName(), customItem);

			return true;
		}
		return false;
	}

	/**
	 * Add a custom item that has been initialized, for items of same type
	 *
	 *
	 * @param customItem The initialized {@link CustomItem}
	 * @return <code>true</code> on success
	 */
	public boolean addCustomItem(CustomItemInitialized customItem) {
		if (!hasCustomItem(customItem.getFullId())) {
			customItems.put(customItem.getFullId(), customItem);
			return true;
		}
		return false;
	}

	/**
	 * Check if a custom item has been loaded
	 *
	 * @param clazz The class of the {@link CustomItem}
	 * @return <code>true</code> if the item has been loaded
	 */
	public boolean hasCustomItem(Class<? extends CustomItem> clazz) {
		return this.hasCustomItem(clazz.getName());
	}

	/**
	 * Check if a custom item has been loaded
	 *
	 * @param className The name of the class of the {@link CustomItem}
	 * @return <code>true</code> if the item has been loaded
	 */
	public boolean hasCustomItem(String className) {
		return customItems.get(className) != null;
	}

	/**
	 * Check if a custom item has been loaded
	 *
	 * @param className The name of the class of the {@link CustomItem}
	 * @return <code>true</code> if the item has been loaded
	 */
	public boolean hasCustomItemId(String id, String className) {
		return hasCustomItem(className + "." + id);
	}

	/**
	 * Get the {@link CustomItem} instance by the class
	 *
	 * @param clazz The {@link CustomItem} class to get
	 * @return The {@link CustomItem} instance or <code>null</code> if not loaded or
	 *         not found
	 */
	@Nullable
	public CustomItem getCustomItem(Class<? extends CustomItem> clazz) {
		return this.getCustomItem(clazz.getName());
	}

	/**
	 * Get the {@link CustomItem} instance by the class and item ID
	 *
	 * @param clazz The {@link CustomItem} class to get
	 * @param id The ID
	 * @return The {@link CustomItem} instance or <code>null</code> if not loaded or
	 *         not found
	 */
	@Nullable
	public CustomItem getCustomItemByID(String id, Class<? extends CustomItemInitialized> clazz) {
		return this.getCustomItem(clazz.getName() + "." + id);
	}

	/**
	 * Get the {@link CustomItem} instance by the class name
	 *
	 * @param className The class name of the {@link CustomItem} to get
	 * @return The {@link CustomItem} instance or <code>null</code> if not loaded or
	 *         not found
	 */
	@Nullable
	public CustomItem getCustomItem(String className) {
		return customItems.get(className);
	}

	/**
	 * Get an {@link ItemStack} from a {@link CustomItem}
	 *
	 * @param clazz  The {@link CustomItem} class to get the {@link ItemStack} from
	 * @param player The player that the item was created by
	 * @return An {@link ItemStack} or <code>null</code> if not loaded or not found
	 */
	@Nullable
	public ItemStack getCustomItemStack(Class<? extends CustomItem> clazz, @Nullable Player player) {
		return this.getCustomItemStack(clazz.getName(), player);
	}

	/**
	 * Get an {@link ItemStack} from a {@link CustomItem}
	 *
	 * @param className The name of the {@link CustomItem} class to get the
	 *                  {@link ItemStack} from
	 * @param player    The player that the item was created by
	 * @return An {@link ItemStack} or <code>null</code> if not loaded or not found
	 */
	@Nullable
	public ItemStack getCustomItemStack(String className, @Nullable Player player) {
		CustomItem customItem = getCustomItem(className);

		if (customItem != null) {
			return customItem.getItem(player);
		}
		return null;
	}

	/**
	 * Get an {@link ItemStack} from a {@link CustomItemInitialized} with id
	 *
	 * @param clazz The {@link CustomItemInitialized} class to get the
	 *                  {@link ItemStack} from
	 * @param id The id for the specific item
	 * @param player    The player that the item was created by
	 * @return An {@link ItemStack} or <code>null</code> if not loaded or not found
	 */
	@Nullable
	public ItemStack getCustomItemStackByID(String id, Class<? extends CustomItemInitialized> clazz, @Nullable Player player) {
		return this.getCustomItemStack(clazz.getName() + "." + id, player);
	}

	/**
	 * Check if a {@link ItemStack} has the {@link CustomItem} NBT data
	 *
	 * @param item The {@link ItemStack} to check
	 * @return <code>true</code> if the item contains the NBT tag <code>novacore -
	 *         iscustomitem</code>
	 */
	public boolean isCustomItem(ItemStack item) {
		if (item == null) {
			return false;
		}

		return NBTEditor.contains(item, "novacore", "iscustomitem");
	}

	/**
	 * Try to get the {@link CustomItem} of an {@link ItemStack}
	 *
	 * @param item The {@link ItemStack} to check
	 * @return The {@link CustomItem} instance or <code>null</code> if the
	 *         {@link CustomItem} has not been loaded or is missing
	 */
	@Nullable
	private CustomItem getCustomItem(ItemStack item) {
		if (isCustomItem(item)) {
			String itemId = NBTEditor.getString(item, "novacore", "customitemid");

			return customItems.get(itemId);
		}

		return null;
	}

	/**
	 * Check if a {@link ItemStack} is of the provided {@link CustomItem} class type
	 *
	 * @param item            The {@link ItemStack} to check
	 * @param customItemClass The {@link CustomItem} class to check
	 * @return <code>true</code> it the item is a custom item of the provided type
	 */
	public boolean isType(ItemStack item, Class<? extends CustomItem> customItemClass) {
		if (isCustomItem(item)) {
			String itemId = NBTEditor.getString(item, "novacore", "customitemid");

			return itemId.equalsIgnoreCase(customItemClass.getName());
		}

		return false;
	}

	public boolean isType(ItemStack item, Class<? extends CustomItemInitialized> customItemClass, String id) {
		if (isCustomItem(item)) {
			String itemId = NBTEditor.getString(item, "novacore", "customitemid");

			return itemId.equalsIgnoreCase(customItemClass.getName() + "." + id);
		}

		return false;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getItem() != null) {
			if (e.getItem().getType() != Material.AIR) {
				if (isCustomItem(e.getItem())) {
					CustomItem customItem = getCustomItem(e.getItem());

					if (customItem != null) {
						customItem.onPlayerInteract(e);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (e.getItemDrop() != null) {
			if (e.getItemDrop().getItemStack().getType() != Material.AIR) {
				if (isCustomItem(e.getItemDrop().getItemStack())) {
					CustomItem customItem = getCustomItem(e.getItemDrop().getItemStack());

					if (customItem != null) {
						customItem.onPlayerDropItem(e);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent e) {
		ItemStack item = NovaCore.getInstance().getVersionIndependentUtils().getItemInMainHand(e.getPlayer());
		if (item != null) {
			if (item.getType() != Material.AIR) {
				if (isCustomItem(item)) {
					CustomItem customItem = getCustomItem(item);

					if (customItem != null) {
						customItem.onBlockBreak(e);
					}
				}
			}
		}
	}


	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (customItemAdditionsManager ==  null) return;
		ItemStack projectileItemStack = customItemAdditionsManager.getProjectileItemStack(event);
		if (!isCustomItem(projectileItemStack) || !(getCustomItem(projectileItemStack) instanceof CustomItemProjectile)) return;
		CustomItemProjectile customItemProjectile = (CustomItemProjectile) getCustomItem(projectileItemStack);
		customItemProjectile.onProjectileHit(event);

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getCurrentItem() != null) {
			if (e.getCurrentItem().getType() != Material.AIR) {
				if (isCustomItem(e.getCurrentItem())) {
					CustomItem customItem = getCustomItem(e.getCurrentItem());

					if (customItem != null) {
						customItem.onInventoryClick(e);
					}
				}
			}
		}
	}
}