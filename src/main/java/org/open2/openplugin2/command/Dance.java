//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.open2.openplugin2.net.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.joml.Random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dance implements CommandExecutor, Listener {
    Location center;
    List<Location> blockLocations = new ArrayList();
    Location[] blocks = new Location[9];
    private int tickCount = 20;
    boolean b = true;
    BukkitTask task;
    private Random random = new Random();

    public Dance(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onbreak(BlockBreakEvent event) {
        Iterator var3 = this.blockLocations.iterator();

        while(var3.hasNext()) {
            Location block = (Location)var3.next();
            if (event.getBlock().getLocation().equals(block)) {
                event.setCancelled(true);
            }
        }

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equals("start")) {
                this.tickCount = 20;
                if (this.center != null) {
                    this.task = (new BukkitRunnable() {
                        public void run() {
                            if (Dance.this.b) {
                                Dance.this.b = false;
                                Dance.this.startLoop(Dance.this.tickCount);
                                if (Dance.this.tickCount != 10) {
                                    Dance var10000 = Dance.this;
                                    var10000.tickCount = var10000.tickCount - 1;
                                } else {
                                    Dance.this.tickCount = 20;
                                }
                            }

                        }
                    }).runTaskTimer(Main.m, 1L, 5L);
                }
            }

            if (args[0].equals("center") && sender instanceof Player) {
                Player player = (Player)sender;
                this.center = player.getLocation();
                this.center.setY(this.center.getY() - 1.0);
                this.center = this.center.getBlock().getLocation();
                this.blockLocations.add(this.center.clone());
                this.center.getBlock().setType(Material.GREEN_CONCRETE);
                int xOffset = -1;

                while(true) {
                    if (xOffset > 1) {
                        this.blocks = (Location[])this.blockLocations.toArray(new Location[9]);
                        break;
                    }

                    for(int zOffset = -1; zOffset <= 1; ++zOffset) {
                        Location blockLoc = this.center.clone().add((double)xOffset, 0.0, (double)zOffset);
                        Block block = blockLoc.getBlock();
                        block.setType(Material.GREEN_CONCRETE);
                        this.blockLocations.add(blockLoc.clone());
                    }

                    ++xOffset;
                }
            }

            if (args[0].equals("stop")) {
                this.task.cancel();
            }
        }

        return false;
    }

    private void startLoop(int tick) {
        final Location location = this.blocks[this.random.nextInt(this.blocks.length)];
        (new BukkitRunnable() {
            int loopCount = 0;

            public void run() {
                Location loc;
                int var2;
                int var3;
                Location[] var4;
                Block block;
                label46:
                switch (this.loopCount) {
                    case 0:
                        Dance.this.center.getWorld().playSound(Dance.this.center, Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0F, 1.0F);
                        var3 = (var4 = Dance.this.blocks).length;
                        var2 = 0;

                        while(true) {
                            if (var2 >= var3) {
                                break label46;
                            }

                            loc = var4[var2];
                            if (!loc.equals(location)) {
                                block = loc.getBlock();
                                block.setType(Material.RED_CONCRETE);
                            }

                            ++var2;
                        }
                    case 1:
                        Dance.this.center.getWorld().playSound(Dance.this.center, Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
                        var3 = (var4 = Dance.this.blocks).length;
                        var2 = 0;

                        while(true) {
                            if (var2 >= var3) {
                                break label46;
                            }

                            loc = var4[var2];
                            if (!loc.equals(location)) {
                                block = loc.getBlock();
                                block.setType(Material.AIR);
                            }

                            ++var2;
                        }
                    case 2:
                        Dance.this.center.getWorld().playSound(Dance.this.center, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0F, 1.0F);
                        var3 = (var4 = Dance.this.blocks).length;
                        var2 = 0;

                        while(true) {
                            if (var2 >= var3) {
                                break label46;
                            }

                            loc = var4[var2];
                            if (!loc.equals(location)) {
                                block = loc.getBlock();
                                block.setType(Material.GREEN_CONCRETE);
                            }

                            ++var2;
                        }
                    case 3:
                        Dance.this.center.getWorld().playSound(Dance.this.center, Sound.BLOCK_NOTE_BLOCK_FLUTE, 1.0F, 1.0F);
                        Dance.this.b = true;
                        this.cancel();
                }

                ++this.loopCount;
            }
        }).runTaskTimer(Main.m, 0L, (long)tick);
    }
}
