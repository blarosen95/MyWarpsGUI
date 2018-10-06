package com.github.blarosen95.mywarpsgui.Listeners;

import com.github.blarosen95.mywarpsgui.Data.SQLiteDatabase;
import com.github.blarosen95.mywarpsgui.Data.Warp;
import com.github.blarosen95.mywarpsgui.GUI.ListPage;
import com.github.blarosen95.mywarpsgui.GUI.ListPagesGUI;
import com.github.blarosen95.mywarpsgui.GUI.MainGUI;
import com.github.blarosen95.mywarpsgui.GUI.WarpListGUI;
import com.github.blarosen95.mywarpsgui.Items.WarpListItem;
import com.github.blarosen95.mywarpsgui.MyWarpsGUI;
import com.github.blarosen95.mywarpsgui.Util.ListItemPagination;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GUIListener implements Listener {
    public SQLiteDatabase db = MyWarpsGUI.getSqLiteDatabase();
    public WarpListItem warpListItem = new WarpListItem();

    public ListPage listPage = new ListPage();

    public ListPagesGUI listPagesGUI = new ListPagesGUI();
    public MainGUI mainGUI = new MainGUI();

    public ArrayList<Inventory> pagesList = new ArrayList<>();

    public GUIListener() {
    }

    private ArrayList<Warp> convertResultSet(ResultSet resultSet) throws SQLException {

        ArrayList<Warp> warps = new ArrayList<>(); // TODO: 10/5/2018 This might need a new name at some point...

        while (resultSet.next()) {
            int warpID = resultSet.getInt(1); // TODO: 10/5/2018 This column is useless as far as Warp objects go, but might be useful in organizing the end results in a menu?
            String warpName = resultSet.getString(2);
            String warpCategory = resultSet.getString(3);
            String warpUUID = resultSet.getString(4);
            String fileName = resultSet.getString(5);
            Warp currentWarp = new Warp(warpName, warpUUID, warpCategory, fileName);
            warps.add(currentWarp);
        }
        return warps;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        // TODO: 10/5/2018 can we just check if the inventory is an instance of MainGUI (best check: if not instanceof, then return)??
        if (inventory.getName().equals("Main GUI") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();

            if (clickedType == Material.WRITTEN_BOOK) {
                // TODO: 10/5/2018 This will open our WarpListGUI menu.
                WarpListGUI.openGUI((Player) event.getWhoClicked()); // TODO: 10/6/2018 Why bother with the casting? We could use the Player player variable instead.
            } else if (clickedType == Material.END_PORTAL_FRAME) {
                // TODO: 10/5/2018 This will open our WarpCreateGUI menu.
                player.sendMessage("This will open WarpCreateGUI");
            } else if (clickedType == Material.ANVIL) {
                // TODO: 10/5/2018 This will open our WarpEditGUI menu.
                player.sendMessage("This will open WarpEditGUI");
            } else if (clickedType == Material.LAVA_BUCKET) {
                // TODO: 10/5/2018 This will open our WarpDeleteGUI menu.
                player.sendMessage("This will open WarpDeleteGUI");
            }

        } else if (inventory.getName().equals("Warp List GUI") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();
            ResultSet resultSet;
            ArrayList<Warp> warps = new ArrayList<>(); // TODO: 10/5/2018 This might need a new name at some point...
            ArrayList<ItemStack> warpItems; // TODO: 10/5/2018 This might need a new name too...

            try {
                if (clickedType == Material.DRAGON_HEAD) {
                    // TODO: 10/5/2018 This will list all warps
                    resultSet = db.getWarpsInCategory(1);
                    warps = convertResultSet(resultSet);
                    // TODO: 10/5/2018 Process Warp ArrayList here for displaying on the menu.
                    // TODO: 10/5/2018 The new menu to display will need a dynamic size (as in the number of slots should be determined on the fly) so that:
                    // TODO: 10/5/2018 Each Warp gets placed in a slot of the menu (with a custom tooltip generated on the fly too) with enough slots to take all warps in the list...
                    //This will sometimes require making the last (and subsequently the first) slot have a next-page button (subsequently a previous-page button)
                    //This will be simple using basic math such as Modulus
                    warpItems = warps.stream().map(warp -> warpListItem.makeListItem(warp)).collect(Collectors.toCollection(ArrayList::new));
                    ListItemPagination listItemPagination = new ListItemPagination(warpItems);
                    listPage.createPages(warpItems, listItemPagination);
                    this.pagesList = listPage.getListPagesList();

                    // TODO: 10/5/2018 The following is to determine whether or not the list fits on one page and then to call the appropriate menus.
                    if (listItemPagination.isNeedsPagination()) {
                        listPagesGUI.openGUI(player, pagesList);


                        // TODO: 10/5/2018 And then:
                        if (listItemPagination.isNeedsExtraPage()) {
                            // TODO: 10/5/2018 create that final page
                        }

                    } else {
                        listPagesGUI.openGUI(player, pagesList);
                        // TODO: 10/5/2018 Create the appropriate single-page menu.
                    }


                } else if (clickedType == Material.SIGN) {
                    // TODO: 10/5/2018 This will list Town warps
                    resultSet = db.getWarpsInCategory(2);
                    warps = convertResultSet(resultSet);

                } else if (clickedType == Material.WHEAT) {
                    // TODO: 10/5/2018 This will list Farm warps
                    resultSet = db.getWarpsInCategory(3);
                    warps = convertResultSet(resultSet);

                } else if (clickedType == Material.CHEST) {
                    // TODO: 10/5/2018 This will list Shop warps
                    resultSet = db.getWarpsInCategory(4);
                    warps = convertResultSet(resultSet);

                } else if (clickedType == Material.COOKIE) {
                    // TODO: 10/5/2018 This will list Other warps
                    resultSet = db.getWarpsInCategory(5);
                    warps = convertResultSet(resultSet);

                } else if (clickedType == Material.MAGENTA_GLAZED_TERRACOTTA) {
                    // TODO: 10/6/2018 This will go back to parent menu (main menu in this case).
                    mainGUI.openGUI(player);
                }
            } catch (SQLException | ClassNotFoundException e) {
                // TODO: 10/5/2018 Use an ErrorLogger to supplement this thrown message.
                e.printStackTrace();
            }
        } else if (inventory.getName().equals("List Page") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();

            if (clickedType == Material.MAGENTA_GLAZED_TERRACOTTA && clicked.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "" + "←" + ChatColor.RESET)) {
                // TODO: 10/6/2018 Go to parent menu (in this case, parent is "Warp List GUI" menu)
                WarpListGUI.openGUI(player);
            } else if (clickedType == Material.BARRIER) {
                // TODO: 10/6/2018 Go back one page.
                if (inventory.getItem(0).getAmount() == 1) {
                    listPagesGUI.openGUI(player, pagesList, inventory.getItem(0).getAmount() - 1);
                } else {
                    listPagesGUI.openGUI(player, pagesList, inventory.getItem(0).getAmount() - 2);
                }
            } else if (clickedType == Material.MAGENTA_GLAZED_TERRACOTTA && clicked.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + "→" + ChatColor.RESET)) {
                // TODO: 10/6/2018 Go to next page (page to open is the amount value for clicked).
                listPagesGUI.openGUI(player, pagesList, inventory.getItem(inventory.getSize() - 1).getAmount());
            }
        }
        //If it's not our main menu:
        else {
            return;
        }
    }
}
