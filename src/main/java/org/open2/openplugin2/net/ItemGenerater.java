package org.open2.openplugin2.net;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGenerater {
    public static ItemStack item(Material m, String name, String lora, boolean enchantment) {
        ItemStack item = new ItemStack(m);
        if (enchantment)
            item.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
        ItemMeta meta = item.getItemMeta();
        if (enchantment)
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        meta.setDisplayName(name);
        List<String> loras = Arrays.asList(lora.split(","));
        meta.setLore(loras);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack item(Material m, String name) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
