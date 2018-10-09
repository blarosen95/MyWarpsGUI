package com.github.blarosen95.mywarpsgui.GUI;

import com.github.blarosen95.mywarpsgui.Items.ButtonFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

public class WarpDeleteGUI {
    private static Inventory warpDeleteGUI = Bukkit.createInventory(null, 9, "Delete Warps");
    private static ButtonFactory buttonFactory = new ButtonFactory();

    public WarpDeleteGUI() {
    }

    public static boolean openGUI(Player player) {
        warpDeleteGUI.setItem(0, buttonFactory.backButton());
        warpDeleteGUI.setItem(1, buttonFactory.create(Material.LAVA_BUCKET, "Delete My Warp", 1));
        warpDeleteGUI.setItem(2, buttonFactory.create(Material.LAVA_BUCKET, "Delete Other Players' Warp", 2));
        IntStream.range(3, 9).forEach(slot -> warpDeleteGUI.setItem(slot, buttonFactory.emptySlot()));
        player.openInventory(warpDeleteGUI);
        return true;
    }
}
