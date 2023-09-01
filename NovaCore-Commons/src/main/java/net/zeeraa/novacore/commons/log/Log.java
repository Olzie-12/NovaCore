package net.zeeraa.novacore.commons.log;

import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.zeeraa.novacore.commons.NovaCommons;

/**
 * This is the logger built into both NovaCore Spigot and Novacore Bungeecord
 * <p>
 * The log function can be called from async tasks
 * 
 * @author Zeeraa
 */
public class Log {
	private static LogLevel consoleLogLevel = LogLevel.INFO;

	private static boolean disableColors = false;

	/**
	 * Check if built in colors are disabled
	 * 
	 * @return <code>true</code> if built in colors are disabled
	 */
	public static boolean isDisableColors() {
		return disableColors;
	}

	/**
	 * Disable the built in colors to the log messages. If the message printed is
	 * using colors it will still print that color to the console
	 * 
	 * @param disableColors <code>true</code> to disable built in colors
	 */
	public static void setDisableColors(boolean disableColors) {
		Log.disableColors = disableColors;
	}

	public static HashMap<UUID, LogLevel> subscribedPlayers = new HashMap<>();

	public static void trace(String message) {
		Log.trace(null, message);
	}

	public static void debug(String message) {
		Log.debug(null, message);
	}

	public static void info(String message) {
		Log.info(null, message);
	}

	public static void success(String message) {
		Log.success(null, message);
	}

	public static void warn(String message) {
		Log.warn(null, message);
	}

	public static void error(String message) {
		Log.error(null, message);
	}

	public static void fatal(String message) {
		Log.fatal(null, message);
	}

	public static void trace(String source, String message) {
		Log.log(source, message, LogLevel.TRACE);
	}

	public static void debug(String source, String message) {
		Log.log(source, message, LogLevel.DEBUG);
	}

	public static void info(String source, String message) {
		Log.log(source, message, LogLevel.INFO);
	}

	public static void success(String source, String message) {
		Log.log(source, message, LogLevel.SUCCESS);
	}

	public static void warn(String source, String message) {
		Log.log(source, message, LogLevel.WARN);
	}

	public static void error(String source, String message) {
		Log.log(source, message, LogLevel.ERROR);
	}

	public static void fatal(String source, String message) {
		Log.log(source, message, LogLevel.FATAL);
	}

	public static void log(String source, String message, LogLevel logLevel) {
		/*
		 * if (NovaCommons.getServerType() == ServerType.SPIGOT) { if
		 * (!Bukkit.isPrimaryThread()) { AsyncManager.runSync(() -> Log.log(source,
		 * message, logLevel)); return; } }
		 */

		String fullMessage;

		if (disableColors) {
			fullMessage = "[" + logLevel.getMessagePrefix(false) + "]" + (source == null ? "" : " [" + ChatColor.stripColor(source) + "]") + ": " + message;
		} else {
			fullMessage = "[" + logLevel.getMessagePrefix(true) + ChatColor.RESET + "]" + (source == null ? "" : " [" + ChatColor.GOLD + source + ChatColor.RESET + "]") + ": " + message;
		}

		if (logLevel.shouldLog(consoleLogLevel)) {
			if (NovaCommons.getAbstractConsoleSender() == null) {
				System.out.println(ChatColor.stripColor(message));
			} else {
				NovaCommons.getAbstractConsoleSender().sendMessage(fullMessage);
			}
		}

		if (NovaCommons.getAbstractPlayerMessageSender() == null) {
			return;
		}

		subscribedPlayers.keySet().forEach(uuid -> {
			LogLevel userLogLevel = subscribedPlayers.get(uuid);
			if (logLevel.shouldLog(userLogLevel)) {
				NovaCommons.getAbstractPlayerMessageSender().trySendMessage(uuid, fullMessage);
			}
		});
	}

	/**
	 * Get the log level the console is set to use. Default is {@link LogLevel#INFO}
	 * 
	 * @return The {@link LogLevel} the console is using
	 */
	public static LogLevel getConsoleLogLevel() {
		return consoleLogLevel;
	}

	/**
	 * Set the log level for the console to use. Default is {@link LogLevel#INFO}
	 * 
	 * @param consoleLogLevel The {@link LogLevel} to use
	 */
	public static void setConsoleLogLevel(LogLevel consoleLogLevel) {
		Log.consoleLogLevel = consoleLogLevel;
	}

	public static HashMap<UUID, LogLevel> getSubscribedPlayers() {
		return subscribedPlayers;
	}
}