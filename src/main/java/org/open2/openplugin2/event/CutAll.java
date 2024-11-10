//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.ConfigLoader;
import org.open2.openplugin2.net.Main;
import org.open2.openplugin2.net.PlayerInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CutAll implements Listener {
    public CutAll(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!e.isCancelled()) {
            Player p = e.getPlayer();
            if (!((PlayerInfo)ConfigLoader.playerinfolist.get(p.getUniqueId())).cutall) {
                ItemStack i = p.getInventory().getItemInMainHand();
                Block block = e.getBlock();
                if (i.getType().toString().contains("AXE") && block.getType().toString().contains("LOG")) {
                    Location loc = block.getLocation();
                    Material m = loc.clone().getBlock().getType();
                    loc.setY(loc.getY() - 1.0);
                    if (loc.getBlock().getType() == Material.DIRT) {
                        loc.setY(loc.getY() + 1.0);
                        List<Location> llist = new ArrayList();

                        for(int c = 0; c < 17; ++c) {
                            loc.setY(loc.getY() + 1.0);
                            Material type = loc.getBlock().getType();
                            if (type.equals(m)) {
                                llist.add(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()));
                            } else if (type.toString().contains("LEAVES")) {
                                Random random = new Random();
                                ItemStack hand = p.getInventory().getItemInMainHand();
                                short durability = hand.getDurability();
                                Iterator var14 = llist.iterator();

                                while(var14.hasNext()) {
                                    Location l = (Location)var14.next();
                                    if (hand.getType().toString().contains("AXE")) {
                                        l.getBlock().breakNaturally();
                                        int durabilityLevel = hand.getEnchantmentLevel(Enchantment.UNBREAKING);
                                        boolean shouldDamage = random.nextDouble() * 100.0 <= 100.0 / (double)(durabilityLevel + 1);
                                        if (shouldDamage) {
                                            ++durability;
                                            hand.setDurability(durability);
                                        }

                                        if (hand.getType().getMaxDurability() <= durability) {
                                            hand.setType(Material.AIR);
                                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, (float)durability, 1.0F);
                                            p.getInventory().setItemInMainHand(hand);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
