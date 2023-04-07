package net.zeeraa.novacore.spigot.mapdisplay.command;

import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.BlockIterator;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplay;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplayManager;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplayNameAlreadyExistsException;

public class MapDisplayCommand extends NovaCommand {
	public static int IMAGE_FETCH_TIMEOUT = 10000;
	public static String useragent = "NovaCore 2.0.0 MapImageDisplays";
	public static boolean disableWebInteractions = false;

	public MapDisplayCommand() {
		super("mapdisplay", NovaCore.getInstance());

		setAllowedSenders(AllowedSenders.PLAYERS);
		setPermission("novacore.command.mapdisplay");
		setPermissionDefaultValue(PermissionDefault.OP);
		setEmptyTabMode(true);
		setFilterAutocomplete(true);

		addSubCommand(new MDListSubCommand());
		addSubCommand(new MDCreateSubCommand());
		addSubCommand(new MDDeleteSubCommand());
		addSubCommand(new MDSetImageSubCommand());
		addSubCommand(new MDDebugFrames());

		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "Use " + ChatColor.AQUA + "/mapdisplay help" + ChatColor.GOLD + " for help");
		return true;
	}
}

class MDCreateSubCommand extends NovaSubCommand {
	public MDCreateSubCommand() {
		super("create");

		setPermission("novacore.command.mapdisplay.create");
		setPermissionDefaultValue(PermissionDefault.OP);
		setDescription("Create a map display");
		setEmptyTabMode(true);
		setAllowedSenders(AllowedSenders.PLAYERS);
		setUsage("/mapdisplay create <name>");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!MapDisplayManager.getInstance().isEnabled()) {
			sender.sendMessage(ChatColor.DARK_RED + "MapDisplayManager is not enabled");
			return false;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please provide a name");
			return false;
		}

		Location location = null;
		BlockIterator iter = new BlockIterator(player, 10);

		Block lastBlock = iter.next();
		while (iter.hasNext()) {
			Block next = iter.next();

			if (next.getType() == Material.AIR) {
				lastBlock = next;
				continue;
			}

			location = lastBlock.getLocation();

			break;
		}

		if (location != null) {
			ItemFrame frame = MapDisplayManager.getInstance().getItemFrameAtLocation(location);

			if (frame != null) {
				try {
					MapDisplay display = MapDisplayManager.getInstance().createMapDisplay(frame, true, args[0].toLowerCase());
					try {
						display.setImage(null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (MapDisplayNameAlreadyExistsException e) {
					sender.sendMessage(ChatColor.RED + "A display with that name already exists");
				}
				return true;
			}
		}
		player.sendMessage(ChatColor.RED + "Please look at an item frame before running this");

		return false;
	}
}

class MDDebugFrames extends NovaSubCommand {
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
		if (!MapDisplayManager.getInstance().isEnabled()) {
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
		if (sender instanceof Player) {
			Player player = (Player) sender;
			World world = player.getWorld();
			MapDisplayManager.getInstance().getMapDisplaysInWorld(world).forEach(display -> result.add(display.getName()));
		}

		return result;
	}
}

class MDDeleteSubCommand extends NovaSubCommand {
	public MDDeleteSubCommand() {
		super("delete");
		setPermission("novacore.command.mapdisplay.delete");
		setPermissionDefaultValue(PermissionDefault.OP);
		setDescription("Delete a map display");
		setFilterAutocomplete(true);
		setAllowedSenders(AllowedSenders.ALL);
		setAliases(NovaCommand.generateAliasList("remove"));
		setUsage("/mapdisplay delete <name>");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!MapDisplayManager.getInstance().isEnabled()) {
			sender.sendMessage(ChatColor.DARK_RED + "MapDisplayManager is not enabled");
			return false;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please provide a name");
			return false;
		}

		String name = args[0];
		if (!name.contains(":")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				name = player.getWorld().getName() + ":" + name;
			}
		}

		name = name.toLowerCase();

		MapDisplay display = MapDisplayManager.getInstance().getMapDisplay(name);
		if (display != null) {
			display.delete();
			sender.sendMessage(ChatColor.GREEN + "Display removed");
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "Could not find map display named " + args[0] + ". If the display is in another world make sure you provide the world name like this: world:display_1");
		}
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<>();

		MapDisplayManager.getInstance().getMapDisplays().forEach(display -> result.add(display.getNamespace()));
		if (sender instanceof Player) {
			Player player = (Player) sender;
			World world = player.getWorld();
			MapDisplayManager.getInstance().getMapDisplaysInWorld(world).forEach(display -> result.add(display.getName()));
		}

		return result;
	}
}

class MDListSubCommand extends NovaSubCommand {

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

class MDSetImageSubCommand extends NovaSubCommand {
	public MDSetImageSubCommand() {
		super("setimage");

		setPermission("novacore.command.mapdisplay.setimage");
		setPermissionDefaultValue(PermissionDefault.OP);
		setDescription("Set the image of a map display");
		setFilterAutocomplete(true);
		setAllowedSenders(AllowedSenders.ALL);
		setUsage("/mapdisplay setimage <name> <url>");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!MapDisplayManager.getInstance().isEnabled()) {
			sender.sendMessage(ChatColor.DARK_RED + "MapDisplayManager is not enabled");
			return false;
		}

		if (MapDisplayCommand.disableWebInteractions) {
			sender.sendMessage(ChatColor.RED + "The server does not allow fetching images from external urls");
			return false;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please provide a name of a display");
			return false;
		}

		if (args.length == 1) {
			sender.sendMessage(ChatColor.RED + "Please provide a url containing an image");
			return false;
		}

		String name = args[0];
		if (!name.contains(":")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				name = player.getWorld().getName() + ":" + name;
			}
		}

		name = name.toLowerCase();

		MapDisplay display = MapDisplayManager.getInstance().getMapDisplay(name);
		if (display != null) {
			BufferedImage image = null;

			try {
				URL url = new URL(args[1]);
				final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(MapDisplayCommand.IMAGE_FETCH_TIMEOUT);
				connection.setReadTimeout(MapDisplayCommand.IMAGE_FETCH_TIMEOUT);
				connection.setRequestProperty("User-Agent", MapDisplayCommand.useragent);
				image = ImageIO.read(connection.getInputStream());
				// image = ImageIO.read(url);
				Log.trace("Image loaded from url: " + url);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Could not read image from url " + args[1] + ". " + e.getClass().getName() + " " + e.getMessage());
				e.printStackTrace();
			}

			if (image != null) {
				try {
					display.setImage(image);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "Failed to set the display image. " + e.getClass().getName() + " " + e.getMessage());
					e.printStackTrace();
				}
			}

			return true;
		}
		sender.sendMessage(ChatColor.RED + "Could not find map display named " + args[0] + ". If the display is in another world make sure you provide the world name like this: world:display_1");

		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<>();

		MapDisplayManager.getInstance().getMapDisplays().forEach(display -> result.add(display.getNamespace()));
		if (sender instanceof Player) {
			Player player = (Player) sender;
			World world = player.getWorld();
			MapDisplayManager.getInstance().getMapDisplaysInWorld(world).forEach(display -> result.add(display.getName()));
		}

		return result;
	}
}