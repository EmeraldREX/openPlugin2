//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Iterator;

public class Scale implements Listener, CommandExecutor {
    private double value = 0.0;
    private double increment = 0.1;
    private double max = 10.0;
    private double min = 0.0;
    BukkitTask r;

    public Scale(Main m) {
        m.getCommand("scale").setExecutor(this);
        m.getServer().getPluginManager().registerEvents(this, m);
    }

    @EventHandler
    public void onclick(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (e.getHand() != EquipmentSlot.OFF_HAND) {
            if (hand != null) {
                if (hand.getType() == Material.IRON_INGOT) {
                    e.getRightClicked().addPassenger(player);
                }

            }
        }
    }

    @EventHandler
    public void onclick(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            boolean var10000 = vehicle instanceof LivingEntity;
        }

        Iterator var5 = player.getPassengers().iterator();

        while(var5.hasNext()) {
            Entity player2 = (Entity)var5.next();
            player2.leaveVehicle();
        }

    }

    @EventHandler
    public void onclick(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player)e.getDamager();
            if (player.getVehicle() instanceof LivingEntity) {
                LivingEntity vehicle = (LivingEntity)player.getVehicle();
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand == null) {
                    return;
                }

                if (hand.getType() == Material.IRON_INGOT) {
                    vehicle.attack(e.getEntity());
                }
            }
        }

    }

    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if (hand != null) {
                if (hand.getType() == Material.APPLE) {
                    e.getPlayer().getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.0);
                }

            }
        } else if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (e.getHand() != EquipmentSlot.OFF_HAND) {
                if (hand != null) {
                    if (hand.getType() == Material.DIAMOND) {
                        Entity vehicle = player.getVehicle();
                        if (vehicle != null) {
                            Vector direction = player.getLocation().getDirection();
                            vehicle.setRotation(player.getLocation().getYaw(), player.getLocation().getPitch());
                            double force = 0.7;
                            Vector velocity = direction.multiply(force);
                            vehicle.setVelocity(velocity);
                            vehicle.setFallDistance(0.0F);
                            player.setFallDistance(0.0F);
                            if (vehicle instanceof LivingEntity) {
                                LivingEntity livingEntity = (LivingEntity)vehicle;
                                livingEntity.setNoActionTicks(20);
                            }
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void onPlayerAttackPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player)event.getDamager();
            ItemStack hand = attacker.getInventory().getItemInMainHand();
            if (hand == null) {
                return;
            }

            if (hand.getType() == Material.DIAMOND && attacker.isOp()) {
                Entity victim = event.getEntity();
                attacker.addPassenger(victim);
                event.setCancelled(true);
            }

            if (hand.getType() == Material.APPLE) {
                if (event.getEntity() instanceof Player) {
                    Player victim = (Player)event.getEntity();
                    if (!victim.getName().equals("viocat77")) {
                        event.setCancelled(true);
                    }

                    attacker.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(victim.getAttribute(Attribute.GENERIC_SCALE).getBaseValue());
                } else if (event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof Horse) && !(event.getEntity() instanceof SkeletonHorse) && !(event.getEntity() instanceof Donkey) && !(event.getEntity() instanceof Mule)) {
                    LivingEntity victim = (LivingEntity)event.getEntity();
                    event.setCancelled(true);
                    double atti = attacker.getAttribute(Attribute.GENERIC_SCALE).getBaseValue();
                    if (atti > 3.0) {
                        atti = 3.0;
                    }

                    if (atti < 0.5) {
                        atti = 0.5;
                    }

                    victim.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(atti);
                }
            }
        }

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.0);
                player.sendMessage("変更 1");
            } else {
                try {
                    Double i = Double.parseDouble(args[0]);
                    if (i <= this.max && i > this.min) {
                        player.sendMessage("変更 " + i);
                        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(i);
                        return true;
                    }

                    player.sendMessage(this.min + "から" + this.max + "である必要");
                } catch (Exception var7) {
                    player.sendMessage(this.min + "から" + this.max + "である必要");
                }
            }
        }

        return false;
    }
}
