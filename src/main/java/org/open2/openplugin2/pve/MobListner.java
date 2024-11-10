package org.open2.openplugin2.pve;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class MobListner {
    PVEentity pvee;

    PVEInfo info;

    PVEmain m;

    BukkitTask place = null;

    List<Material> types = new ArrayList<>();

    public MobListner(PVEentity e, PVEInfo info, PVEmain m) {
        this.info = info;
        this.m = m;
        this.pvee = e;
        this.types.add(Material.AIR);
        this.types.add(Material.LAVA);
        this.types.add(Material.WATER);
        this.types.add(Material.BEDROCK);
        this.types.add(Material.CHEST);
        this.types.add(Material.SHULKER_BOX);
    }

    public void mobgrief() {
        Location villager = this.info.pvecenter;
        int expoled = this.info.phase.intValue() / 10;
        if (expoled == 0)
            expoled = 1;
        for (UUID uid : this.pvee.chickin.keySet()) {
            Entity entity = Bukkit.getEntity(uid);
            if (entity != null) {
                Location loc = entity.getLocation();
                Integer i = this.pvee.chickin.get(uid);
                if (i.intValue() > 5) {
                    Entity fireball = entity.getWorld().spawnEntity(loc, EntityType.FIREBALL);
                    loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 2.0F, 2.0F);
                    fireball.getLocation().setDirection(villager.toVector().subtract(loc.toVector()));
                    fireball.setVelocity(fireball.getLocation().getDirection().multiply(1.8D));
                    this.pvee.chickin.put(uid, Integer.valueOf(0));
                    continue;
                }
                this.pvee.chickin.put(uid, Integer.valueOf(i.intValue() + 1));
            }
        }
        for (UUID uid : this.pvee.tnt.keySet()) {
            Entity entity = Bukkit.getEntity(uid);
            if (entity != null) {
                Location loc = entity.getLocation();
                Integer i = ((Moblocation)this.pvee.tnt.get(uid)).setint(loc);
                if (i.intValue() > 6) {
                    loc.getWorld().createExplosion(loc, expoled, true, true);
                    entity.remove();
                    this.pvee.removeentity(uid);
                }
            }
        }
        for (UUID uid : this.pvee.kohei.keySet()) {
            Entity entity = Bukkit.getEntity(uid);
            if (entity != null) {
                Location loc = entity.getLocation();
                Integer i = ((Moblocation)this.pvee.kohei.get(uid)).setint(loc);
                if (i.intValue() > 3) {
                    if (isblock(loc, true)) {
                        loc.setY(loc.getY() + 1.0D);
                        fix(loc.getBlock());
                    } else {
                        loc = loc.add(loc.getDirection());
                        if (isblock(loc, true)) {
                            loc.setY(loc.getY() + 1.0D);
                            isblock(loc, true);
                        }
                    }
                    loc = loc.add(loc.getDirection());
                    ((Moblocation)this.pvee.kohei.get(uid)).reset();
                }
            }
        }
        for (UUID uid : this.pvee.place.keySet()) {
            Entity entity = Bukkit.getEntity(uid);
            if (entity != null) {
                Location loc = entity.getLocation().clone();
                Integer i = ((Moblocation)this.pvee.place.get(uid)).setint(loc);
                Location l = loc.clone();
                if (i.intValue() > 5) {
                    if (!isblock(loc, false)) {
                        entity.setVelocity(entity.getVelocity().setY(0.5D));
                        if (around(loc)) {
                            loc.setY(loc.getY() + 1.0D);
                            loc.setY(loc.getY() + 1.0D);
                            fix(loc.getBlock());
                            ((LivingEntity)entity)
                                    .addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 99, 99999));
                            this.place = Bukkit.getScheduler().runTaskLater((Plugin)this.m.m, () -> {
                                this.fix(l.getBlock());
                                l.getBlock().setType(Material.SPONGE);
                                this.place.cancel();
                            },10L);
                        }
                    } else {
                        ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 0, 0));
                    }
                    ((Moblocation)this.pvee.place.get(uid)).reset();
                } else {
                    ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 0, 0));
                }
                loc = loc.add(loc.getDirection());
            }
        }
    }

    private boolean isblock(Location loc, boolean b) {
        if (this.types.contains(loc.getBlock().getType()))
            return false;
        if (b) {
            fix(loc.getBlock());
            loc.getBlock().breakNaturally();
        }
        return true;
    }

    private boolean around(Location loc) {
        Location[] locs = new Location[4];
        locs[0] = loc.clone().add(1.0D, 0.0D, 0.0D);
        locs[1] = loc.clone().add(-1.0D, 0.0D, 0.0D);
        locs[2] = loc.clone().add(0.0D, 0.0D, 1.0D);
        locs[3] = loc.clone().add(0.0D, 0.0D, -1.0D);
        boolean b = false;
        byte b1;
        int i;
        Location[] arrayOfLocation1;
        for (i = (arrayOfLocation1 = locs).length, b1 = 0; b1 < i; ) {
            Location l = arrayOfLocation1[b1];
            if (!this.types.contains(l.getBlock().getType()))
                b = true;
            b1++;
        }
        return b;
    }

    private void fix(Block b) {
        if (!this.info.blocks.containsKey(b.getLocation()))
            this.info.blocks.put(b.getLocation(), new BlockInfo(b));
    }
}
