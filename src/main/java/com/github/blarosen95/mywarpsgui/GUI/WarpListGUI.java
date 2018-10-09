package com.github.blarosen95.mywarpsgui.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpListGUI {
    private static Inventory warpListGUI = Bukkit.createInventory(null, 9, "Warp List GUI"); // TODO: 10/5/2018 Give better title.

    public WarpListGUI() {
    }

    // TODO: 10/9/2018 Add a searchWarpsButton that opens a submenu with buttons for which category of warp to search in (same categories as below).
    // After a category is chosen, text input should be retrieved.
    
    public static boolean openGUI(Player player) {
        ItemStack allButton = new ItemStack(Material.DRAGON_HEAD, 1);
        ItemMeta allMeta = allButton.getItemMeta();
        allMeta.setDisplayName("List All Warps");
        allButton.setItemMeta(allMeta);

        ItemStack townButton = new ItemStack(Material.SIGN, 1);
        ItemMeta townMeta = townButton.getItemMeta();
        townMeta.setDisplayName("List Town Warps");
        townButton.setItemMeta(townMeta);

        ItemStack farmButton = new ItemStack(Material.WHEAT, 1);
        ItemMeta farmMeta = farmButton.getItemMeta();
        farmMeta.setDisplayName("List Farm Warps");
        farmButton.setItemMeta(farmMeta);

        ItemStack shopButton = new ItemStack(Material.CHEST, 1);
        ItemMeta shopMeta = shopButton.getItemMeta();
        shopMeta.setDisplayName("List Shop Warps");
        shopButton.setItemMeta(shopMeta);

        ItemStack otherButton = new ItemStack(Material.COOKIE, 1);
        ItemMeta otherMeta = otherButton.getItemMeta();
        otherMeta.setDisplayName("List Other Warps");
        otherButton.setItemMeta(otherMeta);

        ItemStack emptySlot = new ItemStack(Material.AIR, 1);

        ItemStack backButton = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA, 1);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(ChatColor.DARK_RED + "" + "←" + ChatColor.RESET);
        backButton.setItemMeta(backMeta);

        warpListGUI.setItem(0, backButton); //Back to Parent Menu button
        warpListGUI.setItem(1, allButton); //List All Warps button
        warpListGUI.setItem(2, townButton); //List Town Warps button
        warpListGUI.setItem(3, farmButton); //List Farm Warps button
        warpListGUI.setItem(4, shopButton); //List Shop Warps button
        warpListGUI.setItem(5, otherButton); //List Other Warps button
        warpListGUI.setItem(6, emptySlot); //Nothing
        warpListGUI.setItem(7, emptySlot); //Nothing
        warpListGUI.setItem(8, emptySlot); //Nothing

        player.openInventory(warpListGUI);
        return true;
    }
}
