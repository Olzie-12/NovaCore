package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

public class ExperienceUtils {
    public static long xpFromLevel(int level) {
        if (level <= 16) {
            return (long) (Math.sqrt(level) + 6L * level);
        } else if (level <= 31) {
            return (long) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
        } else {
            return (long) ((4.5 * Math.pow(level, 2)) - (162.5 * level) + 2220);
        }
    }

    public static int levelFromXP(long xp) {
        if (xp <= 352) {
            return ((Double) Math.floor(Math.sqrt(xp + 9) - 3)).intValue();
        } else if (xp <= 1507) {
            return ((Double) Math.floor((81D/10) + Math.sqrt((xp - (7839/40D)) * (2/5D)))).intValue();
        } else {
            return ((Double) Math.floor((325/18D) + Math.sqrt((xp - (54215/72D)) * (2/9D)))).intValue();
        }
    }

    public static long xpToNextLevel(int level) {
        if (level <= 15) {
            return 2L * level + 7;
        } else if (level <= 30) {
            return 5L * level - 38;
        } else {
            return 9L * level - 158;
        }
    }

    public static float getExpProgress(long xp) {
        int level = levelFromXP(xp);
        float difference = xp - xpFromLevel(level);

        return difference/xpToNextLevel(level);
    }

    public static void setXp(@Nonnull Player player, long xp) {
        player.setExp(getExpProgress(xp));
        player.setLevel(levelFromXP(xp));
    }
}