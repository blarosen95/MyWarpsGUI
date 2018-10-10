package com.github.blarosen95.mywarpsgui.GUI;

import com.github.blarosen95.mywarpsgui.Data.Warp;
import com.github.blarosen95.mywarpsgui.Items.ButtonFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

public class ConfirmDeleteGUI {
    // TODO: 10/10/2018 This (and other GUI classes) should probably be a local variable instead.
    private static Inventory confirmDeleteGUI = Bukkit.createInventory(null, 9, "Delete Confirmation");
    private static ButtonFactory buttonFactory = new ButtonFactory();

    public static boolean openGUI(Player player, Warp warp) {
        confirmDeleteGUI.setItem(0, buttonFactory.backButton());
        confirmDeleteGUI.setItem(1, buttonFactory.create(Material.NAME_TAG, warp.getName(), 1, "Warp's Name"));
        switch (warp.getCategory()) {
            case "Town":
                confirmDeleteGUI.setItem(2, buttonFactory.create(Material.SIGN, "Town", 1, "Warp's Category"));
                break;
            case "Farm":
                confirmDeleteGUI.setItem(2, buttonFactory.create(Material.WHEAT, "Farm", 1, "Warp's Category"));
                break;
            case "Shop":
                confirmDeleteGUI.setItem(2, buttonFactory.create(Material.CHEST, "Shop", 1, "Warp's Category"));
                break;
            case "Other":
                confirmDeleteGUI.setItem(2, buttonFactory.create(Material.COOKIE, "Other", 1, "Warp's Category"));
                break;
        }
        confirmDeleteGUI.setItem(3, buttonFactory.create(Material.LAVA_BUCKET, "Delete This Warp", 1, "This will refund you $500"));
        IntStream.range(4, 9).forEach(slot -> confirmDeleteGUI.setItem(slot, buttonFactory.emptySlot()));
        player.openInventory(confirmDeleteGUI);
        return true;
    }

    public static boolean openGUI(Player deleter, Warp warp, ItemStack skull) {
        confirmDeleteGUI.setItem(0, buttonFactory.backButton());
        confirmDeleteGUI.setItem(1, skull);
        confirmDeleteGUI.setItem(2, buttonFactory.create(Material.NAME_TAG, warp.getName(), 1, "Warp's Name"));
        switch (warp.getCategory()) {
            case "Town":
                confirmDeleteGUI.setItem(3, buttonFactory.create(Material.SIGN, "Town", 1, "Warp's Category"));
                break;
            case "Farm":
                confirmDeleteGUI.setItem(3, buttonFactory.create(Material.WHEAT, "Farm", 1, "Warp's Category"));
                break;
            case "Shop":
                confirmDeleteGUI.setItem(3, buttonFactory.create(Material.CHEST, "Shop", 1, "Warp's Category"));
                break;
            case "Other":
                confirmDeleteGUI.setItem(3, buttonFactory.create(Material.COOKIE, "Other", 1, "Warp's Category"));
                break;
        }
        confirmDeleteGUI.setItem(4, buttonFactory.create(Material.LAVA_BUCKET, "Delete This Warp", 1, String.format("This will refund %s $500", warp.getCreatorName())));
        IntStream.range(5, 9).forEach(slot -> confirmDeleteGUI.setItem(slot, buttonFactory.emptySlot()));
        deleter.openInventory(confirmDeleteGUI);
        return true;
    }
}
