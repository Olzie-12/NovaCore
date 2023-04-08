package net.zeeraa.novacore.spigot.gamerule;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.abstraction.enums.NovaCoreGameVersion;
import org.bukkit.World;

import java.util.Arrays;

public enum Gamerule {
    DO_FIRE_TICK(NovaCoreGameVersion.V_1_8, "doFireTick", Boolean.class),
    MOB_GRIEFING(NovaCoreGameVersion.V_1_8, "mobGriefing", Boolean.class),
    KEEP_INVENTORY(NovaCoreGameVersion.V_1_8, "keepInventory", Boolean.class),
    DO_MOB_SPAWNING(NovaCoreGameVersion.V_1_8, "doMobSpawning", Boolean.class),
    DO_MOB_LOOT(NovaCoreGameVersion.V_1_8, "doMobLoot", Boolean.class),
    DO_TILE_DROPS(NovaCoreGameVersion.V_1_8, "doTileDrops", Boolean.class),
    COMMAND_BLOCK_OUTPUT(NovaCoreGameVersion.V_1_8, "commandBlockOutput", Boolean.class),
    NATURAL_REGENERATION(NovaCoreGameVersion.V_1_8, "naturalRegeneration", Boolean.class),
    DO_DAYLIGHT_CYCLE(NovaCoreGameVersion.V_1_8, "doDaylightCycle", Boolean.class),
    LOG_ADMIN_COMMANDS(NovaCoreGameVersion.V_1_8, "logAdminCommands", Boolean.class),
    SHOW_DEATH_MESSAGES(NovaCoreGameVersion.V_1_8, "showDeathMessages", Boolean.class),
    RANDOM_TICK_SPEED(NovaCoreGameVersion.V_1_8, "randomTickSpeed", Integer.class),
    SEND_COMMAND_FEEDBACK(NovaCoreGameVersion.V_1_8, "sendCommandFeedback", Boolean.class),
    REDUCED_DEBUG_INFO(NovaCoreGameVersion.V_1_8, "reducedDebugInfo", Boolean.class),
    DO_ENTITY_DROPS(NovaCoreGameVersion.V_1_8, "doEntityDrops", Boolean.class),
    SPECTATORS_GENERATE_CHUNKS(NovaCoreGameVersion.V_1_8, "spectatorsGenerateChunks", Boolean.class),
    SPAWN_RADIUS(NovaCoreGameVersion.V_1_8, "spawnRadius", Integer.class),
    DISABLE_ELYTRA_MOVEMENT_CHECK(NovaCoreGameVersion.V_1_8, "disableElytraMovementCheck", Boolean.class),
    DO_WEATHER_CYCLE(NovaCoreGameVersion.V_1_8, "doWeatherCycle", Boolean.class),
    MAX_ENTITY_CRAMMING(NovaCoreGameVersion.V_1_8, "maxEntityCramming", Integer.class),
    DO_LIMITED_CRAFTING(NovaCoreGameVersion.V_1_12, "doLimitedCrafting", Boolean.class),
    MAX_COMMAND_CHAIN_LENGTH(NovaCoreGameVersion.V_1_12, "maxCommandChainLength", Integer.class),
    ANNOUNCE_ADVANCEMENTS(NovaCoreGameVersion.V_1_12, "announceAdvancements", Boolean.class),
    GAME_LOOP_FUNCTION(NovaCoreGameVersion.V_1_12, "gameLoopFunction", Boolean.class, NovaCoreGameVersion.V_1_16),
    DISABLE_RAIDS(NovaCoreGameVersion.V_1_16, "disableRaids", Boolean.class),
    DO_INSOMNIA(NovaCoreGameVersion.V_1_16, "doInsomnia", Boolean.class),
    DO_IMMEDIATE_RESPAWN(NovaCoreGameVersion.V_1_16, "doImmediateRespawn", Boolean.class),
    DROWNING_DAMAGE(NovaCoreGameVersion.V_1_16, "drowningDamage", Boolean.class),
    FALL_DAMAGE(NovaCoreGameVersion.V_1_16, "fallDamage", Boolean.class),
    FIRE_DAMAGE(NovaCoreGameVersion.V_1_16, "fireDamage", Boolean.class),
    DO_PATROL_SPAWNING(NovaCoreGameVersion.V_1_16, "doPatrolSpawning", Boolean.class),
    DO_TRADER_SPAWNING(NovaCoreGameVersion.V_1_16, "doTraderSpawning", Boolean.class),
    UNIVERSAL_ANGER(NovaCoreGameVersion.V_1_16, "universalAnger", Boolean.class),
    FORGIVE_DEAD_PLAYERS(NovaCoreGameVersion.V_1_16, "forgiveDeadPlayers", Boolean.class),

