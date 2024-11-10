//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command.bet;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bet implements CommandExecutor {
    Betinfo betinfo;

    public Bet(Betinfo betinfo) {
        this.betinfo = betinfo;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!this.betinfo.toggle) {
            sender.sendMessage(this.betinfo.prefix + ChatColor.RED + "現在はできません");
            return true;
        } else if (this.betinfo.playerlist.size() == 0) {
            sender.sendMessage(this.betinfo.prefix + ChatColor.RED + "準備ができていません");
            return true;
        } else {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                player.openInventory(this.betinfo.inv);
            }

            return false;
        }
    }
}
