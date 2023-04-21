package net.zeeraa.novacore.spigot.version.v1_16_R3.packet;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle;
import net.zeeraa.novacore.spigot.abstraction.packet.PacketManager;
import net.zeeraa.novacore.spigot.abstraction.packet.event.PacketEvent;
import net.zeeraa.novacore.spigot.abstraction.packet.event.PlayerAbortBlockDigEvent;
import net.zeeraa.novacore.spigot.abstraction.packet.event.PlayerDigBlockEvent;
import net.zeeraa.novacore.spigot.abstraction.packet.event.PlayerInputEvent;
import net.zeeraa.novacore.spigot.abstraction.packet.event.PlayerStartBlockDigEvent;
import net.zeeraa.novacore.spigot.abstraction.packet.event.PlayerStopBlockDigEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.List;

public class MinecraftChannelDuplexHandler extends net.zeeraa.novacore.spigot.abstraction.packet.MinecraftChannelDuplexHandler {

	public MinecraftChannelDuplexHandler(Player player) {
		super(player);
	}

	public boolean readPacket(Player player, Object packet) {
		List<PacketEvent> events = new ArrayList<>();
		if (packet.getClass().equals(PacketPlayInSteerVehicle.class)) {
			PacketPlayInSteerVehicle steer = (PacketPlayInSteerVehicle) packet;
			float sideways = steer.b();
			float forwards = steer.c();
			boolean jump = steer.d();
			boolean leave = steer.e();
			if (sideways != 0 || forwards != 0 || jump || leave) {
				PlayerInputEvent.Press side;
				PlayerInputEvent.Press front;
				if (sideways > 0) {
					side = PlayerInputEvent.Press.LEFT;
				} else if (sideways < 0) {
					side = PlayerInputEvent.Press.RIGHT;
				} else {
					side = PlayerInputEvent.Press.NONE;
				}
				if (forwards > 0) {
					front = PlayerInputEvent.Press.FRONT;
				} else if (forwards < 0) {
					front = PlayerInputEvent.Press.BACK;
				} else {
					front = PlayerInputEvent.Press.NONE;
				}
				events.add(new PlayerInputEvent(player, side, front, jump, leave));
			}
		} else if (packet.getClass().equals(PacketPlayInBlockDig.class)) {
			PacketPlayInBlockDig dig = (PacketPlayInBlockDig) packet;
			BlockPosition bp = dig.b();
			Block block = player.getWorld().getBlockAt(bp.getX(), bp.getY(), bp.getZ());
			BlockFace face;
			switch (dig.c()) {
				case UP:
					face = BlockFace.UP;
					break;
				case DOWN:
					face = BlockFace.DOWN;
					break;
				case EAST:
					face = BlockFace.EAST;
					break;
				case WEST:
					face = BlockFace.WEST;
					break;
				case NORTH:
					face = BlockFace.NORTH;
					break;
				case SOUTH:
					face = BlockFace.SOUTH;
					break;
				default:
					face = null;
			}
			switch (dig.d()) {
				case STOP_DESTROY_BLOCK:
					events.add(new PlayerStopBlockDigEvent(player, block, face));
					break;
				case ABORT_DESTROY_BLOCK:
					events.add(new PlayerAbortBlockDigEvent(player, block, face));
					break;
				case START_DESTROY_BLOCK:
					events.add(new PlayerStartBlockDigEvent(player, block, face));
					break;

			}
		}

		for (PacketEvent e : events) {
			PacketManager.fireEvent(e);
			if (e instanceof Cancellable) {
				if (((Cancellable) e).isCancelled()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean writePacket(Player player, Object packet) {
		List<PacketEvent> events = new ArrayList<>();

		for (PacketEvent e : events) {
			PacketManager.fireEvent(e);
			if (e instanceof Cancellable) {
				if (((Cancellable) e).isCancelled()) {
					return false;
				}
			}
		}
		return true;
	}
}