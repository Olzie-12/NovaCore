package net.zeeraa.novacore.spigot.mapdisplay.command.subcommand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplay;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplayManager;

public class MDDebugFrames extends NovaSubCommand {

	public MDDebugFrames() {
		super("debugframes");

		setPermission("novacore.command.mapdisplay.debugframes");
		setPermissionDefaultValue(PermissionDefault.OP);
		setDescription("Test command");
		setFilterAutocomplete(true);
		setAllowedSenders(AllowedSenders.ALL);
		setUsage("/mapdisplay debugframes <name>");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if(!MapDisplayManager.getInstance().isEnabled()) {
			sender.sendMessage(ChatColor.DARK_RED + "MapDisplayManager is not enabled");
			return false;
		}
		
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please provide a name");
			return false;
		}

		for (MapDisplay display : MapDisplayManager.getInstance().getMapDisplays()) {
			if (display.getNamespace().equalsIgnoreCase(args[0])) {
				display.debugFrames();
				
				return true;
			}
		}

		sender.sendMessage(ChatColor.RED + "Could not find map display named " + args[0]);

		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<>();

		MapDisplayManager.getInstance().getMapDisplays().forEach(display -> result.add(display.getNamespace()));
		if(sender instanceof Player) {
			Player player = (Player) sender;
			World world = player.getWorld();
			MapDisplayManager.getInstance().getMapDisplaysInWorld(world).forEach(display -> result.add(display.getName()));
		}

		return result;
	}
}