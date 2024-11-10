package org.open2.openplugin2.net;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigLoader {
    Main m;

    public static HashMap<UUID, PlayerInfo> playerinfolist = new HashMap<>();

    private FileConfiguration config;

    public ConfigLoader(Main m, FileConfiguration config, World sigen) {
        this.config = config;
        for (Player p : Bukkit.getOnlinePlayers())
            loadconfig(p);
        this.m = m;
    }

    public void loadconfig(Player p) {
        PlayerInfo info = new PlayerInfo();
        if (this.config.contains("Player." + p.getUniqueId() + ".cutall")) {
            info.cutall = this.config.getBoolean("Player." + p.getUniqueId() + ".cutall");
        } else {
            this.config.set("Player." + p.getUniqueId() + ".cutall", Boolean.valueOf(false));
            info.cutall = false;
            Main.m.saveConfig();
        }
        if (this.config.contains("Player." + p.getUniqueId() + ".japanese")) {
            info.japanese = this.config.getBoolean("Player." + p.getUniqueId() + ".japanese");
        } else {
            this.config.set("Player." + p.getUniqueId() + ".japanese", Boolean.valueOf(true));
            info.japanese = true;
        }
        playerinfolist.put(p.getUniqueId(), info);
    }

    public boolean setcutall(Player p) {
        PlayerInfo info = playerinfolist.get(p.getUniqueId());
        info.cutall = !info.cutall;
        playerinfolist.put(p.getUniqueId(), info);
        this.config.set("Player." + p.getUniqueId() + ".cutall", Boolean.valueOf(info.cutall));
        Main.m.saveConfig();
        return info.cutall;
    }

    public boolean setjapanese(Player p) {
        PlayerInfo info = playerinfolist.get(p.getUniqueId());
        info.japanese = !info.japanese;
        playerinfolist.put(p.getUniqueId(), info);
        this.config.set("Player." + p.getUniqueId() + ".japanese", Boolean.valueOf(info.japanese));
        Main.m.saveConfig();
        return info.japanese;
    }

    public boolean setlocation(Player p, Location loc, String s) {
        PlayerInfo info = playerinfolist.get(p.getUniqueId());
        Location l = null;
        if (s.equals("sigen")) {
            l = info.sigenlocation;
        } else if (s.equals("beworld")) {
            l = info.belocation;
        }
        if (l != null) {
            p.teleport(l);
            this.config.set("Player." + p.getUniqueId() + "." + s, null);
            if (s.equals("sigen")) {
                info.sigenlocation = null;
            } else {
                info.belocation = null;
            }
            playerinfolist.put(p.getUniqueId(), info);
            Main.m.saveConfig();
            return false;
        }
        if (loc.getWorld() == Bukkit.getWorld(s)) {
            p.teleport(Bukkit.getWorld("world").getSpawnLocation());
            return true;
        }
        Main.m.saveConfig();
        p.teleport(Bukkit.getWorld(s).getSpawnLocation());
        return true;
    }
}
