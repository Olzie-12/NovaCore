package net.zeeraa.novacore.spigot.command.commands.novacore.logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.ChatColor;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.log.LogLevel;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

/**
 * A command from NovaCore
 * 
 * @author Zeeraa
 */
public class NovaCoreSubCommandLogger extends NovaSubCommand {
	public NovaCoreSubCommandLogger() {
		super("logger");
		
		this.setAliases(NovaSubCommand.generateAliasList("log"));
		this.setPermission("novacore.command.novacore.logger");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.addHelpSubCommand();
		this.addSubCommand(new NovaCoreSubCommandLoggerSet());
		this.addSubCommand(new NovaCoreSubCommandLoggerUnsubscribe());
		
		this.setDescription("Set log level for player and console");
		
		this.setFilterAutocomplete(true);
		this.setEmptyTabMode(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(Log.getSubscribedPlayers().containsKey(p.getUniqueId())) {
				p.sendMessage(ChatColor.GOLD + "Your log level is: " + Log.getSubscribedPlayers().get(p.getUniqueId()).name());
			} else {
				p.sendMessage(ChatColor.GOLD + "You dont have any log level set");
			}
		}
		
		sender.sendMessage(ChatColor.GOLD + "Use "+ChatColor.AQUA+"/novacore logger help" + ChatColor.GOLD + " for help");
		
		return true;
	}
}

class NovaCoreSubCommandLoggerSet extends NovaSubCommand {
	public NovaCoreSubCommandLoggerSet() {
		super("set");
		this.setPermission("novacore.command.novacore.logger.set");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.setDescription("Set your log level");
		
		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Missing argument: Log level");
		}

		LogLevel level;
		try {
			level = LogLevel.valueOf(args[0].toUpperCase());
		} catch (Exception e) {
			String valid = "";
			for (LogLevel l : LogLevel.values()) {
				valid += l.name() + " ";
			}
			sender.sendMessage(ChatColor.RED + "Invalid log level. Valid values are " + ChatColor.GREEN + valid);
			return false;
		}

		if (sender instanceof Player) {
			Log.getSubscribedPlayers().put(((Player) sender).getUniqueId(), level);
		} else if (sender instanceof ConsoleCommandSender) {
			NovaCore.getInstance().setLogLevel(level);
		} else {
			sender.sendMessage(ChatColor.RED + "This command can only be used by players and the console");
			return false;
		}

		sender.sendMessage(ChatColor.GREEN + "Your log level has been set to " + ChatColor.AQUA + level.name());

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		if (args.length == 0) {
			return ImmutableList.of();
		}

		String lastWord = args[args.length - 1];

		ArrayList<String> matchedLevels = new ArrayList<String>();
		for (LogLevel level : LogLevel.values()) {
			String name = level.name();
			if (StringUtil.startsWithIgnoreCase(name, lastWord)) {
				matchedLevels.add(name);
			}
		}

		Collections.sort(matchedLevels, String.CASE_INSENSITIVE_ORDER);
		return matchedLevels;
	}
}

class NovaCoreSubCommandLoggerUnsubscribe extends NovaSubCommand {
	public NovaCoreSubCommandLoggerUnsubscribe() {
		super("unsubscribe");
		this.setPermission("novacore.command.novacore.logger.unsubscribe");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.setAllowedSenders(AllowedSenders.PLAYERS);

		this.setDescription("Disable all log messages");
		
		this.setFilterAutocomplete(true);
		this.setEmptyTabMode(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player player = (Player) sender;

		if (Log.subscribedPlayers.containsKey(player.getUniqueId())) {
			Log.subscribedPlayers.remove(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + "You will no longer receive logger messages");
		} else {
			player.sendMessage(ChatColor.GREEN + "You already have logger messages disabled");
		}

		return true;
	}
}