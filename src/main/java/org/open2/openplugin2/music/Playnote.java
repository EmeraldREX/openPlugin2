//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.music;

import org.open2.openplugin2.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Playnote {
    int tick = 0;
    Conv cdefg = new Conv();

    public Playnote(final Player p) {
        String filePath = "info.txt.txt";
        final HashMap<Integer, List<SoundData>> hash = new HashMap();
        int size = 100;

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Integer i = 0;

            String line;
            while((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("t")) {
                    i = Integer.parseInt(line.substring(1));
                    hash.put(i, new ArrayList());
                    if (size < i) {
                        size = i;
                    }
                } else {
                    List<SoundData> l = (List)hash.get(i);
                    String[] split = line.split(",");
                    SoundData sound = new SoundData(this.getinInstrument(this.getint(split[0])), this.cdefg.send(this.getint(split[1])));
                    sound.i = this.getint(split[1]);
                    l.add(sound);
                    hash.put(i, l);
                }
            }

            bufferedReader.close();
        } catch (IOException var12) {
            var12.printStackTrace();
        }

        int finalSize = size;
        (new BukkitRunnable() {
            public void run() {
                try {
                    if (hash.containsKey(Playnote.this.tick)) {
                        Iterator var2 = ((List)hash.get(Playnote.this.tick)).iterator();

                        while(var2.hasNext()) {
                            SoundData data = (SoundData)var2.next();
                            Playnote.this.playNoteBlockSound(p, data);
                        }
                    }

                    if (finalSize <= Playnote.this.tick) {
                        this.cancel();
                    }
                } catch (Exception var3) {
                    var3.printStackTrace();
                    this.cancel();
                }

                ++Playnote.this.tick;
            }
        }).runTaskTimer(Main.m, 1L, 1L);
    }

    private short getint(String s) {
        return Short.parseShort(s);
    }

    private Instrument getinInstrument(short instrument) {
        Instrument sound = null;
        switch (instrument) {
            case 0:
                sound = Instrument.PIANO;
                break;
            case 1:
                sound = Instrument.BASS_GUITAR;
                break;
            case 2:
                sound = Instrument.BASS_DRUM;
                break;
            case 3:
                sound = Instrument.SNARE_DRUM;
                break;
            case 4:
                sound = Instrument.STICKS;
                break;
            case 5:
                sound = Instrument.GUITAR;
                break;
            case 6:
                sound = Instrument.FLUTE;
                break;
            case 7:
                sound = Instrument.BELL;
                break;
            case 8:
                sound = Instrument.CHIME;
                break;
            case 9:
                sound = Instrument.XYLOPHONE;
                break;
            case 10:
                sound = Instrument.IRON_XYLOPHONE;
                break;
            case 11:
                sound = Instrument.COW_BELL;
                break;
            case 12:
                sound = Instrument.DIDGERIDOO;
                break;
            case 13:
                sound = Instrument.BIT;
                break;
            case 14:
                sound = Instrument.BANJO;
                break;
            case 15:
                sound = Instrument.PLING;
        }

        return sound;
    }

    public void playNoteBlockSound(Player p, SoundData data) {
        Iterator var4 = Bukkit.getOnlinePlayers().iterator();

        while(var4.hasNext()) {
            Player player = (Player)var4.next();
            if (player.getWorld().equals(p.getWorld()) && player.getLocation().distance(p.getLocation()) < 30.0) {
                player.playNote(player.getLocation(), data.instrument, data.note);
            }
        }

    }
}
