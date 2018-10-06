package com.github.blarosen95.mywarpsgui.GUI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class ListPagesGUI {
    public static Inventory listPagesGUI; // TODO: 10/5/2018 Dynamically created using ListPage object attributes.
    public static ArrayList<Inventory> listPagesList = new ArrayList<>();

    public static void setListPagesList(ArrayList<Inventory> listPagesList) {
        ListPagesGUI.listPagesList = listPagesList;
    }

    public static Inventory getListPagesList(int pageNum) {
        return listPagesList.get(pageNum);
    }

    public ListPagesGUI() {
    }


    public boolean openGUI(Player player, ArrayList<Inventory> listPages) {
        setListPagesList(listPages);
        player.openInventory(getListPagesList(0));
// TODO: 10/6/2018 problem: this isn't an instance variable, the next time a player needs the menu it will delete any and all current players' list menus
        return true;
    }

    public boolean openGUI(Player player, ArrayList<Inventory> listPages, int page) {
        setListPagesList(listPages);
        player.openInventory(getListPagesList(page));

        return true;
    }
}
