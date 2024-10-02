package net.zeeraa.novacore.spigot.version.v1_16_R3;

import org.bukkit.Chunk;
import net.zeeraa.novacore.spigot.abstraction.ChunkLoader;

public class ChunkLoaderImplementation extends ChunkLoader {
	public ChunkLoaderImplementation() {
		super();
	}

	@Override
	protected void onAdd(Chunk chunk) {
		chunk.setForceLoaded(true);
	}

	@Override
	protected void onRemove(Chunk chunk) {
		chunk.setForceLoaded(false);
	}
}