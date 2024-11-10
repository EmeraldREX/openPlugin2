//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.mcmusic;

import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Otoge implements CommandExecutor, TabCompleter {
    Main m;
    File folder;
    BukkitTask r;

    public Otoge(Main m) {
        this.m = m;
        this.folder = new File(m.getDataFolder(), "music");
        if (!this.folder.exists()) {
            this.folder.mkdir();
        }

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (args.length > 0 && this.getlist().contains(args[0])) {
                player.performCommand("mcmusic stop");
                player.performCommand("mcmusic play " + args[0]);
                int tempo = 0;
                final List<Integer> list = new ArrayList();

                try {
                    Throwable var8 = null;
                    Object var9 = null;

                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(this.folder + "/" + args[0] + ".yml"));

                        String line;
                        try {
                            while((line = reader.readLine()) != null) {
                                if (line.startsWith("Line2: ")) {
                                    line = line.replace("Line2: ", "");
                                    line = line.replace("\"", "");
                                    String[] var15;
                                    int var14 = (var15 = line.split(",")).length;

                                    for(int var13 = 0; var13 < var14; ++var13) {
                                        String s = var15[var13];

                                        try {
                                            list.add(Integer.parseInt(s));
                                        } catch (Exception var25) {
                                            list.add(-999);
                                        }
                                    }
                                }

                                if (line.startsWith("Tempo: ")) {
                                    line = line.replace("Tempo: ", "");
                                    line = line.replace("\"", "");
                                    tempo = Integer.parseInt(line);
                                    player.sendMessage("tempo " + line);
                                }
                            }
                        } finally {
                            if (reader != null) {
                                reader.close();
                            }

                        }
                    } catch (Throwable var27) {
                        if (var8 == null) {
                            var8 = var27;
                        } else if (var8 != var27) {
                            var8.addSuppressed(var27);
                        }

                        throw var8;
                    }
                } catch (Throwable var28) {
                    var28.printStackTrace();
                }

                player.sendMessage(list.size() + "size");
                if (tempo != 0 && list != null) {
                    this.r = Bukkit.getScheduler().runTaskTimer(this.m, new Runnable() {
                        int i = 0;

                        public void run() {
                            if (this.i >= list.size()) {
                                Otoge.this.r.cancel();
                            }

                            int c = (Integer)list.get(this.i);
                            if (c != -999) {
                                Location location = player.getLocation();
                                location.setY(location.getY() + 3.0);
                                double scale = 10.2;
                                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB((int)((double)c * scale % 256.0), (int)((double)c * scale * 2.0 % 256.0), (int)((double)c * scale * 3.0 % 256.0)), 0.6F);
                                location.getWorld().spawnParticle(Particle.DUST, location, 5, dust);
                            }

                            ++this.i;
                        }
                    }, 0L, (long)tempo);
                }
            }
        }

        return true;
    }

    private static int scaleTo255(int value) {
        return (int)((double)value / 25.0 * 255.0);
    }

    private List<String> getlist() {
        File[] files = this.folder.listFiles();
        List<String> list = new ArrayList();
        if (files != null) {
            File[] var6 = files;
            int var5 = files.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                File file = var6[var4];
                String name = file.getName();
                int lastDotIndex = name.lastIndexOf(46);
                list.add(name.substring(0, lastDotIndex));
            }
        }

        return list;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return this.getlist();
    }
}
