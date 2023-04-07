package net.zeeraa.novacore.spigot.command.commands.novacore.module;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.module.ModuleEnableFailureReason;
import net.zeeraa.novacore.spigot.module.ModuleManager;
import net.zeeraa.novacore.spigot.module.ModuleNameComparator;
import net.zeeraa.novacore.spigot.module.NovaModule;

/**
 * A command from NovaCore
 * 
 * @author Zeeraa
 */
public class NovaCoreSubCommandModules extends NovaSubCommand {
	public NovaCoreSubCommandModules() {
		super("modules");

		this.setDescription("Manage modules");

		this.setAliases(generateAliasList("module"));

		this.setPermission("novacore.command.novacore.modules");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.addHelpSubCommand();
		this.addSubCommand(new NovaCoreSubCommandModulesList());
		this.addSubCommand(new NovaCoreSubCommandModulesDisable());
		this.addSubCommand(new NovaCoreSubCommandModulesEnable());
		this.addSubCommand(new NovaCoreSubCommandModulesReload());

		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.AQUA + "" + ModuleManager.getModules().size() + ChatColor.GOLD + " Modules loaded. use " + ChatColor.AQUA + "/novacore modules help" + ChatColor.GOLD + " for help");
		return true;
	}
}

class NovaCoreSubCommandModulesDisable extends NovaSubCommand {
	public NovaCoreSubCommandModulesDisable() {
		super("disable");
		setPermission("novacore.command.novacore.modules.disable");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		setDescription("Disable a module");

		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please specify a module name");
			return false;
		}

		NovaModule module = null;

		for (String key : ModuleManager.getModules().keySet()) {
			NovaModule m = ModuleManager.getModule(key);
			if (m.getName().equalsIgnoreCase(args[0])) {
				module = m;
				break;
			}
		}

		if (module == null) {
			sender.sendMessage(ChatColor.RED + "Could not find a module with that name");
			return false;
		}

		if (ModuleManager.isEssential(module)) {
			sender.sendMessage(ChatColor.RED + "This module cant be disabled using this command");
			return false;
		}

		if (!module.isEnabled()) {
			sender.sendMessage(ChatColor.RED + "That module is already disabled");
			return false;
		}

		if (module.disable()) {
			sender.sendMessage(ChatColor.GREEN + "Module " + module.getName() + " was disabled successfully");
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "The module " + module.getName() + " was disabled but an error occured.\nMore info might be avaliable in the console");
			return false;
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> modules = new ArrayList<String>();

		if (args.length == 1) {
			ModuleManager.getModules().keySet().forEach(key -> {
				if (ModuleManager.isEnabled(key)) {
					if (!ModuleManager.isEssential(ModuleManager.getModules().get(key))) {
						modules.add(ModuleManager.getModule(key).getName());
					}
				}
			});
		}

		modules.sort(new ModuleNameComparator());

		return modules;
	}
}

class NovaCoreSubCommandModulesEnable extends NovaSubCommand {
	public NovaCoreSubCommandModulesEnable() {
		super("enable");
		setPermission("novacore.command.novacore.modules.enable");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		setDescription("Enable a module");

		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please specify a module name");
			return false;
		}

		NovaModule module = null;

		for (String key : ModuleManager.getModules().keySet()) {
			NovaModule m = ModuleManager.getModule(key);
			if (m.getName().equalsIgnoreCase(args[0])) {
				module = m;
				break;
			}
		}

		if (module == null) {
			sender.sendMessage(ChatColor.RED + "Could not find a module with that name");
			return false;
		}

		if (ModuleManager.isEssential(module)) {
			sender.sendMessage(ChatColor.RED + "This module cant be enabled using this command");
			return false;
		}

		if (module.isEnabled()) {
			sender.sendMessage(ChatColor.RED + "That module is already enabled");
			return false;
		}

