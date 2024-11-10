//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Freeze implements CommandExecutor, Listener {
    Main m;
    List<UUID> uuids = new ArrayList();

    public Freeze(Main m) {
        this.m = m;
        m.getServer().getPluginManager().registerEvents(this, m);
    }

    @EventHandler
    public void onclick(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (this.contains(player.getUniqueId())) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onclick(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (this.contains(player.getUniqueId())) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onclick(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (this.contains(player.getUniqueId())) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onclick(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (this.contains(player.getUniqueId())) {
            e.setCancelled(true);
        }

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length > 0) {
            String s = args[0];
            Player p2 = Bukkit.getPlayer(s);
            if (p2 != null && !p2.isOp()) {
                UUID uuid = p2.getUniqueId();
                if (this.uuids.contains(uuid)) {
                    sender.sendMessage(s + "の停止を解除");
                    this.uuids.remove(uuid);
                } else {
                    sender.sendMessage(s + "の動きを停止");
                    this.uuids.add(uuid);
                }
            }
        }

        return true;
    }

    private boolean contains(UUID uuid) {
        return this.uuids.contains(uuid);
    }
}
