package org.open2.openplugin2.blueprint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class BluePrints {
    Map<Location, BlockData> map = new TreeMap<>(
            Comparator.<Location>comparingDouble(Location::getY)
                    .thenComparingDouble(Location::getZ)
                    .thenComparingDouble(Location::getX));

    Location loc1;

    Location loc2;

    List<Material> materials = new ArrayList<>();

    public BluePrints(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public Map<Location, BlockData> getmap() {
        return this.map;
    }

    public void addmap(Location loc, BlockData data) {
        this.map.put(loc, data);
    }

    public void remove(Location loc) {
        this.map.remove(loc);
    }
}
