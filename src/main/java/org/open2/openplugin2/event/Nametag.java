//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Nametag implements Listener {
    public Nametag(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onblockclick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getPlayer().isSneaking() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemStack off = player.getInventory().getItemInOffHand();
            if (item != null && item.getType().equals(Material.WRITTEN_BOOK) && off != null && off.getType().equals(Material.INK_SAC)) {
                BookMeta bookMeta = (BookMeta)item.getItemMeta();
                if (bookMeta != null) {
                    bookMeta.setAuthor(bookMeta.getDisplayName());
                    item.setItemMeta(bookMeta);
                }
            }
        }

    }
}
