package org.open2.openplugin2.pve;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PVEDisable {
    public void disable(PVEmain main) {
        if (main.info != null) {
            for (BlockInfo b : main.info.blocks.values()) {
                Location loc = b.loc;
                if (loc != null) {
                    loc.getBlock().setType(b.m);
                    loc.getBlock().setBlockData(b.data);
                }
            }
            deleteentity(main.info.villager);
            for (UUID uid : main.pvee.mobs)
                deleteentity(uid);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (main.info.villagerhealth != null)
                    main.info.villagerhealth.removePlayer(p);
            }
        }
        main.pvee = new PVEentity(main);
        main.info = new PVEInfo();
    }

    private void deleteentity(UUID uid) {
        if (uid != null) {
            Entity entity = Bukkit.getEntity(uid);
            if (entity != null)
                entity.remove();
        }
    }
}
