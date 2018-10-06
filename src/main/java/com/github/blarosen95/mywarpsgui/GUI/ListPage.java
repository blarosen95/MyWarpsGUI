package com.github.blarosen95.mywarpsgui.GUI;

import com.github.blarosen95.mywarpsgui.Items.WarpListItem;
import com.github.blarosen95.mywarpsgui.Util.ListItemPagination;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.stream.IntStream;

// TODO: 10/5/2018 MAJOR: WE CAN TRY TO CREATE AN ARRAYLIST OF LISTPAGEGUI OBJECTS WITH OUR OPENGUI METHOD'S FOR LOOP AND THEN USE THAT ARRAYLIST FOR THE END-MENU!!!!
public class ListPage {
    private WarpListItem warpListItem = new WarpListItem();
    //private Inventory listPageGUI = Bukkit.createInventory(null, 27, "List Page GUI"); // TODO: 10/5/2018 Give better title.

    private ArrayList<Inventory> listPagesList = new ArrayList<>();

    public ListPage() {
    }

    /**
     * @param warpItems  The List of Warp Items to put into the given pages.
     * @param pagination an instance of ListItemPagination used for divvying out all of the contents amongst all pages.
     * @return true for now, eventually I'd like to change this to return the ArrayList of listPageGUI's!
     */
    @SuppressWarnings("Duplicates")
    public boolean createPages(ArrayList<ItemStack> warpItems, ListItemPagination pagination) {
        Inventory listPageGUI = getBlankPage("List Page");

        if (pagination.isNeedsPagination()) {
            // TODO: 10/5/2018 Fori using pagesNeeded and nesting a loop to create x-y where x is the first item to go in the page and y is that + 25?
            // TODO: 10/5/2018 MAJOR: THE CREATION OF AN EXTRA PAGE MIGHT NEED TO BE PERFORMED OUTSIDE OF THE FOLLOWING LOOP.
            for (int page = 0; page < pagination.getPagesNeeded(); page++) {
                // TODO: 10/5/2018 So... for each page we will need to create 27 slots where:
                // the first slot is back (previous page if page > 0, if page is 0 then it should go back to this menu's parent)
                // The last slot is next (a Magenta Glazed Terracotta with an amount equal to page + 1)
                // The 25 items between back and next are the warpItems

                //If we're on the last page
                if (page == pagination.getPagesNeeded() - 1) {
                    if (pagination.isNeedsExtraPage()) {
                        if (page == 0) {
                            listPageGUI.setItem(0, warpListItem.makeButtonItem(Material.MAGENTA_GLAZED_TERRACOTTA, "←", 1, true));
                        } else {
                            listPageGUI.setItem(0, warpListItem.makeButtonItem(Material.BARRIER, "←", page + 1, true));
                        }
                        Inventory finalListPageGUI3 = listPageGUI;
                        IntStream.range(1, 26).forEach(slot -> finalListPageGUI3.setItem(slot, warpItems.get(slot - 1)));
                        //Clear out the consumed 25 items.
                        warpItems.subList(0, 26).clear();
                        listPageGUI.setItem(26, warpListItem.makeButtonItem(Material.MAGENTA_GLAZED_TERRACOTTA, "→", page + 1, false));
                        listPagesList.add(listPageGUI);
                        listPageGUI = getBlankPage("List Page");
                        // TODO: 10/6/2018 breaks the page: listPageGUI.clear();

                        //Here's our extra page creation:
                        listPageGUI.setItem(0, warpListItem.makeButtonItem(Material.BARRIER, "←", page + 1, true));
                        Inventory finalListPageGUI4 = listPageGUI;
                        IntStream.range(1, warpItems.size()).forEach(slot -> finalListPageGUI4.setItem(slot, warpItems.get(slot - 1))); // TODO: 10/5/2018 if extra pages look off, this might be the reason.
                        Inventory finalListPageGUI5 = listPageGUI;
                        IntStream.range(warpItems.size(), 27).forEach(slot -> finalListPageGUI5.setItem(slot, new ItemStack(Material.AIR, 1)));
                        // TODO: 10/6/2018 the following line was missing and I believe I had simply forgotten to write it:
                        listPagesList.add(listPageGUI);
                        listPageGUI = getBlankPage("List Page");
                        // TODO: 10/6/2018 breaks the page: listPageGUI.clear();
                    } else {
                        if (page == 0) {
                            listPageGUI.setItem(0, warpListItem.makeButtonItem(Material.MAGENTA_GLAZED_TERRACOTTA, "←", 1, true));
                        } else {
                            listPageGUI.setItem(0, warpListItem.makeButtonItem(Material.BARRIER, "←", page + 1, true));
                        }
                        Inventory finalListPageGUI2 = listPageGUI;
                        IntStream.range(1, 26).forEach(slot -> finalListPageGUI2.setItem(slot, warpItems.get(slot - 1)));
                        //Clear out the consumed 25 items
                        warpItems.subList(0, 26).clear();
                        listPagesList.add(listPageGUI);
                        listPageGUI = getBlankPage("List Page");
                        // TODO: 10/6/2018 breaks the page: listPageGUI.clear();
                    }
                } else {
                    if (page == 0) {
                        listPageGUI.setItem(0, warpListItem.makeButtonItem(Material.MAGENTA_GLAZED_TERRACOTTA, "←", 1, true));
                    } else {
                        listPageGUI.setItem(0, warpListItem.makeButtonItem(Material.BARRIER, "←", page + 1, true));
                    }
                    Inventory finalListPageGUI1 = listPageGUI;
                    IntStream.range(1, 26).forEach(slot -> finalListPageGUI1.setItem(slot, warpItems.get(slot - 1)));
                    //Clear out the 25 items we just consumed for our current page.
                    warpItems.subList(0, 26).clear();
                    listPageGUI.setItem(26, warpListItem.makeButtonItem(Material.MAGENTA_GLAZED_TERRACOTTA, "→", page + 1, false));
                    listPagesList.add(listPageGUI); // TODO: 10/5/2018 This is the major revision I mention above the class. If this works, I'll shoot my pet rock.
                    listPageGUI = getBlankPage("List Page");
                    // TODO: 10/6/2018 breaks the page: listPageGUI.clear();
                }
            }
        } else {
            System.out.println(warpItems.size());
            listPageGUI.setItem(0, warpListItem.makeButtonItem(Material.MAGENTA_GLAZED_TERRACOTTA, "←", 1, true));
            Inventory finalListPageGUI = listPageGUI;
            IntStream.range(1, warpItems.size()).forEach(slot -> finalListPageGUI.setItem(slot, warpItems.get(slot - 1)));
            IntStream.range(warpItems.size(), 27).forEach(slot -> finalListPageGUI.setItem(slot, new ItemStack(Material.AIR, 1)));
            listPagesList.add(listPageGUI);
            listPageGUI = getBlankPage("List Page");
            // TODO: 10/6/2018 This might be breaking the page(s): listPageGUI.clear();
        }
        return true;
    }

    public ArrayList<Inventory> getListPagesList() {
        return this.listPagesList;
    }

    private Inventory getBlankPage(String name) {
        return Bukkit.createInventory(null, 27, name);
    }
}
