package org.open2.openplugin2.pve.mob;

import java.util.Random;
import org.open2.openplugin2.pve.PVEmain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Boss {
    EntityType[] boss = new EntityType[] { EntityType.WITHER, EntityType.WARDEN };

    ItemStack[] items = new ItemStack[] { new ItemStack(Material.NETHERITE_HELMET), new ItemStack(Material.NETHERITE_LEGGINGS),
            new ItemStack(Material.NETHERITE_CHESTPLATE), new ItemStack(Material.AIR) };

    PVEmain pvemain;

    public Boss(PVEmain m) {
        this.pvemain = m;
    }

    public void startboss(int phase, Location center) {
        center.setY(center.getY() + 13.0D);
        this.items[3] = gethead();
        bossspawn((LivingEntity)center.getWorld().spawnEntity(center, this.boss[(new Random()).nextInt(this.boss.length - 1)]),
                phase);
    }

    private void bossspawn(LivingEntity entity, int phase) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, phase / 5, 99999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, phase / 5, 99999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, phase / 5, 99999));
        if (entity instanceof Giant)
            bigboss((Giant)entity);
        this.pvemain.pvee.settarget((Entity)entity);
        entity.setRemoveWhenFarAway(false);
        this.pvemain.pvee.addmob((Entity)entity);
    }

    private void bigboss(Giant giant) {
        giant.getEquipment().setArmorContents(this.items);
        giant.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_AXE));
    }

    private ItemStack gethead() {
        Player p = Bukkit.getPlayer("east_company");
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (p != null) {
            SkullMeta meta = (SkullMeta)head.getItemMeta();
            if (p.getPlayerProfile() != null)
                meta.setOwnerProfile(p.getPlayerProfile());
            head.setItemMeta((ItemMeta)meta);
        }
        return head;
    }
}
