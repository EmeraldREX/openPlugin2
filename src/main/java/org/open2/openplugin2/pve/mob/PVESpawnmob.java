package org.open2.openplugin2.pve.mob;

import java.util.Random;
import org.open2.openplugin2.pve.PVEmain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class PVESpawnmob {
    PVEmain pvemain;

    int phase = 0;

    PVEspawninfo spawninfo;

    Boss boss;

    public PVESpawnmob(PVEmain m) {
        this.pvemain = m;
        this.spawninfo = new PVEspawninfo(this.pvemain);
        this.boss = new Boss(m);
    }

    public void spawnmob(boolean b) {
        this.phase = this.pvemain.info.phase.intValue();
        Location center = this.pvemain.info.pvecenter;
        if (b) {
            this.boss.startboss(this.phase, getrandomloc(center));
        } else {
            this.spawninfo.spawnmob(Integer.valueOf(this.phase), getrandomloc(center));
        }
    }

    public Location getrandomloc(Location centerLocation) {
        Random random = new Random();
        World world = centerLocation.getWorld();
        double minRadius = 30.0D;
        double maxRadius = 35.0D;
        while (true) {
            double randomRadius = minRadius + (maxRadius - minRadius) * random.nextDouble();
            double randomAngle = random.nextDouble() * 2.0D * Math.PI;
            double xOffset = randomRadius * Math.cos(randomAngle);
            double zOffset = randomRadius * Math.sin(randomAngle);
            double x = centerLocation.getX() + xOffset;
            double z = centerLocation.getZ() + zOffset;
            double y = world.getHighestBlockYAt((int)x, (int)z);
            y--;
            Location loc = new Location(world, x, y, z);
            Material type = loc.getBlock().getType();
            if (type != Material.WATER && type != Material.STONE) {
                y += 5.0D;
                loc.setY(loc.getY() + 5.0D);
                return loc;
            }
        }
    }
}
