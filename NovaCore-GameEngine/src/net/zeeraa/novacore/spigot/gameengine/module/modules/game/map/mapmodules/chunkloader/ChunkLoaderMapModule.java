package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.chunkloader;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import net.zeeraa.novacore.spigot.utils.VectorArea;

public class ChunkLoaderMapModule extends MapModule {
	private List<Chunk> chunks;
	private List<VectorArea> areas;

	public ChunkLoaderMapModule(JSONObject json) {
		super(json);

		this.chunks = new ArrayList<>();
		this.areas = new ArrayList<>();

		JSONArray areas = json.getJSONArray("areas");
		for (int i = 0; i < areas.length(); i++) {
			this.areas.add(VectorArea.fromJSON(areas.getJSONObject(i)));
		}
	}

	@Override
	public void onGameStart(Game game) {
		areas.forEach(area -> {
			for (int x = area.getPosition1().getBlockX(); x < area.getPosition2().getBlockX(); x++) {
				for (int z = area.getPosition1().getBlockZ(); z < area.getPosition2().getBlockZ(); z++) {
					Location location = new Location(game.getWorld(), x, 0, z);
					if (!chunks.contains(location.getChunk())) {
						chunks.add(location.getChunk());
					}
				}
			}
		});

		Log.info("Chunkloader", chunks.size() + " chunks will be loaded");

		chunks.forEach(chunk -> VersionIndependentUtils.get().getChunkLoader().add(chunk));
	}

	@Override
	public void onGameEnd(Game game) {
		chunks.forEach(chunk -> VersionIndependentUtils.get().getChunkLoader().remove(chunk));
	}

	@Override
	public String getSummaryString() {
		String summary = "";
		summary += ChatColor.GOLD + "Chunk count: " + ChatColor.AQUA + chunks.size() + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Chunks: " + ChatColor.AQUA + "";
		for (Chunk chunk : chunks) {
			summary += ChatColor.GOLD + "X: " + ChatColor.AQUA + chunk.getX() + ChatColor.GOLD + " Z: " + ChatColor.AQUA + chunk.getZ() + ChatColor.GOLD + ". ";
		}
		summary += ChatColor.GOLD + ". ";
		return summary;
	}
}