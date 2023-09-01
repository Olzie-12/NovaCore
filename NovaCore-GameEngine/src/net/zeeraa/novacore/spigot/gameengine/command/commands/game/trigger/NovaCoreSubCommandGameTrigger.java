package net.zeeraa.novacore.spigot.gameengine.command.commands.game.trigger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.DelayedGameTrigger;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.GameTrigger;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.ScheduledGameTrigger;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.TriggerFlag;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.TriggerResponse;

public class NovaCoreSubCommandGameTrigger extends NovaSubCommand {
	public NovaCoreSubCommandGameTrigger() {
		super("trigger");

		this.setDescription("Command to manage triggers");
		this.setPermission("novacore.command.game.trigger");
		this.setPermissionDefaultValue(PermissionDefault.OP);
		this.setPermissionDescription("Access to the game trigger command");

		this.addSubCommand(new NovaCoreSubCommandGameTriggerTrigger());
		this.addSubCommand(new NovaCoreSubCommandGameTriggerList());
		this.addSubCommand(new NovaCoreSubCommandGameTriggerStart());
		this.addSubCommand(new NovaCoreSubCommandGameTriggerStop());

		this.addHelpSubCommand();

		this.setFilterAutocomplete(true);
		this.setEmptyTabMode(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "Use " + ChatColor.AQUA + "/game trigger help " + ChatColor.GOLD + " for help");
		return true;
	}
}

class NovaCoreSubCommandGameTriggerList extends NovaSubCommand {
	public NovaCoreSubCommandGameTriggerList() {
		super("list");

		this.setDescription("List all triggers");
		this.setPermission("novacore.command.game.trigger.list");
		this.setPermissionDefaultValue(PermissionDefault.OP);
		this.setPermissionDescription("Access to the /game trigger lidt command");

		this.addHelpSubCommand();

		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (GameManager.getInstance().hasGame()) {
			String message = "";

			message += ChatColor.GOLD + "Color codes:\n";

			message += ChatColor.RESET + "" + ChatColor.ITALIC + "Italic" + ChatColor.RESET + ChatColor.GOLD + ": can't be triggered by commands\n";
			message += ChatColor.AQUA + "X" + ChatColor.GOLD + ": not scheduled, can be triggered\n";
			message += ChatColor.GREEN + "X" + ChatColor.GOLD + ": running, can be triggered\n";
			message += ChatColor.RED + "X" + ChatColor.GOLD + ": not running, can be triggered, can be started\n";
			message += ChatColor.GRAY + "X" + ChatColor.GOLD + ": can't be triggered again\n";

			message += ChatColor.GOLD + "Triggers:\n";

			for (GameTrigger trigger : GameManager.getInstance().getActiveGame().getTriggers()) {
				String style = "";

				if (trigger.hasFlag(TriggerFlag.DENY_TRIGGER_BY_COMMAND)) {
					style += ChatColor.ITALIC;
				}

				if (trigger.hasFlag(TriggerFlag.RUN_ONLY_ONCE) && trigger.hasBeenCalled()) {
					style += ChatColor.GRAY;
				} else {
					if (trigger instanceof ScheduledGameTrigger) {
						if (((ScheduledGameTrigger) trigger).isRunning()) {
							style += ChatColor.GREEN;
						} else {
							style += ChatColor.RED;
						}
					} else {
						style += ChatColor.AQUA;
					}
				}

				message += style + trigger.getName();

				if (trigger instanceof DelayedGameTrigger) {
					float ticks = ((DelayedGameTrigger) trigger).getTicksLeft();
					message += (((DelayedGameTrigger) trigger).isRunning() ? ChatColor.AQUA : ChatColor.GRAY) + " (Ticks left: " + ticks + " seconds: " + ((int) (ticks / 20)) + ")";
				}

				message += ChatColor.GRAY + " (Count: " + trigger.getTriggerCount() + ")";

				message += "\n";
			}
			sender.sendMessage(message);
		} else {
			sender.sendMessage(ChatColor.RED + "No game has been loaded");
		}

		return true;
	}
}

