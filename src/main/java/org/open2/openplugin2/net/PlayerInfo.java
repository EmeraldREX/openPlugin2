package org.open2.openplugin2.net;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PlayerInfo {
    public boolean japanese = false;

    public boolean cutall = false;

    public Location sigenlocation;

    public Location belocation;

    public String getsigenstring() {
        return String.valueOf(this.sigenlocation.getWorld().getName()) + "," + this.sigenlocation.getX() + "," + this.sigenlocation.getY() + "," +
                this.sigenlocation.getZ();
    }

    public Location getlocation(String[] list) {
        return new Location(Bukkit.getWorld(list[0]), Double.parseDouble(list[1]), Double.parseDouble(list[2]),
                Double.parseDouble(list[3]));
    }

    public String getbestring() {
        return String.valueOf(this.belocation.getWorld().getName()) + "," + this.belocation.getX() + "," + this.belocation.getY() + "," +
                this.belocation.getZ();
    }
}
