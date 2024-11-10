package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;

public class Dead implements Listener {
    public Dead(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
    }

    @EventHandler
    public void ondead(PlayerDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = e.getEntity();
            if (p.getWorld().getName().equals("sigen")) {
                Location loc = p.getLocation();
                PlayerProfile profile = p.getPlayerProfile();
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta)head.getItemMeta();
                meta.setOwnerProfile(profile);
                head.setItemMeta((ItemMeta)meta);
                loc.getWorld().dropItem(loc, head);
            }
        }
    }
}
