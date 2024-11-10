//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.yakiu;

import org.open2.openplugin2.net.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class YakiuPower implements Listener {
    YakiuMain m;
    List<Snowball> list = new ArrayList();
    List<UUID> plist = new ArrayList();

    public YakiuPower(YakiuMain yakiuMain) {
        this.m = yakiuMain;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Snowball) {
            final Snowball snowball = (Snowball)event.getEntity();
            if (!(snowball.getShooter() instanceof Player)) {
                return;
            }

            if (snowball.getCustomName() != null) {
                return;
            }

            Bukkit.getScheduler().runTaskLater(Main.m, new Runnable() {
                public void run() {
                    snowball.setGlowing(true);
                    YakiuPower.this.list.add(snowball);
                    snowball.setCustomName(ChatColor.YELLOW + "Base Ball");
                    if (snowball.getShooter() instanceof Player) {
                        final Player player = (Player)snowball.getShooter();
                        if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType().equals(Material.GOLDEN_HELMET)) {
                            snowball.setCustomName(ChatColor.YELLOW + "Base Ball pitch");
                        }

                        double playerY = player.getLocation().getY();
                        double groundY = YakiuPower.this.getGroundY(player.getLocation());
                        double distanceFromGround = (playerY - groundY) / 1.5;
                        if (!player.isSneaking()) {
                            ++distanceFromGround;
                        } else {
                            ++distanceFromGround;
                        }

                        YakiuPower.this.sendActionBar(player, String.valueOf((int)(distanceFromGround * 100.0)));
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (item != null) {
                            double finalDistanceFromGround = distanceFromGround;
                            (new BukkitRunnable() {
                                private int count = 0;

                                public void run() {
                                    Vector direction = player.getLocation().getDirection();

                                    try {
                                        snowball.setVelocity(direction.multiply(finalDistanceFromGround));
                                    } catch (Exception var3) {
                                        this.cancel();
                                    }

                                    ++this.count;
                                    if (this.count >= 15) {
                                        this.cancel();
                                    }

                                }
                            }).runTaskTimer(YakiuPower.this.m.m, 3L, 3L).getTaskId();
                        }
                    }

                }
            }, 1L);
        }

    }

    @EventHandler
    public void ondispenser(BlockDispenseEvent event) {
        if (event.getItem().getType() == Material.SNOWBALL) {
            event.setCancelled(true);
            final Dispenser dispenser = (Dispenser)event.getBlock().getState();
            final BlockFace face = ((Directional)dispenser.getBlockData()).getFacing();
            Block dispensedBlock = event.getBlock().getRelative(face);
            Bukkit.getScheduler().runTaskLater(Main.m, new Runnable() {
                public void run() {
                    dispenser.getInventory().removeItem(new ItemStack[]{new ItemStack(Material.SNOWBALL)});
                }
            }, 1L);
            double d = 1.5;
            if (Tag.SIGNS.isTagged(dispensedBlock.getType())) {
                Sign sign = (Sign)dispensedBlock.getState();

                try {
                    d = Double.parseDouble(sign.getLine(0));
                    if (d > 10.0 || d < 0.0) {
                        d = 5.0;
                        sign.setLine(0, "error");
                        sign.update();
                    }

                    d = d * 2.0 / 10.0 + 1.0;
                } catch (Exception var10) {
                    sign.setLine(0, "error");
                    sign.update();
                }
            }

            final Snowball snowball = (Snowball)event.getBlock().getWorld().spawnEntity(dispensedBlock.getLocation(), EntityType.SNOWBALL);
            snowball.setGlowing(true);
            this.list.add(snowball);
            snowball.setCustomName(ChatColor.YELLOW + "Base Ball");
            double finalD = d;
            (new BukkitRunnable() {
                private int count = 0;

                public void run() {
                    try {
                        Vector direction = face.getDirection();
                        snowball.setVelocity(direction.multiply(finalD));
                    } catch (Exception var2) {
                        this.cancel();
                    }

                    ++this.count;
                    if (this.count >= 3) {
                        this.cancel();
                    }

                }
            }).runTaskTimer(this.m.m, 3L, 3L).getTaskId();
        }

    }

    @EventHandler
    public void pickup(EntityPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            if (item.getType().equals(Material.SNOWBALL) && this.glove(player, 0.3F, 2.0F, false)) {
                event.setCancelled(true);
                event.getItem().remove();
                int amount = event.getItem().getItemStack().getAmount() - 1;
                if (amount >= 1) {
                    player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.SNOWBALL, amount));
                }
            }
        }

    }

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        Player player = event.getPlayer();
        if (item.getType().equals(Material.LEATHER) && item.getItemMeta().getEnchantLevel(Enchantment.SHARPNESS) == 1) {
            item.removeEnchantment(Enchantment.SHARPNESS);
            Item i = player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.SNOWBALL));
            i.setPickupDelay(event.getItemDrop().getPickupDelay());
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball) {
            Snowball snowball = (Snowball)event.getEntity();
            if (snowball.getCustomName().contains(ChatColor.YELLOW + "Base Ball")) {
                this.list.remove(snowball);
                if (event.getHitEntity() instanceof Player) {
                    Player p = (Player)event.getHitEntity();
                    if (p.getInventory().getItemInMainHand() != null) {
                        if (p.getInventory().getItemInMainHand().getType().equals(Material.LEATHER)) {
                            this.glove(p, 10.0F, 1.3F, true);
                            if (p.getVehicle() != null) {
                                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 10.0F, 1.3F);
                                if (p.getLocation().getY() + 1.0 < snowball.getLocation().getY()) {
                                    this.sendActionBar((Player)null, ChatColor.YELLOW + "ストライク！");
                                } else {
                                    this.sendActionBar((Player)null, ChatColor.GREEN + "ボール！");
                                }
                            }

                            return;
                        }

                        event.setCancelled(true);
                        return;
                    }

                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 10.0F, 1.3F);
                } else if (event.getHitEntity() != null) {
                    snowball.setGlowing(true);
                    this.list.add(snowball);
                    snowball.setCustomName(ChatColor.YELLOW + "Base Ball");
                    return;
                }

                Vector vector = snowball.getVelocity();
                snowball.remove();
                if (vector.length() < 0.2) {
                    snowball.getWorld().dropItem(snowball.getLocation(), new ItemStack(Material.SNOWBALL));
                    return;
                }

                if (event.getHitBlock() != null) {
                    if (snowball.getCustomName().contains(ChatColor.YELLOW + "Base Ball pitch")) {
                        this.sendActionBar((Player)null, ChatColor.GREEN + "ボール！");
                    }

                    if (event.getHitBlock().getType().equals(Material.IRON_BARS)) {
                        snowball.getWorld().dropItem(snowball.getLocation(), new ItemStack(Material.SNOWBALL));
                        return;
                    }

                    BlockFace blockFace = event.getHitBlockFace();
                    Vector normalVector = blockFace.getDirection();
                    Vector originalDirection = snowball.getVelocity();
                    Vector reflectedDirection = originalDirection.subtract(normalVector.multiply(originalDirection.dot(normalVector) * 2.0));
                    Location snowballLocation = snowball.getLocation();
                    Snowball newSnowball = (Snowball)snowballLocation.getWorld().spawn(snowballLocation, Snowball.class);
                    newSnowball.setGlowing(true);
                    this.list.add(newSnowball);
                    newSnowball.setCustomName(ChatColor.YELLOW + "Base Ball");
                    reflectedDirection.setY(reflectedDirection.getY() / 1.5);
                    newSnowball.setVelocity(reflectedDirection.multiply(0.9));
                }
            }
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection().normalize();
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            for(double distance = 0.0; distance < 5.0; ++distance) {
                Location particleLocation = eyeLocation.clone().add(direction.clone().multiply(distance));
                Snowball sb = this.getLocation(player, particleLocation, 1.3);
                if (sb != null) {
                    sb.remove();
                    this.glove(player, 5.0F, 1.3F, true);
                    return;
                }
            }
        }

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (item == null) {
                return;
            }

            if (item.getType().equals(Material.LEATHER) && item.getItemMeta().getEnchantLevel(Enchantment.SHARPNESS) == 1) {
                Player target = null;

                for(double distance = 0.0; distance < 5.0; ++distance) {
                    Location particleLocation = eyeLocation.clone().add(direction.clone().multiply(distance));
                    Iterator var11 = Bukkit.getOnlinePlayers().iterator();

                    while(var11.hasNext()) {
                        Player pl = (Player)var11.next();
                        if (pl.getUniqueId() != player.getUniqueId() && pl.getWorld().equals(particleLocation.getWorld()) && pl.getLocation().distance(particleLocation) < 2.0) {
                            target = pl;
                            break;
                        }
                    }
                }

                if (target != null) {
                    event.setCancelled(true);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_WEAK, 3.0F, 1.4F);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 100, 128, false, false));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 128, false, false));
                    return;
                }
            }
        }

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (item == null) {
                return;
            }

            if (item.getType().equals(Material.LEATHER)) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getEnchantLevel(Enchantment.SHARPNESS) == 1) {
                    meta.removeEnchant(Enchantment.SHARPNESS);
                    player.getInventory().getItemInMainHand().setItemMeta(meta);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.0F, 0.0F);
                    player.launchProjectile(Snowball.class);
                }
            }

            if (item.getType().equals(Material.STICK) && player.isSneaking() && !this.plist.contains(player.getUniqueId())) {
                double nearest = Double.MAX_VALUE;
                Location nearloc = null;
                Snowball snowball = null;
                double range = 1.5;

                for(double distance = 0.0; distance < 4.0; distance += 0.2) {
                    Location particleLocation = eyeLocation.clone().add(direction.clone().multiply(distance));
                    Snowball sb = this.getLocation(player, particleLocation, range);
                    sb.setGlowing(true);
                    this.list.add(sb);
                    sb.setCustomName(ChatColor.YELLOW + "Base Ball");
                    if (sb != null && nearest > sb.getLocation().distance(particleLocation)) {
                        nearloc = particleLocation;
                        snowball = this.getLocation(player, particleLocation, range);
                    }
                }

                if (snowball != null) {
                    final Snowball snowball2 = (Snowball)player.getWorld().spawn(snowball.getLocation(), Snowball.class);
                    double rangepower = range - Math.abs(snowball.getLocation().distance(nearloc));
                    final double power = snowball.getVelocity().length() / 1.5 + rangepower;
                    this.sendActionBar(player, "" + ChatColor.AQUA + (int)(power * 100.0));
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 10.0F, (float)power + 0.5F);
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_DECORATED_POT_PLACE, 10.0F, (float)power + 0.5F);
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_DECORATED_POT_BREAK, 10.0F, (float)power + 0.5F);
                    this.list.remove(snowball);
                    snowball2.setVelocity(player.getLocation().getDirection().multiply(power));
                    snowball.remove();
                    (new BukkitRunnable() {
                        private int count = 0;

                        public void run() {
                            Vector direction = player.getLocation().getDirection();

                            try {
                                snowball2.setVelocity(direction.multiply(power));
                            } catch (Exception var3) {
                                this.cancel();
                            }

                            ++this.count;
                            if (this.count >= 5) {
                                this.cancel();
                            }

                        }
                    }).runTaskTimer(this.m.m, 1L, 1L).getTaskId();
                    (new BukkitRunnable() {
                        private int count = 0;

                        public void run() {
                            try {
                                snowball2.getWorld().spawnParticle(Particle.WAX_OFF, snowball2.getLocation(), 1, 0.0, 0.0, 0.0, 0.0);
                            } catch (Exception var2) {
                                this.cancel();
                            }

                            ++this.count;
                            if (this.count >= 40) {
                                this.cancel();
                            }

                        }
                    }).runTaskTimer(this.m.m, 2L, 2L).getTaskId();
                    this.plist.add(player.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(this.m.m, () -> {
                        this.plist.remove(player.getUniqueId());
                    }, 20L);
                }
            }
        }

    }

    private boolean glove(Player player, float large, float f, boolean b) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType().equals(Material.LEATHER)) {
            if (meta.getEnchantLevel(Enchantment.SHARPNESS) == 1) {
                if (b) {
                    player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.SNOWBALL)});
                }

                return false;
            } else {
                meta.addEnchant(Enchantment.SHARPNESS, 1, true);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, large, f);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, large, f);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_HIT, large, f);
                player.getInventory().getItemInMainHand().setItemMeta(meta);
                return true;
            }
        } else {
            return false;
        }
    }

    private Snowball getLocation(Player player, Location loc, double d) {
        if (player.getName().startsWith(".")) {
            d += 0.5;
        }

        List<Snowball> snowball = new ArrayList();
        Iterator var7 = this.list.iterator();

        Snowball ball;
        while(var7.hasNext()) {
            ball = (Snowball)var7.next();
            if (ball.getWorld().equals(loc.getWorld())) {
                if (ball.isDead()) {
                    snowball.add(ball);
                } else if (ball.getLocation().distance(loc) < d) {
                    return ball;
                }
            }
        }

        var7 = snowball.iterator();

        while(var7.hasNext()) {
            ball = (Snowball)var7.next();
            this.list.remove(ball);
        }

        return null;
    }

    private double getGroundY(Location loc) {
        int l = 0;

        while(loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.WHITE_CARPET || loc.getBlock().getType() == Material.TORCH || loc.getBlock().getType() == Material.IRON_BARS) {
            loc = loc.add(0.0, -1.0, 0.0);
            ++l;
            if (l >= 3) {
                break;
            }
        }

        if (loc.getBlock().getType().name().contains("slab")) {
            loc = loc.add(0.0, 0.5, 0.0);
        }

        int i = (int)loc.getY() + 1;
        return (double)i;
    }

    private void sendActionBar(Player player, String message) {
        TextComponent textComponent = new TextComponent(message);
        if (player != null) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
        } else {
            Iterator var5 = Bukkit.getOnlinePlayers().iterator();

            while(var5.hasNext()) {
                Player p = (Player)var5.next();
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
            }
        }

    }
}
