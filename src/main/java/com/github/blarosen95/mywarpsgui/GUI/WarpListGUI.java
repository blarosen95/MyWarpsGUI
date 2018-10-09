package com.github.blarosen95.mywarpsgui.GUI;

import com.github.blarosen95.mywarpsgui.Items.ButtonFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

public class WarpListGUI {
    private static Inventory warpListGUI = Bukkit.createInventory(null, 9, "Warp List GUI"); // TODO: 10/5/2018 Give better title.
    private static ButtonFactory buttonFactory = new ButtonFactory();

    public WarpListGUI() {
    }

    // TODO: 10/9/2018 Add a searchWarpsButton that opens a submenu with buttons for which category of warp to search in (same categories as below).
    // After a category is chosen, text input should be retrieved.

    public static boolean openGUI(Player player) {

        warpListGUI.setItem(0, buttonFactory.backButton()); //Back to Parent Menu button
        warpListGUI.setItem(1, buttonFactory.create(Material.DRAGON_HEAD, "List All Warps", 1)); //List All Warps button
        warpListGUI.setItem(2, buttonFactory.create(Material.SIGN, "List Town Warps", 1)); //List Town Warps button
        warpListGUI.setItem(3, buttonFactory.create(Material.WHEAT, "List Farm Warps", 1)); //List Farm Warps button
        warpListGUI.setItem(4, buttonFactory.create(Material.CHEST, "List Shop Warps", 1)); //List Shop Warps button
        warpListGUI.setItem(5, buttonFactory.create(Material.COOKIE, "List Other Warps", 1)); //List Other Warps button

        IntStream.range(6, 9).forEach(slot -> warpListGUI.setItem(slot, buttonFactory.emptySlot())); //Nothing

        player.openInventory(warpListGUI);
        return true;
    }
}
