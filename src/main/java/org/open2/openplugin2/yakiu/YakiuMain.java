//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.yakiu;

import org.open2.openplugin2.net.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class YakiuMain {
    Main m;
    FileConfiguration config;
    Setpoint setpoint;

    public YakiuMain(Main m) {
        this.m = m;
        this.config = m.getConfig();
        this.setpoint = new Setpoint(this);
        new YakiuPower(this);
    }
}
