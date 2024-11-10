package org.open2.openplugin2.net;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

class Locs {
    List<Location> locations = new ArrayList<>();

    public void add(Location location) {
        this.locations.add(location);
    }

    public List<Location> get() {
        return this.locations;
    }
}
