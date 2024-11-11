package org.open2.openplugin2.command.bet;

import java.util.HashMap;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class Betinfo {
    Inventory inv;

    HashMap<UUID, BetPlayer> bets = new HashMap<>();

    final String prefix = ChatColor.WHITE + "[" + ChatColor.AQUA + "OPEN" + ChatColor.WHITE + "] ";

    final String title = String.valueOf(this.prefix) + ChatColor.GREEN + "賭ける相手";

    final String betinv = String.valueOf(this.prefix) + ChatColor.GREEN + "賭けるアイテムを入れる";

    HashMap<String, UUID> playerlist = new HashMap<>();

    boolean toggle = true;

    final Material[] types = new Material[] {
            Material.COBBLESTONE, Material.COAL, Material.IRON_INGOT, Material.GOLD_INGOT,
            Material.DIAMOND, Material.NETHERITE_INGOT, Material.COPPER_INGOT, Material.EMERALD, Material.EMERALD_BLOCK,
            Material.COAL_BLOCK,
            Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK,
            Material.NETHERITE_BLOCK, Material.COPPER_BLOCK, Material.REDSTONE, Material.REDSTONE_BLOCK };

    final String[] name = new String[] { "丸石", "石炭", "鉄インゴット", "金インゴット", "ダイヤモンド", "ネザライト", "銅インゴット", "エメラルド", "エメラルドブロック", "石炭ブロック",
       "鉄ブロック", "金ブロック", "ダイヤモンドブロック", "ネザライトブロック", "銅ブロック", "レッドストーン", "レッドストーンブロック" };

    public String gettype(Material material) {
        int count = 0;
        byte b;
        int i;
        Material[] arrayOfMaterial;
        for (i = (arrayOfMaterial = this.types).length, b = 0; b < i; ) {
            Material m = arrayOfMaterial[b];
            if (m.equals(material))
                return this.name[count];
            count++;
            b++;
        }
        return null;
    }

    public void reset() {
        this.inv = null;
        this.bets = new HashMap<>();
        this.playerlist = new HashMap<>();
        this.toggle = true;
    }
}
