package org.open2.openplugin2.net;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginListener implements PluginMessageListener {
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        String receivedMessage = in.readUTF();
        Bukkit.broadcastMessage("Received message from BungeeCord: " + receivedMessage);
    }

    private Path saveBytesToFile(byte[] data, String fileName) throws IOException {
        Path filePath = Main.m.getDataFolder().toPath().resolve(fileName);
        Files.write(filePath, data, new java.nio.file.OpenOption[0]);
        return filePath;
    }
}
