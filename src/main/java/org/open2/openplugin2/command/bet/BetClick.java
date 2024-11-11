//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command.bet;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.open2.openplugin2.net.Main;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class BetClick implements Listener {
    Betinfo betinfo;
    Inventory inv;

    public BetClick(Main plugin, Betinfo betinfo) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.betinfo = betinfo;
        this.inv = Bukkit.createInventory((InventoryHolder)null, InventoryType.CHEST, betinfo.betinv);
    }

    @EventHandler
    public void settingclick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (event.getClickedInventory() != null) {
            if (event.getView() != null) {
                if (event.getView().getTitle().equals(this.betinfo.title)) {
                    event.setCancelled(true);
                }

                if (event.getClickedInventory().equals(event.getView().getTopInventory()) && title.equals(this.betinfo.title)) {
                    event.setCancelled(true);
                    ItemStack item = event.getCurrentItem();
                    if (item != null) {
                        HumanEntity humanEntity = event.getWhoClicked();
                        String name = event.getCurrentItem().getItemMeta().getDisplayName();
                        BetPlayer betPlayer;
                        if (!this.betinfo.bets.containsKey(humanEntity.getUniqueId())) {
                            betPlayer = new BetPlayer((UUID)this.betinfo.playerlist.get(name));
                            betPlayer.inv = Bukkit.createInventory((InventoryHolder)null, InventoryType.CHEST, this.betinfo.betinv);
                            this.betinfo.bets.put(humanEntity.getUniqueId(), betPlayer);
                        } else {
                            betPlayer = (BetPlayer)this.betinfo.bets.get(humanEntity.getUniqueId());
                            betPlayer.betto = (UUID)this.betinfo.playerlist.get(name);
                            this.betinfo.bets.put(humanEntity.getUniqueId(), betPlayer);
                        }

                        humanEntity.openInventory(betPlayer.inv);
                    }
                }

            }
        }
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        Inventory inventory = event.getInventory();
        if (!event.getView().equals((Object)null)) {
            if (event.getView().getTitle().equals(this.betinfo.betinv)) {
                BetPlayer betPlayer = (BetPlayer)this.betinfo.bets.get(player.getUniqueId());
                String name = "";
                Map<Material, Integer> map = new HashMap();
                ItemStack[] var10;
                int var9 = (var10 = inventory.getContents()).length;

                for(int var8 = 0; var8 < var9; ++var8) {
                    ItemStack item = var10[var8];
                    if (item != null) {
                        Material material = item.getType();
                        String materialname = this.betinfo.gettype(material);
                        if (materialname != null) {
                            map.compute(material, (k, v) -> {
                                return v == null ? item.getAmount() : v + item.getAmount();
                            });
                        } else {
                            event.getInventory().remove(item);
                            player.getWorld().dropItem(player.getLocation(), item);
                        }
                    }
                }

                Map.Entry entry;
                String materialname;
                for(Iterator var15 = map.entrySet().iterator(); var15.hasNext(); name = name + materialname + entry.getValue() + "個 ") {
                    entry = (Map.Entry)var15.next();
                    materialname = this.betinfo.gettype((Material)entry.getKey());
                }

                this.betinfo.bets.put(player.getUniqueId(), betPlayer);
                if (!name.equals("")) {
                    OfflinePlayer ofp = Bukkit.getOfflinePlayer(betPlayer.betto);
                    Bukkit.broadcastMessage(this.betinfo.prefix + player.getName() + "が" + ofp.getName() + "に" + ChatColor.YELLOW + name + "を賭けました");
                }
            }

        }
    }
}