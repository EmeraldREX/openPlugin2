package org.open2.openplugin2.pve;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.Plugin;

public class Target implements Listener {
    PVEmain pvemain;

    public Target(PVEmain m) {
        this.pvemain = m;
        m.m.getServer().getPluginManager().registerEvents(this, (Plugin)m.m);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (this.pvemain.pvee.mobs.contains(event.getEntity().getUniqueId()) &&
                event.getTarget() != null &&
                this.pvemain.pvee.mobs.contains(event.getTarget().getUniqueId())) {
            event.setCancelled(true);
            return;
        }
        this.pvemain.pvee.kohei.containsKey(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void place(BlockPlaceEvent event) {
        this.pvemain.info.blocks.remove(event.getBlock().getLocation());
    }
}
