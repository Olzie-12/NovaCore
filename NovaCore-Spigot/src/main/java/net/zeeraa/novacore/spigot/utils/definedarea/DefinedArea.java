package net.zeeraa.novacore.spigot.utils.definedarea;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public abstract class DefinedArea {
	protected List<DefinedArea> includedAreas;
	protected List<DefinedArea> excludedAreas;
	protected List<DefinedAreaFlag> flags;

	public DefinedArea() {
		this.includedAreas = new ArrayList<>();
		this.excludedAreas = new ArrayList<>();
		this.flags = new ArrayList<>();
	}

	public List<DefinedArea> getIncludedAreas() {
		return includedAreas;
	}

	public List<DefinedArea> getExcludedAreas() {
		return excludedAreas;
	}

	public List<DefinedAreaFlag> getFlags() {
		return flags;
	}

	public boolean addFlag(DefinedAreaFlag flag) {
		if (!flags.contains(flag)) {
			flags.add(flag);
			return true;
		}
		return false;
	}

	public boolean removeFlag(DefinedAreaFlag flag) {
		return flags.remove(flag);
	}

	public void addIncludedSubArea(DefinedArea area) {
		includedAreas.add(area);
	}

	public void addExcludedSubArea(DefinedArea area) {
		excludedAreas.add(area);
	}

	public abstract boolean isInside(Location location, boolean ignoreWorld, boolean useBlockPosition);

	public boolean isInside(Location location) {
		return this.isInside(location, false);
	}

	public boolean isInside(Location location, boolean ignoreWorld) {
		return this.isInside(location, ignoreWorld, false);
	}

	public boolean isInside(Block block) {
		return this.isInside(block, false);
	}

	public boolean isInside(Block block, boolean ignoreWorld) {
		return this.isInside(block.getLocation(), ignoreWorld, true);
	}

	public boolean isInside(Entity entity) {
		return this.isInside(entity, false);
	}

	public boolean isInside(Entity entity, boolean ignoreWorld) {
		return this.isInside(entity.getLocation(), ignoreWorld, false);
	}

	public boolean isInside(Entity entity, boolean ignoreWorld, boolean useBlockPosition) {
		return this.isInside(entity.getLocation(), ignoreWorld, useBlockPosition);
	}
}