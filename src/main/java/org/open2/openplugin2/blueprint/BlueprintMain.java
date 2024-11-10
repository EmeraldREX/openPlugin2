package org.open2.openplugin2.blueprint;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class BlueprintMain {
    HashMap<String, BluePrints> prints = new HashMap<>();

    Main m;

    public BlueprintMain(Main m) {
        this.m = m;
        m.getCommand("copybuildings").setExecutor(new CopyBuilding(m, this));
        reloadall();
    }

    public void reloadall() {
        File directory = new File(String.valueOf(Main.m.getDataFolder().getAbsolutePath()) + "/blueprints");
        if (!directory.exists())
            directory.mkdir();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                byte b;
                int i;
                File[] arrayOfFile;
                for (i = (arrayOfFile = files).length, b = 0; b < i; ) {
                    File file = arrayOfFile[b];
                    try {
                        getprint(file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    b++;
                }
            }
        }
    }

    public void reload(String str) {
        if (this.prints.containsKey(str))
            this.prints.remove(str);
        try {
            getprint(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getprint(String filename) throws IOException {
        Exception exception2, exception1 = null;
    }

    public boolean sendpacket(Player player, String filename) {
        if (this.prints.containsKey(filename)) {
            for (Map.Entry<Location, BlockData> entry : ((BluePrints)this.prints.get(filename)).getmap().entrySet()) {
                BlockData data = entry.getValue();
                if (((Location)entry.getKey()).getBlock().getBlockData().equals(data))
                    data = Bukkit.createBlockData(Material.BLACK_STAINED_GLASS);
                player.sendBlockChange(entry.getKey(), data);
            }
            return true;
        }
        return false;
    }

    public boolean paste(Player player, String filename) {
        if (this.prints.containsKey(filename)) {
            for (Map.Entry<Location, BlockData> entry : ((BluePrints)this.prints.get(filename)).getmap().entrySet()) {
                BlockData data = entry.getValue();
                ((Location)entry.getKey()).getBlock().setBlockData(data);
            }
            return true;
        }
        return false;
    }

    public boolean restorepacket(Player player, String filename) {
        if (this.prints.containsKey(filename)) {
            for (Location entry : ((BluePrints)this.prints.get(filename)).getmap().keySet())
                player.sendBlockChange(entry, entry.getBlock().getBlockData());
            return true;
        }
        return false;
    }

    public boolean update(Location loc, String filename) {
        if (this.prints.containsKey(filename)) {
            Exception exception2, exception1 = null;
            reload(filename);
            return true;
        }
        return false;
    }
}
