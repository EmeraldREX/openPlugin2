//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class DragonBall implements Listener {
    Main m;

    public DragonBall(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.m = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player sender = event.getPlayer();
        if (sender.getName().equals("shinanoido") && event.getMessage().contains("どうか死")) {
            double nearestDistance = Double.MAX_VALUE;
            Player nearestPlayer = null;
            Iterator var7 = Bukkit.getOnlinePlayers().iterator();

            while(var7.hasNext()) {
                Player player = (Player)var7.next();
                if (player != sender && sender.getWorld().equals(player.getWorld())) {
                    double distance = sender.getLocation().distance(player.getLocation());
                    if (distance < nearestDistance) {
                        nearestDistance = distance;
                        nearestPlayer = player;
                    }
                }
            }

            if (nearestPlayer == null) {
                nearestPlayer = sender;
            }

            (new BukkitRunnable() {
                public void run() {
                    sender.getWorld().spawnParticle(Particle.EXPLOSION, sender.getLocation(), 1);
                    sender.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, sender.getLocation(), 2000, 1.6, 1.4, 1.6, 0.0);
                    sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                    sender.damage(50.0);
                }
            }).runTaskLater(Main.m, 60L);
            Player finalNearestPlayer = nearestPlayer;
            (new BukkitRunnable() {
                public void run() {
                    Bukkit.broadcastMessage("<" + finalNearestPlayer.getName() + "> " + "ふぅ...びっくりさせやがって");
                }
            }).runTaskLater(Main.m, 160L);
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        final Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null) {
            if (event.getHand().equals(EquipmentSlot.HAND)) {
                if (item.getType() == Material.GOLD_INGOT) {
                    Entity target = event.getRightClicked();
                    if (target != null) {
                        final Player targetPlayer = (Player)target;
                        if (player.getName().equals("onchan334")) {
                            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 240, 1));
                            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 240, 1));
                            (new BukkitRunnable() {
                                public void run() {
                                    targetPlayer.getWorld().spawnParticle(Particle.EXPLOSION, targetPlayer.getLocation(), 1);
                                    targetPlayer.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, targetPlayer.getLocation(), 4000, 1.6, 1.4, 1.6, 0.0);
                                    targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 4.0F, 4.0F);
                                    targetPlayer.damage(50.0);
                                    (new BukkitRunnable() {
                                        public void run() {
                                            Bukkit.broadcastMessage("<" + player.getName() + "> " + "きたねぇ 花火だ");
                                        }
                                    }).runTaskLater(Main.m, 20L);
                                }
                            }).runTaskLater(Main.m, 240L);
                        }
                    }
                }

            }
        }
    }
}
