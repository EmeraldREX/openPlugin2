package org.open2.openplugin2.net;

import org.bukkit.event.Listener;
import org.open2.openplugin2.command.Back;
import org.open2.openplugin2.command.CountDown;
import org.open2.openplugin2.command.Freeze;
import org.open2.openplugin2.command.Help;
import org.open2.openplugin2.command.Nametagcolor;
import org.open2.openplugin2.command.Pipis;
import org.open2.openplugin2.command.Quiz;
import org.open2.openplugin2.command.Setting;
import org.open2.openplugin2.command.StartHaihu;
import org.open2.openplugin2.command.TopEntity;
import org.open2.openplugin2.command.WanChat;
import org.open2.openplugin2.command.bet.*;
import org.open2.openplugin2.mcmusic.Otoge;
import org.open2.openplugin2.music.NoteCommand;
import org.open2.openplugin2.pve.PVEmain;
import org.open2.openplugin2.yakiu.YakiuCommand;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class Setup {
    private FileConfiguration config;

    private ConfigLoader configloader;

    private World sigen;

    public Setup(Main m) {
        try {
            config(m);
            world();
            command(m);
            event(m);
            bet(m);
            m.pve = new PVEmain(m, this.config);

            try {

            } catch (Exception exception) {}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void config(Main m) {
        this.config = m.getConfig();
        m.saveDefaultConfig();
        this.configloader = new ConfigLoader(m, this.config, this.sigen);
    }

    private void command(Main m) {
        m.getCommand("pipis").setExecutor((CommandExecutor)new Pipis(m));
        m.getCommand("c").setExecutor((CommandExecutor)new Back(m));
        m.getCommand("setumei").setExecutor((CommandExecutor)new Help());
        m.getCommand("setting").setExecutor((CommandExecutor)new Setting(m));
        m.getCommand("haihu").setExecutor((CommandExecutor)new StartHaihu());
        m.getCommand("quiz").setExecutor((CommandExecutor)new Quiz(m));
        m.getCommand("soundp").setExecutor((CommandExecutor)new NoteCommand());
        m.getCommand("countdown").setExecutor((CommandExecutor)new CountDown());
        m.getCommand("topentities").setExecutor((CommandExecutor)new TopEntity());
        m.getCommand("tag").setExecutor((CommandExecutor)new Nametagcolor());
        m.getCommand("yakiu").setExecutor((CommandExecutor)new YakiuCommand());
        m.getCommand("freeze").setExecutor((CommandExecutor)new Freeze(m));
        m.getCommand("mcgame").setExecutor((CommandExecutor)new Otoge(m));
        m.getCommand("wanchat").setExecutor((CommandExecutor)new WanChat());
    }

    private void bet(Main m) {
        Betinfo betinfo = new Betinfo();
        m.getCommand("entry").setExecutor((CommandExecutor)new Entry(betinfo));
        m.getCommand("bet").setExecutor((CommandExecutor)new Bet(betinfo));
        m.getCommand("goal").setExecutor((CommandExecutor)new Goal(betinfo));
    }

    private void event(Main m) {
        try {
            // Event Listenerの登録
            Bukkit.getPluginManager().registerEvents((Listener) new Setting(m), m);
            // 他のリスナーも登録
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void world() {
        World w = Bukkit.getServer().getWorld("World");
        WorldBorder wb = w.getWorldBorder();
        w.setDifficulty(Difficulty.HARD);
        wb.setCenter(0.0D, 0.0D);
        wb.setSize(10000.0D);
        System.out.println("World Loaded");
        for (World ws : Bukkit.getWorlds())
            ws.setGameRule(GameRule.KEEP_INVENTORY, Boolean.valueOf(true));
    }
}
