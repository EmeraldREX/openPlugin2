package org.open2.openplugin2.pve;

import org.open2.openplugin2.net.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class PVEmain {
    Main m;

    FileConfiguration config;

    public PVEInfo info;

    public PVEentity pvee;

    PVEDisable pvedisable;

    public PVEmain(Main m, FileConfiguration config) {
        this.m = m;
        this.config = config;
        this.pvee = new PVEentity(this);
        this.pvedisable = new PVEDisable();
        this.pvedisable.disable(this);
        m.getCommand("pvecenter").setExecutor(new PVEcenter(this));
        m.getCommand("pvestart").setExecutor(new PVEstart(this));
    }

    public void disable() {
        this.pvedisable.disable(this);
    }
}
