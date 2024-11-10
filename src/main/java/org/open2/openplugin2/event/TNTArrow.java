//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class TNTArrow implements Listener {
    double maxspeed = 3.0;
    FileConfiguration c;
    Main m;

    public TNTArrow(Main plugin, FileConfiguration c) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.c = c;
        this.m = plugin;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType().equals(EntityType.ARROW)) {
            if (event.getHitBlock() != null && event.getHitBlock().getType() == Material.SANDSTONE) {
                event.getHitBlock().setType(Material.AIR);
                TNTPrimed tnt = (TNTPrimed)event.getHitBlock().getWorld().spawn(event.getHitBlock().getLocation().add(0.5, 0.5, 0.5), TNTPrimed.class);
                tnt.setFuseTicks(40);
            }

        }
    }

    @EventHandler
    public void ondamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if (e.getDamager() instanceof Arrow) {
                e.setCancelled(true);
            }
        }

    }
}
