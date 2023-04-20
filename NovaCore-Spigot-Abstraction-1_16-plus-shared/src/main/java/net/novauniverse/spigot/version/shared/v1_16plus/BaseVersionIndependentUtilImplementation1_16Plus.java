package net.novauniverse.spigot.version.shared.v1_16plus;

import java.awt.Color;
import java.util.Collections;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.zeeraa.novacore.commons.utils.LoopableIterator;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.ColoredBlockType;

public abstract class BaseVersionIndependentUtilImplementation1_16Plus extends VersionIndependentUtils {
	private DyeColorToMaterialMapper dyeColorToMaterialMapper;
	
	public BaseVersionIndependentUtilImplementation1_16Plus(DyeColorToMaterialMapper colorToMaterialMapper) {
		this.dyeColorToMaterialMapper = colorToMaterialMapper;
	}
	
	public DyeColorToMaterialMapper getDyeColorToMaterialMapper() {
		return dyeColorToMaterialMapper;
	}
	
	public abstract Material getColoredMaterial(DyeColor color, ColoredBlockType type);
	
	@Override
	public ItemStack getPlayerSkullWithBase64Texture(String b64stringtexture) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		PropertyMap propertyMap = profile.getProperties();
		if (propertyMap == null) {
			throw new IllegalStateException("Profile doesn't contain a property map");
		}
		propertyMap.put("textures", new Property("textures", b64stringtexture));
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
		ItemMeta headMeta = head.getItemMeta();
		Class<?> headMetaClass = headMeta.getClass();
		try {
			getField(headMetaClass, "profile", GameProfile.class, 0).set(headMeta, profile);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		head.setItemMeta(headMeta);
		return head;
	}
	
	@Override
	public void setBlockAsPlayerSkull(Block block) {
		block.setType(Material.PLAYER_HEAD);
		block.getState().update(true);
	}

	@Override
	public ItemStack getItemInMainHand(Player player) {
		return player.getInventory().getItemInMainHand();
	}

	@Override
	public ItemStack getItemInOffHand(Player player) {
		return player.getInventory().getItemInOffHand();
	}

	@Override
	public double getEntityMaxHealth(LivingEntity livingEntity) {
		return livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
	}

	@Override
	public void setEntityMaxHealth(LivingEntity livingEntity, double health) {
		livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
	}

	@Override
	public void resetEntityMaxHealth(LivingEntity livingEntity) {
		livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
				.setBaseValue(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
	}

	@Override
	public void cloneBlockData(Block source, Block target) {
		target.setBlockData(source.getBlockData());
	}

	@Override
	public void setItemInMainHand(Player player, ItemStack item) {
		player.getInventory().setItemInMainHand(item);
	}

	@Override
	public void setItemInOffHand(Player player, ItemStack item) {
		player.getInventory().setItemInOffHand(item);
	}
	
	@Override
	public void setColoredBlock(Block block, DyeColor color, ColoredBlockType type) {
		Material material = getColoredMaterial(color, type);

		block.setType(material);
	}

	@Override
	public ItemStack getColoredItem(DyeColor color, ColoredBlockType type) {
		return new ItemStack(getColoredMaterial(color, type));
	}

	@Override
	public void setShapedRecipeIngredientAsColoredBlock(ShapedRecipe recipe, char ingredient, ColoredBlockType type, DyeColor color) {
		recipe.setIngredient(ingredient, getColoredMaterial(color, type));
	}

	@Override
	public void addShapelessRecipeIngredientAsColoredBlock(ShapelessRecipe recipe, char ingredient, ColoredBlockType type, DyeColor color) {
		recipe.addIngredient(getColoredMaterial(color, type));
	}
	
	@Override
	public void attachMapView(ItemStack item, MapView mapView) {
		MapMeta meta = (MapMeta) item.getItemMeta();

		meta.setMapView(mapView);

		item.setItemMeta(meta);
	}

	@Override
	public MapView getAttachedMapView(ItemStack item) {
		MapMeta meta = (MapMeta) item.getItemMeta();

		return meta.getMapView();
	}

	@Override
	public int getMapViewId(MapView mapView) {
		return mapView.getId();
	}
	
	@Override
	public void setShapedRecipeIngredientAsPlayerSkull(ShapedRecipe recipe, char ingredient) {
		recipe.setIngredient(ingredient, Material.PLAYER_HEAD);
	}
	
	@Override
	public ItemStack getPlayerSkullitem() {
		return new ItemStack(Material.PLAYER_HEAD, 1);
	}
	
	@Override
	public void sendActionBarMessage(Player player, String message) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}
	
