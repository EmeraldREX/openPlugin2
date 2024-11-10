//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Chunk implements Listener {
    Random random = new Random();

    public Chunk(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onchunkloaded(ChunkPopulateEvent event) {
        BlockState[] blocks = event.getChunk().getTileEntities();
        BlockState[] var6 = blocks;
        int var5 = blocks.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            BlockState block = var6[var4];
            if (block instanceof Chest) {
                Chest chest = (Chest)block;
                chest.getInventory().addItem(new ItemStack[]{this.getenchant()});
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (Bukkit.getOfflinePlayers().length <= 6) {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getWorld().getName().equals("sigen") && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.FIREWORK_ROCKET)) {
                event.setCancelled(true);
            }

        }
    }

    private ItemStack getenchant() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        Enchantment[] enchants = Enchantment.values();
        Enchantment enchant = enchants[this.random.nextInt(enchants.length)];
        item.addUnsafeEnchantment(enchant, this.random.nextInt(enchant.getMaxLevel() + 1) + 1);
        return item;
    }
}
