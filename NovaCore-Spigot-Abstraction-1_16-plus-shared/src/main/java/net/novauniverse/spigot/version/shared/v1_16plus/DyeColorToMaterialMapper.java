package net.novauniverse.spigot.version.shared.v1_16plus;

import org.bukkit.DyeColor;
import org.bukkit.Material;

public interface DyeColorToMaterialMapper {
	Material dyeColorToMaterial(DyeColor color);
}
