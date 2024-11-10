//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class StartHaihu implements CommandExecutor {
    public StartHaihu() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"/bin/bash", "h.sh"});
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);
        } catch (InterruptedException | IOException var8) {
            var8.printStackTrace();
        }

        return true;
    }
}
