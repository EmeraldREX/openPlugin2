package org.open2.openplugin2.event;

import java.util.HashMap;
import org.open2.openplugin2.net.Main;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BoostMinecart implements Listener, CommandExecutor {
    HashMap<Integer, Double> speed = new HashMap<>();

    HashMap<Integer, Location> location = new HashMap<>();

    double maxspeed = 3.0D;

    FileConfiguration c;

    Main m;

    Vector flyingmod = new Vector(10.0D, 0.01D, 10.0D);

    Vector noflyingmod = new Vector(1, 1, 1);

    public BoostMinecart(Main plugin, FileConfiguration c) {
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
        plugin.getCommand("boost").setExecutor(this);
        this.c = c;
        this.m = plugin;
        if (c.contains("minecart.speed"))
            this.maxspeed = c.getDouble("minecart.speed");
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        if (event.getVehicle() instanceof Minecart) {
            Minecart cart = (Minecart)event.getVehicle();
            byte b;
            int i;
            Location[] arrayOfLocation;
            for (i = (arrayOfLocation = getAdjacentLocations(cart.getLocation())).length, b = 0; b < i; ) {
                Location loc = arrayOfLocation[b];
                Block block = loc.getBlock();
                Material mat = block.getType();
                if (isSign(mat)) {
                    Sign sign = (Sign)block.getState();
                    String[] text = sign.getLines();
                    if (text[0].equalsIgnoreCase("[open]"))
                        try {
                            double boost = Double.parseDouble(text[1]);
                            if (boost * 0.4D <= this.maxspeed) {
                                this.speed.put(Integer.valueOf(cart.getEntityId()), Double.valueOf(boost));
                                cart.setMaxSpeed(this.maxspeed);
                            } else {
                                sign.setLine(2, "  エラー");
                                sign.setLine(3, "値は"+ this.maxspeed + "以下である必要");
                                sign.update();
                            }
                        } catch (Exception e) {
                            sign.setLine(2, "  エラー");
                            sign.setLine(3, "間違った文");
                            sign.update();
                        }
                }
                b++;
            }
        }
    }

    @EventHandler
    public void exit(VehicleExitEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (vehicle instanceof Minecart) {
            final Minecart minecart = (Minecart)vehicle;
            if (this.speed.containsKey(Integer.valueOf(minecart.getEntityId())))
                (new BukkitRunnable() {
                    public void run() {
                        if (minecart != null &&
                                minecart.getPassengers().size() == 0) {
                            minecart.remove();
                            minecart.getWorld().dropItem(minecart.getLocation(), new ItemStack(Material.MINECART));
                        }
                    }
                }).runTaskLater((Plugin)Main.m, 300L);
        }
    }

    @EventHandler
    public void onMinecartMove(VehicleMoveEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (vehicle instanceof Minecart) {
            Minecart minecart = (Minecart)vehicle;
            Vector velocity = minecart.getVelocity();
            if (this.speed.containsKey(Integer.valueOf(minecart.getEntityId()))) {
                Location loc = minecart.getLocation();
                boolean b = false;
                if (!loc.getBlock().getType().equals(Material.POWERED_RAIL))
                    return;
                if (this.location.containsKey(Integer.valueOf(minecart.getEntityId()))) {
                    if (((Location)this.location.get(Integer.valueOf(minecart.getEntityId()))).equals(loc.getBlock().getLocation()))
                        return;
                } else {
                    b = true;
                }
                this.location.put(Integer.valueOf(minecart.getEntityId()), loc.getBlock().getLocation());
                Vector direction = minecart.getLocation().getDirection();
                double length = direction.length();
                if (Math.abs(length - 1.0D) < 0.001D) {
                    loc.setY(loc.getY() - 1.0D);
                    if (!loc.getBlock().getType().equals(Material.GOLD_BLOCK) &&
                            !loc.getBlock().getType().equals(Material.SOUL_SAND)) {
                        if (Math.abs(velocity.getX()) > 0.1D || Math.abs(velocity.getZ()) > 0.1D) {
                            minecart.setMaxSpeed(((Double)this.speed.get(Integer.valueOf(minecart.getEntityId()))).doubleValue());
                            minecart.setGravity(false);
                            Vector newVelocity = minecart.getVelocity()
                                    .add(direction.multiply(((Double)this.speed.get(Integer.valueOf(minecart.getEntityId()))).doubleValue()));
                            minecart.setVelocity(newVelocity);
                            return;
                        }
                    } else {
                        minecart.setMaxSpeed(0.4D);
                    }
                }
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("コマンド失敗");
            return true;
        }
        this.maxspeed = Double.valueOf(args[0]).doubleValue();
        this.c.set("minecart.speed", Double.valueOf(this.maxspeed));
        this.m.saveConfig();
        sender.sendMessage("マックススピード"+ args[0] + "倍");
        return true;
    }

    public boolean isSign(Material m) {
        return Tag.SIGNS.isTagged(m);
    }

    private Location[] getAdjacentLocations(Location baseLocation) {
        Location[] adjacentLocations = new Location[5];
        adjacentLocations[0] = baseLocation.clone().add(new Vector(0, 1, 0));
        adjacentLocations[1] = baseLocation.clone().add(new Vector(1, 0, 0));
        adjacentLocations[2] = baseLocation.clone().add(new Vector(-1, 0, 0));
        adjacentLocations[3] = baseLocation.clone().add(new Vector(0, 0, 1));
        adjacentLocations[4] = baseLocation.clone().add(new Vector(0, 0, -1));
        return adjacentLocations;
    }
}
