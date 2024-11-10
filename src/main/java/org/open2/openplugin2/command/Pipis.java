//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Iterator;

public class Pipis implements CommandExecutor {
    private boolean start = false;
    private int taskId;
    private Scoreboard scoreboard;
    private Objective objective;
    Main m;

    public Pipis(Main m) {
        this.m = m;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (this.start) {
            this.start = false;
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.objective.unregister();
        } else {
            this.start = true;
            this.setupscoreboard();
            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.m, new Runnable() {
                public void run() {
                    Iterator var2;
                    if (Pipis.this.objective != null) {
                        var2 = Pipis.this.scoreboard.getEntries().iterator();

                        while(var2.hasNext()) {
                            String entry = (String)var2.next();
                            Pipis.this.objective.getScoreboard().resetScores(entry);
                        }
                    }

                    var2 = Bukkit.getOnlinePlayers().iterator();

                    while(var2.hasNext()) {
                        Player player = (Player)var2.next();
                        if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                            Pipis.this.setScore(player, (int)player.getLocation().getY());
                        }
                    }

                }
            }, 0L, 10L);
        }

        return true;
    }

    private void setScore(Player player, int score) {
        Score playerScore = this.objective.getScore(player.getName());
        playerScore.setScore(score);
    }

    private void setupscoreboard() {
        this.objective = this.scoreboard.getObjective("Pipis");
        if (this.objective != null) {
            this.objective.unregister();
        }

        this.objective = this.scoreboard.registerNewObjective("Pipis", "Pipis");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
}
