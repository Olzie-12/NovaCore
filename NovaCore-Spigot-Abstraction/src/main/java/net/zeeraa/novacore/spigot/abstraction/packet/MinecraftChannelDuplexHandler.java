package net.zeeraa.novacore.spigot.abstraction.packet;

import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

public abstract class MinecraftChannelDuplexHandler extends ChannelDuplexHandler {
	private final Player player;
	private static boolean debug = false;

	public static void setDebug(boolean debug) {
		MinecraftChannelDuplexHandler.debug = debug;
	}

	public static boolean isDebug() {
		return debug;
	}

	public MinecraftChannelDuplexHandler(Player player) {
		this.player = player;
	}

	@Override
	public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
		if (!readPacket(player, packet)) {
			return;
		}
		if (debug) {
			String message = ChatColor.AQUA + "[NovaCore Packet Reader]" + ChatColor.RESET + ChatColor.GREEN + "Packet read name: " + ChatColor.YELLOW + packet.getClass().getSimpleName() + ChatColor.RESET + ChatColor.GREEN + " | Packet read class path: " + ChatColor.YELLOW + packet.getClass().getName() + ChatColor.RESET + ChatColor.GREEN + " | Packet Fields:\n";
			for (Field field : packet.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				message += ChatColor.GREEN + "FIELD NAME: " + field.getName() + " | FIELD TYPE: " + field.getType().getSimpleName() + " | FIELD VALUE: " + field.get(packet) + "\n";
			}
			message += ChatColor.GREEN + "------------------------------------------";
			Bukkit.getLogger().log(Level.FINE, message);
		}
		super.channelRead(channelHandlerContext, packet);
	}

	@Override
	public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
		if (!writePacket(player, packet))
			return;
		if (debug) {
			String message = ChatColor.AQUA + "[NovaCore Packet Writer]" + ChatColor.RESET + ChatColor.RED + "Packet written name: " + ChatColor.YELLOW + packet.getClass().getSimpleName() + ChatColor.RESET + ChatColor.RED + " | Packet written class path: " + ChatColor.YELLOW + packet.getClass().getName() + ChatColor.RESET + ChatColor.RED + " | Packet Fields:\n";
			for (Field field : packet.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				message += ChatColor.RED + "FIELD NAME: " + field.getName() + " | FIELD TYPE: " + field.getType().getSimpleName() + " | FIELD VALUE: " + field.get(packet) + "\n";
			}
			message += ChatColor.RED + "------------------------------------------";
			Bukkit.getLogger().log(Level.FINE, message);
		}
		super.write(channelHandlerContext, packet, channelPromise);
	}

	public abstract boolean readPacket(Player player, Object packet) throws NoSuchFieldException, IllegalAccessException;

	public abstract boolean writePacket(Player player, Object packet) throws NoSuchFieldException, IllegalAccessException;

	protected boolean canBreak(Player player, Block block) {

		if (block == null) {
			return false;
		}

		if (block.getType() == Material.AIR) {

			return false;
		}
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {

			return false;
		}

		List<Player> playersDigging = VersionIndependentUtils.get().getPacketManager().getPlayersDigging();

		if (playersDigging.stream().noneMatch(pl -> pl.getUniqueId().equals(player.getUniqueId()))) {
			return false;
		}

		if (player.getGameMode() == GameMode.SURVIVAL) {
			return true;
		}

		return VersionIndependentUtils.get().canBreakBlock(player.getItemInHand(), block.getType());
	}
}