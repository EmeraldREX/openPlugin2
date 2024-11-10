package org.open2.openplugin2.command.bet;

import java.util.UUID;
import org.bukkit.inventory.Inventory;

public class BetPlayer {
    UUID betto;

    Inventory inv;

    public BetPlayer(UUID betto) {
        this.betto = betto;
    }
}
