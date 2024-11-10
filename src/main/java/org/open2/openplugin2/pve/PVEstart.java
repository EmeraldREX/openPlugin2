package org.open2.openplugin2.pve;

import net.md_5.bungee.api.ChatColor;
import org.open2.openplugin2.pve.mob.PVESpawnmob;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

public class PVEstart implements CommandExecutor {
    PVEmain m;

    PVESpawnmob spawn;

    public PVEstart(PVEmain m) {
        this.m = m;
        this.spawn = new PVESpawnmob(m);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (this.m.info.villager != null) {
            sender.sendMessage("PVEは既に始まっています");
            return true;
        }
        (new PVEDisable()).disable(this.m);
        if (this.m.config.contains("pve.spawn")) {
            this.m.info.pvecenter = this.m.config.getLocation("pve.spawn");
        } else {
            sender.sendMessage("座標がせっとされていませんn /pvespawn");
            return true;
        }
        Location loc = this.m.info.pvecenter;
        loc.getWorld().setGameRule(GameRule.MOB_GRIEFING, Boolean.valueOf(false));
        Bukkit.broadcastMessage(ChatColor.YELLOW + "PVEが"+ loc.getWorld().getName() + "X" + loc.getX() + " Y" +
                loc.getY() + " Z" + loc.getZ() + "で始まります");
                LivingEntity mob = (LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        Villager villager = (Villager)mob;
        mob.setAI(false);
        villager.setCustomName("矢野さとる");
                villager.setCustomNameVisible(true);
        villager.setHealth(20.0D);
        this.m.info.villager = villager.getUniqueId();
        BossBar villagerHealthBar = Bukkit.createBossBar("Villager Health", BarColor.GREEN, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
        villagerHealthBar.setVisible(true);
        double maxHealth = villager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double currentHealth = villager.getHealth();
        double progress = currentHealth / maxHealth;
        villagerHealthBar.setProgress(progress);
        this.m.info.villagerhealth = villagerHealthBar;
        return true;
    }
}
