package net.zeeraa.novacore.spigot.abstraction.packet.event;

import net.zeeraa.novacore.spigot.abstraction.packet.listener.PacketEventBus;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * This event is called whenever a Player mounted on an entity presses an input (DOES NOT CANCEL JUMPS)
 *
 * @author Bruno
 */
public class PlayerInputEvent extends PacketEvent implements Cancellable {

    private static final PacketEventBus bus = new PacketEventBus();
    private boolean cancelled = false;

    private Player player;
    private Press sideways;
    private Press forwards;
    private boolean jump;
    private boolean leave;

    public PlayerInputEvent(Player player, Press sideways, Press forwards, boolean jump, boolean leave) {
        this.player = player;
        this.sideways = sideways;
        this.forwards = forwards;
        this.jump = jump;
        this.leave = leave;
    }

    public enum Press {
        FRONT, LEFT, RIGHT, BACK, NONE;
    }

    public static PacketEventBus getEventBus() {
        return bus;
    }
    @Override
    public PacketEventBus getPacketEventBus() {
        return bus;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isGoingForwards() {
        return forwards == Press.FRONT;
    }

    public boolean isGoingBackwards() {
        return forwards == Press.BACK;
    }

    public boolean isGoingLeft() {
        return sideways == Press.LEFT;
    }

    public boolean isGoingRight() {
        return sideways == Press.RIGHT;
    }

    public Press getSidewaysDirection() {
        return sideways;
    }

    public Press getForwardsDirection() {
        return forwards;
    }

    public boolean isJumping() {
        return jump;
    }

    public boolean isLeaving() {
        return leave;
    }
}
