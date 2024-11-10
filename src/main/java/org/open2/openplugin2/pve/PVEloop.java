package org.open2.openplugin2.pve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.open2.openplugin2.pve.mob.PVESpawnmob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PVEloop {
    boolean isgameend = false;

    Integer count = Integer.valueOf(15);

    Integer count2 = Integer.valueOf(10);

    boolean setup = true;

    int wave = 0;

    int bosscount = 0;

    final UUID uid;

    int maxwave = 4;

    final MobListner moblistner;

    final Location center;

    final double radius = 100.0D;

    Integer maxcount = this.count;

    public PVEloop(final PVEmain m, final PVESpawnmob spawn) {
        this.moblistner = new MobListner(m.pvee, m.info, m);
        this.center = m.info.pvecenter;
        this.uid = m.info.uid;
        m.info.pl = getdistance(100.0D, this.center);
        for (Player p : m.info.pl)
            p.playSound((Entity)p, Sound.ENTITY_CHICKEN_AMBIENT, 20.0F, 1.0F);
        (new BukkitRunnable() {
            public void run() {
                List<Player> pl = PVEloop.this.getdistance(100.0D, PVEloop.this.center);
                BossBar villagerbar = m.info.villagerhealth;
                if (!(Bukkit.getEntity(m.info.villager) instanceof Villager) &&
                        !villagerbar.getTitle().equals(ChatColor.RED + "-敗北-")) {
                    villagerbar.setTitle(ChatColor.RED + "-敗北-");
                    PVEloop.this.isgameend = true;
                    villagerbar.setProgress(0.0D);
                    for (Player p : pl) {
                        p.playSound((Entity)p, Sound.ENTITY_VILLAGER_DEATH, 0.5F, 1.0F);
                        p.sendMessage(ChatColor.YELLOW + "矢野さとるが死亡");
                                p.sendMessage(ChatColor.YELLOW + "最終到達レベル" + m.info.phase);
                    }
                }
                if (!PVEloop.this.uid.equals(m.info.uid)) {
                    m.pvedisable.disable(m);
                    cancel();
                    return;
                }
                if (m.pvee.mobs.size() < 15 && PVEloop.this.wave > 0 && !PVEloop.this.setup) {
                    int repeatCount = PVEloop.this.maxwave;
                    for (int i = 0; i < repeatCount; i++)
                        spawn.spawnmob(false);
                    PVEloop.this.wave -= PVEloop.this.maxwave;
                    if (PVEloop.this.wave <= 0) {
                        PVEloop.this.bosscount++;
                        if (PVEloop.this.bosscount % 4 == 0)
                            spawn.spawnmob(true);
                    }
                } else if (PVEloop.this.setup) {
                    double progress = PVEloop.this.count.intValue() / PVEloop.this.maxcount.intValue();
                    villagerbar.setProgress(progress);
                    villagerbar.setTitle(ChatColor.YELLOW + "- -");
                    HashMap<Material, Integer> droppingitems = m.info.droppingitems;
                    for (Map.Entry<Material, Integer> entry : droppingitems.entrySet())
                        droppingitems.put(entry.getKey(), Integer.valueOf(((Integer)entry.getValue()).intValue() / 3));
                    m.info.droppingitems = new HashMap<>();
                    if (PVEloop.this.count.intValue() == 0) {
                        PVEloop.this.setup = false;
                        m.info.phase = Integer.valueOf(m.info.phase.intValue() + 10);
                        PVEloop.this.maxwave = 4 + m.info.phase.intValue() / 30;
                        int repeatCount = m.info.phase.intValue() / 3 + 2;
                        if (repeatCount < 4) {
                            for (int i = 0; i < repeatCount; i++)
                                spawn.spawnmob(false);
                        } else {
                            PVEloop.this.wave = repeatCount - PVEloop.this.maxwave;
                            repeatCount = PVEloop.this.maxwave;
                            for (int i = 0; i < repeatCount; i++)
                                spawn.spawnmob(false);
                        }
                        PVEloop.this.count = PVEloop.this.maxcount;
                    }
                    PVEloop.this.count = Integer.valueOf(PVEloop.this.count.intValue() - 1);
                } else if (PVEloop.this.isgameend) {
                    double progress = PVEloop.this.count.intValue() / PVEloop.this.maxcount.intValue();
                    villagerbar.setProgress(progress);
                    PVEloop.this.count = Integer.valueOf(PVEloop.this.count.intValue() - 1);
                    if (PVEloop.this.count.intValue() == 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            villagerbar.removePlayer(p);
                            villagerbar.removeAll();
                        }
                        for (UUID uid : m.pvee.mobs) {
                            Entity entity = Bukkit.getEntity(uid);
                            if (entity != null)
                                entity.remove();
                        }
                        m.pvedisable.disable(m);
                        cancel();
                        return;
                    }
                } else if (villagerbar.getTitle().equals(ChatColor.YELLOW + "- -")) {
                    villagerbar.setTitle(ChatColor.RED + "- -");
                    PVEloop.this.isgameend = true;
                } else if (m.pvee.mobs.size() < 4) {
                    for (Player p : pl)
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0F, 10.0F);
                    PVEloop.this.setup = true;
                } else {
                    Villager villager = (Villager)Bukkit.getEntity(m.info.villager);
                    double maxHealth = villager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double currentHealth = villager.getHealth();
                    double progress = currentHealth / maxHealth;
                    villagerbar.setProgress(progress);
                    villagerbar.setTitle(
                            ChatColor.DARK_PURPLE + "-- + m.pvee.mobs.size() + "+ m.info.phase + " --");
                    PVEloop.this.moblistner.mobgrief();
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (pl.contains(p)) {
                        villagerbar.addPlayer(p);
                        continue;
                    }
                    villagerbar.removePlayer(p);
                }
                for (UUID uid : m.pvee.mobs) {
                    Entity entity = Bukkit.getEntity(uid);
                    if (entity != null &&
                            !entity.isDead())
                        m.pvee.settarget(entity);
                }
            }
        }).runTaskTimer((Plugin)m.m, 20L, 20L);
    }

    private List<Player> getdistance(double radius, Location center) {
        List<Player> pl = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (center.getWorld().equals(p.getWorld()) &&
                    center.distance(p.getLocation()) <= radius)
                pl.add(p);
        }
        return pl;
    }
}
