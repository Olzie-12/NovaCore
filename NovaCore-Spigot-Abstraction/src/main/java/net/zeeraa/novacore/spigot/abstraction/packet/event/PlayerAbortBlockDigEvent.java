package net.zeeraa.novacore.spigot.abstraction.packet.event;

import net.zeeraa.novacore.spigot.abstraction.packet.listener.PacketEventBus;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
/**
 * This event is called when a player stops digging a block
 *
 * @author Bruno
 */
public class PlayerAbortBlockDigEvent extends PlayerDigBlockEvent {

    private static final PacketEventBus bus = new PacketEventBus();

    public static PacketEventBus getEventBus() {
        return bus;
    }
    @Override
    public PacketEventBus getPacketEventBus() {
        return bus;
    }

    public PlayerAbortBlockDigEvent(Player player, Block block, BlockFace blockFace) {
        super(player, block, blockFace);
    }

}
