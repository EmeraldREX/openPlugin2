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
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;
import java.util.Random;

public class Quiz implements CommandExecutor {
    Main m;
    int delay;
    String message;
    Random random = new Random();
    String sendmessage;
    int currentIndex;
    String copymessage;
    Random r = new Random();
    int wait;
    int count = 2;
    boolean stop;
    String p;

    public Quiz(Main m) {
        this.m = m;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return true;
        } else if (args[0].equalsIgnoreCase("stop")) {
            this.stop = false;
            return true;
        } else if (args[0].equalsIgnoreCase("start")) {
            this.stop = true;
            return true;
        } else {
            final Player p;
            if (args[0].equalsIgnoreCase("perm")) {
                p = Bukkit.getPlayer(args[1]);
                if (p != null) {
                    this.p = p.getName();
                    Bukkit.broadcastMessage("出題者 :" + args[1]);
                    this.setPlayerPrefix(p);
                }

                return true;
            } else if (!(sender instanceof Player)) {
                return true;
            } else {
                p = (Player)sender;
                if (!p.isOp() && !this.p.equals(p.getName())) {
                    p.sendMessage(ChatColor.RED + "出題者ではありません");
                    return true;
                } else {
                    this.message = "";
                    this.delay = 4;
                    this.wait = 0;
                    this.sendmessage = "";
                    this.currentIndex = 0;
                    this.stop = true;
                    this.copymessage = "";
                    String[] var9 = args;
                    int var8 = args.length;

                    for(int var7 = 0; var7 < var8; ++var7) {
                        String string = var9[var7];
                        string = string;
                        this.message = this.message + " " + string;
                    }

                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.3F, 1.5F);
                    this.count = 1;
                    (new BukkitRunnable() {
                        public void run() {
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0F, 1.0F);
                            if (Quiz.this.count == 0) {
                                this.cancel();
                            } else {
                                --Quiz.this.count;
                            }

                        }
                    }).runTaskTimer(this.m, 2L, 2L);
                    final Location loc = p.getLocation();
                    (new BukkitRunnable() {
                        public void run() {
                            if (Quiz.this.stop) {
                                if (Quiz.this.wait < 1) {
                                    if (Quiz.this.currentIndex < Quiz.this.message.length()) {
                                        char currentChar = Quiz.this.message.charAt(Quiz.this.currentIndex);
                                        Quiz var10000 = Quiz.this;
                                        var10000.sendmessage = var10000.sendmessage + currentChar;
                                        Quiz.this.sendActionBar(Quiz.this.sendmessage, loc);
                                        ++Quiz.this.currentIndex;
                                        if (Quiz.this.message.length() > Quiz.this.currentIndex && (Quiz.this.message.charAt(Quiz.this.currentIndex) == ' ' || Quiz.this.message.charAt(Quiz.this.currentIndex) == 12289)) {
                                            Bukkit.broadcastMessage("[" + ChatColor.YELLOW + "問題" + ChatColor.WHITE + "] :" + Quiz.this.sendmessage);
                                            Quiz.this.wait = 10 + Quiz.this.r.nextInt(10);
                                            Quiz.this.copymessage = Quiz.this.sendmessage;
                                            Quiz.this.sendmessage = "";
                                        }
                                    } else {
                                        Bukkit.broadcastMessage("[" + ChatColor.YELLOW + "問題" + ChatColor.WHITE + "] :" + Quiz.this.sendmessage);
                                        this.cancel();
                                    }
                                } else {
                                    Quiz.this.sendActionBar(Quiz.this.copymessage, loc);
                                    --Quiz.this.wait;
                                }
                            } else {
                                Quiz.this.sendActionBar(Quiz.this.sendmessage, loc);
                            }

                        }
                    }).runTaskTimer(this.m, 0L, (long)this.delay);
                    return true;
                }
            }
        }
    }

    private void sendActionBar(String message, Location loc) {
        TextComponent textComponent = new TextComponent(ChatColor.YELLOW + message);
        Iterator var5 = Bukkit.getOnlinePlayers().iterator();

        while(var5.hasNext()) {
            Player p = (Player)var5.next();
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
        }

    }

    public void sendTitleMessage(String title) {
        Iterator var3 = Bukkit.getOnlinePlayers().iterator();

        while(var3.hasNext()) {
            Player player = (Player)var3.next();
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', title), (String)null, 0, this.delay, this.delay);
        }

    }

    private void setPlayerPrefix(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("Quiz");
        if (team != null) {
            team.unregister();
        }

        team = scoreboard.registerNewTeam("Quiz");
        team.setPrefix(ChatColor.WHITE + "[" + ChatColor.YELLOW + "出題者" + ChatColor.WHITE + "]");
        team.addEntry(player.getName());
    }
}
