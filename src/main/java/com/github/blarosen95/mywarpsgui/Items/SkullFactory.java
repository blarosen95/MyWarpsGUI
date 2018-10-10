package com.github.blarosen95.mywarpsgui.Items;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullFactory {
    public ItemStack getHead(OfflinePlayer player, String name) {
        if (!player.hasPlayedBefore()) {
            System.out.println(player.getUniqueId() + " has not played before!");
            ItemStack domeUnknown = new ItemStack(Material.CREEPER_HEAD, 1);

            ItemMeta domeUnknownItemMeta = domeUnknown.getItemMeta();
            domeUnknownItemMeta.setDisplayName(name);
            domeUnknown.setItemMeta(domeUnknownItemMeta);
            return domeUnknown;
        }

        ItemStack dome = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta domeMeta = (SkullMeta) dome.getItemMeta();
        domeMeta.setOwningPlayer(player);
        domeMeta.setDisplayName(player.getName());

        System.out.println(String.format("%s: %s (%s)", domeMeta.hasOwner(), domeMeta.getOwningPlayer().getUniqueId(), domeMeta.getOwningPlayer().getName()));

        dome.setItemMeta(domeMeta);
        return dome;
    }
}
