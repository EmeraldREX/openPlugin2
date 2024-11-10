//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command.bet;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Goal implements CommandExecutor {
    Betinfo betinfo;

    public Goal(Betinfo betinfo) {
        this.betinfo = betinfo;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            String name = args[0];
            Player winner = Bukkit.getPlayer(name);
            if (winner != null) {
                double odds = (double)(1 + this.odds(winner.getUniqueId()) * 1);
                String message = "";
                int count = 1;
                String[] var14 = args;
                int var13 = args.length;

                for(int var12 = 0; var12 < var13; ++var12) {
                    String s = var14[var12];
                    Player pl = Bukkit.getPlayer(s);
                    if (pl == null) {
                        sender.sendMessage(this.betinfo.prefix + ChatColor.RED + "エラー　存在しない　プレイヤー");
                        return true;
                    }

                    message = message + count + "位" + s + " ";
                    if (count == 1) {
                        message = message + ChatColor.WHITE;
                    }

                    ++count;
                }

                Iterator var23 = this.betinfo.bets.entrySet().iterator();

                while(true) {
                    label104:
                    while(true) {
                        Map.Entry entry;
                        BetPlayer betPlayer;
                        do {
                            if (!var23.hasNext()) {
                                Bukkit.broadcastMessage(this.betinfo.prefix + ChatColor.GOLD + message + "倍率 " + odds);
                                this.betinfo.reset();
                                return true;
                            }

                            entry = (Map.Entry)var23.next();
                            betPlayer = (BetPlayer)entry.getValue();
                        } while(!betPlayer.betto.equals(winner.getUniqueId()));

                        ItemStack[] items = betPlayer.inv.getContents();
                        Map<Material, Integer> map = new HashMap();
                        ItemStack[] var19 = items;
                        int var18 = items.length;

                        for(int var17 = 0; var17 < var18; ++var17) {
                            ItemStack item = var19[var17];
                            if (item != null) {
                                map.compute(item.getType(), (k, v) -> {
                                    return v == null ? item.getAmount() : v + item.getAmount();
                                });
                            }
                        }

                        Iterator var29 = map.entrySet().iterator();

                        while(var29.hasNext()) {
                            Map.Entry<Material, Integer> e = (Map.Entry)var29.next();
                            e.setValue((int)((double)(Integer)e.getValue() * odds));
                        }

                        List<ItemStack> result = new ArrayList();
                        Iterator var31 = map.entrySet().iterator();

                        while(var31.hasNext()) {
                            Map.Entry<Material, Integer> e = (Map.Entry)var31.next();
                            Bukkit.broadcastMessage(this.betinfo.prefix + Bukkit.getOfflinePlayer((UUID)entry.getKey()).getName() + this.betinfo.gettype((Material)e.getKey()) + e.getValue());

                            for(int amout = (Integer)e.getValue(); amout >= 0; amout -= 64) {
                                if (amout <= 64) {
                                    result.add(new ItemStack((Material)e.getKey(), amout));
                                } else {
                                    result.add(new ItemStack((Material)e.getKey(), 64));
                                }
                            }
                        }

                        Player player = Bukkit.getPlayer((UUID)entry.getKey());
                        Iterator var36;
                        if (player != null) {
                            var36 = result.iterator();

                            while(var36.hasNext()) {
                                ItemStack item = (ItemStack)var36.next();
                                player.getWorld().dropItem(player.getLocation(), item);
                            }
                        } else {
                            var36 = Bukkit.getOnlinePlayers().iterator();

                            while(var36.hasNext()) {
                                Player op = (Player)var36.next();
                                if (op.isOp()) {
                                    Iterator var21 = result.iterator();

                                    while(true) {
                                        if (!var21.hasNext()) {
                                            continue label104;
                                        }

                                        ItemStack item = (ItemStack)var21.next();
                                        op.getWorld().dropItem(op.getLocation(), item);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(this.betinfo.prefix + ChatColor.RED + "エラー　存在しない　プレイヤー");
            }
        } else {
            this.betinfo.toggle = !this.betinfo.toggle;
            sender.sendMessage(this.betinfo.prefix + ChatColor.YELLOW + "賭け" + this.betinfo.toggle);
        }

        return true;
    }

    private Integer odds(UUID uuid) {
        int i = 0;
        Iterator var4 = this.betinfo.bets.entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry<UUID, BetPlayer> entry = (Map.Entry)var4.next();
            if (((BetPlayer)entry.getValue()).betto.equals(uuid)) {
                --i;
            } else {
                ++i;
            }
        }

        if (i <= 1) {
            i = 1;
        }

        return i;
    }
}
