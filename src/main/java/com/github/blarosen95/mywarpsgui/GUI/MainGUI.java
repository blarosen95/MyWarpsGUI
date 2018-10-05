package com.github.blarosen95.mywarpsgui.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

// TODO: 10/5/2018 Let's refactor to use AnvilGUI (eventually)
public class MainGUI {

    public static Inventory mainGUI = Bukkit.createInventory(null, 9, "Main GUI"); // TODO: 10/5/2018 give this a better title param

    public MainGUI() {
    }

    public static boolean openGUI(Player player) {
        mainGUI.setItem(0, new ItemStack(Material.WRITTEN_BOOK, 1)); //List warps
        mainGUI.setItem(1, new ItemStack(Material.END_PORTAL_FRAME, 1)); //Create warp
        mainGUI.setItem(2, new ItemStack(Material.ANVIL, 1)); //Edit warp
        mainGUI.setItem(3, new ItemStack(Material.LAVA_BUCKET, 1)); //Delete warp
        mainGUI.setItem(4, new ItemStack(Material.AIR, 1)); //Does nothing
        mainGUI.setItem(5, new ItemStack(Material.AIR, 1)); //Does nothing
        mainGUI.setItem(6, new ItemStack(Material.AIR, 1)); //Does nothing
        mainGUI.setItem(7, new ItemStack(Material.AIR, 1)); //Does nothing
        mainGUI.setItem(8, new ItemStack(Material.AIR, 1)); //Does nothing

        player.openInventory(mainGUI);


        return true;
    }
}
