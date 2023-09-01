package net.zeeraa.novacore.spigot.module.modules.multiverse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.lootdrop.LootDropManager;

public class MultiverseManager extends NovaModule implements Listener {
	private Map<String, MultiverseWorld> worlds;

	public static String UNLOADING_WORLD_MESSAGE = "Unloading world";

	private static MultiverseManager instance;

	public static MultiverseManager getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		instance = this;
		this.worlds = new HashMap<>();
	}

	@Override
	public void onDisable() {
		try {
			this.unloadAll();
		} catch (Exception e) {
			Log.error(getName(), "An error occured while disabling. " + e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace();
		}
	}

	public MultiverseManager() {
		super("NovaCore.MultiverseManager");
	}

	public Map<String, MultiverseWorld> getWorlds() {
		return worlds;
	}

	public boolean isWorldMultiverse(World world) {
		return worlds.values().stream().filter(w -> w.getWorld().equals(world)).findFirst().isPresent();
	}

	@Nullable
	public MultiverseWorld worldToMultiverse(World world) {
		return worlds.values().stream().filter(w -> w.getWorld().equals(world)).findFirst().orElse(null);
	}

	public MultiverseWorld createWorld(WorldOptions options, WorldLoadingFlags... loadFlags) {
		WorldCreator worldCreator = new WorldCreator(options.getName());

		if (options.hasSeed()) {
			worldCreator.seed(options.getSeed());
		}

		worldCreator.environment(worldCreator.environment());

		worldCreator.generateStructures(options.isGenerateStructures());

		if (options.getGenerator() != null) {
			worldCreator.generator(options.getGenerator());
		}

		World world = worldCreator.createWorld();

		MultiverseWorld multiverseWorld = new MultiverseWorld(options.getName(), world, options.getUnloadOption(), options.getPlayerUnloadOption(), options.isSaveOnUnload(), options.isLockWeather(), loadFlags);

		worlds.put(options.getName(), multiverseWorld);

		if (!WorldLoadingFlags.hasFlag(loadFlags, WorldLoadingFlags.PREVENT_AG_RELOAD)) {
			runAGReload();
		}

		return multiverseWorld;
	}

	/**
	 * Load a world from a file. This version of the function will also delete the
	 * uid file
	 * 
	 * @param worldFile    The world {@link File}
	 * @param unloadOption The {@link WorldUnloadOption} to use
	 * @param loadFlags    Settings for loading the world
	 * @return The {@link MultiverseWorld} that was loaded
	 * @throws IOException           An {@link IOException} can occur due to the
	 *                               world already existing, failing to unload the
	 *                               world or failing to copy the world file
	 * @throws FileNotFoundException A {@link FileNotFoundException} if the world
	 *                               file does not exist
	 */
	public MultiverseWorld createFromFile(File worldFile, WorldUnloadOption unloadOption, WorldLoadingFlags... loadFlags) throws IOException {
		String worldName = worldFile.getName();

		return this.createFromFile(worldFile, worldName, unloadOption, true, loadFlags);
	}

	/**
	 * Load a world from a file
	 * 
	 * @param worldFile     The world {@link File}
	 * @param unloadOption  The {@link WorldUnloadOption} to use
	 * @param deleteUidFile <code>true</code> to delete the <code>uid.dat</code>
	 *                      file
	 * @param loadFlags     Settings for loading the world
	 * @return The {@link MultiverseWorld} that was loaded
	 * @throws IOException           An {@link IOException} can occur due to the
	 *                               world already existing, failing to unload the
	 *                               world or failing to copy the world file
	 * @throws FileNotFoundException A {@link FileNotFoundException} if the world
	 *                               file does not exist
	 */
	public MultiverseWorld createFromFile(File worldFile, WorldUnloadOption unloadOption, boolean deleteUidFile, WorldLoadingFlags... loadFlags) throws IOException {
		String worldName = worldFile.getName();

		return this.createFromFile(worldFile, worldName, unloadOption, deleteUidFile, loadFlags);
	}

	/**
	 * Load a world from a file. This version of the function will also delete the
	 * uid file
	 * 
	 * @param worldFile    The world {@link File}
	 * @param name         The name the world should be loaded as
	 * @param unloadOption The {@link WorldUnloadOption} to use
	 * @param loadFlags    Settings for loading the world
	 * 
	 * @return The {@link MultiverseWorld} that was loaded
	 * @throws IOException           An {@link IOException} can occur due to the
	 *                               world already existing, failing to unload the
	 *                               world or failing to copy the world file
	 * @throws FileNotFoundException A {@link FileNotFoundException} if the world
	 *                               file does not exist
	 */
	public MultiverseWorld createFromFile(File worldFile, String name, WorldUnloadOption unloadOption, WorldLoadingFlags... loadFlags) throws IOException {
		return this.createFromFile(worldFile, name, unloadOption, true, loadFlags);
	}

	/**
	 * Load a world from a file
	 * 
	 * @param worldFile     The world {@link File}
	 * @param name          The name the world should be loaded as
	 * @param unloadOption  The {@link WorldUnloadOption} to use
	 * @param deleteUidFile <code>true</code> to delete the <code>uid.dat</code>
	 *                      file
	 * @param loadFlags     Settings for loading the world
	 * 
	 * @return The {@link MultiverseWorld} that was loaded
	 * @throws IOException           An {@link IOException} can occur due to the
	 *                               world already existing, failing to unload the
	 *                               world or failing to copy the world file
	 * @throws FileNotFoundException A {@link FileNotFoundException} if the world
	 *                               file does not exist
	 */
	public MultiverseWorld createFromFile(File worldFile, String name, WorldUnloadOption unloadOption, boolean deleteUidFile, WorldLoadingFlags... loadFlags) throws IOException {
		if (!worldFile.exists()) {
			throw new FileNotFoundException("Could not find world file: " + worldFile.getPath());
		}

		File worldContainer = Bukkit.getServer().getWorldContainer();

		File targetFile = Paths.get(worldContainer.getAbsolutePath() + File.separator + name).toFile();
		if (targetFile.exists()) {
			Log.info("Multiverse", "Replacing old world " + name);
			targetFile.delete();
			FileUtils.deleteDirectory(targetFile);
		}

		Log.info("Multiverse", "Adding " + name + " to " + worldContainer.getAbsolutePath());
		FileUtils.copyDirectory(worldFile, targetFile);

		if (deleteUidFile) {
			File uidFile = new File(targetFile.getAbsolutePath() + File.separator + "uid.dat");

			if (uidFile.exists()) {
				Log.debug("Multiverse", "Deleting uid file " + uidFile.getPath());
				FileUtils.forceDelete(uidFile);
			}
		}

		World world = Bukkit.getServer().createWorld(new WorldCreator(name));

		MultiverseWorld multiverseWorld = new MultiverseWorld(name, world, unloadOption, PlayerUnloadOption.KICK, true, false, loadFlags);

		worlds.put(multiverseWorld.getName(), multiverseWorld);

		if (!WorldLoadingFlags.hasFlag(loadFlags, WorldLoadingFlags.PREVENT_AG_RELOAD)) {
			runAGReload();
		}

		return multiverseWorld;
	}

	public MultiverseWorld cloneWorld(World world, String name, WorldUnloadOption unloadOption, WorldLoadingFlags... loadFlags) {
		WorldCreator worldCreator = new WorldCreator(name);

		worldCreator.copy(world);

		World world2 = worldCreator.createWorld();

		MultiverseWorld multiverseWorld = new MultiverseWorld(name, world2, unloadOption, PlayerUnloadOption.KICK, true, false, loadFlags);

		worlds.put(name, multiverseWorld);

		if (!WorldLoadingFlags.hasFlag(loadFlags, WorldLoadingFlags.PREVENT_AG_RELOAD)) {
			runAGReload();
		}

		return multiverseWorld;
	}

	private void runAGReload() {
		if (Bukkit.getServer().getPluginManager().getPlugin("AdvancedGUI") != null) {
			if (!NovaCore.getInstance().isAdvancedGUISupportDisabled()) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Log.debug("MultiverseManager", "Reloading AdvancedGUI using command");
						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ag reload");
					}
				}.runTaskLater(NovaCore.getInstance(), NovaCore.getInstance().getAdvancedGUIMultiverseReloadDelay());
			}
		}
	}

	public MultiverseWorld getWorld(String name) {
		return worlds.get(name);
	}

	public MultiverseWorld getWorld(World world) {
		return worlds.get(world.getName());
	}

	public boolean hasWorld(World world) {
		return hasWorld(world.getName());
	}

	public boolean hasWorld(String name) {
		return worlds.containsKey(name);
	}

	public boolean unload(String name) {
		if (worlds.containsKey(name)) {
			return unload(worlds.get(name));
		}

		return false;
	}

	public boolean unload(MultiverseWorld multiverseWorld) {
		String worldName = multiverseWorld.getWorld().getName();
		Log.info("Multiverse", "Unloading world " + worldName);

		if (LootDropManager.getInstance().isEnabled()) {
			LootDropManager.getInstance().removeFromWorld(multiverseWorld.getWorld());
		}

		for (Player player : multiverseWorld.getWorld().getPlayers()) {
			switch (multiverseWorld.getPlayerUnloadOptions()) {
			case KICK:
				player.kickPlayer(UNLOADING_WORLD_MESSAGE);
				break;

			case SEND_TO_FIRST:
				boolean failed = true;

				for (World world : Bukkit.getServer().getWorlds()) {
					if (!world.getUID().toString().equalsIgnoreCase(multiverseWorld.getWorld().getUID().toString())) {
						player.teleport(world.getSpawnLocation());
						failed = false;
						break;
					}
				}

				if (failed) {
					player.kickPlayer("Unloading world\nCould not find a fallback world");
				}
				break;

			case DO_NOTHING:
				break;

			default:
				player.kickPlayer("Unloading world.\nA server error has occured: ERR:BAD_PLAYER_UNLOAD_OPTION");
				break;
			}
		}

		Bukkit.getServer().unloadWorld(multiverseWorld.getWorld(), multiverseWorld.isSaveOnUnload());
		worlds.remove(multiverseWorld.getName());

		if (multiverseWorld.getUnloadOption() == WorldUnloadOption.DELETE) {
			Log.info("Multiverse", "Deleting unloaded world " + worldName);
			try {
				FileUtils.deleteDirectory(new File(Bukkit.getServer().getWorldContainer().getPath(), worldName));
			} catch (IOException e) {
				Log.error("Multiverse", "IOException while trying to delete world " + worldName);
				e.printStackTrace();
			}
		}
		return true;
	}

	public void unloadAll() {
		while (worlds.size() > 0) {
			String key = worlds.keySet().iterator().next();
			this.unload(worlds.get(key));
		}
	}

	@EventHandler
	public void onWatherChage(WeatherChangeEvent e) {
		if (this.hasWorld(e.getWorld())) {
			if (this.getWorld(e.getWorld()).isLockWeather()) {
				Log.debug("Multiverse", "Prevented weather change in world " + e.getWorld().getName());
				e.setCancelled(true);
			}
		}
	}
}