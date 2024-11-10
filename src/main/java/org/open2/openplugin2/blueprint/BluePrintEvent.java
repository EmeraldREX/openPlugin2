package org.open2.openplugin2.blueprint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class BluePrintEvent implements Listener {
    BlueprintMain bl;

    List<Player> list = new ArrayList<>();

    Main m;

    public HashMap<Player, String> map = new HashMap<>();

    public BluePrintEvent(BlueprintMain bl, Main m) {
        this.bl = bl;
        m.getServer().getPluginManager().registerEvents(this, (Plugin)m);
        this.m = m;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack i = p.getInventory().getItemInMainHand();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (i != null &&
                    i.getType().equals(Material.BOOK) &&
                    i.getItemMeta().getDisplayName().startsWith("")) {
                String name = i.getItemMeta().getDisplayName();
                name = name.replace("", "");
                if (!this.list.contains(p)) {
                    event.setCancelled(true);
                    removeenchant(i);
                    this.list.add(p);
                    this.map.put(p, name);
                    this.bl.sendpacket(p, name);
                    Bukkit.getScheduler().runTaskLater((Plugin)this.m, () -> {
                        this.addenchant(i);
                        this.list.remove(p);
                    },90L);
                }
            }
        } else if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) &&
                i != null &&
                i.getType().equals(Material.BOOK) &&
                i.getItemMeta().getDisplayName().startsWith("")) {
            String name = i.getItemMeta().getDisplayName();
            name = name.replace("", "");
            event.setCancelled(true);
            addenchant(i);
            this.bl.restorepacket(p, name);
            this.map.remove(p);
        }
    }

    public void onBreakBlockevent(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        Location loc = event.getBlock().getLocation();
        if (this.map.containsKey(p) &&
                this.bl.prints.containsKey(this.map.get(p)))
            for (Map.Entry<Location, BlockData> entry : ((BluePrints)this.bl.prints.get(this.map.get(p))).getmap().entrySet()) {
                if (((Location)entry.getKey()).getBlock().getLocation().equals(loc) && (
                        (BlockData)entry.getValue()).equals(event.getBlock().getBlockData()))
                    Bukkit.getScheduler().runTaskLater(this.m, () -> {
                        p.sendBlockChange(loc, Bukkit.createBlockData(Material.BLACK_STAINED_GLASS));
                    },2L);
            }
    }

    private void addenchant(ItemStack i) {
        try {
            ItemMeta meta = i.getItemMeta();
            meta.addEnchant(Enchantment.EFFICIENCY, 1, false);
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
            i.setItemMeta(meta);
        } catch (Exception exception) {}
    }

    private void removeenchant(ItemStack i) {
        try {
            ItemMeta meta = i.getItemMeta();
            meta.removeEnchant(Enchantment.EFFICIENCY);
            i.setItemMeta(meta);
        } catch (Exception exception) {}
    }
}
