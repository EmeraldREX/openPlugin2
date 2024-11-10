//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.*;

public class Tsubojijii implements Listener {
    Material[] items;
    boolean toggele;
    List<UUID> plaList;
    boolean b;
    BukkitRunnable l;
    UUID uuid;

    public Tsubojijii() {
        this.items = new Material[]{Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD};
        this.toggele = false;
        this.plaList = new ArrayList();
        this.b = true;
        Main.m.getServer().getPluginManager().registerEvents(this, Main.m);
    }

    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand != null) {
            if (hand.getType() == Material.WHITE_DYE && e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.CHEST)) {
                Chest chest = (Chest)e.getClickedBlock();
                int i = 0;
                ItemStack[] var9;
                int var8 = (var9 = chest.getInventory().getContents()).length;

                for(int var7 = 0; var7 < var8; ++var7) {
                    ItemStack item = var9[var7];
                    if (item != null) {
                        i += item.getAmount();
                    }
                }

                player.sendMessage("個数 :" + i);
            }

            Player player2;
            Iterator var12;
            if (hand.getType() == Material.GREEN_DYE && e.getPlayer().getName().equals("east_company")) {
                if (this.toggele) {
                    player.setGlowing(true);
                    var12 = Bukkit.getOnlinePlayers().iterator();

                    while(var12.hasNext()) {
                        player2 = (Player)var12.next();
                        player2.hidePlayer(player);
                    }
                } else {
                    player.setGlowing(false);
                    var12 = Bukkit.getOnlinePlayers().iterator();

                    while(var12.hasNext()) {
                        player2 = (Player)var12.next();
                        player2.showPlayer(player);
                    }
                }

                this.toggele = !this.toggele;
            }

            if (hand.getType() == Material.RED_DYE && e.getPlayer().getName().equals("east_company")) {
                if (this.toggele) {
                    player.setGlowing(true);
                    BlockDisplay entity = (BlockDisplay)player.getWorld().spawnEntity(player.getLocation(), EntityType.BLOCK_DISPLAY);
                    this.uuid = entity.getUniqueId();
                    if (player.getInventory().getItemInOffHand() != null) {
                        entity.setBlock(Bukkit.createBlockData(player.getInventory().getItemInOffHand().getType()));
                    } else {
                        entity.setBlock(Bukkit.createBlockData(Material.STONE));
                    }

                    this.l = new BukkitRunnable() {
                        public void run() {
                            if (player != null) {
                                if (player.isOnline()) {
                                    Location l = player.getLocation().clone();
                                    l.setPitch(0.0F);
                                    l.setYaw(0.0F);
                                    l.setX(l.getX() - 0.5);
                                    l.setZ(l.getZ() - 0.5);
                                    Bukkit.getEntity(Tsubojijii.this.uuid).teleport(l);
                                } else {
                                    Bukkit.getEntity(Tsubojijii.this.uuid).remove();
                                    this.cancel();
                                }
                            }

                        }
                    };
                    this.l.runTaskTimer(Main.m, 0L, 1L);
                    Iterator var14 = Bukkit.getOnlinePlayers().iterator();

                    while(var14.hasNext()) {
                        Player player3 = (Player)var14.next();
                        player3.hidePlayer(player);
                    }
                } else {
                    player.setGlowing(false);
                    if (this.uuid != null) {
                        Bukkit.getEntity(this.uuid).remove();
                    }

                    if (this.l != null) {
                        this.l.cancel();
                    }

                    var12 = Bukkit.getOnlinePlayers().iterator();

                    while(var12.hasNext()) {
                        player2 = (Player)var12.next();
                        player2.showPlayer(player);
                    }
                }

                this.toggele = !this.toggele;
            }

            if (hand.getType() == Material.LIGHT_GRAY_DYE && e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (this.plaList.contains(player.getUniqueId())) {
                    var12 = Bukkit.getOnlinePlayers().iterator();

                    while(var12.hasNext()) {
                        player2 = (Player)var12.next();
                        player.showPlayer(player2);
                    }

                    this.plaList.remove(player.getUniqueId());
                } else {
                    var12 = Bukkit.getOnlinePlayers().iterator();

                    while(var12.hasNext()) {
                        player2 = (Player)var12.next();
                        player.hidePlayer(player2);
                    }

                    this.plaList.add(player.getUniqueId());
                }
            }

            if (hand.getType() == Material.BLACK_DYE && this.b && player.isOp() && e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                final Entity entity = this.getPlayerLookingEntity(player);
                if (entity != null) {
                    this.b = false;
                    BukkitRunnable task = new BukkitRunnable() {
                        int count = 0;

                        public void run() {
                            if (this.count > 9) {
                                Tsubojijii.this.b = true;
                                this.cancel();
                            } else {
                                Location location = player.getLocation().clone();
                                location.setY(location.getY() + 7.0);
                                Location loc = Tsubojijii.this.set(entity.getLocation().clone(), location);
                                if (loc != null) {
                                    loc = Tsubojijii.this.shiftLocation(loc);
                                    final BlockDisplay bd = Tsubojijii.this.createsword(loc);
                                    Bukkit.getScheduler().runTaskLater(Main.m, new Runnable() {
                                        public void run() {
                                            Transformation trans = Tsubojijii.this.move(bd, entity.getLocation());
                                            bd.setTransformation(trans);
                                            BukkitRunnable task = new BukkitRunnable() {
                                                int count = 0;

                                                public void run() {
                                                    if (this.count > 20) {
                                                        bd.remove();
                                                        bd.getWorld().spawnParticle(Particle.EXPLOSION, bd.getLocation(), this.count);
                                                        this.cancel();
                                                    } else {
                                                        Location bdlocation = bd.getLocation();
                                                        if (bdlocation.getWorld().equals(entity.getWorld()) && bdlocation.distance(entity.getLocation()) < 1.0) {
                                                            bd.getWorld().playSound(bdlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);
                                                            bd.getWorld().spawnParticle(Particle.EXPLOSION, bdlocation, this.count);
                                                            bd.remove();
                                                            this.cancel();
                                                        }

                                                        if (!bdlocation.getBlock().getType().isAir()) {
                                                            bd.getWorld().playSound(bdlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);
                                                            bd.getWorld().spawnParticle(Particle.EXPLOSION, bdlocation, this.count);
                                                            bd.remove();
                                                            this.cancel();
                                                        }
                                                    }

                                                    ++this.count;
                                                }
                                            };
                                            task.runTaskTimer(Main.m, 5L, 5L);
                                        }
                                    }, 20L);
                                }
                            }

                            ++this.count;
                        }
                    };
                    task.runTaskTimer(Main.m, 20L, 20L);
                }
            }

        }
    }

    private Location shiftLocation(Location location) {
        Random random = new Random();
        double newX = location.getX() + (random.nextDouble() * 6.0 - 2.0);
        double newY = location.getY() + random.nextDouble() * 6.0;
        double newZ = location.getZ() + (random.nextDouble() * 6.0 - 2.0);
        return new Location(location.getWorld(), newX, newY, newZ, location.getYaw(), location.getPitch());
    }

    private Location set(Location location2, Location location1) {
        if (location1.getWorld().equals(location1.getWorld())) {
            Vector direction = location2.toVector().subtract(location1.toVector());
            double yaw = Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
            double pitch = Math.toDegrees(Math.asin(-direction.getY() / direction.length()));
            location1.setYaw((float)yaw);
            location1.setPitch((float)pitch);
        } else {
            location1 = null;
        }

        return location1;
    }

    private Entity getPlayerLookingEntity(Player player) {
        double maxDistance = 20.0;
        Entity targetEntity = null;
        Iterator var6 = player.getNearbyEntities(maxDistance, maxDistance, maxDistance).iterator();

        while(var6.hasNext()) {
            Entity entity = (Entity)var6.next();
            if (entity.getLocation().toVector().subtract(player.getEyeLocation().toVector()).normalize().dot(player.getEyeLocation().getDirection()) > 0.99) {
                targetEntity = entity;
                break;
            }
        }

        return targetEntity;
    }

    private BlockDisplay createsword(Location location) {
        World world = location.getWorld();
        BlockDisplay block = (BlockDisplay)world.spawnEntity(location, EntityType.BLOCK_DISPLAY);
        block.setBlock(Bukkit.createBlockData(Material.DIAMOND_BLOCK));
        double d = (double)location.getPitch();
        int i = 90;
        if (d + 90.0 > 90.0) {
            i = -90;
        }

        block.setRotation(location.getYaw(), location.getPitch() + (float)i);
        block.setTransformation(this.change(block, 5.0F));
        return block;
    }

    private Transformation change(BlockDisplay b, float d) {
        Transformation transformation = b.getTransformation();
        transformation.getScale().set(new Vector3f(1.0F, d, 1.0F));
        return transformation;
    }

    private Transformation move(BlockDisplay b, Location loc) {
        Transformation transformation = b.getTransformation();
        Location bdlocation = b.getLocation();
        transformation.getTranslation().set(0.0, -(loc.distance(bdlocation) + 3.0), 0.0);
        b.setInterpolationDelay(0);
        b.setInterpolationDuration((int)loc.distance(bdlocation));
        return transformation;
    }
}