		if (module.enable()) {
			sender.sendMessage(ChatColor.GREEN + "Module " + module.getName() + " was enabled successfully");
			return true;
		} else {
			ModuleEnableFailureReason reason = module.getEnableFailureReason();

			if (reason == ModuleEnableFailureReason.MISSING_PLUGIN_DEPENDENCY) {
				sender.sendMessage(ChatColor.RED + "This module cant be enabled sinse it depends on the plugin " + module.getMissingPluginName() + " that is not installed. Please install the plugin before trying to enable this module");
			} else {
				sender.sendMessage(ChatColor.RED + "An error occured while trying to enable module " + module.getName() + ".\nMore info might be avaliable in the console." + ChatColor.RED + reason.name());
			}
			return false;
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> modules = new ArrayList<String>();

		if (args.length == 1) {
			ModuleManager.getModules().keySet().forEach(key -> {
				if (ModuleManager.isDisabled(key)) {
					if (!ModuleManager.isEssential(ModuleManager.getModules().get(key))) {
						modules.add(ModuleManager.getModule(key).getName());
					}
				}
			});
		}
		
		modules.sort(new ModuleNameComparator());

		return modules;
	}
}

class NovaCoreSubCommandModulesList extends NovaSubCommand {
	public NovaCoreSubCommandModulesList() {
		super("list");

		this.setPermission("novacore.command.novacore.modules.list");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		this.setDescription("list modules");

		this.setFilterAutocomplete(true);
		this.setEmptyTabMode(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		String message = ChatColor.AQUA + "" + ModuleManager.getModules().size() + ChatColor.GOLD + " modules loaded\n";

		int enabled = 0;
		int disabled = 0;

		String moduleList = "";

		List<String> keys = new ArrayList<>(ModuleManager.getModules().keySet());

		keys.sort(new ModuleNameComparator());

		for (String key : keys) {
			NovaModule module = ModuleManager.getModule(key);

			if (module.isEnabled()) {
				enabled++;
			} else {
				disabled++;
			}

			String style = "";

			if (ModuleManager.isEssential(module)) {
				style += ChatColor.BOLD;
			}

			moduleList += ChatColor.AQUA + module.getName() + ChatColor.GOLD + " : " + (module.isEnabled() ? ChatColor.GREEN + style + "Enabled" : ChatColor.RED + style + "Disabled") + ChatColor.RESET + "\n";
		}

		message += ChatColor.AQUA + "" + enabled + ChatColor.GOLD + " Enabled, " + ChatColor.AQUA + disabled + ChatColor.GOLD + " Disabled\n";

		message += ChatColor.GOLD + "Module list:\n\n" + moduleList;

		sender.sendMessage(message);

		return false;
	}
}

class NovaCoreSubCommandModulesReload extends NovaSubCommand {
	public NovaCoreSubCommandModulesReload() {
		super("reload");
		setPermission("novacore.command.novacore.modules.reload");
		this.setPermissionDefaultValue(PermissionDefault.OP);

		setDescription("Reloads a module");

		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please specify a module name");
			return false;
		}

		NovaModule module = null;

		for (String key : ModuleManager.getModules().keySet()) {
			NovaModule m = ModuleManager.getModule(key);
			if (m.getName().equalsIgnoreCase(args[0])) {
				module = m;
				break;
			}
		}

		if (module == null) {
			sender.sendMessage(ChatColor.RED + "Could not find a module with that name");
			return false;
		}

		if (ModuleManager.isEssential(module)) {
			sender.sendMessage(ChatColor.RED + "This module cant be disabled using this command");
			return false;
		}

		if (!module.isEnabled()) {
			sender.sendMessage(ChatColor.RED + "That module is disabled");
			return false;
		}

		if (module.reload()) {
			sender.sendMessage(ChatColor.GREEN + "Module " + module.getName() + " reloaded");
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "Failed to reload module. check console for details");
			return false;
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> modules = new ArrayList<String>();

		if (args.length == 1) {
			ModuleManager.getModules().keySet().forEach(key -> {
				if (ModuleManager.isEnabled(key)) {
					if (!ModuleManager.isEssential(ModuleManager.getModules().get(key))) {
						modules.add(ModuleManager.getModule(key).getName());
					}
				}
			});
		}

		modules.sort(new ModuleNameComparator());

		return modules;
	}
}