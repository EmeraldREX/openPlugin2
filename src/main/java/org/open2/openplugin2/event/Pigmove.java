//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class Pigmove implements Listener {
    Location loc;

    public Pigmove(Main m) {
        m.getServer().getPluginManager().registerEvents(this, m);
        this.startPigMovementTask(m);
    }

    @EventHandler
    public void onblockclick(EntityDamageEvent e) {
        if (e.getEntityType().equals(EntityType.PIG) && e.getCause().equals(DamageCause.FALL)) {
            e.setCancelled(true);
        }

    }

    private void startPigMovementTask(Main m) {
        BukkitRunnable task = new BukkitRunnable() {
            public void run() {
                Iterator var2 = Bukkit.getOnlinePlayers().iterator();

                while(var2.hasNext()) {
                    Player player = (Player)var2.next();
                    ItemStack hand = player.getInventory().getItemInMainHand();
                    Entity entity = player.getVehicle();
                    if (hand == null) {
                        return;
                    }

                    if (hand.getType() == Material.STICK) {
                        if (entity.equals((Object)null)) {
                            return;
                        }

                        if (entity.getType().equals(EntityType.PIG)) {
                            Pig pig = (Pig)entity;
                            pig.getLocation().setDirection(player.getVelocity());
                            pig.setVelocity(player.getLocation().getDirection().normalize().multiply(1.5));
                        }
                    }
                }

            }
        };
        task.runTaskTimer(m, 0L, 20L);
    }
}
