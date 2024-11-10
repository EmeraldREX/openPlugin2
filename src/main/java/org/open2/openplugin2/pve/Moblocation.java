package org.open2.openplugin2.pve;

import org.bukkit.Location;

public class Moblocation {
    Integer i = Integer.valueOf(0);

    Location location;

    public Moblocation(Location l) {
        this.location = l;
    }

    public Integer setint(Location l) {
        if (this.location.distance(l) < 1.5D) {
            this.i = Integer.valueOf(this.i.intValue() + 1);
        } else {
            this.i = Integer.valueOf(0);
        }
        this.location = l;
        return this.i;
    }

    public Integer reset() {
        this.i = Integer.valueOf(0);
        return this.i;
    }
}
