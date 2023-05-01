package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.farmlandprotection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.json.JSONObject;

import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentMaterial;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;

public class FarmlandProtectionMapModule extends MapModule implements Listener {
	public FarmlandProtectionMapModule(JSONObject json) {
		super(json);
	}

	@Override
	public void onGameStart(Game game) {
		Bukkit.getServer().getPluginManager().registerEvents(this, game.getPlugin());
	}

	@Override
	public void onGameEnd(Game game) {
		HandlerList.unregisterAll(this);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL) {
			if (e.getClickedBlock() != null) {
				if (e.getClickedBlock().getType() == VersionIndependentMaterial.FARMLAND.toBukkitVersion()) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockFade(BlockFadeEvent e) {
		if (e.getBlock().getType() == VersionIndependentMaterial.FARMLAND.toBukkitVersion()) {
			if (e.getNewState().getType() == Material.DIRT) {
				e.setCancelled(true);
			}
		}
	}
}