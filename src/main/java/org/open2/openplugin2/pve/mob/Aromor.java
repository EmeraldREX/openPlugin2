package org.open2.openplugin2.pve.mob;

import org.open2.openplugin2.pve.PVEmain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Aromor implements Cloneable {
    Material[] armor;

    Material sword;

    Material drop;

    Material bow = Material.BOW;

    ItemStack playerhand;

    PVEmain m;

    public Aromor(Material helmet, Material chest, Material legins, Material boots, Material sword, Material drop) {
        Material[] armor = { helmet, chest, legins, boots };
        this.drop = drop;
        this.sword = sword;
        this.armor = armor;
    }

    public Aromor clone() {
        try {
            return (Aromor)super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public ItemStack[] getaromor(int phase) {
        ItemStack[] i = new ItemStack[4];
        int count = 3;
        byte b;
        int j;
        Material[] arrayOfMaterial;
        for (j = (arrayOfMaterial = this.armor).length, b = 0; b < j; ) {
            Material a = arrayOfMaterial[b];
            if (a == Material.PLAYER_HEAD) {
                if (Bukkit.getOnlinePlayers().size() != 0) {
                    Player player = Bukkit.getOnlinePlayers().stream()
                            .skip((int)(Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
                    i = (ItemStack[])player.getInventory().getArmorContents().clone();
                    i[3] = gethead(player);
                    this.playerhand = player.getInventory().getItemInMainHand().clone();
                    break;
                }
            } else if (a != null) {
                i[count] = enchantment(new ItemStack(a), Enchantment.FIRE_PROTECTION, phase);
            }
            count--;
            b++;
        }
        if (i[0] != null)
            i[0] = enchantment(i[0], Enchantment.FROST_WALKER, 10);
        i[3] = enchantment(i[3], Enchantment.FEATHER_FALLING, phase);
        i[2] = enchantment(i[2], Enchantment.FIRE_PROTECTION, phase);
        return i;
    }

    private ItemStack enchantment(ItemStack i, Enchantment enchant, int phase) {
        phase /= 3;
        int level = 0;
        if (phase > 9)
            level = phase - 9;
        if (i == null)
            return null;
        if (level == 0)
            return i;
        if (level > enchant.getMaxLevel())
            level = enchant.getMaxLevel();
        i.addUnsafeEnchantment(enchant, level);
        return i;
    }

    public ItemStack getsword(int phase) {
        if (this.playerhand != null)
            return this.playerhand;
        return enchantment(new ItemStack(this.sword), Enchantment.SHARPNESS, phase);
    }

    public ItemStack getbow(int phase) {
        ItemStack i = enchantment(new ItemStack(this.bow), Enchantment.PUNCH, phase);
        if (phase > 30)
            i = enchantment(new ItemStack(this.bow), Enchantment.FLAME, phase / 10);
        return enchantment(i, Enchantment.POWER, phase / 10);
    }

    private ItemStack gethead(Player p) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta)head.getItemMeta();
        if (p.getPlayerProfile() != null)
            meta.setOwnerProfile(p.getPlayerProfile());
        head.setItemMeta((ItemMeta)meta);
        return head;
    }
}
