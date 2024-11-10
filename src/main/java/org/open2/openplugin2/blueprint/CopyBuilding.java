package org.open2.openplugin2.blueprint;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.open2.openplugin2.net.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CopyBuilding implements CommandExecutor, TabCompleter {
    WorldEditPlugin plugin;

    Main m;

    BlueprintMain bl;

    public CopyBuilding(Main m, BlueprintMain bl) {
        this.m = m;
        this.bl = bl;
        m.getCommand("copybuildings").setTabCompleter(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "paste <filename>, copy <filename>, center <filename>");
            return true;
        }
        this.plugin = (WorldEditPlugin)this.m.getServer().getPluginManager().getPlugin("WorldEdit");
        File fl = new File("blueprints");
        if (!fl.exists())
            fl.mkdir();
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        String filename = args[1];
        if (args[0].equalsIgnoreCase("paste")) {
            this.bl.paste(player, filename);
            player.sendMessage(String.valueOf(filename) + " pasted");
            return true;
        }
        if (args[0].equalsIgnoreCase("copy")) {
            if (this.plugin == null) {
                player.sendMessage(ChatColor.RED + "World Edit");
                return false;
            }
            try {
                Region sel = this.plugin.getSession(player).getSelection();
                BlockVector3 min = sel.getMinimumPoint();
                BlockVector3 max = sel.getMaximumPoint();
                Location loc1 = new Location(player.getWorld(), min.getX(), min.getY(), min.getZ());
                Location loc2 = new Location(player.getWorld(), max.getX(), max.getY(), max.getZ());
                save(player.getLocation().getBlock().getLocation(), filename, getBlocksBetweenLocations(loc1, loc2),
                        loc1, loc2);
                player.sendMessage(String.valueOf(filename) + " Saved");
                this.bl.reload(filename);
                return true;
            } catch (IncompleteRegionException e) {
                player.sendMessage(ChatColor.RED + "");
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            this.bl.reloadall();
            player.sendMessage("Reloaded");
        } else if (args[0].equalsIgnoreCase("center")) {
            if (this.bl.update(player.getLocation(), filename)) {
                player.sendMessage(String.valueOf(filename) + " Center Changed");
            } else {
                String ls = "";
                for (String list : this.bl.prints.keySet())
                    ls = String.valueOf(ls) + list + " ";
                player.sendMessage(ChatColor.RED + ls);
            }
        }
        return false;
    }

    private void save(Location loc, String filename, List<Block> blocks, Location min, Location max) {
        filename = String.valueOf(Main.m.getDataFolder().getAbsolutePath()) + "/blueprints/" + filename;
        Exception exception2, exception1 = null;
    }

    private List<Block> getBlocksBetweenLocations(Location loc1, Location loc2) {
        World world = loc1.getWorld();
        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();
        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);
        List<Block> blocks = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (!world.getBlockAt(x, y, z).getType().equals(Material.AIR))
                        blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("copy");
            completions.add("pasete");
            completions.add("reload");
            completions.add("center");
        }
        return completions;
    }
}
