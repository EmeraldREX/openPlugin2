//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import java.util.Iterator;
import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Hallowin implements Listener {
    public Hallowin(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        (new MyTask((MyTask)null)).runTaskTimer(plugin, 0L, 105L);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.getMessage().contains("ハロウィン")) {
            Player p = e.getPlayer();
            LivingEntity mob = (LivingEntity)p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
            Zombie z = (Zombie)mob;
            z.setCustomName("§cトリックオアトリート！");
            z.setCustomNameVisible(true);
            z.setHealth(50.0);
        }

    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player)event.getEntity();
            if (p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType().equals(Material.CARVED_PUMPKIN)) {
                event.setDamage(4.0);
            }
        }

    }

    private static class MyTask extends BukkitRunnable {
        private MyTask(MyTask myTask) {
        }

        public void run() {
            Iterator var2 = Bukkit.getOnlinePlayers().iterator();

            while(var2.hasNext()) {
                Player p = (Player)var2.next();
                if (p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType().equals(Material.CARVED_PUMPKIN)) {
                    PotionEffect inv = new PotionEffect(PotionEffectType.INVISIBILITY, 135, 2);
                    p.addPotionEffect(inv);
                    PotionEffect blindnessEffect = new PotionEffect(PotionEffectType.BLINDNESS, 135, 2);
                    p.addPotionEffect(blindnessEffect);
                }
            }

        }
    }
}
