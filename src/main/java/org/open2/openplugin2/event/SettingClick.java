//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.command.Setting;
import org.open2.openplugin2.net.ConfigLoader;
import org.open2.openplugin2.net.Main;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SettingClick implements Listener {
    ConfigLoader configloader;
    FileConfiguration config;

    public SettingClick(Main plugin, World sigen, ConfigLoader configLoader, FileConfiguration config) {
        this.configloader = configLoader;
        this.config = config;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void settingclick(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        Inventory inv = e.getView().getTopInventory();
        if (inv.getType().equals(InventoryType.HOPPER)) {
            if (e.getView().getTitle().equals(ChatColor.DARK_GREEN + "メニュー")) {
                e.setCancelled(true);
                if (e.getClickedInventory() == null) {
                    return;
                }

                if (e.getClickedInventory().equals(inv)) {
                    World main = Bukkit.getWorld("world");
                    World w;
                    switch (e.getSlot()) {
                        case 0:
                            if (this.configloader.setcutall(player)) {
                                inv.setItem(0, (ItemStack)Setting.items.get(0));
                            } else {
                                inv.setItem(0, (ItemStack)Setting.items.get(1));
                            }
                            break;
                        case 1:
                            if (this.configloader.setjapanese(player)) {
                                inv.setItem(1, (ItemStack)Setting.items.get(2));
                            } else {
                                inv.setItem(1, (ItemStack)Setting.items.get(3));
                            }
                            break;
                        case 2:
                            w = Bukkit.getWorld("sigen");
                            if (w != null) {
                                if (player.getLocation().getWorld().getName().equals(w.getName())) {
                                    if (this.config.get("world_loc." + player.getUniqueId()) != null) {
                                        player.teleport(this.config.getLocation("world_loc." + player.getUniqueId()));
                                        this.config.set("world_loc." + player.getUniqueId(), (Object)null);
                                        player.closeInventory();
                                    } else {
                                        player.teleport(main.getSpawnLocation());
                                        player.closeInventory();
                                    }

                                    player.closeInventory();
                                } else if (player.getLocation().getWorld().getName().equals(main.getName())) {
                                    this.config.set("world_loc." + player.getUniqueId(), player.getLocation());
                                    player.teleport(w.getSpawnLocation());
                                    player.closeInventory();
                                }
                            }
                            break;
                        case 3:
                            w = Bukkit.getWorld("beworld");
                            if (w != null) {
                                if (player.getLocation().getWorld().getName().equals(w.getName())) {
                                    if (this.config.get("world_loc." + player.getUniqueId()) != null) {
                                        player.teleport(this.config.getLocation("world_loc." + player.getUniqueId()));
                                        this.config.set("world_loc." + player.getUniqueId(), (Object)null);
                                        player.closeInventory();
                                    } else {
                                        player.teleport(main.getSpawnLocation());
                                        player.closeInventory();
                                    }

                                    player.closeInventory();
                                } else if (player.getLocation().getWorld().getName().equals(main.getName())) {
                                    this.config.set("world_loc." + player.getUniqueId(), player.getLocation());
                                    player.teleport(w.getSpawnLocation());
                                    player.closeInventory();
                                }
                            }
                            break;
                        case 4:
                            this.sendBungeeCordMessage(player, "nanj_channel", "move," + player.getName() + ",haihu");
                    }
                }
            }

        }
    }

    public void sendBungeeCordMessage(Player player, String subchannel, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        out.writeUTF(message);
        player.sendPluginMessage(Main.m, "BungeeCord", out.toByteArray());
    }
}
