package com.github.blarosen95.mywarpsgui.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WarpListGUI {
    private static Inventory warpListGUI = Bukkit.createInventory(null, 9, "Warp List GUI"); // TODO: 10/5/2018 Give better title.

    public WarpListGUI() {
    }

    public static boolean openGUI(Player player) {
        warpListGUI.setItem(0, new ItemStack(Material.DRAGON_HEAD, 1)); //List All Warps button
        warpListGUI.setItem(1, new ItemStack(Material.SIGN, 1)); //List Town Warps button
        warpListGUI.setItem(2, new ItemStack(Material.WHEAT, 1)); //List Farm Warps button
        warpListGUI.setItem(3, new ItemStack(Material.CHEST, 1)); //List Shop Warps button
        warpListGUI.setItem(4, new ItemStack(Material.COOKIE, 1)); //List Other Warps button
        warpListGUI.setItem(5, new ItemStack(Material.AIR, 1)); //Nothing
        warpListGUI.setItem(6, new ItemStack(Material.AIR, 1)); //Nothing
        warpListGUI.setItem(7, new ItemStack(Material.AIR, 1)); //Nothing
        warpListGUI.setItem(8, new ItemStack(Material.AIR, 1)); //Nothing

        player.openInventory(warpListGUI);
        return true;
    }
}
