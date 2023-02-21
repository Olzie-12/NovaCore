package net.zeeraa.novacore.spigot.teams;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.zeeraa.novacore.spigot.NovaCore;

/**
 * A utility to provide a easy to use team system from your minigames
 * 
 * @author Zeeraa
 */
public abstract class TeamManager {
	/**
	 * The list containing all teams. To add a team simple add the {@link Team}
	 * object to this list
	 */
	protected List<Team> teams;

	public TeamManager() {
		this.teams = new ArrayList<Team>();
	}

	/**
	 * Get a list of all {@link Team}s
	 * 
	 * @return List of teams
	 */
	public List<Team> getTeams() {
		return teams;
	}

	/**
	 * Get a {@link Stream} with all the teams.If data is modified in the stream the
	 * team list will be affected
	 * 
	 * @return {@link Stream} with teams
	 */
	public Stream<Team> stream() {
		return teams.stream();
	}

	/**
	 * Get the team of a {@link OfflinePlayer}. This return null if the player is
	 * not in a team
	 * 
	 * @param player The player to get the team from
	 * @return {@link Team} or <code>null</code>
	 */
	@Nullable
	public Team getPlayerTeam(OfflinePlayer player) {
		return this.getPlayerTeam(player.getUniqueId());
	}

	/**
	 * Get the team of a player by their {@link UUID}. This return null if the
	 * player is not in a team
	 * 
	 * @param uuid The {@link UUID} of the player to get the team from
	 * @return {@link Team} or <code>null</code>
	 */
	@Nullable
	public Team getPlayerTeam(UUID uuid) {
		return teams.stream().filter(team -> team.isMember(uuid)).findFirst().orElse(null);
	}

	/**
	 * Run a consumer if an {@link OfflinePlayer} has a team
	 * 
	 * @param player   The {@link OfflinePlayer} to check
	 * @param consumer The {@link Consumer} with type of {@link Team} thats called
	 *                 if the player is in a team
	 */
	public void ifHasTeam(OfflinePlayer player, Consumer<Team> consumer) {
		this.ifHasTeam(player.getUniqueId(), consumer);
	}

	/**
	 * Run a consumer if a player has a team
	 * 
	 * @param uuid     The {@link UUID} of the player to check
	 * @param consumer The {@link Consumer} with type of {@link Team} thats called
	 *                 if the player is in a team
	 */
	public void ifHasTeam(UUID uuid, Consumer<Team> consumer) {
		teams.stream().filter(team -> team.isMember(uuid)).findFirst().ifPresent(consumer);
	}

	/**
	 * Check if an {@link OfflinePlayer} has a team
	 * 
	 * @param player The {@link OfflinePlayer} to check
	 * @return <code>true</code> if the player is in a team
	 */
	public boolean hasTeam(OfflinePlayer player) {
		return getPlayerTeam(player.getUniqueId()) != null;
	}

	/**
	 * Check if a player is in a team by their {@link UUID} has a team
	 * 
	 * @param uuid The {@link UUID} of the player to check
	 * @return <code>true</code> if the player is in a team
	 */
	public boolean hasTeam(UUID uuid) {
		return getPlayerTeam(uuid) != null;
	}

	/**
	 * Try to get the color of the team the player is in. If the player is not in a
	 * team the provided fallback color will be used
	 * 
	 * @param player   The {@link Player} to try to get the color of
	 * @param fallback The color to use if the player has no team
	 * @return the {@link ChatColor} of the team or the provided fallback color
	 */
	public ChatColor tryGetPlayerTeamColor(Player player, ChatColor fallback) {
		return this.tryGetPlayerTeamColor(player.getUniqueId(), fallback);
	}

	/**
	 * Try to get the color of the team the player is in. If the player is not in a
	 * team the provided fallback color will be used
	 * 
	 * @param uuid     The {@link UUID} of the player to try to get the color of
	 * @param fallback The color to use if the player has no team
	 * @return the {@link ChatColor} of the team or the provided fallback color
	 */
	public ChatColor tryGetPlayerTeamColor(UUID uuid, ChatColor fallback) {
		if (hasTeam(uuid)) {
			return getPlayerTeam(uuid).getTeamColor();
		}
		return fallback;
	}

	/**
	 * Get a {@link Team} by the teams {@link UUID}
	 * 
	 * @param uuid The {@link UUID} of the team to get
	 * @return {@link Team} or <code>null</code> if not found
	 */
	@Nullable
	public Team getTeamByTeamUUID(UUID uuid) {
		return teams.stream().filter(t -> t.getTeamUuid().equals(uuid)).findFirst().orElse(null);
	}

	/**
	 * Check if 2 players is in the same team
	 * 
	 * @param player1 Player 1 to check
	 * @param player2 Player 2 to check
	 * @return <code>true</code> if both is in the same team, if neither of the
	 *         players is in a team this will return <code>false</code>
	 */
	public boolean isInSameTeam(OfflinePlayer player1, OfflinePlayer player2) {
		return this.isInSameTeam(player1.getUniqueId(), player2.getUniqueId());
	}

	/**
	 * Check if 2 players is in the same team
	 * 
	 * @param uuid1 The {@link UUID} of player 1 to check
	 * @param uuid2 The {@link UUID} of player 2 to check
	 * @return <code>true</code> if both is in the same team, if neither of the
	 *         players is in a team this will return <code>false</code>
	 */
	public boolean isInSameTeam(UUID uuid1, UUID uuid2) {
		Team team1 = this.getPlayerTeam(uuid1);

		if (team1 != null) {
			return team1.isMember(uuid2);
		}

		return false;
	}

	/**
	 * Check if a team is required to join the minigame on this server
	 * 
	 * @param player The {@link Player} that is attempting to join a game
	 * @return <code>true</code> if a team is required
	 */
	public abstract boolean requireTeamToJoin(Player player);

	/**
	 * Check if {@link NovaCore} has a team manager
	 * 
	 * @return The result of {@link NovaCore#hasTeamManager()}
	 */
	public static boolean hasTeamManager() {
		return NovaCore.getInstance().hasTeamManager();
	}

	/**
	 * Get the {@link TeamManager} from {@link NovaCore}
	 * 
	 * @return The result of {@link NovaCore#getTeamManager()}
	 */
	public static TeamManager getTeamManager() {
		return NovaCore.getInstance().getTeamManager();
	}
}