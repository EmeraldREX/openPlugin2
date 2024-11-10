package org.open2.openplugin2.pve;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class BlockInfo {
    Material m;

    Location loc;

    BlockData data;

    public BlockInfo(Block b) {
        this.loc = b.getLocation();
        this.data = b.getBlockData();
        this.m = b.getType();
    }
}
