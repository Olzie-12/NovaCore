package net.zeeraa.novacore.spigot.gameengine;

import java.io.File;
import java.io.IOException;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.buildlimit.BuildLimitMapModule;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.utils.JSONFileUtils;
import net.zeeraa.novacore.spigot.command.CommandRegistry;
import net.zeeraa.novacore.spigot.gameengine.command.commands.gamelobby.NovaCoreCommandGameLobby;
import net.zeeraa.novacore.spigot.gameengine.debugtriggers.GameEngineDebugTriggers;
import net.zeeraa.novacore.spigot.gameengine.lootdrop.medical.MedicalSupplyDropManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModuleManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.blockloot.BlockLootMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.blockreplacer.BlockReplacerMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.chestloot.ChestLootMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.chunkloader.ChunkLoaderMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.disablespectatechests.DisableSpectateChestsMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.farmlandprotection.FarmlandProtectionMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.fireresistance.FireReistanceMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.gamerule.GameruleMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.giveitems.GiveItemInstantMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.giveitems.GiveItemSlowMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.graceperiod.falldamagegraceperiod.FallDamageGracePeriodMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.graceperiod.graceperiod.GracePeriodMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.handcraftingtable.HandCraftingTableMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.infinitefood.InfiniteFoodMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.infiniteoxygen.InfiniteOxygenMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.instantvoidkill.InstantVoidKillMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.lootdrop.LootDropMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.lootdrop.medical.MedicalSupplyDropMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.mapprotection.MapProtectionMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.noweather.NoWeatherMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.potioneffect.AddPotionEffectMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.settime.SetTimeMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.simplemapdecay.SimpleBoxDecayMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.startmessage.StartMessageMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.worldborder.WorldborderMapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.gamelobby.GameLobby;
import net.zeeraa.novacore.spigot.language.LanguageReader;
import net.zeeraa.novacore.spigot.module.ModuleManager;
import net.zeeraa.novacore.spigot.novaplugin.NovaPlugin;

public class NovaCoreGameEngine extends NovaPlugin {
	private static NovaCoreGameEngine instance;

	public static NovaCoreGameEngine getInstance() {
		return instance;
	}

	private boolean debugDisableAutoEndGame;
	private File requestedGameDataDirectory;

	public boolean isDebugDisableAutoEndGame() {
		return debugDisableAutoEndGame;
	}
	
