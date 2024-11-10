package org.open2.openplugin2.pve;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PVEcenter implements CommandExecutor {
    PVEmain m;

    public PVEcenter(PVEmain m) {
        this.m = m;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーによって実行する必要があります。");
            return true;
        }
        Player player = (Player)sender;
        Location location = player.getLocation().getBlock().getLocation();
        this.m.config.set("pve.spawn", location);
        this.m.m.saveConfig();
        this.m.info.pvecenter = location;
        player.sendMessage("PVEのスポーンを設定しました" + location.getX() + ", " + location.getBlockY() + ", " +
                location.getBlock().getZ());
        return true;
    }
}