	@Override
	public ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable) {
		meta.setUnbreakable(unbreakable);
		return meta;
	}

	@Override
	public void setCreatureItemInMainHand(Creature creature, ItemStack item) {
		creature.getEquipment().setItemInMainHand(item);
	}
	
	@Override
	public void setCustomModelData(ItemMeta meta, int data) {
		meta.setCustomModelData(data);
	}
	
	@Override
	public boolean isInteractEventMainHand(PlayerInteractEvent e) {
		return e.getHand() == EquipmentSlot.HAND;
	}

	@Override
	public Entity getEntityByUUID(UUID uuid) {
		return Bukkit.getEntity(uuid);
	}
	
	@Override
	public void setShapedRecipeIngredientAsDye(ShapedRecipe recipe, char ingredient, DyeColor color) {
		recipe.setIngredient(ingredient, dyeColorToMaterialMapper.dyeColorToMaterial(color));
	}

	@Override
	public void addShapelessRecipeIngredientAsDye(ShapelessRecipe recipe, int count, DyeColor color) {
		recipe.addIngredient(count, dyeColorToMaterialMapper.dyeColorToMaterial(color));
	}
	
	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		if (title.length() == 0) {
			title = " ";
		}
		player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}
	
	@Override
	public void setAI(LivingEntity entity, boolean ai) {
		entity.setAI(ai);
	}

	@Override
	public void setSilent(LivingEntity entity, boolean silent) {
		entity.setSilent(silent);
	}
	
	@Override
	public String colorize(Color color, String message) {
		return ChatColor.of(color).toString() + message;
	}
	
	@Override
	public String colorizeGradient(Color[] colors, String message) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			builder.append(ChatColor.of(new Color(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue())))
					.append(message.toCharArray()[i]);
		}
		return builder.toString();
	}

	@Override
	public String colorizeRainbow(Color[] colors, int charsPerColor, String message) {
		LoopableIterator<Color> iterator = new LoopableIterator<>();
		Collections.addAll(iterator, colors);
		StringBuilder finalBuild = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {

			if (i % charsPerColor == 0) {
				finalBuild.append(colorize(iterator.next(), message.toCharArray()[i] + ""));
			} else {
				finalBuild.append(message.toCharArray()[i]);
			}

		}
		return finalBuild.toString();
	}
	
	@Override
	public void setPotionEffect(ItemStack item, ItemMeta meta, PotionEffect effect, boolean color) {
		if (meta instanceof PotionMeta) {
			PotionMeta potMeta = (PotionMeta) meta;
			potMeta.addCustomEffect(effect, true);
			if (color) {
				potMeta.setColor(effect.getType().getColor());
			}
		}
	}
	
	@Override
	public void setPotionColor(ItemMeta meta, org.bukkit.Color color) {
		if (meta instanceof PotionMeta) {
			PotionMeta potMeta = (PotionMeta) meta;
			potMeta.setColor(color);
		}
	}
	
	@Override
	public Block getBlockFromProjectileHitEvent(ProjectileHitEvent e) {
		return e.getHitBlock();
	}
	
	@Override
	public ShapedRecipe createShapedRecipeSafe(ItemStack result, Plugin owner, String key) {
		return new ShapedRecipe(new NamespacedKey(owner, key.toLowerCase()), result);
	}

	@Override
	public ShapelessRecipe createShapelessRecipe(ItemStack result, Plugin owner, String key) {
		return new ShapelessRecipe(new NamespacedKey(owner, key.toLowerCase()), result);
	}

	@Override
	public Color bungeecordChatColorToJavaColor(ChatColor color) {
		return color.getColor();
	}
	
	@Override
	public void setMarker(ArmorStand stand, boolean marker) {
		stand.setMarker(marker);
	}

	@Override
	public boolean isMarker(ArmorStand stand) {
		return stand.isMarker();
	}
	
	@Override
	public ItemStack getColoredBannerItemStack(DyeColor color) {
		return SharedBannerItemStackCreator.getColoredBannerItemStack(color);
	}
	
	@Override
	public void registerCustomEntity(Class<?> entity, String name) {
		// there is no need to register custom entities on 1.14+
	}
	
	@Override
	public void registerCustomEntityWithEntityId(Class<?> entity, String name, int id) {
		// there is no need to register custom entities on 1.14+
	}
	
	@Override
	public boolean isArrowInBlock(Arrow arrow) {
		return arrow.isInBlock();
	}
}