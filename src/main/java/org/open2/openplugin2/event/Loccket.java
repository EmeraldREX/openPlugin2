package org.open2.openplugin2.event;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import org.open2.openplugin2.net.Main;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Loccket {
    private LWC lwc;

    private boolean b = false;

    public Loccket(Main m) {
        Plugin lwcPlugin = m.getServer().getPluginManager().getPlugin("LWC");
        if (lwcPlugin instanceof LWCPlugin) {
            this.lwc = ((LWCPlugin)lwcPlugin).getLWC();
            this.b = true;
        } else {
            this.b = false;
        }
    }

    public boolean isturn() {
        return this.b;
    }

    public void remove(Player player, Block b) {
        Protection protection = this.lwc.findProtection(b);
        if (protection != null && protection.getOwner().equals(player.getUniqueId().toString())) {
            protection.remove();
            this.lwc.getPhysicalDatabase().performDatabaseUpdates();
        }
    }
}
