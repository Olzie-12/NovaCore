package net.zeeraa.novacore.spigot.module.modules.scoreboard.text;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent.Builder;

public class ModifiableTextLine extends ScoreboardLine {
	private String text;
	private Component comp;

	public ModifiableTextLine(String text) {
		this.text = text;
		comp = Component.text(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		comp = Component.text(text);
	}

	@Override
	public Builder apply(Builder builder) {
		builder.addDynamicLine(() -> {
			return comp;
		});
		return builder;
	}
	
	@Override
	public Component getComponent() {
		return comp;
	}
}