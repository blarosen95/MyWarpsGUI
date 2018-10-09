package com.github.blarosen95.mywarpsgui.GUI;

import com.github.blarosen95.mywarpsgui.Items.ButtonFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

public class WarpCreationGUI {
    private static Inventory warpCreationGUI = Bukkit.createInventory(null, 9, "Warp Category");
    private static ButtonFactory buttonFactory = new ButtonFactory();

    public static boolean openGUI(Player player) {
        warpCreationGUI.setItem(0, buttonFactory.backButton());
        warpCreationGUI.setItem(1, buttonFactory.create(Material.SIGN, "Create a Town Warp", 1));
        warpCreationGUI.setItem(2, buttonFactory.create(Material.WHEAT, "Create a Farm Warp", 1));
        warpCreationGUI.setItem(3, buttonFactory.create(Material.CHEST, "Create a Shop Warp", 1));
        warpCreationGUI.setItem(4, buttonFactory.create(Material.COOKIE, "Create an Other Warp", 1));
        IntStream.range(5, 9).forEach(slot -> warpCreationGUI.setItem(slot, buttonFactory.emptySlot()));
        player.openInventory(warpCreationGUI);
        return true;
    }
}
