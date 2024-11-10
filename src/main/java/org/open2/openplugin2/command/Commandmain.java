//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.command;

import org.jetbrains.annotations.NotNull;
import org.open2.openplugin2.net.Main;
import org.open2.openplugin2.event.Scale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commandmain implements CommandExecutor {
    Main m;

    public Commandmain(Main m) {
        this.m = m;
    }

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        try {
            if (command.getName().equalsIgnoreCase("scale")) {
                sender.sendMessage("成功。");

                return false;
            } else if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage("Open2chプラグイン リロード完了");
                this.m.reloadConfig();
                return true;


            } else {
                sender.sendMessage("Unknown command: " + args[0]);
                return true;
            }
        } catch (Exception e) {
            sender.sendMessage("コマンドの実行中にエラーが発生しました。");
            e.printStackTrace();  // サーバーコンソールにエラー情報を出力
            return false;
        }
    }
}