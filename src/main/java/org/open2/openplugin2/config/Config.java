package org.open2.openplugin2.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Config {
    public String LocationToString(Location l) {
        return String.valueOf(l.getWorld().getName()) + "," + slnum(Double.valueOf(l.getX())) + "," + slnum(Double.valueOf(l.getY())) + "," + slnum(Double.valueOf(l.getZ()));
    }

    public Location StringToLocation(String s) {
        String[] spilit = s.split(",");
        return new Location(Bukkit.getWorld(spilit[0]), number(spilit[1]).doubleValue(), number(spilit[2]).doubleValue(), number(spilit[3]).doubleValue());
    }

    private Double number(String s) {
        return Double.valueOf(Double.parseDouble(s));
    }

    private Double slnum(Double d) {
        return Double.valueOf(Math.floor(d.doubleValue() * 10.0D) / 10.0D);
    }
}
