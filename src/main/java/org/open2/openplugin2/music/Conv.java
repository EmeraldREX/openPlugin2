//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.music;

import org.bukkit.Bukkit;
import org.bukkit.Note;
import org.bukkit.Note.Tone;

public class Conv {
    public Conv() {
    }

    public Note send(int i) {
        while(i < 0) {
            i += 8;
        }

        while(i > 24) {
            i -= 8;
        }

        switch (i) {
            case 0:
                return Note.flat(0, Tone.G);
            case 1:
                return Note.natural(0, Tone.G);
            case 2:
                return Note.flat(0, Tone.A);
            case 3:
                return Note.natural(0, Tone.A);
            case 4:
                return Note.flat(0, Tone.B);
            case 5:
                return Note.flat(0, Tone.C);
            case 6:
                return Note.natural(0, Tone.C);
            case 7:
                return Note.flat(0, Tone.D);
            case 8:
                return Note.natural(0, Tone.D);
            case 9:
                return Note.flat(0, Tone.E);
            case 10:
                return Note.flat(0, Tone.F);
            case 11:
                return Note.natural(0, Tone.F);
            case 12:
                return Note.flat(1, Tone.G);
            case 13:
                return Note.natural(1, Tone.G);
            case 14:
                return Note.flat(1, Tone.A);
            case 15:
                return Note.natural(1, Tone.A);
            case 16:
                return Note.flat(1, Tone.B);
            case 17:
                return Note.flat(1, Tone.C);
            case 18:
                return Note.natural(1, Tone.C);
            case 19:
                return Note.flat(1, Tone.D);
            case 20:
                return Note.flat(1, Tone.D);
            case 21:
                return Note.natural(1, Tone.D);
            case 22:
                return Note.natural(1, Tone.E);
            case 23:
                return Note.natural(1, Tone.F);
            case 24:
                return Note.sharp(2, Tone.F);
            default:
                Bukkit.broadcastMessage(String.valueOf(i));
                return Note.sharp(0, (Tone)null);
        }
    }
}
