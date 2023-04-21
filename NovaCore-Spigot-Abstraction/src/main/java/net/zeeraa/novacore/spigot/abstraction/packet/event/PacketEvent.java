package net.zeeraa.novacore.spigot.abstraction.packet.event;

import net.zeeraa.novacore.spigot.abstraction.packet.listener.PacketEventBus;

public abstract class PacketEvent {
    public abstract PacketEventBus getPacketEventBus();

}
