package net.zeeraa.novacore.spigot.abstraction.packet.event;

import net.zeeraa.novacore.spigot.abstraction.packet.listener.PacketEventBus;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * This event is called when a player breaks a block (WARNING: It will not be called if the block breaks instantly)
 *
 * @author Bruno
 */
public class PlayerStopBlockDigEvent extends PlayerDigBlockEvent {

    private static final PacketEventBus bus = new PacketEventBus();

    public static PacketEventBus getEventBus() {
        return bus;
    }
    @Override
    public PacketEventBus getPacketEventBus() {
        return bus;
    }

    public PlayerStopBlockDigEvent(Player player, Block block, BlockFace blockFace) {
        super(player, block, blockFace);
    }

}
