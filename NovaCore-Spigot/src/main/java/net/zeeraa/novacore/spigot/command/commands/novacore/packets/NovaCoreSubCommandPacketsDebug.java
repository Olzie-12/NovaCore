package net.zeeraa.novacore.spigot.command.commands.novacore.packets;

import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.abstraction.packet.MinecraftChannelDuplexHandler;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class NovaCoreSubCommandPacketsDebug extends NovaSubCommand {
	public NovaCoreSubCommandPacketsDebug() {
		super("toggledebug");
		this.setPermission("novacore.command.novacore.packet.debug");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.setDescription("Allow/Disallow packet debugging");

		this.setFilterAutocomplete(true);
		this.setEmptyTabMode(true);

		this.setAllowedSenders(AllowedSenders.ALL);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		String againImLazy = MinecraftChannelDuplexHandler.isDebug() ? ChatColor.GREEN + "Packet Debugging Enabled, remember to run \"nova log set DEBUG\" on the CONSOLE (not in game)" : ChatColor.RED + "Packet Debugging Disabled.";
		Player player = (Player) sender;

		boolean b = MinecraftChannelDuplexHandler.isDebug() ? NovaCore.getInstance().disablePacketDebugging() : NovaCore.getInstance().enablePacketDebugging();

		if (b) {
			player.sendMessage(againImLazy);
		} else {
			player.sendMessage(ChatColor.RED + "An error occurred while running command.");
		}

		return true;
	}
}