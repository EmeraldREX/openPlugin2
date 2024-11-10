package org.open2.openplugin2.command.bet;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

public class Entry implements CommandExecutor {
    Betinfo betinfo;

    public Entry(Betinfo betinfo) {
        this.betinfo = betinfo;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.betinfo.reset();
        if (args.length > 0 &&
                args[0].equals("toggle")) {
            sender.sendMessage(String.valueOf(this.betinfo.prefix) + "Bet " + (this.betinfo.toggle ? 0 : 1));
            this.betinfo.toggle = !this.betinfo.toggle;
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(String.valueOf(this.betinfo.prefix) + "引数が足りません");
            return true;
        }
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, this.betinfo.title);
        String message = "";
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = args).length, b = 0; b < i; ) {
            String name = arrayOfString[b];
            Player player = Bukkit.getPlayer(name);
            if (player == null) {
                sender.sendMessage(String.valueOf(this.betinfo.prefix) + ChatColor.RED + "プレイヤー名に誤りがあります");
                return true;
            }
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta)head.getItemMeta();
            meta.setDisplayName(player.getName());
            try {
                PlayerProfile profile = player.getPlayerProfile();
                meta.setOwnerProfile(profile);
                head.setItemMeta((ItemMeta)meta);
            } catch (Exception e) {
                sender.sendMessage(String.valueOf(this.betinfo.prefix) + ChatColor.RED + "エラー");
            }
            message = String.valueOf(message) + name + " ";
            inv.addItem(new ItemStack[] { head });
            this.betinfo.playerlist.put(player.getName(), player.getUniqueId());
            b++;
        }
        Bukkit.broadcastMessage(String.valueOf(this.betinfo.prefix) + ChatColor.BOLD + "エントリー"+ message);
        this.betinfo.inv = inv;
        return true;
    }
}
