//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.ConfigLoader;
import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class PlayerLogin implements Listener {
    ConfigLoader config;
    Location capondes;
    Main m;

    public PlayerLogin(Main plugin, ConfigLoader config) {
        this.config = config;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.m = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (p.getName().equals("capondes")) {
            if (this.capondes != null) {
                e.setCancelled(true);
                if (e.getMessage().equals("1945815")) {
                    Bukkit.getScheduler().runTask(this.m, () -> {
                        p.teleport(this.capondes);
                        this.capondes = null;
                        p.setGameMode(GameMode.SURVIVAL);
                        p.sendMessage("解除！");
                    });
                    return;
                }
            }

            if (e.getMessage().equals("1945815")) {
                e.setCancelled(true);
                if (this.capondes != null) {
                    e.setCancelled(true);
                    if (e.getMessage().equals("1945815")) {
                        Bukkit.getScheduler().runTask(this.m, () -> {
                            p.teleport(this.capondes);
                            this.capondes = null;
                            p.setGameMode(GameMode.SURVIVAL);
                            p.sendMessage("解除！");
                        });
                        return;
                    }
                }
            }
        }

    }

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        e.setJoinMessage((String)null);
        Player p = e.getPlayer();
        if (p.getName().equals("capondes")) {
            this.capondes = p.getLocation();
            p.sendMessage(p.getLocation().toString());
            p.setGameMode(GameMode.ADVENTURE);
            e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 370.5, 64.0, -62.5));
        }

        FileConfiguration c = Main.m.getConfig();
        String uid = p.getUniqueId().toString();
        int i = (new Random()).nextInt(4);
        this.config.loadconfig(p);
        this.contains(c, uid, "name", p.getDisplayName());
        this.contains(c, uid, "ip", p.getAddress().toString());
        Main.m.saveConfig();
    }

    @EventHandler
    public void disconnect(PlayerQuitEvent e) {
        e.setQuitMessage((String)null);
    }

    private boolean contains(FileConfiguration c, String uid, String name, String s) {
        if (!c.contains("Player." + uid + "." + name)) {
            c.set("Player." + uid + "." + name, s);
            return true;
        } else {
            return false;
        }
    }
}
