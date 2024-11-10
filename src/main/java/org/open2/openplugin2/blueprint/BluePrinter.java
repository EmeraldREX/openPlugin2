package org.open2.openplugin2.blueprint;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.InventoryHolder;
import org.open2.openplugin2.net.ItemGenerater;
import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BluePrinter implements Listener {
    private String sekkei = ChatColor.DARK_PURPLE + "";

    private String sekkeis = ChatColor.YELLOW + "";

    private String center = ChatColor.GREEN + "";

    private String radius = ChatColor.RED + "";

    private String turn = ChatColor.GREEN + "";

    private String materials = ChatColor.BLUE + "";

    BlueprintMain bm;
    Plugin plugin;

    HashMap<Player, Location> pl = new HashMap<>();

    HashMap<Location, String> map = new HashMap<>();

    public BluePrinter(BlueprintMain bm, Main m) {
        this.bm = bm;
        m.getServer().getPluginManager().registerEvents(this, (Plugin)m);
    }

    @EventHandler(ignoreCancelled = true)
    public void onhopper(InventoryMoveItemEvent event) {
        Inventory destinationInventory = event.getDestination();
        ItemStack item = event.getItem();
        Location loc = destinationInventory.getLocation();
        loc.setY(loc.getY() - 1.0D);
        if (isprinter(loc.getBlock()) &&
                this.map.containsKey(loc)) {
            BluePrints bp = this.bm.prints.get(this.map.get(loc));
            if (bp.materials.contains(item.getType()))
                for (Map.Entry<Location, BlockData> entry : bp.getmap().entrySet()) {
                    Material m = ((BlockData)entry.getValue()).getMaterial();
                    Location location = entry.getKey();
                    if (m.equals(item.getType()) && location.getBlock().getType().isAir()) {
                        ((Location)entry.getKey()).getBlock().setBlockData(entry.getValue());
                        bp.remove(location);
                        event.setItem(new ItemStack(Material.AIR));
                        return;
                    }
                }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onclick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        Location loc = this.pl.get(player);
        String filename = this.map.get(loc);
        if (item == null)
            return;
        if (event.getView() == null)
            return;
        String title = event.getView().getTitle();
        if (title.equals(this.sekkei)) {
            Inventory inv;
            switch (event.getSlot()) {
                case 10:
                    inv = Bukkit.createInventory(null, InventoryType.CHEST, this.sekkeis);
                    for (String s : this.bm.prints.keySet()) {
                        inv.addItem(new ItemStack[] { ItemGenerater.item(Material.MAP, s) });
                    }
                    player.openInventory(inv);
                    break;
                case 12:
                    if (filename != null) {
                        this.bm.update(loc, filename);
                        player.sendMessage(ChatColor.YELLOW + "");
                        break;
                    }
                    player.sendMessage(ChatColor.RED + "");
                    break;
                case 14:
                    if (filename != null) {
                        this.bm.sendpacket(player, filename);
                        Bukkit.getScheduler().runTaskLater((Plugin) this.bm, () -> {
                            this.bm.restorepacket(player, filename);
                        }, 200L);
                    } else {
                        player.sendMessage(ChatColor.RED + "エラー");
                    }
                    break;

                case 18:
                    player.openInventory(getinv(filename));
                    break;
            }
        } else if (title.equals(this.sekkeis)) {
            this.map.put(loc, item.getItemMeta().getDisplayName());
        } else if (title.equals(this.materials)) {
            event.setCancelled(true);
        } else {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onclick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block b = event.getClickedBlock();
            if (isprinter(b))
                if (player.isOp()) {
                    Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, this.sekkei);
                    inv.setItem(10, ItemGenerater.item(Material.FILLED_MAP, this.sekkeis));
                    inv.setItem(12, ItemGenerater.item(Material.BEACON, this.center));
                    inv.setItem(14, ItemGenerater.item(Material.REDSTONE, this.radius));
                    inv.setItem(16, ItemGenerater.item(Material.BARRIER, this.turn));
                    inv.setItem(18, ItemGenerater.item(Material.CHEST, this.materials));
                    this.pl.put(player, b.getLocation());
                    player.openInventory(inv);
                    event.setCancelled(true);
                } else {
                    player.openInventory(getinv(this.map.get(b.getLocation())));
                }
        }
    }

    private Inventory getinv(String str) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, this.materials);
        BluePrints bp = (BluePrints)this.bm.prints.get(str);
        Map<Material, Integer> countMap = new HashMap();
        Iterator var6 = bp.map.entrySet().iterator();

        Map.Entry entry;
        while(var6.hasNext()) {
            entry = (Map.Entry)var6.next();
            Material material = ((BlockData)entry.getValue()).getMaterial();
            if (!material.equals(((Location)entry.getKey()).getBlock().getType()) && ((Location)entry.getKey()).getBlock().getType() != Material.AIR) {
                countMap.put(material, (Integer)countMap.getOrDefault(material, 0) + 1);
            }
        }

        var6 = countMap.entrySet().iterator();

        while(var6.hasNext()) {
            entry = (Map.Entry)var6.next();
            inv.addItem(new ItemStack[]{ItemGenerater.item((Material)entry.getKey(), (String)null, entry.getValue() + "個", false)});
        }

        return inv;
    }

    private boolean isprinter(Block b) {
        Location loc = b.getLocation();
        if (b.getType().equals(Material.FLETCHING_TABLE)) {
            loc.setY(loc.getY() + 1.0);
            if (loc.getBlock().getType().equals(Material.CHEST)) {
                loc.setY(loc.getY() + 1.0);
                if (loc.getBlock().getType().equals(Material.HOPPER)) {
                    return true;
                }
            }
        }

        return false;
    }
}
