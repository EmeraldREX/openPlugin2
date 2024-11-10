//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Boatboost implements Listener {
    public Boatboost(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand != null) {
            if (hand.getType() == Material.STICK && player.getVehicle() != null && player.getVehicle().getType().equals(EntityType.BOAT)) {
                Boat boat = (Boat)player.getVehicle();
                player.sendMessage("a");
                boat.setMaxSpeed(0.0);
            }

        }
    }
}
