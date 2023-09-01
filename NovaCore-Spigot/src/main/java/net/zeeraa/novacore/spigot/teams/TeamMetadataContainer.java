package net.zeeraa.novacore.spigot.teams;

public abstract class TeamMetadataContainer {
	protected final Team team;
	
	public TeamMetadataContainer(Team team) {
		this.team = team;
	}
	
	public final Team getTeam() {
		return team;
	}
}