    // fuck you
    @SuppressWarnings("deprecation")
    FREEZE_DAMAGE(NovaCoreGameVersion.V_1_17, "freezeDamage", Boolean.class),
    @SuppressWarnings("deprecation")
    PLAYERS_SLEEPING_PERCENTAGE(NovaCoreGameVersion.V_1_17, "playersSleepingPercentage", Integer.class),
    DO_WARDEN_SPAWNING(NovaCoreGameVersion.V_1_19_R1, "doWardenSpawning", Boolean.class),
    BLOCK_EXPLOSION_DROP_DECAY(NovaCoreGameVersion.V_1_19_R2, "blockExplosionDropDecay", Boolean.class),
    MOB_EXPLOSION_DROP_DECAY(NovaCoreGameVersion.V_1_19_R2, "mobExplosionDropDecay", Boolean.class),
    TNT_EXPLOSION_DROP_DECAY(NovaCoreGameVersion.V_1_19_R2, "tntExplosionDropDecay", Boolean.class),
    SNOW_ACCUMULATION_HEIGHT(NovaCoreGameVersion.V_1_19_R2, "snowAccumulationHeight", Integer.class),
    WATER_SOURCE_CONVERSION(NovaCoreGameVersion.V_1_19_R2, "waterSourceConversion", Boolean.class),
    LAVA_SOURCE_CONVERSION(NovaCoreGameVersion.V_1_19_R2, "lavaSourceConversion", Boolean.class),
    GLOBAL_SOUND_EVENTS(NovaCoreGameVersion.V_1_19_R2, "lavaSourceConversion", Boolean.class),
    COMMAND_MODIFICATION_BLOCK_LIMIT(NovaCoreGameVersion.V_1_19_R3, "commandModificationBlockLimit", Integer.class),
    DO_VINES_SPREAD(NovaCoreGameVersion.V_1_19_R3, "doVinesSpread", Boolean.class);

    private final NovaCoreGameVersion introducedVersion;
    private final String name;
    private final NovaCoreGameVersion until;
    private final Class<?> argumentType;

    Gamerule(NovaCoreGameVersion introduced, String name, Class<?> argumentType) {
        introducedVersion = introduced;
        this.name = name;
        this.argumentType = argumentType;
        this.until = null;

    }

    Gamerule(NovaCoreGameVersion introduced, String name, Class<?> argumentType, NovaCoreGameVersion until) {
        introducedVersion = introduced;
        this.name = name;
        this.argumentType = argumentType;
        this.until = until;
    }

    public NovaCoreGameVersion getIntroducedVersion() {
        return introducedVersion;
    }

    public String getName() {
        return name;
    }

    public NovaCoreGameVersion until() {
        return until;
    }

    public boolean isPermanent() {
        return until == null;
    }

    public void set(World world, Object value) {
        String stringValue = value.toString();
        if (isValidArgument(stringValue)) {
            if (isBoolean(stringValue)) {
                setBoolean(world, Boolean.parseBoolean(stringValue));
            } else if (isInteger(stringValue)) {
                setInteger(world, Integer.parseInt(stringValue));
            } else {
                Log.error("NovaCore Gamerules", "Couldn't set gamerule " + name + " (" + argumentType.getSimpleName() + ") with value \"" + stringValue + "\" (" + value.getClass().getSimpleName() + ")");
            }
        } else {
            Log.error("NovaCore Gamerules", "Value \"" + stringValue + "\" from class " + value.getClass().getName() + " is not valid.");
        }
    }

