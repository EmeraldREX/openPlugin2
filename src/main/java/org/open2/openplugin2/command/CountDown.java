//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.open2.openplugin2.net.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class CountDown implements CommandExecutor {
    HashMap<UUID, Countdownl> map = new HashMap();
    boolean toggele = false;

    public CountDown() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Location loc = null;
        if (args[0].equals("toggle")) {
            this.toggele = !this.toggele;
            return true;
        } else {
            if (sender instanceof BlockCommandSender) {
                BlockCommandSender cmd = (BlockCommandSender)sender;
                loc = cmd.getBlock().getLocation();
            } else {
                if (!(sender instanceof Player)) {
                    return true;
                }

                loc = ((Player)sender).getLocation();
            }

            Player p;
            if (args[0].equals("@p")) {
                p = this.getNearestPlayer(loc);
            } else {
                p = Bukkit.getPlayer(args[0]);
            }

            UUID uuid = p.getUniqueId();
            if (args[1].equals("stop")) {
                if (this.map.containsKey(uuid)) {
                    ((Countdownl)this.map.get(uuid)).task.cancel();
                }

                return true;
            } else {
                if (this.map.containsKey(uuid)) {
                    ((Countdownl)this.map.get(uuid)).task.cancel();
                }

                this.map.put(uuid, new Countdownl(Double.parseDouble(args[1])));
                this.startCountdown(p, uuid, loc.getBlockY() - 2, loc);
                return true;
            }
        }
    }

    private void startCountdown(final Player p, final UUID uuid, final int y, final Location loc) {
        Countdownl c = (Countdownl)this.map.get(uuid);
        c.time = c.maxtime;
        BukkitTask task = (new BukkitRunnable() {
            public void run() {
                if (!p.isOnline()) {
                    this.cancel();
                }

                if (p.getLocation().getY() < (double)y) {
                    loc.setY(loc.getY() + 2.0);
                    loc.setZ(loc.getZ() - 1.0);
                    this.cancel();
                }

                Countdownl c = (Countdownl)CountDown.this.map.get(uuid);
                double time = c.time;
                if (time < 0.1) {
                    ((Countdownl)CountDown.this.map.get(uuid)).task.cancel();
                }

                if (time < 20.0) {
                    CountDown.this.sendActionBar(p, ChatColor.RED + String.format("%.1f", time));
                } else {
                    CountDown.this.sendActionBar(p, ChatColor.YELLOW + String.format("%.1f", time));
                }

                time -= 0.1;
                c.time = time;
                CountDown.this.map.put(uuid, c);
            }
        }).runTaskTimer(Main.m, 0L, 2L);
        c.task = task;
        this.map.put(uuid, c);
    }

    private void sendActionBar(Player p, String message) {
        TextComponent textComponent = new TextComponent(message);
        if (this.toggele) {
            Iterator var5 = Bukkit.getOnlinePlayers().iterator();

            while(var5.hasNext()) {
                Player pl = (Player)var5.next();
                pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
            }
        } else {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
        }

    }

    public Player getNearestPlayer(Location loc) {
        Player nearestPlayer = null;
        double nearestDistanceSquared = Double.MAX_VALUE;
        Iterator var6 = Bukkit.getOnlinePlayers().iterator();

        while(var6.hasNext()) {
            Player player = (Player)var6.next();
            if (player.getWorld().equals(loc.getWorld())) {
                double distanceSquared = loc.distanceSquared(player.getLocation());
                if (distanceSquared < nearestDistanceSquared) {
                    nearestDistanceSquared = distanceSquared;
                    nearestPlayer = player;
                }
            }
        }

        return nearestPlayer;
    }
}
