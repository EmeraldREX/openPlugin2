//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.yakiu;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YakiuCommand implements CommandExecutor, TabCompleter {
    int strike = 0;
    int strikemax = 3;
    int ball = 0;
    int ballmax = 4;
    int out = 0;
    int outmax = 3;
    int round = 2;
    int roundmax = 19;
    int point1 = 0;
    int point2 = 0;
    String teamname1 = "A";
    String teamname2 = "B";
    String yakiu;
    List<String> list;
    boolean[] base;

    public YakiuCommand() {
        this.yakiu = "" + ChatColor.WHITE + this.point1 + "-" + this.point2;
        this.list = new ArrayList();
        this.base = new boolean[3];
        this.list.add("setup");
        this.list.add("reset");
        this.list.add("s");
        this.list.add("b");
        this.list.add("o");
        this.list.add("r");
        this.list.add("c");
        this.list.add("t1");
        this.list.add("t2");
        this.list.add("base");
        this.reset();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            Integer i = 1;
            if (args.length > 1) {
                try {
                    i = Integer.parseInt(args[1]);
                } catch (Exception var7) {
                }
            }

            String change = args[0];
            if (change.equalsIgnoreCase("setup")) {
                this.strike = 0;
                this.ball = 0;
                this.out = 0;
                this.round = 2;
                if (args.length > 2) {
                    this.teamname1 = args[1];
                    this.teamname2 = args[2];
                }
            } else {
                if (change.equalsIgnoreCase("reset")) {
                    this.reset();
                    return true;
                }

                if (change.equalsIgnoreCase("s")) {
                    this.strike += i;
                    if (this.strike >= this.strikemax) {
                        this.scoreset();
                        ++this.out;
                    }

                    if (this.out >= this.outmax) {
                        this.out = 0;
                        ++this.round;
                    }
                } else if (change.equalsIgnoreCase("b")) {
                    this.ball += i;
                    if (this.ball >= this.ballmax) {
                        this.scoreset();
                    }
                } else if (change.equalsIgnoreCase("o")) {
                    this.out += i;
                    this.scoreset();
                    if (this.out >= this.outmax) {
                        this.scoreset();
                        this.out = 0;
                        this.base[0] = false;
                        this.base[1] = false;
                        this.base[2] = false;
                        ++this.round;
                    }
                } else if (change.equalsIgnoreCase("r")) {
                    this.round += i;
                    this.scoreset();
                } else if (change.equalsIgnoreCase("c")) {
                    this.scoreset();
                } else if (change.equalsIgnoreCase("t1")) {
                    System.out.println("  " + i);
                    this.point1 += i;
                    this.scoreset();
                } else if (change.equalsIgnoreCase("t2")) {
                    this.point2 += i;
                    this.scoreset();
                } else if (change.equalsIgnoreCase("base")) {
                    i = i - 1;
                    this.base[i] = !this.base[i];
                }
            }
        }

        this.setup();
        return true;
    }

    private void scoreset() {
        this.strike = 0;
        this.ball = 0;
    }

    private void remove() {
        Iterator var2 = Bukkit.getScoreboardManager().getMainScoreboard().getObjectives().iterator();

        while(var2.hasNext()) {
            Objective objective = (Objective)var2.next();
            objective.unregister();
        }

    }

    private void reset() {
        this.point1 = 0;
        this.point2 = 0;
        this.strike = 0;
        this.ball = 0;
        this.out = 0;
        this.round = 2;
        this.teamname1 = "A";
        this.teamname2 = "B";
        this.remove();
    }

    private void setup() {
        this.remove();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective(this.yakiu);
        if (objective == null) {
            objective = scoreboard.registerNewObjective(this.yakiu, this.yakiu, this.yakiu);
        }

        String s = " ";
        String s2 = "";
        int count = 0;
        boolean[] var9;
        int var8 = (var9 = this.base).length;

        for(int var7 = 0; var7 < var8; ++var7) {
            boolean b = var9[var7];
            if (count == 0) {
                if (b) {
                    s2 = ChatColor.RED + "◆";
                } else {
                    s2 = ChatColor.WHITE + "◇";
                }
            } else if (count == 1) {
                if (b) {
                    s = "    " + ChatColor.RED + "◆";
                } else {
                    s = "    " + ChatColor.WHITE + "◇";
                }
            } else if (b) {
                s2 = "   " + ChatColor.RED + "◆ " + s2;
            } else {
                s2 = "   " + ChatColor.WHITE + "◇ " + s2;
            }

            ++count;
        }

        this.onobject(s, 6);
        this.onobject(s2, 5);
        if (this.round % 2 == 0) {
            this.onobject(ChatColor.RED + " |" + ChatColor.WHITE + this.teamname1 + "  " + this.point1, 4);
            this.onobject(ChatColor.WHITE + " |" + this.teamname2 + "  " + this.point2, 3);
            objective.setDisplayName("" + ChatColor.WHITE + this.round / 2 + "回" + ChatColor.GOLD + "表");
        } else {
            this.onobject(ChatColor.WHITE + " |" + this.teamname1 + "  " + this.point1, 4);
            this.onobject(ChatColor.RED + " |" + ChatColor.WHITE + this.teamname2 + "  " + this.point2, 3);
            objective.setDisplayName("" + ChatColor.WHITE + this.round / 2 + "回" + ChatColor.GRAY + "裏");
        }

        this.getcount(this.ball, this.ballmax, ChatColor.GREEN, 2);
        this.getcount(this.strike, this.strikemax, ChatColor.YELLOW, 1);
        this.getcount(this.out, this.outmax, ChatColor.RED, 0);
        Iterator var11 = Bukkit.getOnlinePlayers().iterator();

        while(var11.hasNext()) {
            Player player = (Player)var11.next();
            player.setScoreboard(scoreboard);
        }

    }

    private String getcount(Integer i, int max, ChatColor color, int c) {
        String ans = " ";

        int count;
        for(count = 0; count < i; ++count) {
            ans = ans + "■";
        }

        for(count = 0; count < max - i - 1; ++count) {
            ans = ans + "□";
        }

        for(count = 0; count < 4 - max; ++count) {
            ans = ans + "　";
        }

        ans = color + ans;
        this.onobject(ans, c);
        return this.yakiu;
    }

    private void onobject(String ans, int c) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective(this.yakiu);
        if (objective == null) {
            objective = scoreboard.registerNewObjective(this.yakiu, this.yakiu, ans);
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(ans).setScore(c);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return this.list;
    }
}
