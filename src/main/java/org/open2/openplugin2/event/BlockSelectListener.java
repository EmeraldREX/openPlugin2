package org.open2.openplugin2.event;

import java.util.ArrayList;
import java.util.List;
import org.open2.openplugin2.net.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

public class BlockSelectListener implements Listener {
    private Location pos1 = null;

    private Location pos2 = null;

    private Location chair = null;

    List<BlockDisplay> bd;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp())
            return;
        if (event.getHand() == EquipmentSlot.OFF_HAND)
            return;
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand == null)
            return;
        if (hand.getType().equals(Material.STICK))
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block clickedBlock = event.getClickedBlock();
                Location clickedLocation = clickedBlock.getLocation();
                if (this.pos1 == null) {
                    this.pos1 = clickedLocation;
                    player.sendMessage(
                            "Position 1 set to: " + this.pos1.getBlockX() + ", " + this.pos1.getBlockY() + ", " +
                                    this.pos1.getBlockZ());
                } else if (this.pos2 == null) {
                    this.pos2 = clickedLocation;
                    player.sendMessage(
                            "Position 2 set to: " + this.pos2.getBlockX() + ", " + this.pos2.getBlockY() + ", " +
                                    this.pos2.getBlockZ());
                } else if (this.chair == null) {
                    this.chair = clickedLocation;
                    player.sendMessage(
                            "Chair set to: " + this.chair.getBlockX() + ", " + this.chair.getBlockY() + ", " +
                                    this.chair.getBlockZ());
                } else {
                    player.sendMessage("reset");
                    this.pos1 = null;
                    this.pos2 = null;
                    this.chair = null;
                    for (BlockDisplay b : this.bd)
                        b.remove();
                    this.bd = new ArrayList<>();
                }
            }
        if (hand.getType().equals(Material.SADDLE)) {
            for (BlockDisplay b : this.bd)
                b.remove();
            this.bd = new ArrayList<>();
            getBlocksInSelection(player);
        }
    }

    public BlockSelectListener(Main plugin) {
        this.bd = new ArrayList<>();
    }

    private void getBlocksInSelection(Player player) {
        if (this.pos1 != null && this.pos2 != null) {
            int minX = Math.min(this.pos1.getBlockX(), this.pos2.getBlockX());
            int minY = Math.min(this.pos1.getBlockY(), this.pos2.getBlockY());
            int minZ = Math.min(this.pos1.getBlockZ(), this.pos2.getBlockZ());
            int maxX = Math.max(this.pos1.getBlockX(), this.pos2.getBlockX());
            int maxY = Math.max(this.pos1.getBlockY(), this.pos2.getBlockY());
            int maxZ = Math.max(this.pos1.getBlockZ(), this.pos2.getBlockZ());
            Location l = this.chair.clone();
            l.setY(l.getY() + 1.0D);
            BlockDisplay b2 = (BlockDisplay)this.chair.getWorld().spawnEntity(l,
                    EntityType.BLOCK_DISPLAY);
            b2.addPassenger((Entity)player);
            this.bd.add(b2);
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Block block = this.pos1.getWorld().getBlockAt(x, y, z);
                        BlockDisplay b = (BlockDisplay)block.getWorld().spawnEntity(block.getLocation(),
                                EntityType.BLOCK_DISPLAY);
                        b.setBlock(block.getBlockData());
                        this.bd.add(b);
                    }
                }
            }
        } else {
            player.sendMessage("Both positions must be set to get blocks.");
        }
    }

    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR))
            return;
        if (e.getHand() == EquipmentSlot.OFF_HAND)
            return;
        if (hand == null)
            return;
        if (hand.getType() == Material.DIAMOND) {
            Entity vehicle = player.getVehicle();
            if (vehicle != null &&
                    vehicle instanceof BlockDisplay) {
                BlockDisplay blockDisplay = (BlockDisplay)vehicle;
                if (this.bd.contains(blockDisplay))
                    for (BlockDisplay b : this.bd) {
                        Transformation transformation = b.getTransformation();
                        transformation.getTranslation().set(0.0F, 2.0F, 3.0F);
                        b.setInterpolationDuration(80);
                        b.setTransformation(transformation);
                        b.setInterpolationDelay(0);
                    }
            }
        }
    }
}
