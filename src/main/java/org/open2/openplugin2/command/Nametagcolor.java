//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Nametagcolor implements CommandExecutor {
    public Nametagcolor() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item == null) {
                return false;
            }

            List<String> list = new ArrayList();
            list.add(player.getName());
            ItemMeta meta = item.getItemMeta();
            if (meta.getLore() == null) {
                meta.setLore(list);
                item.setItemMeta(meta);
            } else {
                meta.setLore((List)null);
            }

            player.getInventory().setItemInMainHand(item);
        }

        return false;
    }
}
