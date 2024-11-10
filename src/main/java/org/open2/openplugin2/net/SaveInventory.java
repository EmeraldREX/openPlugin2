package org.open2.openplugin2.net;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SaveInventory {
    public SaveInventory(Player p, World warpto) {
        String world = warpto.getName();
        if (world.equals("world_nether") || world.equals("world_the_end") || world.equals("sigen"))
            world = "world";
        if (p.getWorld().getName().equals(world))
            return;
        File f = new File(Main.m.getDataFolder().getAbsolutePath(), String.valueOf(p.getUniqueId().toString()) + ".yml");
        if (!f.exists()) {
            try {
                saveInventory(p, f);
                p.getInventory().clear();
                p.setLevel(0);
                p.setExp(0.0F);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
        try {
            saveInventory(p, f);
            restoreInventory(p, (FileConfiguration)yamlConfiguration, warpto.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveInventory(Player p, File f) throws IOException {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
        String world = p.getLocation().getWorld().getName();
        yamlConfiguration.set(String.valueOf(world) + "inventory.armor", p.getInventory().getArmorContents());
        yamlConfiguration.set(String.valueOf(world) + "inventory.content", p.getInventory().getContents());
        yamlConfiguration.set(String.valueOf(world) + "inventory.exp", Float.valueOf(p.getExp()));
        yamlConfiguration.set(String.valueOf(world) + "inventory.level", Integer.valueOf(p.getLevel()));
        yamlConfiguration.set(String.valueOf(world) + "inventory.location", p.getLocation());
        yamlConfiguration.set("location", p.getLocation());
        yamlConfiguration.save(f);
    }

    public void restoreInventory(Player p, FileConfiguration c, String world) throws IOException {
        ItemStack[] content = (ItemStack[])((List)c.get(String.valueOf(world) + "inventory.armor")).toArray((Object[])new ItemStack[0]);
        p.getInventory().setArmorContents(content);
        content = (ItemStack[])((List)c.get(String.valueOf(world) + "inventory.content")).toArray((Object[])new ItemStack[0]);
        p.getInventory().setContents(content);
        p.setLevel(c.getInt(String.valueOf(world) + "inventory.level"));
        p.setExp((float)c.getDouble(String.valueOf(world) + "inventory.exp"));
    }
}
