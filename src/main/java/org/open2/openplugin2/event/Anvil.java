//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Anvil implements Listener {
    public Anvil(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAnvil(final InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        if (e.getWhoClicked() instanceof Player) {
            if (e.getView().getTopInventory() != null) {
                if (e.getView().getTopInventory().getType().equals(InventoryType.ANVIL)) {
                    final AnvilInventory anvilinv = (AnvilInventory)e.getView().getTopInventory();
                    final int slot = e.getRawSlot();
                    if (slot == 2 && e.getClickedInventory() == anvilinv) {
                        ItemStack standitem = anvilinv.getItem(0);
                        ItemStack book = anvilinv.getItem(1);
                        if (this.canenchant(standitem, book) && player.getLevel() >= 30) {
                            ItemStack result = anvilinv.getItem(2);
                            boolean b = false;
                            Iterator var10 = result.getEnchantments().entrySet().iterator();

                            while(var10.hasNext()) {
                                Map.Entry<Enchantment, Integer> enchant = (Map.Entry)var10.next();
                                if (((Enchantment)enchant.getKey()).getMaxLevel() < (Integer)enchant.getValue()) {
                                    b = true;
                                }
                            }

                            if (player.getInventory().firstEmpty() != -1 && b) {
                                player.getInventory().addItem(new ItemStack[]{result});
                                ItemStack air = new ItemStack(Material.AIR);
                                anvilinv.setItem(0, air);
                                anvilinv.setItem(1, air);
                                player.getWorld().playSound(player, Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
                                player.updateInventory();
                                player.setLevel(player.getLevel() - 30);
                            }
                        }
                    } else {
                        Bukkit.getScheduler().runTaskLater(Main.m, new Runnable() {
                            public void run() {
                                ItemStack standitem = anvilinv.getItem(0);
                                ItemStack book = anvilinv.getItem(1);
                                if (book == null && slot == 1) {
                                    book = e.getCurrentItem();
                                }

                                if (Anvil.this.canenchant(standitem, book)) {
                                    standitem = standitem.clone();
                                    boolean b = true;
                                    Set<Map.Entry<Enchantment, Integer>> enchants = new HashSet();
                                    EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta)book.getItemMeta();
                                    ItemMeta meta = book.getItemMeta();
                                    if (meta != null) {
                                        enchants.addAll(meta.getEnchants().entrySet());
                                    }

                                    if (meta2 != null) {
                                        enchants.addAll(meta2.getStoredEnchants().entrySet());
                                    }

                                    if (!enchants.isEmpty()) {
                                        Iterator var8 = enchants.iterator();

                                        while(var8.hasNext()) {
                                            Map.Entry<Enchantment, Integer> enchant = (Map.Entry)var8.next();
                                            ItemStack i = new ItemStack(standitem.getType());
                                            if (((Enchantment)enchant.getKey()).canEnchantItem(i) && ((Enchantment)enchant.getKey()).getMaxLevel() < (Integer)enchant.getValue()) {
                                                standitem.addUnsafeEnchantment((Enchantment)enchant.getKey(), (Integer)enchant.getValue());
                                                b = false;
                                            }
                                        }
                                    }

                                    if (b) {
                                        return;
                                    }

                                    anvilinv.setItem(2, standitem);
//                                    AnvilView.setRepairCost(30);
                                }

                            }
                        }, 1L);
                    }
                }

            }
        }
    }

    private boolean canenchant(ItemStack standitem, ItemStack book) {
        if (standitem == null) {
            return false;
        } else if (book == null) {
            return false;
        } else {
            return book.getType().equals(Material.ENCHANTED_BOOK);
        }
    }
}
