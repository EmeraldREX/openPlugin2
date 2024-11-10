package org.open2.openplugin2.pve.mob;

import java.util.ArrayList;
import java.util.List;
import org.open2.openplugin2.pve.Moblocation;
import org.open2.openplugin2.pve.PVEmain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.joml.Random;

public class PVEspawninfo {
    PVEmain pvemain;

    int phase;

    Random r;

    List<Aromor> armors = new ArrayList<>();

    List<Aromor> koheiarmors = new ArrayList<>();

    EntityType[] types = new EntityType[] {
            EntityType.ZOMBIE, EntityType.SKELETON,
            EntityType.SPIDER, EntityType.WITCH, EntityType.WITHER_SKELETON,
            EntityType.CREEPER, EntityType.ZOMBIFIED_PIGLIN, EntityType.GHAST,
            EntityType.SILVERFISH, EntityType.VINDICATOR,
            EntityType.PHANTOM,
            EntityType.HUSK, EntityType.DROWNED, EntityType.STRAY, EntityType.HOGLIN,
            EntityType.ZOGLIN, EntityType.ILLUSIONER, EntityType.CAVE_SPIDER, EntityType.RAVAGER,
            EntityType.IRON_GOLEM };

    EntityType[] kohei = new EntityType[] { EntityType.ZOMBIE, EntityType.ZOMBIFIED_PIGLIN, EntityType.SKELETON };

    Aromor tnt = new Aromor(Material.TNT, Material.AIR, Material.AIR, Material.AIR, Material.TNT, Material.GUNPOWDER);

    public PVEspawninfo(PVEmain m) {
        this.r = new Random();
        this.pvemain = m;
        this.tnt.bow = Material.TNT;
        this.armors.add(new Aromor(Material.LEATHER_HELMET, Material.AIR, Material.AIR, Material.AIR, Material.AIR,
                Material.AIR));
        this.armors.add(new Aromor(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS,
                Material.LEATHER_BOOTS, Material.WOODEN_SWORD, Material.APPLE));
        this.armors.add(new Aromor(Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS,
                Material.CHAINMAIL_BOOTS, Material.STONE_SWORD, Material.EMERALD));
        this.armors.add(new Aromor(Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS,
                Material.GOLDEN_BOOTS, Material.GOLDEN_SWORD, Material.GOLD_INGOT));
        this.armors.add(new Aromor(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS,
                Material.IRON_BOOTS, Material.IRON_SWORD, Material.IRON_INGOT));
        this.armors.add(new Aromor(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_BOOTS, Material.DIAMOND_SWORD, Material.DIAMOND));
        this.armors.add(new Aromor(Material.PLAYER_HEAD, Material.AIR, Material.AIR, Material.AIR, Material.AIR,
                Material.NETHERITE_SCRAP));
        this.armors.add(new Aromor(Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS,
                Material.NETHERITE_BOOTS, Material.NETHERITE_SWORD, Material.NETHERITE_SCRAP));
    }

    public void spawnmob(Integer phase, Location loc) {
        this.tnt.bow = Material.TNT;
        this.phase = phase.intValue();
        int level = this.r.nextInt(this.armors.size() - 1) + this.r.nextInt(phase.intValue()) / 10;
        int l = this.phase / 2;
        if (l > this.types.length - 1)
            l = this.types.length - 1;
        int randomIndex = this.r.nextInt(l);
        if (randomIndex > this.types.length - 1)
            randomIndex = this.types.length - 1;
        EntityType type = this.types[randomIndex];
        LivingEntity entity = (LivingEntity)this.pvemain.info.pvecenter.getWorld().spawnEntity(loc, type);
        Random random = new Random();
        int chance = random.nextInt(100);
        boolean b = (chance < 50);
        setmob(entity, b);
        setarmor(entity, b);
        int repeatCount = 4;
        level = this.r.nextInt(level);
        for (int i = 0; i < repeatCount; i++) {
            LivingEntity mobs = (LivingEntity)this.pvemain.info.pvecenter.getWorld()
                    .spawnEntity(getRandomLocationAround(loc), type);
            setmob(mobs, b);
            setarmor(mobs, b);
        }
    }

    private void setmob(LivingEntity entity, boolean b) {
        this.pvemain.pvee.settarget((Entity)entity);
        entity.setRemoveWhenFarAway(false);
        this.pvemain.pvee.addmob((Entity)entity);
    }