	@Override
	public void onEnable() {
		requestedGameDataDirectory = null;

		saveDefaultConfig();

		ConfigurationSection debugSettings = getConfig().getConfigurationSection("Debug");
		debugDisableAutoEndGame = debugSettings.getBoolean("DisableAutoEndGame");

		this.getDataFolder().mkdir();

		Log.info("NovaCoreGameEngine", "Loading language files...");
		try {
			LanguageReader.readFromJar(this.getClass(), "/lang/en-us.json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		NovaCoreGameEngine.instance = this;

		Log.info("NovaCoreGameEngine", "Adding debug triggers...");
		GameEngineDebugTriggers.init();

		Log.info("NovaCoreGameEngine", "Loading modules...");
		ModuleManager.loadModule(this, GameManager.class);
		ModuleManager.loadModule(this, GameLobby.class);
		ModuleManager.loadModule(this, MedicalSupplyDropManager.class);

		Log.info("NovaCoreGameEngine", "Loading map modules...");
		MapModuleManager.addMapModule("novacore.chestloot", ChestLootMapModule.class);
		MapModuleManager.addMapModule("novacore.lootdrop", LootDropMapModule.class);
		MapModuleManager.addMapModule("novacore.lootdrop.medical", MedicalSupplyDropMapModule.class);
		MapModuleManager.addMapModule("novacore.mapprotection", MapProtectionMapModule.class);
		MapModuleManager.addMapModule("novacore.handcraftingtable", HandCraftingTableMapModule.class);
		MapModuleManager.addMapModule("novacore.worldborder", WorldborderMapModule.class);
		MapModuleManager.addMapModule("novacore.settime", SetTimeMapModule.class);
		MapModuleManager.addMapModule("novacore.startmessage", StartMessageMapModule.class);
		MapModuleManager.addMapModule("novacore.graceperiod", GracePeriodMapModule.class);
		MapModuleManager.addMapModule("novacore.falldamagegraceperiod", FallDamageGracePeriodMapModule.class);
		MapModuleManager.addMapModule("novacore.simpleboxdecay", SimpleBoxDecayMapModule.class);
		MapModuleManager.addMapModule("novacore.blockloot", BlockLootMapModule.class);
		MapModuleManager.addMapModule("novacore.blockreplacer", BlockReplacerMapModule.class);
		MapModuleManager.addMapModule("novacore.noweather", NoWeatherMapModule.class);
		MapModuleManager.addMapModule("novacore.gamerule", GameruleMapModule.class);
		MapModuleManager.addMapModule("novacore.addpotioneffect", AddPotionEffectMapModule.class);
		MapModuleManager.addMapModule("novacore.giveitem.slow", GiveItemSlowMapModule.class);
		MapModuleManager.addMapModule("novacore.giveitem.instant", GiveItemInstantMapModule.class);
		MapModuleManager.addMapModule("novacore.instantvoidkill", InstantVoidKillMapModule.class);
		MapModuleManager.addMapModule("novacore.fireresistance", FireReistanceMapModule.class);
		MapModuleManager.addMapModule("novacore.farmlandprotection", FarmlandProtectionMapModule.class);
		MapModuleManager.addMapModule("novacore.chunkloader", ChunkLoaderMapModule.class);
		MapModuleManager.addMapModule("novacore.infiniteoxygen", InfiniteOxygenMapModule.class);
		MapModuleManager.addMapModule("novacore.infinitefood", InfiniteFoodMapModule.class);
		MapModuleManager.addMapModule("novacore.disablespectatechests", DisableSpectateChestsMapModule.class);
		MapModuleManager.addMapModule("novacore.buildlimit", BuildLimitMapModule.class);

		// Legacy modules
		MapModuleManager.addMapModule("novauniverse.survivalgames.medicalsupplydrop", MedicalSupplyDropMapModule.class);

		CommandRegistry.registerCommand(new NovaCoreCommandGameLobby(this));
		CommandRegistry.syncCommands();

		File overridesFile = new File(this.getDataFolder().getAbsolutePath() + File.separator + "overrides.json");
		if (overridesFile.exists()) {
			Log.info("NovaCoreGameEngine", "Found overrides.json");
			try {
				JSONObject overrides = JSONFileUtils.readJSONObjectFromFile(overridesFile);

				if (overrides.has("name_override")) {
					String name = overrides.getString("name_override");
					GameManager.getInstance().setDisplayNameOverride(name);
					Log.info("NovaCoreGameEngine", "Using name override: " + name);
				}
			} catch (JSONException | IOException e) {
				e.printStackTrace();
				Log.error("NovaCoreGameEngine", "Failed to read overrides.json. " + e.getClass().getName() + " " + e.getMessage());
			}
		}

		Log.success("NovaCoreGameEngine", "Game engine enabled");

		if (debugDisableAutoEndGame) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Log.warn("GameEngine", "DisableAutoEndGame set to true in config.yml. If this server is not a dev server disable this option immediately");
				}
			}.runTaskLater(this, 1L);
		}
	}

	public File getRequestedGameDataDirectory() {
		return requestedGameDataDirectory;
	}

	public void setRequestedGameDataDirectory(File requestedGameDataDirectory) {
		this.requestedGameDataDirectory = requestedGameDataDirectory;
	}

	public boolean hasRequestedDataDirectory() {
		return this.getRequestedGameDataDirectory() != null;
	}
}