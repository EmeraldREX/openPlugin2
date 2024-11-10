//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class MobSpawn implements Listener {
    public MobSpawn(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void spawnmmob(CreatureSpawnEvent e) {
        if (!e.getEntityType().equals(EntityType.PHANTOM) && !e.getEntityType().equals(EntityType.BAT)) {
            if (e.getEntityType().equals(EntityType.HORSE) && e.getSpawnReason().equals(SpawnReason.SPAWNER_EGG)) {
                Horse horse = (Horse)e.getEntity();
                horse.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(5.0);
            }
        } else if (e.getSpawnReason().equals(SpawnReason.NATURAL) && (e.getLocation().getWorld().getName().equals("world") || e.getLocation().getWorld().getName().equals("beworld"))) {
            e.setCancelled(true);
        }

    }
}
