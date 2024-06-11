package com.wasmake.itemupgrade.timedevent;

import com.wasmake.itemupgrade.ItemUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TimedEventManager {
    private int tickFiveMinutes = 1200;
    private int tickMinute = 240;
    private int tickHalfHour = 7200;
    private int tickSecond = 4;

    public TimedEventManager() {
        runTaskManager();
    }
    public void runTaskManager() {
        new BukkitRunnable() {
            @Override
            public void run() {
                tickSecond--;
                tickMinute--;
                tickFiveMinutes--;
                tickHalfHour--;

                if (tickSecond == 0) {
                    tickSecond = 20;
                    Bukkit.getServer().getPluginManager().callEvent(new TimedEvent(UpdateTime.SECOND));
                }

                if (tickMinute == 0) {
                    tickMinute = 1200;
                    Bukkit.getServer().getPluginManager().callEvent(new TimedEvent(UpdateTime.MINUTE));
                }
                if (tickFiveMinutes == 0) {
                    tickFiveMinutes = 6000;
                    Bukkit.getServer().getPluginManager().callEvent(new TimedEvent(UpdateTime.FIVE_MINUTES));
                }

                if (tickHalfHour == 0) {
                    tickHalfHour = 36000;
                    Bukkit.getServer().getPluginManager().callEvent(new TimedEvent(UpdateTime.HALFHOUR));
                }

                Bukkit.getServer().getPluginManager().callEvent(new TimedEvent(UpdateTime.TICK));
            }

        }.runTaskTimer(ItemUpgrade.getInstance(), 0, 1);
    }
}
