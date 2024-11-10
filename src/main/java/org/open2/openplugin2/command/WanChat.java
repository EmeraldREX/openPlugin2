//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WanChat implements CommandExecutor {
    public static List<UUID> list = new ArrayList();

    public WanChat() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            UUID uuid = ((Player)sender).getUniqueId();
            if (list.contains(uuid)) {
                player.sendMessage("ワンチャットオフ");
                list.remove(uuid);
            } else {
                player.sendMessage("ワンチャットオン");
                list.add(uuid);
            }
        }

        return false;
    }
}
