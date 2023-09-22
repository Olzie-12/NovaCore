package net.zeeraa.novacore.spigot.module.modules.scoreboard.text;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent.Builder;

public class ModifyableText extends ScoreboardEntry {
	private String text;

	public ModifyableText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public Builder apply(Builder builder) {
		builder.addDynamicLine(() -> {
			return Component.text(text);
		});
		return builder;
	}
}