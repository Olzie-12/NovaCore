package net.zeeraa.novacore.spigot.abstraction.packet.listener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PacketHandler {
    int priority() default 0;
    boolean ignoreCancelled() default true;
}
