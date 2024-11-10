//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Shulker implements Listener {
    Loccket locket;
    Main plugin;

    public Shulker(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;

        try {
            this.locket = new Loccket(plugin);
        } catch (Exception var3) {
        }

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!this.locket.isturn()) {
            try {
                this.locket = new Loccket(this.plugin);
            } catch (Exception var7) {
            }
        }

        Player player = event.getPlayer();
        if (player.isSneaking()) {
            if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                Block clicked;
                ShulkerBox shulkerBox;
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    clicked = event.getClickedBlock();
                    if (clicked.getType().name().contains("SHULKER_BOX")) {
                        event.setCancelled(true);
                        this.locket.remove(player, clicked);
                        shulkerBox = (ShulkerBox)clicked.getState();
                        ItemStack shulkeritem = new ItemStack(shulkerBox.getType());
                        BlockStateMeta meta = (BlockStateMeta)shulkeritem.getItemMeta();
                        meta.setBlockState(shulkerBox);
                        shulkeritem.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(shulkeritem);
                        player.updateInventory();
                        clicked.setType(Material.AIR);
                    }
                } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    clicked = event.getClickedBlock();
                    if (clicked.getType().name().contains("SHULKER_BOX")) {
                        event.setCancelled(true);
                        shulkerBox = (ShulkerBox)clicked.getState();
                        this.sortitems(shulkerBox.getInventory().getContents(), shulkerBox.getInventory());
                        player.sendMessage(ChatColor.GREEN + "アイテムをソートしました");
                    } else if (clicked.getType().equals(Material.CHEST)) {
                        event.setCancelled(true);
                        Chest chest = (Chest)clicked.getState();
                        Inventory inv = chest.getInventory();
                        if (inv.getHolder() instanceof DoubleChest) {
                            DoubleChest doubleChest = (DoubleChest)chest.getInventory().getHolder();
                            inv = doubleChest.getInventory();
                        }

                        this.sortitems(inv.getContents(), inv);
                        player.sendMessage(ChatColor.GREEN + "アイテムをソートしました");
                    }
                }
            }

        }
    }

    private void sortitems(ItemStack[] i, Inventory iv) {
        List<ItemStack> items = new ArrayList(Arrays.asList(i));
        items.removeIf(Objects::isNull);
        items.sort(Comparator.comparing((itemx) -> {
            return itemx.getType().name();
        }));
        iv.clear();
        Inventory inv = iv;
        Iterator var6 = items.iterator();

        while(var6.hasNext()) {
            ItemStack item = (ItemStack)var6.next();
            inv.addItem(new ItemStack[]{item});
        }

    }

    @EventHandler
    public void ondamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player player = (Player)e.getEntity();
            Player damager = (Player)e.getDamager();
            if (player.getName().equals(".AquaWritter86870")) {
                ItemStack item = damager.getInventory().getItemInMainHand();
                if (item != null && item.getType().equals(Material.STICK) && item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "AQUA死ぬ棒")) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 9999, 9999, false, false));
                }
            }
        }

    }

    public static Collection<Entity> getEntitiesNearLocation(Location location) {
        World world = location.getWorld();
        Collection<Entity> nearbyEntities = world.getNearbyEntities(location, 1.0, 1.0, 1.0);
        return nearbyEntities;
    }
}
