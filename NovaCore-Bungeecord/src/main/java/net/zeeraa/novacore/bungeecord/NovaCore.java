package net.zeeraa.novacore.bungeecord;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.zeeraa.novacore.bungeecord.abstraction.AbstractBungeecordConsoleSender;
import net.zeeraa.novacore.bungeecord.abstraction.AbstractBungeecordPlayerMessageSender;
import net.zeeraa.novacore.bungeecord.abstraction.BungeecordAsyncManager;
import net.zeeraa.novacore.bungeecord.abstraction.BungeecordSimpleTaskCreator;
import net.zeeraa.novacore.bungeecord.delayedrunner.DelayedRunnerImplementationBungee;
import net.zeeraa.novacore.bungeecord.novaplugin.NovaPlugin;
import net.zeeraa.novacore.bungeecord.platformindependent.BungeePlatformIndependentBungeecordAPI;
import net.zeeraa.novacore.bungeecord.platformindependent.BungeecordPlatformIndependentPlayerAPI;
import net.zeeraa.novacore.commons.NovaCommons;
import net.zeeraa.novacore.commons.ServerType;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.log.LogLevel;
import net.zeeraa.novacore.commons.utils.DelayedRunner;
import net.zeeraa.novacore.commons.utils.Hastebin;

public class NovaCore extends NovaPlugin {
	private static NovaCore instance;

	public static NovaCore getInstance() {
		return instance;
	}

	private File logSeverityConfigFile;
	private Configuration logSeverityConfig;

	public void setLogLevel(LogLevel logLevel) {
		try {
			Log.info("NovaCore", "Setting console log level to " + logLevel.name());
			Log.setConsoleLogLevel(logLevel);
			logSeverityConfig.set("severity", logLevel.name());
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(logSeverityConfig, logSeverityConfigFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnable() {
		saveDefaultConfiguration();

		NovaCore.instance = this;

		NovaCommons.setAbstractSimpleTaskCreator(new BungeecordSimpleTaskCreator());
		NovaCommons.setAbstractConsoleSender(new AbstractBungeecordConsoleSender());
		NovaCommons.setAbstractPlayerMessageSender(new AbstractBungeecordPlayerMessageSender());
		NovaCommons.setAbstractAsyncManager(new BungeecordAsyncManager(this));
		NovaCommons.setPlatformIndependentBungeecordAPI(new BungeePlatformIndependentBungeecordAPI());
		NovaCommons.setPlatformIndependentPlayerAPI(new BungeecordPlatformIndependentPlayerAPI());
		NovaCommons.setServerType(ServerType.BUNGEECORD);

		DelayedRunner.setImplementation(new DelayedRunnerImplementationBungee());
		
		try {
			FileUtils.forceMkdir(this.getDataFolder());

			logSeverityConfigFile = new File(this.getDataFolder(), "log_severity.yml");

			if (!logSeverityConfigFile.exists()) {
				Log.info("NovaCore", "Creating log_severity.yml");
				FileUtils.touch(logSeverityConfigFile);
			}

			logSeverityConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "log_severity.yml"));

			if (!logSeverityConfig.contains("severity")) {
				setLogLevel(LogLevel.INFO);
			}

			String logLevelName = logSeverityConfig.getString("severity");

			try {
				LogLevel logLevel = LogLevel.valueOf(logLevelName);
				Log.setConsoleLogLevel(logLevel);
			} catch (Exception e) {
				Log.warn("NovaCore", "The value " + logLevelName + " is not a valid LogLevel. Resetting it to " + LogLevel.INFO.name());
				setLogLevel(LogLevel.INFO);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.fatal("NovaCore", "Failed to setup data directory");
			return;
		}

		String hastebinUrl = "https://hastebin.novauniverse.net";
		try {
			Configuration configuration = getConfig();

			if (configuration.contains("HastebinURL")) {
				hastebinUrl = configuration.getString("HastebinURL");
			}
		} catch (Exception e) {
			Log.error("NovaCore", "Failed to read config.yml");
		}
		NovaCommons.setDefaultHastebinInstance(new Hastebin(hastebinUrl));
		Log.debug("Novacore", "Using hastebin url " + hastebinUrl);

		Log.info("NovaCore", "LogLevel: " + Log.getConsoleLogLevel().name());
		Log.info("NovaCore", "NovaCore Bungeecord has been enabled");
	}

	@Override
	public void onDisable() {
		ProxyServer.getInstance().getPluginManager().unregisterListeners((Plugin) this);
		ProxyServer.getInstance().getScheduler().cancel(this);
	}
}