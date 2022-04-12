package me.snazzy.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Battle Royale has loaded");
        getServer().getPluginManager().registerEvents(new BattleRoyale(), this);
        getServer().getPluginManager().registerEvents(new Loot(), this);
        getCommand("ready").setExecutor(new BattleRoyale());
        getCommand("unready").setExecutor(new BattleRoyale());
        getCommand("force").setExecutor(new BattleRoyale());
        getCommand("fix").setExecutor(new BattleRoyale());
        getCommand("refill").setExecutor(new Loot());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Player[] players = getServer().getOnlinePlayers().toArray(new Player[0]);
                for (Player player : players) {
                    if (player.getLocation().getBlock().getType().equals(Material.WATER) && !player.isInsideVehicle()) {
                        player.addPotionEffect(PotionEffectType.POISON.createEffect(20, 1));
                    }
                }
            }
        }, 0, 10);
    }


}
