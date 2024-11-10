package org.open2.openplugin2.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Recipi implements Listener {
    public Recipi(Main m) {
        ItemStack lightBlock = new ItemStack(Material.LIGHT, 4);
        ShapedRecipe recipe = new ShapedRecipe(lightBlock);
        recipe.shape(new String[] { "TT", "TT" });
        recipe.setIngredient('T', Material.TORCH);
        Bukkit.getRecipesFor(lightBlock);
        if (!isRecipeRegistered(recipe))
            Bukkit.addRecipe((Recipe)recipe);
        m.getServer().getPluginManager().registerEvents(this, (Plugin)m);
    }

    @EventHandler
    public void onclick(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Player player = event.getPlayer();
            if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                Block block = event.getClickedBlock();
                if (block.getType().equals(Material.LIGHT)) {
                    block.setType(Material.AIR);
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.LIGHT));
                }
            }
        }
    }

    private boolean isRecipeRegistered(ShapedRecipe newRecipe) {
        Iterator<Recipe> it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe recipe = it.next();
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe)recipe;
                if (shapedRecipe.getResult().isSimilar(newRecipe.getResult()) &&
                        shapedRecipe.getShape().equals(newRecipe.getShape()))
                    return true;
            }
        }
        return false;
    }

    HashMap<UUID, Locs> changed = new HashMap<>();

    @EventHandler
    public void onPlayerItemHeld(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            final Block block = event.getBlock();
            if (block.getType().equals(Material.LIGHT))
                (new BukkitRunnable() {
                    public void run() {
                        player.sendBlockChange(block.getLocation(), Material.FIRE.createBlockData());
                        Locs locs = new Locs();
                        if (Recipi.this.changed.containsKey(player.getUniqueId()))
                            locs = Recipi.this.changed.get(player.getUniqueId());
                        locs.add(block.getLocation());
                        Recipi.this.changed.put(player.getUniqueId(), locs);
                    }
                }).runTaskLaterAsynchronously((Plugin)Main.m, 5L);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (item != null && item.getType() == Material.LIGHT) {
            changeNearbyBlocks(player, Material.LIGHT, Material.FIRE, 30);
        } else if (this.changed.containsKey(player.getUniqueId())) {
            for (Location loc : ((Locs)this.changed.get(player.getUniqueId())).locations) {
                Locs locs = new Locs();
                if (this.changed.containsKey(player.getUniqueId()))
                    locs = this.changed.get(player.getUniqueId());
                player.sendBlockChange(loc, loc.getBlock().getBlockData());
                this.changed.put(player.getUniqueId(), locs);
            }
            this.changed.remove(player.getUniqueId());
        }
    }

    private void changeNearbyBlocks(Player player, Material from, Material to, int radius) {
        int px = player.getLocation().getBlockX();
        int py = player.getLocation().getBlockY();
        int pz = player.getLocation().getBlockZ();
        Locs locs = new Locs();
        if (this.changed.containsKey(player.getUniqueId()))
            locs = this.changed.get(player.getUniqueId());
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = player.getWorld().getBlockAt(px + x, py + y, pz + z);
                    if (block.getType() == from) {
                        player.sendBlockChange(block.getLocation(), to.createBlockData());
                        locs.add(block.getLocation());
                    }
                }
            }
        }
        this.changed.put(player.getUniqueId(), locs);
    }
}
