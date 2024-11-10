//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import org.open2.openplugin2.net.Main;
import org.bukkit.event.Listener;

public class Worldhandler implements Listener {
    public Worldhandler(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
