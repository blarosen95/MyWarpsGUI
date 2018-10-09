package com.github.blarosen95.mywarpsgui.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ButtonFactory {
    public ItemStack create(Material material, String name, int amount, String... lore) {
        ArrayList<String> loreStrings = new ArrayList<>();
        if (lore.length >= 1) {
            loreStrings.addAll(Arrays.asList(lore));
        }
        ItemStack button = new ItemStack(material, amount);
        ItemMeta buttonMeta = button.getItemMeta();
        buttonMeta.setDisplayName(name);
        if (loreStrings.size() >= 1) {
            buttonMeta.setLore(loreStrings);
        }
        button.setItemMeta(buttonMeta);
        return button;
    }

    public ItemStack emptySlot() {
        return new ItemStack(Material.AIR, 1);
    }

    public ItemStack backButton() {
        ItemStack backButton = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA, 1);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(ChatColor.DARK_RED + "←" + ChatColor.RESET);
        backButton.setItemMeta(backMeta);
        return backButton;
    }
}
