//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Villagerai implements Listener {
    public Villagerai(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onvillagerdamaged(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Villager) {
            Villager villager = (Villager)e.getEntity();
            if (e.getDamager() instanceof Player) {
                Player player = (Player)e.getDamager();
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item != null && item.getType().equals(Material.STICK)) {
                    villager.setAI(!villager.hasAI());
                    villager.getWorld().playSound(villager.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0F, 0.1F);
                    e.setCancelled(true);
                }
            }
        }

    }
}
