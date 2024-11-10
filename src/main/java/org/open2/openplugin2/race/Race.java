//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.race;

import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Race implements Listener {
    HashMap<UUID, BukkitTask> hash = new HashMap();
    HashMap<UUID, Integer> counter = new HashMap();
    Main m;
    Random r = new Random();

    public Race(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.m = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Entity entity = player.getVehicle();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (entity != null) {
            EntityType type = entity.getType();
            if ((type.equals(EntityType.SKELETON_HORSE) || type.equals(EntityType.HORSE) || type.equals(EntityType.PIG) || type.equals(EntityType.LLAMA) || type.equals(EntityType.CAMEL) || type.equals(EntityType.DONKEY)) && (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && item != null && item.getType().equals(Material.BLAZE_ROD)) {
                UUID uid = player.getUniqueId();
                if (this.counter.containsKey(uid) && (Integer)this.counter.get(uid) == 200) {
                    this.addpotion(entity, type, new PotionEffect(PotionEffectType.SPEED, 20, 5));
                    return;
                }

                if (this.hash.containsKey(uid)) {
                    ((BukkitTask)this.hash.get(uid)).cancel();
                    if (this.counter.containsKey(uid)) {
                        if ((Integer)this.counter.get(uid) > 10) {
                            this.addpotion(entity, type, new PotionEffect(PotionEffectType.SPEED, 20, 5));
                            if ((Integer)this.counter.get(uid) != 200) {
                                this.counter.put(uid, 200);
                                player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_HORSE_HURT, 1.0F, 1.0F);
                                Bukkit.getScheduler().runTaskLater(this.m, () -> {
                                    this.addpotion(entity, type, new PotionEffect(PotionEffectType.SLOWNESS, 200, 3));
                                    this.hash.remove(uid);
                                    this.counter.remove(uid);
                                }, 100L);
                                return;
                            }
                        } else if ((Integer)this.counter.get(uid) < 5) {
                            player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_HORSE_AMBIENT, 1.0F, 1.0F);
                        }

                        this.counter.put(uid, (Integer)this.counter.get(uid) + 1);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_HORSE_AMBIENT, 1.0F, 1.0F);
                        this.addpotion(entity, type, new PotionEffect(PotionEffectType.SLOWNESS, 20, 1));
                        this.counter.put(uid, 0);
                    }

                    this.hash.remove(uid);
                } else {
                    this.counter.remove(uid);
                    this.addpotion(entity, type, new PotionEffect(PotionEffectType.SPEED, 15, 3));
                }

                long f = (long)(20 + this.r.nextInt(10));
                BukkitTask task = Bukkit.getScheduler().runTaskLater(this.m, () -> {
                    this.hash.remove(uid);
                }, f);
                this.hash.put(uid, task);
            }

        }
    }

    private void addpotion(Entity entity, EntityType type, PotionEffect effect) {
        LivingEntity lentity = (LivingEntity)entity;
        lentity.addPotionEffect(effect);
    }
}
