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
            sender.sendMessage(String.valueOf(this.betinfo.prefix) + ChatColor.RED + "現在は出来ません");
            return true;
        }
        if (this.betinfo.playerlist.size() == 0) {
            sender.sendMessage(String.valueOf(this.betinfo.prefix) + ChatColor.RED + "準備ができていません");
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player)sender;
            player.openInventory(this.betinfo.inv);
        }
        return false;
    }
}
