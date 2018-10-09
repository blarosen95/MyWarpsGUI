package com.github.blarosen95.mywarpsgui.Listeners;

import com.github.blarosen95.mywarpsgui.Data.SQLiteDatabase;
import com.github.blarosen95.mywarpsgui.Data.Warp;
import com.github.blarosen95.mywarpsgui.GUI.*;
import com.github.blarosen95.mywarpsgui.Items.WarpListItem;
import com.github.blarosen95.mywarpsgui.MyWarpsGUI;
import com.github.blarosen95.mywarpsgui.Util.ListItemPagination;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GUIListener implements Listener {
    private SQLiteDatabase db = MyWarpsGUI.getSqLiteDatabase();
    private WarpListItem warpListItem = new WarpListItem();

    private ListPage listPage = new ListPage();

    private ListPagesGUI listPagesGUI = new ListPagesGUI();
    private MainGUI mainGUI = new MainGUI();

    public ArrayList<Inventory> pagesList = new ArrayList<>();

    private ArrayList<Inventory> pagesListAll = new ArrayList<>();
    private ArrayList<Inventory> pagesListTown = new ArrayList<>();
    private ArrayList<Inventory> pagesListFarm = new ArrayList<>();
    private ArrayList<Inventory> pagesListShop = new ArrayList<>();
    private ArrayList<Inventory> pagesListOther = new ArrayList<>();

    public GUIListener() {
    }

    private ArrayList<Warp> convertResultSet(ResultSet resultSet) throws SQLException {

        ArrayList<Warp> warps = new ArrayList<>(); // TODO: 10/5/2018 This might need a new name at some point...

        while (resultSet.next()) {
            int warpID = resultSet.getInt(1); // TODO: 10/5/2018 This column is useless as far as Warp objects go, but might be useful in organizing the end results in a menu?
            String warpName = resultSet.getString(2);
            String warpCategory = resultSet.getString(3);
            String warpUUID = resultSet.getString(4);
            String warpCreator = resultSet.getString(5);
            String fileName = resultSet.getString(6);
            Warp currentWarp = new Warp(warpName, warpUUID, warpCreator, warpCategory, fileName);
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
                //This will open our WarpListGUI menu.
                WarpListGUI.openGUI((Player) event.getWhoClicked()); // TODO: 10/6/2018 Why bother with the casting? We could use the Player player variable instead.
            } else if (clickedType == Material.END_PORTAL_FRAME) {
                //This will open our WarpCreateGUI's parent menu.
                //WarpCreateGUI.openGUI(player);
                WarpCreationGUI.openGUI(player);
            } else if (clickedType == Material.ANVIL) {
                //This will open our WarpEditGUI menu.
                WarpEditGUI.openGUI(player);
            } else if (clickedType == Material.LAVA_BUCKET) {
                //This will open our WarpDeleteGUI menu.
                WarpDeleteGUI.openGUI(player);
            }

        } else if (inventory.getName().equals("Warp List GUI") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();
            ResultSet resultSet;
            ArrayList<Warp> warps; // TODO: 10/5/2018 This might need a new name at some point...
            ArrayList<ItemStack> warpItems; // TODO: 10/5/2018 This might need a new name too...

            //ListPage listPage = new ListPage();

            try {
                if (clickedType == Material.DRAGON_HEAD) {
                    resultSet = db.getWarpsInCategory(1);
                    warps = convertResultSet(resultSet);

                    warpItems = warps.stream().map(warp -> warpListItem.makeListItem(warp, true)).collect(Collectors.toCollection(ArrayList::new));
                    ListItemPagination listItemPagination = new ListItemPagination(warpItems);
                    listPage.createPages(warpItems, listItemPagination, "All List Page");
                    this.pagesListAll = listPage.getListPagesList();

                    // TODO: 10/5/2018 The following is to determine whether or not the list fits on one page and then to call the appropriate menus.
                    if (listItemPagination.isNeedsPagination()) {
                        listPagesGUI.openGUI(player, pagesListAll);


                        // TODO: 10/8/2018 Pretty sure there's no longer a need to check for this if statement (and also probably no need for the statement this one is in, nor the following else statement
                        // TODO: 10/5/2018 And then:
                        if (listItemPagination.isNeedsExtraPage()) {
                            // TODO: 10/5/2018 create that final page
                        }

                    } else {
                        listPagesGUI.openGUI(player, pagesListAll);
                        // TODO: 10/5/2018 Create the appropriate single-page menu.
                    }


                } else if (clickedType == Material.SIGN) {
                    // This will list Town warps.
                    resultSet = db.getWarpsInCategory(2);
                    warps = convertResultSet(resultSet);

                    warpItems = warps.stream().map(warp -> warpListItem.makeListItem(warp, false)).collect(Collectors.toCollection(ArrayList::new));
                    ListItemPagination listItemPagination = new ListItemPagination(warpItems);
                    listPage.createPages(warpItems, listItemPagination, "Towns List Page");
                    this.pagesListTown = listPage.getListPagesList();

                    listPagesGUI.openGUI(player, pagesListTown);

                } else if (clickedType == Material.WHEAT) {
                    // This will list Farm warps.
                    resultSet = db.getWarpsInCategory(3);
                    warps = convertResultSet(resultSet);

                    warpItems = warps.stream().map(warp -> warpListItem.makeListItem(warp, false)).collect(Collectors.toCollection(ArrayList::new));
                    ListItemPagination listItemPagination = new ListItemPagination(warpItems);
                    listPage.createPages(warpItems, listItemPagination, "Farms List Page");
                    this.pagesListFarm = listPage.getListPagesList();

                    listPagesGUI.openGUI(player, pagesListFarm);

                } else if (clickedType == Material.CHEST) {
                    // This will list Shop warps.
                    resultSet = db.getWarpsInCategory(4);
                    warps = convertResultSet(resultSet);

                    warpItems = warps.stream().map(warp -> warpListItem.makeListItem(warp, false)).collect(Collectors.toCollection(ArrayList::new));
                    ListItemPagination listItemPagination = new ListItemPagination(warpItems);
                    listPage.createPages(warpItems, listItemPagination, "Shops List Page");
                    this.pagesListShop = listPage.getListPagesList();

                    listPagesGUI.openGUI(player, pagesListShop);

                } else if (clickedType == Material.COOKIE) {
                    // This will list Other warps.
                    resultSet = db.getWarpsInCategory(5);
                    warps = convertResultSet(resultSet);

                    warpItems = warps.stream().map(warp -> warpListItem.makeListItem(warp, false)).collect(Collectors.toCollection(ArrayList::new));
                    ListItemPagination listItemPagination = new ListItemPagination(warpItems);
                    listPage.createPages(warpItems, listItemPagination, "Others List Page");
                    this.pagesListOther = listPage.getListPagesList();

                    listPagesGUI.openGUI(player, pagesListOther);

                } else if (clickedType == Material.MAGENTA_GLAZED_TERRACOTTA) {
                    // This will go back to parent menu (main menu in this case).
                    mainGUI.openGUI(player);
                }
            } catch (SQLException | ClassNotFoundException e) {
                // TODO: 10/5/2018 Use an ErrorLogger to supplement this thrown message.
                e.printStackTrace();
            }
        } else if (inventory.getName().contains("List Page") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();
            ArrayList<Inventory> pagesListOut = new ArrayList<>();

            switch (inventory.getName().replace(" List Page", "")) {
                case "All":
                    pagesListOut = pagesListAll;
                    break;
                case "Towns":
                    pagesListOut = pagesListTown;
                    break;
                case "Farms":
                    pagesListOut = pagesListFarm;
                    break;
                case "Shops":
                    pagesListOut = pagesListShop;
                    break;
                case "Others":
                    pagesListOut = pagesListOther;
            }

            if (clickedType == Material.MAGENTA_GLAZED_TERRACOTTA && clicked.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "" + "←" + ChatColor.RESET)) {
                //Go to parent menu (in this case, parent is "Warp List GUI" menu)
                WarpListGUI.openGUI(player);
                //Reset the pages so that a new list can be created in the same instance.
                this.listPage = new ListPage();
            } else if (clickedType == Material.BARRIER) {
                //Go back one page.
                if (inventory.getItem(0).getAmount() == 1) {
                    listPagesGUI.openGUI(player, pagesListOut, inventory.getItem(0).getAmount() - 1);
                } else {
                    listPagesGUI.openGUI(player, pagesListOut, inventory.getItem(0).getAmount() - 2);
                }
            } else if (clickedType == Material.MAGENTA_GLAZED_TERRACOTTA && clicked.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + "→" + ChatColor.RESET)) {
                //Go to next page (page to open is the amount value for clicked).
                listPagesGUI.openGUI(player, pagesListOut, inventory.getItem(inventory.getSize() - 1).getAmount());
            }
        } else if (inventory.getName().equals("Create Warp") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();

            if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA)) {
                WarpCreationGUI.openGUI(player);
            } else if (clickedType.equals(Material.ACACIA_STAIRS)) {
                switch (clicked.getAmount()) {
                    case 1:
                        System.out.println(player.getName());
                        // TODO: 10/9/2018 Get input for Warp's Name.
                        new AnvilGUI(MyWarpsGUI.getInstance(), player, "Warp's Name", (playerBi, reply) -> {
                            Warp tempWarp = new Warp(reply, player.getUniqueId().toString(), player.getName(), inventory.getItem(8).getItemMeta().getDisplayName(), reply + ".yml");
                            System.out.println(tempWarp.getWarpAsString());
                            // TODO: 10/9/2018 Charge the player $1,000 (if they don't have it, exit menu and inform them why they cant make the warp)
                            // TODO: 10/9/2018 Try to add the warp to the database, if the addWarp call returns any issues, refund the player $1,000 and explain why the warp couldn't be created.
                            return null;
                        });
                    case 3:
                        // TODO: 10/9/2018 Submit new Warp.
                        // TODO: 10/9/2018 The previous case actually handles the whole thing.
                }
            }
        } else if (inventory.getName().equals("Edit Warps") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();

            if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA)) {
                MainGUI.openGUI(player);
            } else if (clickedType.equals(Material.ANVIL)) {
                switch (clicked.getAmount()) {
                    case 1:
                        // TODO: 10/9/2018 Open submenu for editing this player's warps.
                    case 2:
                        // TODO: 10/9/2018 If the player has the right perms, open submenu for editing other players' warps. Otherwise exit the menus and send them a warning.
                }
            }
        } else if (inventory.getName().equals("Delete Warps") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();

            if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA)) {
                MainGUI.openGUI(player);
            } else if (clickedType.equals(Material.LAVA_BUCKET)) {
                switch (clicked.getAmount()) {
                    case 1:
                        // TODO: 10/9/2018 Open submenu for deleting this player's warps.
                    case 2:
                        // TODO: 10/9/2018 If the player has the right perms, open submenu for deleting other players' warps. Otherwise exit the menus and send them a warning.
                }
            }
        } else if (inventory.getName().equals("Warp Category") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();

            if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA)) {
                MainGUI.openGUI(player);
            } else if (clickedType.equals(Material.SIGN)) {
                WarpCreateGUI.openGUI(player, "Town");
            } else if (clickedType.equals(Material.WHEAT)) {
                WarpCreateGUI.openGUI(player, "Farm");
            } else if (clickedType.equals(Material.CHEST)) {
                WarpCreateGUI.openGUI(player, "Shop");
            } else if (clickedType.equals(Material.COOKIE)) {
                WarpCreateGUI.openGUI(player, "Other");
            }
        }
        //If it's not our main menu:
        else {
            return;
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        //If the inventory is a list page (might have uses for this EventHandler for other menus eventually)
        if (inventory.getName().contains("List Page")) {
            //Reset the pages so a new list can be created in the same instance.
            this.listPage = new ListPage();
        }
    }

}
