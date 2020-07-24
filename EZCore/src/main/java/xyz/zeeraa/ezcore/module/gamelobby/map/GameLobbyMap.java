package xyz.zeeraa.ezcore.module.gamelobby.map;

import org.bukkit.Location;
import org.bukkit.World;


public class GameLobbyMap {
	private World world;
	private GameLobbyMapData mapData;
	
	private Location spawnLocation;

	public GameLobbyMap(World world, GameLobbyMapData gameLobbyMapData, Location location) {
		
	}

	/**
	 * Get the world that the lobby map loaded
	 * 
	 * @return The world for the lobby map
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Return the instance of the {@link GameLobbyMapData} this object was created from
	 * 
	 * @return The {@link GameLobbyMapData} for this lobby map
	 */
	public GameLobbyMapData getMapData() {
		return mapData;
	}

	/**
	 * Get the spawn location
	 * 
	 * @return the spawn location
	 */
	public Location getSpawnLocation() {
		return spawnLocation;
	}
}