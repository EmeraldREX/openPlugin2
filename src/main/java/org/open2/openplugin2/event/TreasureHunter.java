//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Iterator;
import java.util.Random;

public class TreasureHunter implements Listener {
    Random random = new Random();

    public TreasureHunter(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChunkLoad(InventoryMoveItemEvent event) {
        if (event.getDestination() != null) {
            World world = event.getDestination().getLocation().getWorld();
            if (world.getName().contains("sigen")) {
                Block block = event.getDestination().getLocation().getBlock();
                block.setMetadata("placed_by", new FixedMetadataValue(Main.m, ""));
            }
        }

    }

    @EventHandler
    public void onChunkLoad(InventoryOpenEvent event) {
        World world = event.getPlayer().getWorld();
        if (world.getName().contains("sigen")) {
            if (event.getInventory().getHolder() instanceof Chest) {
                Chest chest = (Chest)event.getInventory().getHolder();
                Block block = chest.getBlock();
                if (block.getType() == Material.CHEST && !block.hasMetadata("placed_by")) {
                    ItemStack[] var8;
                    int var7 = (var8 = chest.getInventory().getContents()).length;

                    for(int var6 = 0; var6 < var7; ++var6) {
                        ItemStack item = var8[var6];
                        if (item != null && this.random.nextDouble() < 0.2) {
                            if (item.getType() != Material.ENCHANTED_BOOK) {
                                Iterator var18 = item.getItemMeta().getEnchants().keySet().iterator();

                                while(var18.hasNext()) {
                                    Enchantment enchantment = (Enchantment)var18.next();
                                    item.addUnsafeEnchantment(enchantment, enchantment.getMaxLevel() + 1);
                                    chest.getBlockInventory().setItem(chest.getBlockInventory().first(item), item);
                                }
                            } else {
                                EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
                                if (meta != null && meta.hasStoredEnchants()) {
                                    Iterator var11 = meta.getStoredEnchants().keySet().iterator();

                                    while(var11.hasNext()) {
                                        Enchantment enchantment = (Enchantment)var11.next();
                                        meta.addStoredEnchant(enchantment, enchantment.getMaxLevel() + 1, true);
                                    }

                                    item.setItemMeta(meta);
                                    chest.getBlockInventory().setItem(chest.getBlockInventory().first(item), item);
                                }
                            }
                        }
                    }

                    block.setMetadata("placed_by", new FixedMetadataValue(Main.m, event.getPlayer().getName()));
                }
            } else if (event.getInventory().getHolder() instanceof StorageMinecart) {
                StorageMinecart chest = (StorageMinecart)event.getInventory().getHolder();
                if (!chest.hasMetadata("placed_by")) {
                    Iterator var14 = chest.getInventory().iterator();

                    while(true) {
                        ItemStack item;
                        do {
                            do {
                                if (!var14.hasNext()) {
                                    chest.setMetadata("placed_by", new FixedMetadataValue(Main.m, event.getPlayer().getName()));
                                    return;
                                }

                                item = (ItemStack)var14.next();
                            } while(item == null);
                        } while(!(this.random.nextDouble() < 0.2));

                        chest.getInventory().removeItem(new ItemStack[]{item});
                        Iterator var16 = item.getItemMeta().getEnchants().keySet().iterator();

                        while(var16.hasNext()) {
                            Enchantment enchantment = (Enchantment)var16.next();
                            item.addUnsafeEnchantment(enchantment, enchantment.getMaxLevel() + 1);
                        }

                        chest.getInventory().addItem(new ItemStack[]{item});
                    }
                }
            }
        }

    }

    @EventHandler
    public void onminecart(EntityPlaceEvent e) {
        if (e.getEntity() instanceof StorageMinecart && e.getEntity().getWorld().getName().contains("sigen")) {
            e.getEntity().setMetadata("placed_by", new FixedMetadataValue(Main.m, "AAA"));
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Material type = block.getType();
        if (block.getWorld().getName().contains("sigen") && type == Material.CHEST) {
            block.setMetadata("placed_by", new FixedMetadataValue(Main.m, event.getPlayer().getName()));
        }

    }
}
