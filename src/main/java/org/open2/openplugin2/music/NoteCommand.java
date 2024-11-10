//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.music;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoteCommand implements CommandExecutor {
    Playnote playnote;

    public NoteCommand() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if (this.playnote != null) {
            this.playnote.tick = 99999;
        }

        this.playnote = new Playnote(player);
        return true;
    }
}

