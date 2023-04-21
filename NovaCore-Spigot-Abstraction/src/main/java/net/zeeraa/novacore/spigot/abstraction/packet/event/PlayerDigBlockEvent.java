package net.zeeraa.novacore.spigot.abstraction.packet.event;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public abstract class PlayerDigBlockEvent extends PacketEvent {

    private Player player;
    private Block block;
    private BlockFace blockFace;
    public PlayerDigBlockEvent(Player player, Block block, BlockFace blockFace) {
        this.player = player;
        this.block = block;
        this.blockFace = blockFace;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }



}