class NovaCoreSubCommandGameTriggerStart extends NovaSubCommand {
	public NovaCoreSubCommandGameTriggerStart() {
		super("start");

		this.setDescription("Start a trigger");
		this.setPermission("novacore.command.game.trigger.start");
		this.setPermissionDefaultValue(PermissionDefault.OP);
		this.setPermissionDescription("Access to the /game trigger start command");

		this.addHelpSubCommand();

		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (GameManager.getInstance().hasGame()) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please provide a trigger. You can use tab to autocomplete avaliable trigger names");
			} else {
				GameTrigger trigger = GameManager.getInstance().getActiveGame().getTrigger(args[0]);

				if (trigger != null) {
					if (trigger instanceof ScheduledGameTrigger) {
						if (!((ScheduledGameTrigger) trigger).isRunning()) {
							if (((ScheduledGameTrigger) trigger).start()) {
								sender.sendMessage(ChatColor.GREEN + "Trigger started successfully");
							} else {
								sender.sendMessage(ChatColor.RED + "Trigger did not start successfully");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "This trigger is already running");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "This trigger is not a scheduled trigger");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Could not find a trigger with that name");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No game has been loaded");
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<String>();

		if (GameManager.getInstance().hasGame()) {
			for (GameTrigger trigger : GameManager.getInstance().getActiveGame().getTriggers()) {
				if (trigger instanceof ScheduledGameTrigger) {
					if (!((ScheduledGameTrigger) trigger).isRunning()) {
						result.add(trigger.getName());
					}
				}
			}
		}

		return result;
	}
}

class NovaCoreSubCommandGameTriggerStop extends NovaSubCommand {
	public NovaCoreSubCommandGameTriggerStop() {
		super("stop");

		this.setDescription("Stop a trigger");
		this.setPermission("novacore.command.game.trigger.start");
		this.setPermissionDefaultValue(PermissionDefault.OP);
		this.setPermissionDescription("Access to the /game trigger stop command");

		this.addHelpSubCommand();

		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (GameManager.getInstance().hasGame()) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please provide a trigger. You can use tab to autocomplete avaliable trigger names");
			} else {
				GameTrigger trigger = GameManager.getInstance().getActiveGame().getTrigger(args[0]);

				if (trigger != null) {
					if (trigger instanceof ScheduledGameTrigger) {
						if (((ScheduledGameTrigger) trigger).isRunning()) {
							if (((ScheduledGameTrigger) trigger).stop()) {
								sender.sendMessage(ChatColor.GREEN + "Trigger stopped successfully");
							} else {
								sender.sendMessage(ChatColor.RED + "Trigger did not stop successfully");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "This trigger is not running");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "This trigger is not a scheduled trigger");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Could not find a trigger with that name");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No game has been loaded");
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<String>();

		if (GameManager.getInstance().hasGame()) {
			for (GameTrigger trigger : GameManager.getInstance().getActiveGame().getTriggers()) {
				if (trigger instanceof ScheduledGameTrigger) {
					if (((ScheduledGameTrigger) trigger).isRunning()) {
						result.add(trigger.getName());
					}
				}
			}
		}

		return result;
	}
}

class NovaCoreSubCommandGameTriggerTrigger extends NovaSubCommand {
	public NovaCoreSubCommandGameTriggerTrigger() {
		super("trigger");

		this.setDescription("Activate a trigger");
		this.setPermission("novacore.command.game.trigger.trigger");
		this.setPermissionDefaultValue(PermissionDefault.OP);
		this.setPermissionDescription("Access to the /game trigger trigger command");

		this.addHelpSubCommand();

		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (GameManager.getInstance().hasGame()) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please provide a trigger. You can use tab to autocomplete avaliable trigger names");
			} else {
				GameTrigger trigger = GameManager.getInstance().getActiveGame().getTrigger(args[0]);

				if (trigger != null) {
					if (!trigger.hasFlag(TriggerFlag.DENY_TRIGGER_BY_COMMAND)) {
						TriggerResponse response = trigger.trigger(TriggerFlag.COMMAND_ACTIVATION);

						if (response.isSuccess()) {
							sender.sendMessage(ChatColor.GREEN + "Trigger activated succesfully");
						} else {
							sender.sendMessage(ChatColor.RED + "Trigger did not activate");
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "This trigger does not allow execution by commands");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Could not find a trigger with that name");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No game has been loaded");
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<String>();

		if (GameManager.getInstance().hasGame()) {
			for (GameTrigger trigger : GameManager.getInstance().getActiveGame().getTriggers()) {
				if (trigger.hasFlag(TriggerFlag.DENY_TRIGGER_BY_COMMAND)) {
					continue;
				}
				result.add(trigger.getName());
			}
		}

		return result;
	}
}