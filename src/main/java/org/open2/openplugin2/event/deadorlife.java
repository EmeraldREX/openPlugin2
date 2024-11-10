package org.open2.openplugin2.event;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.open2.openplugin2.net.Main;


public class deadorlife implements Listener {
    Main m;

    public deadorlife(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
        this.m = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player sender = event.getPlayer();
        if (sender.getName().equals("viocat77") && event.getMessage().contains("生き") || sender.getName().equals("EmeraldREX91") && event.getMessage().contains("生き")) {
        double nearestDistance = Double.MAX_VALUE;
        Player nearestPlayer = null;
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (player1 != sender &&
                    sender.getWorld().equals(player1.getWorld())) {
                double distance = sender.getLocation().distance(player1.getLocation());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPlayer = player1;
                }
            }
        }
        if (nearestPlayer == null)
            nearestPlayer = sender;
        final Player player = nearestPlayer;
        (new BukkitRunnable() {
            public void run() {
                sender.getWorld().spawnParticle(Particle.EXPLOSION, sender.getLocation(), 1);
                sender.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, sender.getLocation(),
                        2000, 1.6D, 1.4D, 1.6D, 0.0D);
                sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F,
                        1.0F);
                sender.damage(50.0D);
            }
        }).runTaskLater((Plugin)Main.m, 60L);

    }
    }}