    private Integer setarmor(LivingEntity entity, boolean b) {
        int level = this.r.nextInt(this.phase / 10);
        if (level >= this.armors.size() - 1)
            level = this.armors.size() - 1;
        if (level < 0)
            level = 0;
        Aromor armor = ((Aromor)this.armors.get(level)).clone();
        if (b) {
            Location loc, l = entity.getLocation();
            switch (entity.getType()) {
                case SKELETON:
                    armor = this.tnt;
                    this.pvemain.pvee.tnt.put(entity.getUniqueId(), new Moblocation(l));
                    break;
                case ZOMBIE:
                    armor.sword = Material.SPONGE;
                    this.pvemain.pvee.place.put(entity.getUniqueId(), new Moblocation(l));
                    break;
                case ZOMBIFIED_PIGLIN:
                    armor.sword = Material.STONE_PICKAXE;
                    this.pvemain.pvee.kohei.put(entity.getUniqueId(), new Moblocation(l));
                    break;
                case CHICKEN:
                    entity.setAI(false);
                    this.pvemain.pvee.chickin.put(entity.getUniqueId(), Integer.valueOf(0));
                    loc = entity.getLocation();
                    loc.setY(loc.getY() + 20.0D);
                    entity.teleport(loc);
                    break;
            }
        }
        this.pvemain.pvee.drops.put(entity.getUniqueId(), armor.drop);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2, 99999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, this.phase / 5, 99999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, this.phase / 5, 99999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, this.phase / 5, 99999));
        if (entity instanceof Zombie) {
            Zombie mob = (Zombie)entity;
            mob.getEquipment().setArmorContents(armor.getaromor(this.phase));
            mob.getEquipment().setItemInMainHand(armor.getsword(this.phase));
        } else if (entity instanceof Skeleton) {
            this.pvemain.pvee.drops.put(entity.getUniqueId(), armor.drop);
            Skeleton mob = (Skeleton)entity;
            mob.getEquipment().setArmorContents(armor.getaromor(this.phase));
            mob.getEquipment().setItemInMainHand(armor.getbow(this.phase));
        } else if (entity instanceof WitherSkeleton) {
            this.pvemain.pvee.drops.put(entity.getUniqueId(), armor.drop);
            WitherSkeleton mob = (WitherSkeleton)entity;
            mob.getEquipment().setArmorContents(armor.getaromor(this.phase));
            mob.getEquipment().setItemInMainHand(armor.getsword(this.phase));
        } else if (entity instanceof Creeper) {
            this.pvemain.pvee.drops.put(entity.getUniqueId(), armor.drop);
            PotionEffect potionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, 0, 9999);
            entity.addPotionEffect(potionEffect);
        } else if (entity instanceof Illusioner) {
            this.pvemain.pvee.drops.put(entity.getUniqueId(), armor.drop);
            Illusioner mob = (Illusioner)entity;
            mob.getEquipment().setItemInMainHand(armor.getbow(this.phase));
        } else if (entity instanceof Husk) {
            this.pvemain.pvee.drops.put(entity.getUniqueId(), armor.drop);
            Husk mob = (Husk)entity;
            mob.getEquipment().setArmorContents(armor.getaromor(this.phase));
            mob.getEquipment().setItemInMainHand(armor.getsword(this.phase));
        } else {
            if (level <= this.phase / 10)
                level = this.phase / 10;
            if (entity instanceof org.bukkit.entity.Ghast) {
                Location loc = entity.getLocation();
                loc.setY(loc.getY() + 20.0D);
                entity.teleport(loc);
            } else if (entity instanceof Creeper) {
                Creeper c = (Creeper)entity;
                if (this.r.nextInt(this.phase) > 30)
                    c.setPowered(true);
                if (this.r.nextInt(this.phase) > 30) {
                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, 1, 99999);
                    entity.addPotionEffect(potionEffect);
                }
                c.setPowered(true);
            }
        }
        return Integer.valueOf(level);
    }

    public Location getRandomLocationAround(Location center) {
        World world = center.getWorld();
        Random random = new Random();
        int radius = 6;
        int xOffset = random.nextInt(radius * 2 + 1) - radius;
        int zOffset = random.nextInt(radius * 2 + 1) - radius;
        int x = center.getBlockX() + xOffset;
        int z = center.getBlockZ() + zOffset;
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x + 0.5D, y, z + 0.5D);
    }
}
