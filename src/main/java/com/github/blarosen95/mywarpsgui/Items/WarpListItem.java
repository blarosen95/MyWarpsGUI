package com.github.blarosen95.mywarpsgui.Items;

import com.github.blarosen95.mywarpsgui.Data.Warp;
import com.github.blarosen95.mywarpsgui.MyWarpsGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class WarpListItem {


    public ItemStack makeListItem(Warp warp) {
        ItemStack item = new ItemStack(Material.SIGN, 1);

        //Create item ItemMeta
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + warp.getName() + ChatColor.RESET); // TODO: 10/5/2018 Is RESET necessary? Let's check after it works.

        ArrayList<String> lore = new ArrayList<>();
        // TODO: 10/6/2018 the following json query is fairly slow. I'd rather do this only during both our initial warps.yml parsing and then upon creating new Warp Objects (as an attribute).
        lore.add(MyWarpsGUI.getUuidToName().getName(warp.getCreatorUUID())); //Warp's Creator (by username)

        meta.setLore(lore);

        //Hide vanilla tooltip
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        return item;
    }

    /**
     * @param material material to use for this ItemStack
     * @param name     DisplayName to use
     * @param amount   Amount of item in ItemStack
     * @param isBack   when true, the ChatColor is DARK_RED; GREEN when false.
     * @return returns the item requested
     */
    public ItemStack makeButtonItem(Material material, String name, int amount, boolean isBack) {
        ItemStack item = new ItemStack(material, amount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(isBack ? ChatColor.DARK_RED + "" + name + ChatColor.RESET : ChatColor.GREEN + "" + name + ChatColor.RESET);

        // TODO: 10/5/2018 lore displaying is undesirable anyways, so we should probably just remove this.
        ArrayList<String> lore = new ArrayList<>();
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        return item;
    }

}
