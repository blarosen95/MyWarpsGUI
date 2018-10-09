package com.github.blarosen95.mywarpsgui.GUI;

import com.github.blarosen95.mywarpsgui.Items.ButtonFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.stream.IntStream;

// TODO: 10/5/2018 Let's refactor to use AnvilGUI (eventually)
public class MainGUI {

    public Inventory mainGUI = Bukkit.createInventory(null, 9, "Main GUI"); // TODO: 10/5/2018 give this a better title param ("Main Menu"?)
    private ButtonFactory buttonFactory = new ButtonFactory();

    public MainGUI() {
    }

    public boolean openGUI(Player player) {
        ItemStack listButton = buttonFactory.create(Material.WRITTEN_BOOK, "List Warps", 1);
        BookMeta listBookMeta = (BookMeta) listButton.getItemMeta();
        listBookMeta.setAuthor(ChatColor.AQUA + "The_Dale_Gribble" + ChatColor.RESET);
        listButton.setItemMeta(listBookMeta);
        mainGUI.setItem(0, listButton); //List warps

        mainGUI.setItem(1, buttonFactory.create(Material.END_PORTAL_FRAME, "Create Warp", 1)); //Create warp
        mainGUI.setItem(2, buttonFactory.create(Material.ANVIL, "Edit Warp", 1)); //Edit warp
        mainGUI.setItem(3, buttonFactory.create(Material.LAVA_BUCKET, "Delete Warp", 1)); //Delete warp
        IntStream.range(4, 9).forEach(slot -> mainGUI.setItem(slot, buttonFactory.emptySlot())); //Does nothing
        player.openInventory(mainGUI);
        return true;
    }
}
