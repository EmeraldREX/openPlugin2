//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command.bet;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public class Betinfo {
    Inventory inv;
    HashMap<UUID, BetPlayer> bets = new HashMap();
    final String prefix;
    final String title;
    final String betinv;
    HashMap<String, UUID> playerlist;
    boolean toggle;
    final Material[] types;
    final String[] name;

    public Betinfo() {
        this.prefix = ChatColor.WHITE + "[" + ChatColor.AQUA + "OPEN" + ChatColor.WHITE + "] ";
        this.title = this.prefix + ChatColor.GREEN + "賭ける相手";
        this.betinv = this.prefix + ChatColor.GREEN + "賭けるアイテムをいれる";
        this.playerlist = new HashMap();
        this.toggle = true;
        this.types = new Material[]{Material.COBBLESTONE, Material.COAL, Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.NETHERITE_INGOT, Material.COPPER_INGOT, Material.EMERALD, Material.EMERALD_BLOCK, Material.COAL_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.NETHERITE_BLOCK, Material.COPPER_BLOCK, Material.REDSTONE, Material.REDSTONE_BLOCK};
        this.name = new String[]{"丸石", "石炭", "鉄インゴット", "金インゴット", "ダイヤモンド", "ネザライト", "銅インゴット", "エメラルド", "エメラルドブロック", "石炭ブロック", "鉄ブロック", "金ブロック", "ダイヤモンドブロック", "ネザライトブロック", "銅ブロック", "レッドストーン", "レッドストーンブロック"};
    }

    public String gettype(Material material) {
        int count = 0;
        Material[] var6;
        int var5 = (var6 = this.types).length;

        for(int var4 = 0; var4 < var5; ++var4) {
            Material m = var6[var4];
            if (m.equals(material)) {
                return this.name[count];
            }

            ++count;
        }

        return null;
    }

    public void reset() {
        this.inv = null;
        this.bets = new HashMap();
        this.playerlist = new HashMap();
        this.toggle = true;
    }
}
