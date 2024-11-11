package org.open2.openplugin2.command.bet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                double odds = 1 + odds(winner.getUniqueId());
                int count = 1;

                StringBuilder message = new StringBuilder();
                for (String s : args) {
                    Player pl = Bukkit.getPlayer(s);
                    if (pl != null) {
                        message.append(count).append("位").append(s).append(" ");
                        count++;
                    } else {
                        sender.sendMessage(this.betinfo.prefix + ChatColor.RED + "エラー 存在しないプレイヤー");
                        return true;
                    }
                }
                for (Map.Entry<UUID, BetPlayer> entry : this.betinfo.bets.entrySet()) {
                    BetPlayer betPlayer = entry.getValue();
                    if (betPlayer.betto.equals(winner.getUniqueId())) {
                        ItemStack[] items = betPlayer.inv.getContents();
                        Map<Material, Integer> map = new HashMap<>();
                        byte b1;
                        int j;
                        ItemStack[] arrayOfItemStack1;
                        for (j = (arrayOfItemStack1 = items).length, b1 = 0; b1 < j; ) {
                            ItemStack item = arrayOfItemStack1[b1];
                            if (item != null)
                                map.compute(item.getType(), (k, v) -> (v == null) ? item.getAmount() : v + item.getAmount());
                            b1++;
                        }
                        for (Map.Entry<Material, Integer> e : map.entrySet())
                            e.setValue(Integer.valueOf((int)(((Integer)e.getValue()).intValue() * odds)));
                        List<ItemStack> result = new ArrayList<>();
                        for (Map.Entry<Material, Integer> e : map.entrySet()) {
                            Bukkit.broadcastMessage(this.betinfo.prefix + this.betinfo.gettype(e.getKey()) + e.getValue() + "個");

                            int amout = ((Integer)e.getValue()).intValue();
                            while (amout > 0) {  // 0のときにループに入らないように修正
                                if (amout <= 64) {
                                    result.add(new ItemStack(e.getKey(), amout));
                                } else {
                                    result.add(new ItemStack(e.getKey(), 64));
                                }
                                amout -= 64;
                            }
                        }
                        Player player = Bukkit.getPlayer(entry.getKey());
                        if (player != null) {
                            for (ItemStack item : result)
                                player.getWorld().dropItem(player.getLocation(), item);
                            continue;
                        }
                        for (Player op : Bukkit.getOnlinePlayers()) {
                            if (op.isOp()) {
                                for (ItemStack item : result)
                                    op.getWorld().dropItem(op.getLocation(), item);
                                break;
                            }
                        }
                    }
                }
                Bukkit.broadcastMessage(String.valueOf(this.betinfo.prefix) + ChatColor.GOLD + message + "倍率" + odds);
                this.betinfo.reset();
            } else {
                sender.sendMessage(String.valueOf(this.betinfo.prefix) + ChatColor.RED + "エラー　存在しない　プレイヤー");
            }
        } else {
            this.betinfo.toggle = !this.betinfo.toggle;
            sender.sendMessage(String.valueOf(this.betinfo.prefix) + ChatColor.YELLOW + "賭け"+ this.betinfo.toggle);
        }
        return true;
    }

    private int odds(UUID uuid) {
        int i = 0;
        for (BetPlayer betPlayer : this.betinfo.bets.values()) {
            if (betPlayer.betto.equals(uuid)) {
                i--;
            } else {
                i++;
            }
        }
        return Math.max(i, 1);
    }
}
