package net.zeeraa.novacore.spigot.mapdisplay.command.subcommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplayManager;

public class MDListSubCommand extends NovaSubCommand {

	public MDListSubCommand() {
		super("list");

		setPermission("novacore.command.mapdisplay.list");
		setPermissionDefaultValue(PermissionDefault.OP);
		setDescription("List all map displays");
		setEmptyTabMode(true);
		setAllowedSenders(AllowedSenders.ALL);
		setUsage("/mapdisplay list");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!MapDisplayManager.getInstance().isEnabled()) {
			sender.sendMessage(ChatColor.DARK_RED + "MapDisplayManager is not enabled");
			return false;
		}

		sender.sendMessage(ChatColor.AQUA.toString() + MapDisplayManager.getInstance().getMapDisplays().size() + ChatColor.GOLD + " displays found");
		MapDisplayManager.getInstance().getMapDisplays().forEach(display -> sender.sendMessage(ChatColor.AQUA + display.getNamespace() + ChatColor.GOLD + " in world " + ChatColor.AQUA + display.getWorld().getName()));
		return true;
	}
}