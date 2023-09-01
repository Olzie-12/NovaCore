package net.zeeraa.novacore.spigot.command.commands.novacore.loottable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.loottable.LootTable;

/**
 * A command from NovaCore
 * 
 * @author Zeeraa
 */
public class NovaCoreSubCommandLootTable extends NovaSubCommand {
	public NovaCoreSubCommandLootTable() {
		super("loottable");

		this.setAliases(generateAliasList("loottables"));

		this.setDescription("Manage loot tables");

		this.setPermission("novacore.command.novacore.loottable");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.addHelpSubCommand();

		this.addSubCommand(new NovaCoreSubCommandLootTableList());
		this.addSubCommand(new NovaCoreSubCommandLootTableTest());
		this.addSubCommand(new NovaCoreSubCommandLootTableLoaderList());

		this.setFilterAutocomplete(true);
		this.setEmptyTabMode(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.AQUA + "" + NovaCore.getInstance().getLootTableManager().getLootTables().size() + ChatColor.GOLD + " Loot tables loaded and " + ChatColor.AQUA + "" + NovaCore.getInstance().getLootTableManager().getLoaders().size() + ChatColor.GOLD + " loaders added. use " + ChatColor.AQUA + "/novacore loottable help" + ChatColor.GOLD + " for help");
		return true;
	}
}

class NovaCoreSubCommandLootTableList extends NovaSubCommand {
	public NovaCoreSubCommandLootTableList() {
		super("list");

		this.setDescription("List loot tables");

		this.setPermission("novacore.command.novacore.loottable.list");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.setFilterAutocomplete(true);

		this.setEmptyTabMode(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		String message = ChatColor.AQUA + "" + NovaCore.getInstance().getLootTableManager().getLootTables().size() + ChatColor.GOLD + " Loot tables loaded\n";

		String lootTableList = "";
		for (String key : NovaCore.getInstance().getLootTableManager().getLootTables().keySet()) {
			LootTable lootTable = NovaCore.getInstance().getLootTableManager().getLootTable(key);

			lootTableList += ChatColor.AQUA + lootTable.getName() + ChatColor.GOLD + " : " + ChatColor.AQUA + lootTable.getDisplayName() + ChatColor.RESET + "\n";
		}

		message += ChatColor.GOLD + "Loot table list:\n-- name -- | -- display name --\n\n" + lootTableList;

		sender.sendMessage(message);

		return false;
	}
}

class NovaCoreSubCommandLootTableTest extends NovaSubCommand {
	public NovaCoreSubCommandLootTableTest() {
		super("test");

		this.setDescription("Test the loot of a loot table");

		this.setPermission("novacore.command.novacore.loottable.test");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.setAllowedSenders(AllowedSenders.PLAYERS);

		this.addHelpSubCommand();

		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please specify a loot table name");
			return false;
		}

		LootTable lootTable = NovaCore.getInstance().getLootTableManager().getLootTable(args[0]);

		if (lootTable == null) {
			sender.sendMessage(ChatColor.RED + "Could not find a loot table with that name");
			return false;
		}

		Player player = (Player) sender;

		List<ItemStack> items = lootTable.generateLoot();

		player.getInventory().clear();
		while (items.size() > 0) {

			player.getInventory().addItem(items.remove(0));
		}

		player.sendMessage(ChatColor.GOLD + "Added loot from the loot table " + ChatColor.AQUA + lootTable.getDisplayName() + ChatColor.GOLD + " to your inventory");

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		ArrayList<String> lootTables = new ArrayList<String>();

		if (args.length == 1) {
			NovaCore.getInstance().getLootTableManager().getLootTables().keySet().forEach(key -> lootTables.add(key));
		}

		return lootTables;
	}
}

class NovaCoreSubCommandLootTableLoaderList extends NovaSubCommand {
	public NovaCoreSubCommandLootTableLoaderList() {
		super("loaders");

		this.setAliases(generateAliasList("loader"));

		this.setDescription("List loot table loaders");

		this.setPermission("novacore.command.novacore.loottable.loaders");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.setFilterAutocomplete(true);
		this.setEmptyTabMode(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		String message = ChatColor.AQUA + "" + NovaCore.getInstance().getLootTableManager().getLoaders().size() + ChatColor.GOLD + " Loot tables loaders added\n";

		String loadersList = "";
		for (String key : NovaCore.getInstance().getLootTableManager().getLoaders().keySet()) {
			loadersList += ChatColor.AQUA + key + ChatColor.RESET + "\n";
		}

		message += ChatColor.GOLD + "Loot table loader list:\n" + loadersList;

		sender.sendMessage(message);

		return false;
	}
}