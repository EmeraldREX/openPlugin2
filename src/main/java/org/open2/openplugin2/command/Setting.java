//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.bukkit.inventory.InventoryHolder;
import org.open2.openplugin2.net.ConfigLoader;
import org.open2.openplugin2.net.ItemGenerater;
import org.open2.openplugin2.net.Main;
import org.open2.openplugin2.net.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Setting implements CommandExecutor {
    public static List<ItemStack> items = new ArrayList();
    Main m;

    public Setting(Main m) {
        this.m = m;
        items.add(0, ItemGenerater.item(Material.STONE_AXE, ChatColor.GREEN + "木こりモード " + ChatColor.RED + "オフ", ChatColor.DARK_GREEN + "木を伐採してくれる機能やで," + ChatColor.DARK_GREEN + "木の一番下を斧で壊すと発動するで", false));
        items.add(1, ItemGenerater.item(Material.STONE_AXE, ChatColor.GREEN + "木こりモード" + ChatColor.AQUA + "オン", ChatColor.DARK_GREEN + "木を伐採してくれる機能やで," + ChatColor.DARK_GREEN + "木の一番下を斧で壊すと発動するで", true));
        items.add(2, ItemGenerater.item(Material.BOOK, ChatColor.BLUE + "チャット日本語入力" + ChatColor.RED + "オフ", ChatColor.AQUA + "JAVA向けの機能や," + ChatColor.AQUA + "ローマ字入力を自動で日本語に変換してくれる", false));
        items.add(3, ItemGenerater.item(Material.BOOK, ChatColor.BLUE + "チャット日本語入力" + ChatColor.AQUA + "オン", ChatColor.AQUA + "JAVA向けの機能や," + ChatColor.AQUA + "ローマ字入力を自動で日本語に変換してくれる", true));
        items.add(4, ItemGenerater.item(Material.GRASS_BLOCK, ChatColor.DARK_PURPLE + "資源ワールドに移動", ChatColor.RED + "資源採取専用のワールドやで," + ChatColor.RED + "定期的にリセットされるで", false));
        items.add(5, ItemGenerater.item(Material.GRASS_BLOCK, ChatColor.DARK_PURPLE + "資源ワールドから戻る", ChatColor.RED + "資源採取専用のワールドやで," + ChatColor.RED + "定期的にリセットされるで", true));
        items.add(6, ItemGenerater.item(Material.SAND, ChatColor.GOLD + "旧ワールド", "" + ChatColor.DARK_AQUA, true));
        items.add(7, ItemGenerater.item(Material.DIAMOND_BLOCK, ChatColor.GOLD + "配布ワールド", "" + ChatColor.DARK_AQUA, true));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        Inventory inv = Bukkit.createInventory((InventoryHolder)player, InventoryType.HOPPER, ChatColor.DARK_GREEN + "メニュー");
        if (((PlayerInfo)ConfigLoader.playerinfolist.get(player.getUniqueId())).cutall) {
            inv.setItem(0, items.get(0));
        } else {
            inv.setItem(0, items.get(1));
        }
        if (((PlayerInfo)ConfigLoader.playerinfolist.get(player.getUniqueId())).japanese) {
            inv.setItem(1, items.get(2));
        } else {
            inv.setItem(1, items.get(3));
        }
        inv.setItem(2, items.get(4));
        inv.setItem(3, items.get(6));
        inv.setItem(4, items.get(7));
        player.openInventory(inv);
        return true;
    }
}