    public void setBoolean(World world, boolean value) {
        if (NovaCore.getInstance().getNovaCoreGameVersion().isBefore(introducedVersion) || (until != null && NovaCore.getInstance().getNovaCoreGameVersion().isAfterOrEqual(until))) {
            Log.error("NovaCore Gamerules", "Gamerule " + name + " isn't available on this version (" + NovaCore.getInstance().getNovaCoreGameVersion().name() + ")");
            return;
        }
        if (argumentType.isAssignableFrom(Boolean.class)) {
            world.setGameRuleValue(name, value + "");
        } else {
            Log.error("NovaCore Gamerules", "Gamerule " + name + " isn't a Boolean gamerule.");
        }
    }

    public void setInteger(World world, int value) {
        if (NovaCore.getInstance().getNovaCoreGameVersion().isBefore(introducedVersion) || (until != null && NovaCore.getInstance().getNovaCoreGameVersion().isAfterOrEqual(until))) {
            Log.error("NovaCore Gamerules", "Gamerule " + name + " isn't available on this version (" + NovaCore.getInstance().getNovaCoreGameVersion().name() + ")");
            return;
        }
        if (argumentType.isAssignableFrom(Integer.class)) {
            world.setGameRuleValue(name, value + "");
        } else {
            Log.error("NovaCore Gamerules", "Gamerule " + name + " isn't an Integer gamerule.");
        }
    }

    public String get(World world) {
        if (NovaCore.getInstance().getNovaCoreGameVersion().isBefore(introducedVersion) || (until != null && NovaCore.getInstance().getNovaCoreGameVersion().isAfterOrEqual(until))) {
            Log.error("NovaCore Gamerules", "Gamerule " + name + " isn't available on this version (" + NovaCore.getInstance().getNovaCoreGameVersion().name() + ")");
            return null;
        }
        return world.getGameRuleValue(name);
    }

    public boolean getBoolean(World world) {
        if (NovaCore.getInstance().getNovaCoreGameVersion().isBefore(introducedVersion) || (until != null && NovaCore.getInstance().getNovaCoreGameVersion().isAfterOrEqual(until))) {
            Log.error("NovaCore Gamerules", "Gamerule " + name + " isn't available on this version (" + NovaCore.getInstance().getNovaCoreGameVersion().name() + ")");
            throw new IllegalArgumentException();
        }
        String value = world.getGameRuleValue(name);
        if (isBoolean(value)) {
            return value.equals("true");
        } else {
            Log.error("NovaCore Gamerules", "Gamerule " + name + " does not have a boolean value.");
            throw new IllegalArgumentException("Gamerule " + name + " does not have a boolean value.");
        }
    }

    public int getInteger(World world) {
        if (NovaCore.getInstance().getNovaCoreGameVersion().isBefore(introducedVersion) || (until != null && NovaCore.getInstance().getNovaCoreGameVersion().isAfterOrEqual(until))) {
            Log.error("NovaCore Gamerules", "Gamerule " + name + " isn't available on this version (" + NovaCore.getInstance().getNovaCoreGameVersion().name() + ")");
            throw new IllegalArgumentException();
        }
        String value = world.getGameRuleValue(name);
        if (isInteger(value)) {
            return Integer.parseInt(value);
        } else {
            Log.error("NovaCore Gamerules", "Gamerule " + name + " does not have an integer value.");
            throw new IllegalArgumentException("Gamerule " + name + " does not have an integer value.");
        }
    }

    private static boolean isBoolean(String s) {
        return s.equals("true") || s.equals("false");
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidArgument(String s) {
        return isBoolean(s) || isInteger(s);
    }

    public static Gamerule getFromName(String gameruleName) {
       Gamerule gamerule = Arrays.stream(Gamerule.values()).filter(rule -> rule.name.equals(gameruleName)).findFirst().orElse(null);
       if (gamerule == null) {
           Log.warn("NovaCore Gamerules", "Gamerule has not been found, returning null");
       }
       return gamerule;
    }

}
