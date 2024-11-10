package org.open2.openplugin2.pve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class PVEInfo {
    public Location pvecenter;

    public Integer phase = Integer.valueOf(0);

    public UUID villager;

    public BossBar villagerhealth;

    public List<Player> pl = new ArrayList<>();

    public UUID uid = UUID.randomUUID();

    public HashMap<Material, Integer> droppingitems = new HashMap<>();

    public HashMap<Location, BlockInfo> blocks = new HashMap<>();

    public void addphase() {
        this.phase = Integer.valueOf(this.phase.intValue() + 1);
    }

    public Integer getphase() {
        return this.phase;
    }

    public UUID getuid() {
        return this.villager;
    }
}
