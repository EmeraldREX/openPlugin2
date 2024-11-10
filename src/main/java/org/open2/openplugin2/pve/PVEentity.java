package org.open2.openplugin2.pve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.Zombie;

public class PVEentity {
    public List<UUID> mobs = new ArrayList<>();

    public HashMap<UUID, Material> drops = new HashMap<>();

    public HashMap<UUID, Moblocation> tnt = new HashMap<>();

    public HashMap<UUID, Moblocation> kohei = new HashMap<>();

    public HashMap<UUID, Moblocation> place = new HashMap<>();

    PVEmain m;

    public HashMap<UUID, Integer> chickin = new HashMap<>();

    public PVEentity(PVEmain m) {
        this.m = m;
    }

    public void addmob(Entity entity) {
        this.mobs.add(entity.getUniqueId());
    }

    public void removeentity(UUID uid) {
        if (this.mobs.contains(uid))
            this.mobs.remove(uid);
        if (this.chickin.containsKey(uid))
            this.chickin.remove(uid);
        if (this.drops.containsKey(uid))
            this.drops.remove(uid);
        if (this.tnt.containsKey(uid))
            this.tnt.remove(uid);
        if (this.kohei.containsKey(uid))
            this.kohei.remove(uid);
        if (this.place.containsKey(uid))
            this.place.remove(uid);
    }

    public Entity settarget(Entity entity) {
        EntityType entityType = entity.getType();
        Villager villager = (Villager)Bukkit.getEntity(this.m.info.villager);
        if (villager == null)
            return null;
        if (entityType == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie)entity;
            if (zombie.getTarget() == null || zombie.getTarget() instanceof Skeleton)
                zombie.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.SKELETON) {
            Skeleton skeleton = (Skeleton)entity;
            if (skeleton.getTarget() == null || skeleton.getTarget() instanceof Skeleton)
                skeleton.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.CREEPER) {
            Creeper creeper = (Creeper)entity;
            if (creeper.getTarget() == null || creeper.getTarget() instanceof Skeleton)
                creeper.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.SPIDER) {
            Spider spider = (Spider)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.STRAY) {
            Stray spider = (Stray)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.HUSK) {
            Husk spider = (Husk)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.SLIME) {
            Slime spider = (Slime)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.CAVE_SPIDER) {
            CaveSpider spider = (CaveSpider)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.SILVERFISH) {
            Silverfish spider = (Silverfish)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.PHANTOM) {
            Phantom spider = (Phantom)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.GHAST) {
            Ghast spider = (Ghast)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.ZOMBIFIED_PIGLIN) {
            PigZombie spider = (PigZombie)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.WITHER_SKELETON) {
            WitherSkeleton spider = (WitherSkeleton)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.BLAZE) {
            Blaze spider = (Blaze)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.WITHER) {
            Wither spider = (Wither)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.WITCH) {
            Witch spider = (Witch)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.VINDICATOR) {
            Vindicator spider = (Vindicator)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.DROWNED) {
            Drowned spider = (Drowned)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.EVOKER) {
            Evoker spider = (Evoker)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.VINDICATOR) {
            Vindicator spider = (Vindicator)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.RAVAGER) {
            Ravager spider = (Ravager)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.HOGLIN) {
            Hoglin spider = (Hoglin)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.ZOGLIN) {
            Zoglin spider = (Zoglin)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.PIGLIN_BRUTE) {
            PiglinBrute spider = (PiglinBrute)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.ILLUSIONER) {
            Illusioner spider = (Illusioner)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        } else if (entityType == EntityType.IRON_GOLEM) {
            IronGolem spider = (IronGolem)entity;
            if (spider.getTarget() == null || spider.getTarget() instanceof Skeleton)
                spider.setTarget((LivingEntity)villager);
        }
        return entity;
    }
}
