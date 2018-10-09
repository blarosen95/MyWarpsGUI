package com.github.blarosen95.mywarpsgui.GUI;

import com.github.blarosen95.mywarpsgui.Items.ButtonFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

public class WarpCreateGUI {
    private static Inventory warpCreateGUI = Bukkit.createInventory(null, 9, "Create Warp");
    private static ButtonFactory buttonFactory = new ButtonFactory();

    public WarpCreateGUI() {
    }

    public static boolean openGUI(Player player, String category) {
        switch (category) {
            case "Town":
                warpCreateGUI.setItem(8, buttonFactory.create(Material.MAGENTA_GLAZED_TERRACOTTA, "Town", 1));
                break;
            case "Shop":
                warpCreateGUI.setItem(8, buttonFactory.create(Material.MAGENTA_GLAZED_TERRACOTTA, "Shop", 2));
                break;
            case "Farm":
                warpCreateGUI.setItem(8, buttonFactory.create(Material.MAGENTA_GLAZED_TERRACOTTA, "Farm", 3));
                break;
            case "Other":
                warpCreateGUI.setItem(8, buttonFactory.create(Material.MAGENTA_GLAZED_TERRACOTTA, "Other", 4));
                break;
        }
        warpCreateGUI.setItem(0, buttonFactory.backButton());
        warpCreateGUI.setItem(1, buttonFactory.create(Material.NAME_TAG, "Warp's Name?", 1)); // Opens the player input prompt for the Warp's name.
        warpCreateGUI.setItem(2, buttonFactory.create(Material.ACACIA_STAIRS, "Create Warp", 3,
                ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.RED + "This will cost $1000" + ChatColor.RESET)); // Submits the warp, calling the DB addWarp method.

        IntStream.range(3, 8).forEach(slot -> warpCreateGUI.setItem(slot, buttonFactory.emptySlot())); // Does nothing.
        player.openInventory(warpCreateGUI);
        return true;
    }
}
