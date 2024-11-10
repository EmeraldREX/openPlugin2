//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.*;

public class TopEntity implements CommandExecutor, TabCompleter {
    List<String> entityTypeNames = new ArrayList();

    public TopEntity() {
        EntityType[] entityTypes = EntityType.values();
        EntityType[] var5 = entityTypes;
        int var4 = entityTypes.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            EntityType entityType = var5[var3];
            String entityTypeName = entityType.name();
            this.entityTypeNames.add(entityTypeName);
        }

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("remove")) {
            sender.sendMessage(ChatColor.RED + "削除中...");

            try {
                EntityType type = EntityType.valueOf(args[1].toUpperCase());
                int count = 0;
                Iterator var17 = Bukkit.getWorlds().iterator();

                while(var17.hasNext()) {
                    World world = (World)var17.next();
                    Iterator var10 = world.getEntities().iterator();

                    while(var10.hasNext()) {
                        Entity entity = (Entity)var10.next();
                        if (entity.getType().equals(type) && entity.getCustomName() == null) {
                            entity.remove();
                            ++count;
                        }
                    }
                }

                sender.sendMessage(ChatColor.YELLOW + args[1] + " モブが消去 " + count + "匹");
                return true;
            } catch (Exception var12) {
                sender.sendMessage(ChatColor.RED + "Error");
                return true;
            }
        } else {
            Map<EntityType, Integer> hashMap = new HashMap();
            Iterator var7 = Bukkit.getWorlds().iterator();

            while(var7.hasNext()) {
                World world = (World)var7.next();
                Iterator var9 = world.getEntities().iterator();

                while(var9.hasNext()) {
                    Entity entity = (Entity)var9.next();
                    hashMap.put(entity.getType(), (Integer)hashMap.getOrDefault(entity.getType(), 0) + 1);
                }
            }

            List<Map.Entry<EntityType, Integer>> entryList = new ArrayList(hashMap.entrySet());
            entryList.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));

            int i;
            try {
                i = Integer.parseInt(args[0]);
            } catch (Exception var11) {
                i = 0;
            }

            if (entryList.size() <= i) {
                i = entryList.size() - 1;
            }

            sender.sendMessage(((EntityType)((Map.Entry)entryList.get(i)).getKey()).name() + " :" + ((Map.Entry)entryList.get(i)).getValue() + "匹");
            return false;
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList();
        if (args.length == 2) {
            String input = args[1].toUpperCase();
            Iterator var8 = this.entityTypeNames.iterator();

            while(var8.hasNext()) {
                String s = (String)var8.next();
                if (s.startsWith(input)) {
                    list.add(s);
                }
            }

            return list;
        } else {
            list.add("remove");
            return list;
        }
    }
}
