package xyz.zeeraa.ezcore.module.gamelobby.map;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import xyz.zeeraa.ezcore.log.EZLogger;
import xyz.zeeraa.ezcore.module.game.GameManager;
import xyz.zeeraa.ezcore.module.game.mapselector.MapSelector;
import xyz.zeeraa.ezcore.module.gamelobby.GameLobby;

public abstract class GameLobbyReader {
	/**
	 * Try to load all JSON files from a directory as maps and add them to the lobby list
	 * 
	 * @param directory      Directory to scan
	 * @param worldDirectory The directory containing the worlds
	 */
	public void loadAll(File directory, File worldDirectory) {
		EZLogger.info("Scanning folder " + directory.getName() + " for lobby maps");
		for (File file : directory.listFiles()) {
			if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("json")) {
				if (!file.isDirectory()) {
					this.readMap(file, worldDirectory);
				}
			}
		}
	}

	/**
	 * Read {@link GameLobbyMapData} from a {@link File} list and add it to the active
	 * {@link MapSelector} defined by {@link GameManager#getMapSelector()}
	 * 
	 * @param file           The {@link File} to read
	 * @param worldDirectory The directory containing the worlds
	 * @return Instance of the {@link GameLobbyMapData} or <code>null</code> on failure
	 */
	public GameLobbyMapData readMap(File file, File worldDirectory) {
		try {
			EZLogger.info("Reading lobby map from file " + file.getName());

			String data = FileUtils.readFileToString(file, "UTF-8");

			GameLobbyMapData map = this.readMap(new JSONObject(data), worldDirectory);

			GameLobby.getInstance().getMapSelector().addMap(map);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Read {@link GameLobbyMapData} from a {@link JSONObject}
	 * 
	 * @param json           The {@link JSONObject} to read
	 * @param worldDirectory The directory containing the worlds
	 * @return Instance of the {@link GameLobbyMapData} or <code>null</code> on failure
	 */
	public abstract GameLobbyMapData readMap(JSONObject json, File worldDirectory);
}