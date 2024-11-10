package org.open2.openplugin2.net;

import net.md_5.bungee.api.ChatColor;

import org.open2.openplugin2.event.*;
import org.open2.openplugin2.pve.PVEmain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.open2.openplugin2.race.Race;


public class Main extends JavaPlugin {
    public static Main m;

    public Location capondes;
     Setup setup;

    public PVEmain pve = null;
    public void onEnable() {
        m = this;
        try{
        getServer().getMessenger().registerOutgoingPluginChannel((Plugin) this, "BungeeCord");
        System.out.println(ChatColor.GREEN + "Open Loaded");
        for (Player p : Bukkit.getOnlinePlayers())
            p.sendMessage(ChatColor.GREEN + "リロード完了");
        new Scale(this);
        setup = new Setup(this); // Setupクラスの初期化

        getServer().getPluginManager().registerEvents(new Race(this),this);
            getServer().getPluginManager().registerEvents(new Recipi(this),this);
        new BoostMinecart(this, this.getConfig());
        getServer().getPluginManager().registerEvents(new Shulker(this), this);
            }catch (Exception e) {
            getLogger().severe("プラグインの有効化中にエラーが発生しました: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onDisable() {
        System.out.println(ChatColor.RED + "Open Disable");
        Bukkit.broadcastMessage(ChatColor.GREEN + "リロード中・・・");
        if (this.pve != null)
            this.pve.disable();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("Quiz");
        if (team != null)
            team.unregister();
    }
}