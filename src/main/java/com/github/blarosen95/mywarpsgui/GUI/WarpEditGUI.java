package com.github.blarosen95.mywarpsgui.GUI;

import com.github.blarosen95.mywarpsgui.Items.ButtonFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

public class WarpEditGUI {
    private static Inventory warpEditGUI = Bukkit.createInventory(null, 9, "Edit Warps");
    private static ButtonFactory buttonFactory = new ButtonFactory();

    public WarpEditGUI() {
    }

    public static boolean openGUI(Player player) {
        warpEditGUI.setItem(0, buttonFactory.backButton());
        warpEditGUI.setItem(1, buttonFactory.create(Material.ANVIL, "My Warps", 1));
        warpEditGUI.setItem(2, buttonFactory.create(Material.ANVIL, "Other Players' Warps", 2));
        IntStream.range(3, 9).forEach(slot -> warpEditGUI.setItem(slot, buttonFactory.emptySlot()));
        player.openInventory(warpEditGUI);
        return true;
    }

}
