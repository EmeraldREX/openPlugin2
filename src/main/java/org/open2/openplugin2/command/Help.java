//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.open2.openplugin2.net.LoadText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class Help implements CommandExecutor {
    private final HashMap<String, String> map = new HashMap();
    final String[] list = new String[]{"be", "info.txt", "history", "setting", "chest", "shop", "money"};

    public Help() {
        String[] var4;
        int var3 = (var4 = this.list).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            String s = var4[var2];
            this.map.put(s, (new LoadText()).loadtext("help/" + s));
        }

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String s;
        if (args.length == 0) {
            s = "info.txt";
        } else {
            s = args[0];
        }

        s = s.toLowerCase();
        if (this.map.containsKey(s)) {
            sender.sendMessage((String)this.map.get(s));
        }

        return true;
    }
}
