package net.zeeraa.novacore.spigot.version.v1_12_R1;

import net.zeeraa.novacore.spigot.abstraction.MaterialNameList;
import org.bukkit.Material;

import java.util.HashMap;

public class MaterialNameList1_12 implements MaterialNameList {
	private HashMap<String, Material> materialMap;

	public static MaterialNameList1_12 get() {
		return new MaterialNameList1_12();
	}

	@Override
	public HashMap<String, Material> getMaterialMap() {
		return materialMap;
	}

	public MaterialNameList1_12() {
		materialMap = new HashMap<>();
		materialMap.put("planks", Material.WOOD);
		materialMap.put("flowing_water", Material.WATER);
		materialMap.put("water", Material.STATIONARY_WATER);
		materialMap.put("flowing_lava", Material.LAVA);
		materialMap.put("lava", Material.STATIONARY_LAVA);
		materialMap.put("noteblock", Material.NOTE_BLOCK);
		materialMap.put("golden_rail", Material.POWERED_RAIL);
		materialMap.put("sticky_piston", Material.PISTON_STICKY_BASE);
		materialMap.put("tallgrass", Material.LONG_GRASS);
		materialMap.put("piston", Material.PISTON_BASE);
		materialMap.put("piston_head", Material.PISTON_EXTENSION);
		materialMap.put("red_flower", Material.RED_ROSE);
		materialMap.put("double_stone_slab", Material.DOUBLE_STEP);
		materialMap.put("stone_slab", Material.STEP);
		materialMap.put("brick_block", Material.BRICK);
		materialMap.put("oak_stairs", Material.WOOD_STAIRS);
		materialMap.put("crafting_table", Material.WORKBENCH);
		materialMap.put("wheat", Material.CROPS);
		materialMap.put("farmland", Material.SOIL);
		materialMap.put("lit_furnace", Material.BURNING_FURNACE);
		materialMap.put("standing_sign", Material.SIGN_POST);
		materialMap.put("rail", Material.RAILS);
		materialMap.put("stone_stairs", Material.COBBLESTONE_STAIRS);
		materialMap.put("stone_pressure_plate", Material.STONE_PLATE);
		materialMap.put("iron_door", Material.IRON_DOOR_BLOCK);
		materialMap.put("wooden_pressure_plate", Material.WOOD_PLATE);
		materialMap.put("lit_restone_ore", Material.GLOWING_REDSTONE_ORE);
		materialMap.put("unlit_redstone_torch", Material.REDSTONE_TORCH_OFF);
		materialMap.put("redstone_torch", Material.REDSTONE_TORCH_ON);
		materialMap.put("snow_layer", Material.SNOW);
		materialMap.put("snow", Material.SNOW_BLOCK);
		materialMap.put("reeds", Material.SUGAR_CANE_BLOCK);
		materialMap.put("lit_pumpkin", Material.JACK_O_LANTERN);
		materialMap.put("cake", Material.CAKE_BLOCK);
		materialMap.put("unpowered_repeater", Material.DIODE_BLOCK_OFF);
		materialMap.put("powered_repeater", Material.DIODE_BLOCK_ON);
		materialMap.put("trapdoor", Material.TRAP_DOOR);
		materialMap.put("monster_egg", Material.MONSTER_EGGS);
		materialMap.put("stonebrick", Material.SMOOTH_BRICK);
		materialMap.put("brown_mushroom_block", Material.HUGE_MUSHROOM_1);
		materialMap.put("red_mushroom_block", Material.HUGE_MUSHROOM_2);
		materialMap.put("iron_bars", Material.IRON_FENCE);
		materialMap.put("glass_pane", Material.THIN_GLASS);
		materialMap.put("stone_brick_stairs", Material.SMOOTH_STAIRS);
		materialMap.put("mycelium", Material.MYCEL);
		materialMap.put("waterlily", Material.WATER_LILY);
		materialMap.put("nether_brick_fence", Material.NETHER_FENCE);
		materialMap.put("nether_wart", Material.NETHER_WARTS);
		materialMap.put("enchanting_table", Material.ENCHANTMENT_TABLE);
		materialMap.put("end_portal", Material.ENDER_PORTAL);
		materialMap.put("end_portal_frame", Material.ENDER_PORTAL_FRAME);
		materialMap.put("end_stone", Material.ENDER_STONE);
		materialMap.put("redstone_lamp", Material.REDSTONE_LAMP_OFF);
		materialMap.put("lit_redstone_lamp", Material.REDSTONE_LAMP_ON);
		materialMap.put("double_wooden_slab", Material.WOOD_DOUBLE_STEP);
		materialMap.put("wooden_slab", Material.WOOD_STEP);
		materialMap.put("spruce_stairs", Material.SPRUCE_WOOD_STAIRS);
		materialMap.put("birch_stairs", Material.BIRCH_WOOD_STAIRS);
		materialMap.put("jungle_stairs", Material.JUNGLE_WOOD_STAIRS);
		materialMap.put("command_block", Material.COMMAND);
		materialMap.put("cobblestone_wall", Material.COBBLE_WALL);
		materialMap.put("carrots", Material.CARROT);
		materialMap.put("potatoes", Material.POTATO);
		materialMap.put("wooden_button", Material.WOOD_BUTTON);
		materialMap.put("light_weighted_pressure_plate", Material.GOLD_PLATE);
		materialMap.put("heavy_weighted_pressure_plate", Material.IRON_PLATE);
		materialMap.put("unpowered_comparator", Material.REDSTONE_COMPARATOR_OFF);
		materialMap.put("powered_comparator", Material.REDSTONE_COMPARATOR_ON);
		materialMap.put("stained_hardened_clay", Material.STAINED_CLAY);
		materialMap.put("leaves2", Material.LEAVES_2);
		materialMap.put("log2", Material.LOG_2);
		materialMap.put("slime", Material.SLIME_BLOCK);
		materialMap.put("hardened_clay", Material.HARD_CLAY);
		materialMap.put("beetroots", Material.BEETROOT_BLOCK);
		materialMap.put("repeating_command_block", Material.COMMAND_REPEATING);
		materialMap.put("chain_command_block", Material.COMMAND_CHAIN);
	}
}