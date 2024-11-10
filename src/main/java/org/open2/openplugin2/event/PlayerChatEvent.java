//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.command.WanChat;
import org.open2.openplugin2.net.ConfigLoader;
import org.open2.openplugin2.net.Main;
import org.open2.openplugin2.net.PlayerInfo;
import org.open2.openplugin2.net.japanize.IMEConverter;
import org.open2.openplugin2.net.japanize.YukiKanaConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Random;

import java.util.Iterator;

public class PlayerChatEvent implements Listener {
    Main m;
    Random random = new Random();

    public PlayerChatEvent(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.m = plugin;
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onChat(AsyncPlayerChatEvent e) {
        if (!((PlayerInfo)ConfigLoader.playerinfolist.get(e.getPlayer().getUniqueId())).japanese) {
            String message = e.getMessage();
            if (e.getMessage().startsWith(".")) {
                message = message.substring(1);
                e.setMessage(IMEConverter.convByGoogleIME(YukiKanaConverter.conv(message)));
            } else if (e.getMessage().startsWith(",")) {
                message = message.substring(1);
                e.setMessage(message);
            } else {
                String kana = YukiKanaConverter.conv(message);
                String conv = IMEConverter.convByGoogleIME(kana);
                if (!conv.equals(message) || !conv.equals(kana)) {
                    e.setMessage(IMEConverter.convByGoogleIME(YukiKanaConverter.conv(message)) + ChatColor.GRAY + "(" + message + ")");
                }
            }
        }

        Iterator var6 = Bukkit.getOnlinePlayers().iterator();

        while(var6.hasNext()) {
            final Player player = (Player)var6.next();
            if (WanChat.list.contains(player.getUniqueId())) {
                (new BukkitRunnable() {
                    public void run() {
                        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 3.0F, 1.5F);
                    }
                }).runTaskLater(Main.m, 4L);
            }
        }

        if (e.getMessage().startsWith("http://") || e.getMessage().startsWith("https://") || e.getMessage().startsWith("www.")) {
            this.sendClickableMessage(e.getPlayer().getName(), e.getMessage());
        }

    }

    private void sendClickableMessage(String name, String url) {
        String jsonMessage = "{\"text\":\"<" + name + "> " + url + "\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + url + "\"}}";
        Bukkit.getScheduler().runTask(Main.m, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw @a " + jsonMessage);
        });
    }
}
