//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.yakiu;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Setpoint {
        FileConfiguration config;
        YakiuMain m;

        public Setpoint(YakiuMain yakiu) {
                this.config = yakiu.config;
                this.m = yakiu;
        }

        public Integer getpower(Player p) {
                UUID uid = p.getUniqueId();
                if (this.config.contains("Player." + uid + ".power")) {
                        return this.config.getInt("Player." + uid + ".power");
                } else {
                        this.config.set("Player." + uid + ".power", 0);
                        this.saveconfig();
                        return 0;
                }
        }

        public void increespower(Player p, Integer i) {
                UUID uid = p.getUniqueId();
                Integer power = this.getpower(p);
                power = power + i;
                this.config.set("Player." + uid + ".power", power);
                this.saveconfig();
        }

        private void saveconfig() {
                this.m.m.saveConfig();
        }

        public void setloc(Location loc) {
                this.config.set("Location.power", loc);
                this.saveconfig();
        }
}
