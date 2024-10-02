package net.zeeraa.novacore.spigot.teams;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentSound;

/**
 * Represents a team used by games.
 * <p>
 * After a team object has been constructed you need to call
 * {@link Team#applyTeamMetadataClasses()} for the
 * {@link TeamMetadataContainer}s to be initialized
 * 
 * @author Zeeraa
 */
public abstract class Team {
	protected UUID teamUuid;
	protected List<UUID> members;

	protected List<TeamMetadataContainer> metadataContainers;

	public Team() {
		this.teamUuid = UUID.randomUUID();
		this.members = new ArrayList<>();
		this.metadataContainers = new ArrayList<>();
	}

	public List<Player> getOnlinePlayers() {
		return Bukkit.getServer().getOnlinePlayers().stream().filter(player -> isMember(player)).collect(Collectors.toList());
	}

	public void applyTeamMetadataClasses() {
		if (TeamManager.hasTeamManager()) {
			TeamManager.getTeamManager().getRegisteredMetadataContainerClasses()
					.stream()
					.filter(clazz -> metadataContainers
							.stream()
							.noneMatch(container -> container.getClass().equals(clazz)))
					.forEach(clazz -> {
						Constructor<? extends TeamMetadataContainer> constructor;
						try {
							constructor = clazz.getConstructor(Team.class);
						} catch (NoSuchMethodException e) {
							Log.error("Team", "Could not fetch constructor of class " + clazz.getName() + " that takes an argument of type " + this.getClass().getName());
							e.printStackTrace();
							return;
						} catch (SecurityException e) {
							e.printStackTrace();
							return;
						}

						try {
							TeamMetadataContainer container = constructor.newInstance(this);
							metadataContainers.add(container);
							Log.trace("Team", "Loaded team metadata container class " + clazz.getName() + " for team " + this.getDisplayName());
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							Log.error("Team", "Failed to create instance of team metadata container class " + clazz.getName() + ". " + e.getClass().getName() + " " + e.getMessage());
							e.printStackTrace();
						}
					});
		}
	}

	public boolean hasMetadataContainer(Class<? extends TeamMetadataContainer> clazz) {
		return metadataContainers.stream().anyMatch(c -> c.getClass().equals(clazz));
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public <T extends TeamMetadataContainer> T getMetadataContainer(Class<T> clazz) {
		return (T) metadataContainers.stream().filter(c -> c.getClass().equals(clazz)).findFirst().orElse(null);
	}

	@Nullable
	public <T extends TeamMetadataContainer> T getMetadataContainer(Class<T> clazz, Consumer<T> consumer) {
		T result = this.getMetadataContainer(clazz);
		if (result != null) {
			consumer.accept(result);
		}
		return result;
	}
	
	/**
	 * @return True if the entire team is offline
	 */
	public boolean isOffline() {
		return this.getOnlinePlayers().size() == 0;
	}

	@Nullable
	public Player getRandomOnlineMember() {
		List<Player> online = this.getOnlinePlayers();
		if (online.size() > 0) {
			return online.get(new Random().nextInt(online.size()));
		}
		return null;
	}

	/**
	 * Get a list with the {@link UUID} of all team members
	 * 
	 * @return List with the {@link UUID} of all team members
	 */
	public List<UUID> getMembers() {
		return members;
	}

	/**
	 * Check if a player is a member of this team
	 * 
	 * @param player The {@link OfflinePlayer} to check
	 * @return <code>true</code> if the player is a member of this team
	 */
	public boolean isMember(OfflinePlayer player) {
		return isMember(player.getUniqueId());
	}

	/**
	 * Check if a player is a member of this team
	 * 
	 * @param uuid The {@link UUID} of the player to check
	 * @return <code>true</code> if the player is a member of this team
	 */
	public boolean isMember(UUID uuid) {
		return members.contains(uuid);
	}

	/**
	 * Get the {@link UUID} of this team
	 * 
	 * @return Team {@link UUID}
	 */
	public UUID getTeamUuid() {
		return teamUuid;
	}

	/**
	 * Add a player to the team
	 * 
	 * @param player The {@link OfflinePlayer} to add
	 */
	public void addPlayer(OfflinePlayer player) {
		this.addPlayer(player.getUniqueId());
	}

	/**
	 * Add a player to the team
	 * 
	 * @param uuid The {@link UUID} of the player to add
	 */
	public void addPlayer(UUID uuid) {
		if (members.contains(uuid)) {
			return;
		}

		members.add(uuid);
	}

	public boolean hasOnlineMembersInThisServer() {
		return this.getOnlinePlayers().size() > 0;
	}

	/**
	 * Send a message to all team members on this servers
	 * 
	 * @param message The message to send
	 * @return number of players that the message was sent to
	 */
	public int sendMessage(String message) {
		int count = 0;
		for (UUID uuid : members) {
			Player player = Bukkit.getServer().getPlayer(uuid);
			if (player != null) {
				if (player.isOnline()) {
					player.sendMessage(message);
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Send a title to all the team members
	 * 
	 * @param title    The title to send
	 * @param subtitle The sub title to send
	 * @param fadeIn   The time in ticks the title should fade in
	 * @param stay     The time in ticks the title should stay
	 * @param fadeOut  The time in ticks the title should fade out
	 * @return number of players that the title was sent to
	 * @since 2.0.0
	 */
	public int sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		int count = 0;
		for (UUID uuid : members) {
			Player player = Bukkit.getServer().getPlayer(uuid);
			if (player != null) {
				if (player.isOnline()) {
					VersionIndependentUtils.get().sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
					count++;
				}
			}
		}
		return count;
	}

	public void playSound(VersionIndependentSound sound) {
		this.playSound(sound, 1F, 1F);
	}

	public void playSound(VersionIndependentSound sound, float volume) {
		this.playSound(sound, volume, 1F);
	}

	public void playSound(VersionIndependentSound sound, float volume, float pitch) {
		getOnlinePlayers().forEach(player -> sound.play(player, volume, pitch));
	}

	public void playSound(Sound sound) {
		this.playSound(sound, 1F, 1F);
	}

	public void playSound(Sound sound, float volume) {
		this.playSound(sound, volume, 1F);
	}

	public void playSound(Sound sound, float volume, float pitch) {
		getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
	}

	/**
	 * Send an action bar message to all players in the team
	 * 
	 * @param message The message to send
	 * @return number of players that the action bar message was sent to
	 * @since 2.0.0
	 */
	public int sendActionBarMessage(String message) {
		int count = 0;
		for (UUID uuid : members) {
			Player player = Bukkit.getServer().getPlayer(uuid);
			if (player != null) {
				if (player.isOnline()) {
					VersionIndependentUtils.get().sendActionBarMessage(player, message);
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Get the {@link ChatColor} of the team
	 * 
	 * @return {@link ChatColor} of the team
	 */
	public abstract ChatColor getTeamColor();

	/**
	 * Get the display name of the team
	 * 
	 * @return display name
	 */
	public abstract String getDisplayName();

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Team) {
			return this.getTeamUuid().equals(((Team) obj).getTeamUuid());
		}

		return false;
	}

	/**
	 * Get the size of the team
	 * 
	 * @return The number of players in the team
	 */
	public int size() {
		return members.size();
	}
}