//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FireworkEvent implements Listener, CommandExecutor {
    private final JavaPlugin plugin;
    final Random random = new Random();
    boolean tick = true;
    BukkitTask task;
    String name = null;
    boolean randomfire = false;

    public FireworkEvent(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("firework").setExecutor(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        Firework firework;
        Location location;
        Firework skinImage;
        if (this.randomfire) {
            firework = event.getEntity();
            location = event.getEntity().getLocation();
            FireworkMeta meta = firework.getFireworkMeta();
            if (meta.getDisplayName() != null && !meta.getDisplayName().equals("AAA")) {
                FireworkMeta meta3;
                if (Math.random() < 0.9) {
                    skinImage = (Firework)location.getWorld().spawnEntity(location, EntityType.FIREWORK_ROCKET);
                    meta3 = skinImage.getFireworkMeta();
                    meta3.setPower((new Random()).nextInt(1) + 1);
                    meta3.setDisplayName("AAA");
                    skinImage.setFireworkMeta(meta3);
                }

                skinImage = (Firework)location.getWorld().spawnEntity(location, EntityType.FIREWORK_ROCKET);
                meta3 = skinImage.getFireworkMeta();
                meta3.setPower((new Random()).nextInt(1) + 1);
                meta3.setDisplayName("AAA");
                skinImage.setFireworkMeta(meta3);
            }
        }

        if (this.tick) {
            this.tick = false;
            if (this.task != null) {
                this.task.cancel();
            }

            this.task = (new BukkitRunnable() {
                public void run() {
                    FireworkEvent.this.tick = true;
                }
            }).runTaskLater(this.plugin, 20L);
            firework = event.getEntity();
            location = firework.getLocation();

            try {
                if (firework.getCustomName() != null) {
                    String url = firework.getCustomName();
                    skinImage = null;
                    BufferedImage skinImage1;
                    if (firework.getCustomName().equals("player")) {
                        List<Player> onlinePlayers = new ArrayList(Bukkit.getOnlinePlayers());
                        if (onlinePlayers.isEmpty()) {
                            final Villager entity = (Villager)location.getWorld().spawnEntity(location, EntityType.VILLAGER);
                            entity.setGravity(false);
                            entity.setInvulnerable(true);
                            entity.setGlowing(true);
                            entity.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(2.0);
                            Location finalLocation = location;
                            (new BukkitRunnable() {
                                public void run() {
                                    try {
                                        finalLocation.getWorld().spawnParticle(Particle.EXPLOSION, finalLocation, 0);
                                        finalLocation.getWorld().playSound(finalLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                                        entity.setInvulnerable(false);
                                        entity.damage(40.0);
                                    } catch (Exception var2) {
                                    }

                                }
                            }).runTaskLater(this.plugin, 40L);
                            return;
                        }

                        Player randomPlayer = (Player)onlinePlayers.get((new Random()).nextInt(onlinePlayers.size()));
                        url = "https://crafatar.com/skins/" + randomPlayer.getUniqueId();
                        skinImage1 = SkinUtils.getPlayerSkin(url);
                        if (skinImage1 != null) {
                            skinImage1 = skinImage1.getSubimage(8, 8, 8, 8);
                        }
                    } else {
                        skinImage1 = SkinUtils.getPlayerSkin(url);
                    }

                    if (skinImage != null) {
                        if (skinImage.getHeight() * skinImage.getWidth() < 410) {
                            this.showSkinFirework(location, skinImage1);
                        }

                        return;
                    }
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            event.setCancelled(false);
        }
    }

    @EventHandler
    public void DispenserShoot(BlockDispenseEvent event) {
        if (event.getItem().getType().equals(Material.FIREWORK_ROCKET)) {
            this.name = this.handleFireworkFromDispenser(event.getItem());
        }

    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Firework) {
            final Firework firework = (Firework)event.getEntity();
            event.setCancelled(true);

            try {
                if (event.getEntity().getShooter() instanceof Player) {
                    Player player = (Player)event.getEntity().getShooter();
                    this.handleFireworkFromPlayer(player.getInventory().getItemInMainHand(), firework);
                } else {
                    if (this.randomfire) {
                        FireworkMeta meta = firework.getFireworkMeta();
                        meta.addEffect(this.createRandomFireworkEffect());
                        if (meta.getDisplayName() != null && meta.getDisplayName().equals("AAA")) {
                            meta.setPower((new Random()).nextInt(1) + 1);
                        } else {
                            meta.setPower((new Random()).nextInt(3) + 4);
                        }

                        firework.setFireworkMeta(meta);
                        (new BukkitRunnable() {
                            public void run() {
                                FireworkEvent.this.setRandomVelocity(firework);
                            }
                        }).runTaskLater(this.plugin, 1L);
                    }

                    firework.setCustomName(this.name);
                    this.name = null;
                }
            } catch (Exception var4) {
            }

            event.setCancelled(false);
        }

    }

    private String handleFireworkFromDispenser(ItemStack item) {
        String name = null;

        try {
            String n = item.getItemMeta().getDisplayName();
            if (n != null) {
                name = n;
            }
        } catch (Exception var4) {
        }

        return name;
    }

    private void handleFireworkFromPlayer(ItemStack item, Firework firework) {
        if (item != null && item.getType() == Material.FIREWORK_ROCKET) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String itemName = meta.getDisplayName();
                if (itemName != null) {
                    firework.setCustomName(itemName);
                }
            }
        }

    }

    private void setRandomVelocity(Firework firework) {
        Random random = new Random();
        Vector randomDirection = new Vector(random.nextDouble() * 0.4 - 0.2, random.nextDouble() * 0.3 + 0.3, random.nextDouble() * 0.4 - 0.2);
        firework.setVelocity(randomDirection);
    }

    public void showSkinFirework(Location location, BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        location.setY(location.getY());
        double baseX = location.getX();
        double baseY = location.getY();
        double baseZ = location.getZ();
        double spacing = 0.3;
        final List<ParticleData> particleDataList = new ArrayList();
        double centerX = (double)width / 2.0;
        double centerY = (double)height / 2.0;

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb, true);
                if (color.getAlpha() > 0 && (color.getRed() > -1 || color.getGreen() > -1 || color.getBlue() > -1)) {
                    Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()), 1.0F);
                    Location dustLocation = new Location(location.getWorld(), baseX + ((double)x - centerX) * spacing, baseY - ((double)y - centerY) * spacing, baseZ);
                    particleDataList.add(new ParticleData(dustLocation, dustOptions));
                }
            }
        }

        (new BukkitRunnable() {
            private int ticks = 0;
            private final int durationTicks = 100;

            public void run() {
                if (this.ticks >= 100) {
                    this.cancel();
                } else {
                    Iterator var2 = particleDataList.iterator();

                    while(var2.hasNext()) {
                        ParticleData particleData = (ParticleData)var2.next();
                        Location location = particleData.getLocation();
                        Particle.DustOptions dustOptions = particleData.getDustOptions();
                        location.getWorld().spawnParticle(Particle.DUST, location, 1, 0.1, 0.1, 0.1, 0.0, dustOptions, true);
                    }

                    ++this.ticks;
                }
            }
        }).runTaskTimer(this.plugin, 0L, 1L);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length > 0) {
                String imageUrl = "";
                if (args[0].equalsIgnoreCase("url")) {
                    if (args.length > 1) {
                        imageUrl = args[1];

                        try {
                            BufferedImage image = SkinUtils.getPlayerSkin(imageUrl);
                            if (this.count(image)) {
                                if (player.getInventory().getItemInMainHand().getType().equals(Material.FIREWORK_ROCKET)) {
                                    player.sendMessage("アイテム名を変更しました。");
                                    ItemStack item = player.getInventory().getItemInMainHand();
                                    ItemMeta meta = item.getItemMeta();
                                    meta.setDisplayName(imageUrl);
                                    item.setItemMeta(meta);
                                } else {
                                    player.sendMessage("メインハンドに花火ロケットを持っている必要があります。");
                                }
                            } else {
                                player.sendMessage("画像は20x20以下である必要があります。");
                            }

                            return true;
                        } catch (Exception var10) {
                            player.sendMessage("画像の取得に失敗しました。URLを確認してください。");
                            return false;
                        }
                    }

                    player.sendMessage("画像のURLを指定してください。");
                    return false;
                }

                if (args[0].equalsIgnoreCase("player")) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("player");
                    player.sendMessage("アイテム名を変更しました。");
                    item.setItemMeta(meta);
                    return true;
                }

                if (args[0].equalsIgnoreCase("random")) {
                    this.randomfire = !this.randomfire;
                    if (this.randomfire) {
                        player.sendMessage("ランダムにしました");
                    } else {
                        player.sendMessage("通常にしました");
                    }

                    return true;
                }

                player.sendMessage("第一引数は 'url' か 'player' である必要があります。");
                return false;
            }

            player.sendMessage("第一引数を指定してください。");
        } else {
            sender.sendMessage("このコマンドはプレイヤーのみが実行できます。");
        }

        return false;
    }

    private boolean count(BufferedImage image) {
        int i = 0;
        int width = image.getWidth();
        int height = image.getHeight();

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb, true);
                if (color.getAlpha() > 0 && (color.getRed() > -1 || color.getGreen() > -1 || color.getBlue() > -1)) {
                    ++i;
                }
            }
        }

        if (i < 400) {
            return true;
        } else {
            return false;
        }
    }

    private FireworkEffect createRandomFireworkEffect() {
        Random random = new Random();
        Type type = Type.values()[random.nextInt(Type.values().length)];
        org.bukkit.Color color = org.bukkit.Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        org.bukkit.Color[] fadeColors = new org.bukkit.Color[]{org.bukkit.Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)), org.bukkit.Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256))};
        return FireworkEffect.builder().with(type).withColor(color).withFade(fadeColors[random.nextInt(fadeColors.length)]).trail(random.nextBoolean()).flicker(random.nextBoolean()).build();
    }

    private static class ParticleData {
        private final Location location;
        private final Particle.DustOptions dustOptions;

        public ParticleData(Location location, Particle.DustOptions dustOptions) {
            this.location = location;
            this.dustOptions = dustOptions;
        }

        public Location getLocation() {
            return this.location;
        }

        public Particle.DustOptions getDustOptions() {
            return this.dustOptions;
        }
    }
}
