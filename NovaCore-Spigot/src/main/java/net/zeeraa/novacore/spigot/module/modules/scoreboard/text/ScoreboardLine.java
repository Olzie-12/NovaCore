package net.zeeraa.novacore.spigot.module.modules.scoreboard.text;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;

public abstract class ScoreboardLine {
	public abstract SidebarComponent.Builder apply(SidebarComponent.Builder builder);
	
	public abstract Component getComponent();
	
	public void tick() {
	}
}
