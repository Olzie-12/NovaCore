package net.zeeraa.novacore.spigot.version.v1_12_R1;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkUnloadEvent;

import net.zeeraa.novacore.spigot.abstraction.ChunkLoader;

public class ChunkLoaderImplementation extends ChunkLoader {
	public ChunkLoaderImplementation() {
		super();
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onChunkUnload(ChunkUnloadEvent e) {
		if (chunks.contains(e.getChunk())) {
			e.setCancelled(true);
		}
	}
}