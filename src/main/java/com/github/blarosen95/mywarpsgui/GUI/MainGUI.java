package com.github.blarosen95.mywarpsgui.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

// TODO: 10/5/2018 Let's refactor to use AnvilGUI (eventually)
public class MainGUI {

    public Inventory mainGUI = Bukkit.createInventory(null, 9, "Main GUI"); // TODO: 10/5/2018 give this a better title param

    public MainGUI() {
    }

    public boolean openGUI(Player player) {
        ItemStack listButton = new ItemStack(Material.WRITTEN_BOOK, 1);
        ItemMeta listMeta = listButton.getItemMeta();
        listMeta.setDisplayName("List Warps");
        listButton.setItemMeta(listMeta);

        ItemStack createButton = new ItemStack(Material.END_PORTAL_FRAME, 1);
        ItemMeta createMeta = createButton.getItemMeta();
        createMeta.setDisplayName("Create Warp");
        createButton.setItemMeta(createMeta);

        ItemStack editButton = new ItemStack(Material.ANVIL, 1);
        ItemMeta editMeta = editButton.getItemMeta();
        editMeta.setDisplayName("Edit Warp");
        editButton.setItemMeta(editMeta);

        ItemStack deleteButton = new ItemStack(Material.LAVA_BUCKET, 1);
        ItemMeta deleteMeta = deleteButton.getItemMeta();
        deleteMeta.setDisplayName("Delete Warp");
        deleteButton.setItemMeta(deleteMeta);

        ItemStack emptySlot = new ItemStack(Material.AIR, 1);

        mainGUI.setItem(0, listButton); //List warps
        mainGUI.setItem(1, createButton); //Create warp
        mainGUI.setItem(2, editButton); //Edit warp
        mainGUI.setItem(3, deleteButton); //Delete warp
        mainGUI.setItem(4, emptySlot); //Does nothing
        mainGUI.setItem(5, emptySlot); //Does nothing
        mainGUI.setItem(6, emptySlot); //Does nothing
        mainGUI.setItem(7, emptySlot); //Does nothing
        mainGUI.setItem(8, emptySlot); //Does nothing

        player.openInventory(mainGUI);


        return true;
    }
}
