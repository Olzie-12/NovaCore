package net.zeeraa.novacore.spigot.gameengine.utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.zeeraa.novacore.spigot.utils.LocationData;

/**
 * Represents a hologram used in maps
 * 
 * @author Zeeraa
 */
public class HologramData {
	private List<String> lines;
	private LocationData location;
	private Hologram hologram;

	public HologramData(List<String> lines, LocationData location) {
		this.lines = lines;
		this.location = location;

		this.hologram = null;
	}

	/**
	 * Get a {@link List} with the lines of the hologram
	 * 
	 * @return A {@link List} with line strings
	 */
	public List<String> getLines() {
		return lines;
	}

	/**
	 * Get the location data where the hologram will be created
	 * 
	 * @return The {@link LocationData}
	 */
	public LocationData getLocation() {
		return location;
	}

	/**
	 * Get the hologram
	 * <p>
	 * This will be null until {@link HologramData#create(World)} has been
	 * successfully called
	 * 
	 * @return The {@link Hologram} if it has been created or <code>null</code> if
	 *         it has not been created
	 */
	public Hologram getHologram() {
		return hologram;
	}

	/**
	 * Try to create the hologram. This will fail if HolographicDisplays is not
	 * installed
	 * 
	 * @param world The world to create the hologram in
	 * @return <code>true</code> on success
	 */
	public boolean create(World world) {
		if (Bukkit.getServer().getPluginManager().getPlugin("DecentHolograms") != null) {
			if (hologram != null) {
				hologram.delete();
			}
			hologram = DHAPI.createHologram(UUID.randomUUID().toString(),  location.toLocation(world), false);
			lines.forEach(line -> DHAPI.addHologramLine(hologram, line));
		}
		return false;
	}
}