package com.github.blarosen95.mywarpsgui.GUI;

import com.github.blarosen95.mywarpsgui.Data.Warp;
import com.github.blarosen95.mywarpsgui.Items.ButtonFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

public class ConfirmCreateGUI {
    private static Inventory confirmCreateGUI = Bukkit.createInventory(null, 9, "Confirmation");
    private static ButtonFactory buttonFactory = new ButtonFactory();

    public static boolean openGUI(Player player, Warp warpToBe) {
        confirmCreateGUI.setItem(0, buttonFactory.backButton());
        confirmCreateGUI.setItem(1, buttonFactory.create(Material.NAME_TAG, warpToBe.getName(), 1, "Warp's Name"));
        switch (warpToBe.getCategory()) {
            case "Town":
                confirmCreateGUI.setItem(2, buttonFactory.create(Material.SIGN, warpToBe.getCategory(), 1, "Warp's Category"));
                break;
            case "Farm":
                confirmCreateGUI.setItem(2, buttonFactory.create(Material.WHEAT, warpToBe.getCategory(), 1, "Warp's Category"));
                break;
            case "Shop":
                confirmCreateGUI.setItem(2, buttonFactory.create(Material.CHEST, warpToBe.getCategory(), 1, "Warp's Category"));
                break;
            case "Other":
                confirmCreateGUI.setItem(2, buttonFactory.create(Material.COOKIE, warpToBe.getCategory(), 1, "Warp's Category"));
                break;
        }
        confirmCreateGUI.setItem(3, buttonFactory.create(Material.END_PORTAL_FRAME, "Create Warp", 1, "This will cost $1000"));
        IntStream.range(4, 9).forEach(slot -> confirmCreateGUI.setItem(slot, buttonFactory.emptySlot()));
        player.openInventory(confirmCreateGUI);
        return true;
    }
}
