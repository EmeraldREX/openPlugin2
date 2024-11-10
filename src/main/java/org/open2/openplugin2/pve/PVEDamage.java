package org.open2.openplugin2.pve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BellRingEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.joml.Random;

public class PVEDamage implements Listener {
    PVEmain pvemain;

    int count = 0;

    public BukkitTask vglowTask;

    public BukkitTask glow;

    public PVEDamage(PVEmain m) {
        this.pvemain = m;
        m.m.getServer().getPluginManager().registerEvents(this, (Plugin)m.m);
    }

    @EventHandler
    public void onentitydamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            if (event.getEntity() instanceof Villager) {
                Villager villager = (Villager)event.getEntity();
                if (villager.getUniqueId().equals(this.pvemain.info.villager))
                    event.setDamage(2.0D);
            }
    }

    @EventHandler
    public void explode(BlockExplodeEvent e) {
        if (this.pvemain.info.villager != null)
            for (Block b : e.blockList()) {
                if (!this.pvemain.info.blocks.containsKey(b.getLocation()))
                    this.pvemain.info.blocks.put(b.getLocation(), new BlockInfo(b));
            }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Villager) {
            Entity damager = event.getDamager();
            Villager villager = (Villager)event.getEntity();
            if (villager.getUniqueId().equals(this.pvemain.info.villager)) {
                if (damager instanceof Arrow) {
                    Arrow arrow = (Arrow)damager;
                    if (arrow.getShooter() instanceof Player)
                        event.setCancelled(true);
                }
                if (damager instanceof Player) {
                    Player player = (Player)event.getDamager();
                    if (!player.isOp())
                        event.setCancelled(true);
                } else {
                    event.setDamage(1.0D);
                }
                villager.setGlowing(true);
                this.vglowTask = Bukkit.getScheduler().runTaskLater((Plugin)this.pvemain.m, () -> {
                    villager.setGlowing(false);
                    this.vglowTask.cancel();
                },60L);
            }
        }
        if (event.getDamager() instanceof org.bukkit.entity.Phantom &&
                this.pvemain.pvee.mobs.contains(event.getDamager().getUniqueId()))
            event.getDamager().getWorld().createExplosion(event.getDamager().getLocation(), 2.0F, true, true);
        boolean var10000 = event.getEntity() instanceof Player;
    }

    @EventHandler
    public void explode(ExplosionPrimeEvent e) {
        if (e.getEntity() instanceof org.bukkit.entity.Creeper &&
                e.getEntity().getName().equals("A"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onbell(BellRingEvent event) {
        if (this.pvemain.pvee.mobs.size() != 0) {
            List<UUID> uids = new ArrayList<>();
            String message = "";
            for (UUID uid : this.pvemain.pvee.mobs) {
                Entity e = Bukkit.getEntity(uid);
                if (e != null) {
                    e.setGlowing(true);
                    if (this.pvemain.pvee.mobs.size() < 5) {
                        Location loc = e.getLocation().getBlock().getLocation();
                        message = String.valueOf(message) + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "|";
                    }
                    continue;
                }
                uids.add(uid);
            }
            for (Player pl : this.pvemain.info.pl)
                sendActionBar(pl, message);
            for (UUID uid : uids)
                this.pvemain.pvee.removeentity(uid);
            this.glow = Bukkit.getScheduler().runTaskLater((Plugin)this.pvemain.m, () -> {
                for (UUID uid : this.pvemain.pvee.mobs) {
                    Entity e = Bukkit.getEntity(uid);
                    if (e != null)
                        e.setGlowing(false);
                }
                this.glow.cancel();
            },160L);
        }
    }

    @EventHandler
    public void onDead(EntityDeathEvent e) {
        LivingEntity livingEntity = e.getEntity();
        if (this.pvemain.pvee.drops.containsKey(livingEntity.getUniqueId())) {
            e.getDrops().clear();
            Material m = this.pvemain.pvee.drops.get(livingEntity.getUniqueId());
            HashMap<Material, Integer> drops = this.pvemain.info.droppingitems;
            if (drops.containsKey(m)) {
                this.pvemain.info.droppingitems.put(m, Integer.valueOf(((Integer)drops.get(m)).intValue() + 1));
            } else {
                this.pvemain.info.droppingitems.put(m, Integer.valueOf(1));
            }
            this.pvemain.pvee.removeentity(livingEntity.getUniqueId());
        }
        if (this.pvemain.info.villager != null)
            if (livingEntity.getUniqueId().equals(this.pvemain.info.villager)) {
                this.pvemain.info.villagerhealth.setTitle(ChatColor.YELLOW + "-敗北-");
                this.pvemain.info.villagerhealth.setProgress(0.0D);
                for (Player p : this.pvemain.info.pl) {
                    p.playSound((Entity)p, Sound.ENTITY_VILLAGER_DEATH, 20.0F, 1.0F);
                    p.sendMessage(ChatColor.YELLOW + livingEntity.getCustomName() + "が死亡");
                }
                final Villager villager = (Villager)Bukkit.getEntity(this.pvemain.info.villager);
                (new BukkitRunnable() {
                    public void run() {
                        if (PVEDamage.this.count < 20) {
                            PVEDamage.this.launchRandomFirework(villager);
                            PVEDamage.this.count++;
                        } else {
                            PVEDamage.this.count = 20;
                            cancel();
                        }
                    }
                }).runTaskTimer((Plugin)this.pvemain.m, 0L, 4L);
            }
    }

    public void launchRandomFirework(Villager villager) {
        Firework firework = (Firework)villager.getWorld().spawn(villager.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Type[] types = FireworkEffect.Type.values();
        FireworkEffect.Type randomType = types[(new Random()).nextInt(types.length)];
        int red = (new Random()).nextInt(256);
        int green = (new Random()).nextInt(256);
        int blue = (new Random()).nextInt(256);
        Color randomColor = Color.fromRGB(red, green, blue);
        FireworkEffect effect = FireworkEffect.builder().withColor(randomColor).with(randomType).trail(true).build();
        meta.addEffect(effect);
        meta.setPower((new Random()).nextInt(50) / 10 + 5);
        firework.setFireworkMeta(meta);
    }

    private void sendActionBar(Player p, String message) {
        TextComponent textComponent = new TextComponent(ChatColor.YELLOW + message);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent)textComponent);
    }
}
