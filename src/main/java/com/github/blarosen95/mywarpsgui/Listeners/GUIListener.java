package com.github.blarosen95.mywarpsgui.Listeners;

import com.github.blarosen95.mywarpsgui.Data.SQLiteDatabase;
import com.github.blarosen95.mywarpsgui.Data.Warp;
import com.github.blarosen95.mywarpsgui.GUI.*;
import com.github.blarosen95.mywarpsgui.Items.SkullFactory;
import com.github.blarosen95.mywarpsgui.Items.WarpListItem;
import com.github.blarosen95.mywarpsgui.Materials.ButtonMaterials;
import com.github.blarosen95.mywarpsgui.MyWarpsGUI;
import com.github.blarosen95.mywarpsgui.Util.ListItemPagination;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class GUIListener implements Listener {
    private SQLiteDatabase db = MyWarpsGUI.getSqLiteDatabase();
    private WarpListItem warpListItem = new WarpListItem();
    private SkullFactory skullFactory = new SkullFactory();

    private ListPage listPage = new ListPage();

    private ListPagesGUI listPagesGUI = new ListPagesGUI(); // TODO: 10/10/2018 If issues occur when two or more players open a listPagesGUI at the same time, this can't be instanced.
    private MainGUI mainGUI = new MainGUI();

    public ArrayList<Inventory> pagesList = new ArrayList<>();

    private ArrayList<Inventory> pagesListAll = new ArrayList<>();
    private ArrayList<Inventory> pagesListTown = new ArrayList<>();
    private ArrayList<Inventory> pagesListFarm = new ArrayList<>();
    private ArrayList<Inventory> pagesListShop = new ArrayList<>();
    private ArrayList<Inventory> pagesListOther = new ArrayList<>();

    //    private ArrayList<Inventory> pagesListDeleteOwned = new ArrayList<>();
    private HashMap<String, ArrayList<Inventory>> pagesListDeleteOwned = new HashMap<>();
    //    private ArrayList<Inventory> pagesListDeleteOthers = new ArrayList<>();
    private HashMap<String, ArrayList<Inventory>> pagesListDeleteHeads = new HashMap<>();

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
                    break;
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
            } else if (clickedType.equals(Material.NAME_TAG)) {
                new AnvilGUI(MyWarpsGUI.getInstance(), player, "Name?", (playerBi, reply) -> {
                    Warp tempWarp = new Warp(reply, player.getUniqueId().toString(), player.getName(), inventory.getItem(8).getItemMeta().getDisplayName(), reply + ".yml");
                    ConfirmCreateGUI.openGUI(player, tempWarp);
                    return "test";
                });
            } else if (clickedType.equals(Material.ACACIA_STAIRS)) {
                switch (clicked.getAmount()) {
                    case 1:
                        // TODO: 10/9/2018 Get input for Warp's Name.
                        new AnvilGUI(MyWarpsGUI.getInstance(), player, "Warp's Name", (playerBi, reply) -> {
                            Warp tempWarp = new Warp(reply, player.getUniqueId().toString(), player.getName(), inventory.getItem(8).getItemMeta().getDisplayName(), reply.toLowerCase() + ".yml");
                            // TODO: 10/9/2018 Charge the player $1,000 (if they don't have it, exit menu and inform them why they cant make the warp)
                            // TODO: 10/9/2018 Try to add the warp to the database, if the addWarp call returns any issues, refund the player $1,000 and explain why the warp couldn't be created.
                            return null;
                        });
                        break;
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
            ResultSet resultSet;
            ArrayList<Warp> warps = new ArrayList<>();
            ArrayList<ItemStack> warpItems;
            ArrayList<ItemStack> heads;

            if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA)) {
                MainGUI.openGUI(player);
            } else if (clickedType.equals(Material.LAVA_BUCKET)) {
                try {
                    switch (clicked.getAmount()) {
                        case 1:
                            // TODO: 10/9/2018 Open submenu for deleting this player's warps.
                            resultSet = db.getWarpsByPlayer(player);
                            warps = convertResultSet(resultSet);
                            warpItems = warps.stream().map(warp -> warpListItem.makeListItem(warp, true)).collect(Collectors.toCollection(ArrayList::new));

                            ListItemPagination listItemPagination = new ListItemPagination(warpItems);
                            listPage.createPages(warpItems, listItemPagination, "Delete Page");
                            this.pagesListDeleteOwned.put(player.getUniqueId().toString(), listPage.getListPagesList());
                            listPagesGUI.openGUI(player, pagesListDeleteOwned.get(player.getUniqueId().toString()));
                            break; // TODO: 10/10/2018 if this page isn't right anymore, this is why.

                        case 2:
                            // TODO: 10/9/2018 If the player has the right perms, open submenu for deleting other players' warps. Otherwise exit the menus and send them a warning.
                            if (player.hasPermission("essentials.delwarp")) {
                                // TODO: 10/9/2018 I'd like to have this open up a SkullItem Menu like in ArtisticMaps.
                                heads = db.getHeads();
                                ListItemPagination listItemPaginationHeads = new ListItemPagination(heads);
                                listPage.createPages(heads, listItemPaginationHeads, "Warp Owners Page");
                                this.pagesListDeleteHeads.put(player.getUniqueId().toString(), listPage.getListPagesList());
                                listPagesGUI.openGUI(player, pagesListDeleteHeads.get(player.getUniqueId().toString()));
                            } else {
                                player.sendMessage("You do not have permission to delete warps created by other players.");
                                WarpDeleteGUI.openGUI(player);
                            }
                            break; // TODO: 10/10/2018 If this page isn't right anymore, this is why.
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println(String.format("Caused by player %s", player.getName()));
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
        } else if (inventory.getName().equals("Confirmation") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();

            if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA)) {
                WarpCreationGUI.openGUI(player);
            } else if (clickedType.equals(Material.END_PORTAL_FRAME)) {
                if (MyWarpsGUI.getEconomy().has(player, 1000)) {
                    // TODO: 10/9/2018 Try to create the warp, refunding the player $1000 if the warp can't be created (also send a message to them saying why it can't be created).
                    Warp tempWarp = new Warp(inventory.getItem(1).getItemMeta().getDisplayName(), player.getUniqueId().toString(), player.getName(), inventory.getItem(2).getItemMeta().getDisplayName(), inventory.getItem(1).getItemMeta().getDisplayName().toLowerCase() + ".yml");
                    try {
                        String wasWarpAdded = db.addWarp(tempWarp, player);
                        if (wasWarpAdded != null) {
                            player.closeInventory();
                            player.sendMessage(String.format("Your warp, %s, could not be created because: %s", tempWarp.getName(), wasWarpAdded));
                        } else {
                            player.sendMessage(String.format("%s was successfully created as a new %s Warp! You have been charged $1,000 for the warp.", tempWarp.getName(), tempWarp.getCategory()));
                            MyWarpsGUI.getEconomy().withdrawPlayer(player, 1000);
                            MyWarpsGUI.getInstance().getServer().broadcastMessage(String.format("%s just created \"%s\" a new %s warp!", player.getName(), tempWarp.getName(), tempWarp.getCategory()));
                            player.closeInventory();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        System.out.println("Caused by player: " + player.getName());
                        player.sendMessage("An internal error occurred while attempting to create your warp; you have not been charged.");
                    }
                } else {
                    // TODO: 10/9/2018 Tell the player they don't have enough money to create a warp.
                    player.closeInventory();
                    player.sendMessage("You do not have enough money to make a warp.");
                }
            }
        } else if (inventory.getName().equals("Warp Owners Page") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();
            ArrayList<Warp> warps;
            ArrayList<ItemStack> warpItems;
            ResultSet resultSet;

            if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA) && clicked.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "" + "←" + ChatColor.RESET)) {
                WarpDeleteGUI.openGUI(player);
            } else if (clickedType.equals(Material.PLAYER_HEAD)) {
                // TODO: 10/10/2018 Open up a Delete Page for that player. (needs to have an instance hashmap with this player's uuid as key and the desired owner's uuid as the value)
                SkullMeta skullMeta = (SkullMeta) clicked.getItemMeta();
                try {
                    resultSet = db.getWarpsByPlayer(skullMeta.getOwningPlayer());
                    warps = convertResultSet(resultSet);

                    warpItems = warps.stream().map(warp -> warpListItem.makeListItem(warp, true)).collect(Collectors.toCollection(ArrayList::new));
                    ListItemPagination listItemPagination = new ListItemPagination(warpItems);
                    listPage.createPages(warpItems, listItemPagination, "Delete Page");
                    this.pagesListDeleteOwned.put(skullMeta.getOwningPlayer().getUniqueId().toString(), listPage.getListPagesList());
                    listPagesGUI.openGUI(player, pagesListDeleteOwned.get(skullMeta.getOwningPlayer().getUniqueId().toString()));

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA) && clicked.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + "→" + ChatColor.RESET)) {
                //pull next page.
                listPagesGUI.openGUI(player, pagesListDeleteHeads.get(player.getUniqueId().toString()), inventory.getItem(inventory.getSize() - 1).getAmount());
            } else if (clickedType.equals(Material.BARRIER)) {
                //go back one page.
                if (inventory.getItem(0).getAmount() == 1) {
                    listPagesGUI.openGUI(player, pagesListDeleteHeads.get(player.getUniqueId().toString()), inventory.getItem(0).getAmount() - 1);
                } else {
                    listPagesGUI.openGUI(player, pagesListDeleteHeads.get(player.getUniqueId().toString()), inventory.getItem(0).getAmount() - 2);
                }
            }
        } else if (inventory.getName().equals("Delete Page") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();
            String pageUUID = null;
            //Ensure that the page contains at least one Warp
            if (inventory.getItem(1).getType().equals(Material.SIGN)) {
                try {
                    pageUUID = db.getUUIDByWarpName(inventory.getItem(1).getItemMeta().getDisplayName().replaceAll("§\\w", ""));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA) && clicked.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "" + "←" + ChatColor.RESET)) {
                WarpDeleteGUI.openGUI(player);
            } else if (clickedType.equals(Material.SIGN)) {
                //Using the Sign's name, send the warp to be deleted to a ConfirmDeleteGUI menu (where they can: go back, review the warp, and confirm the deletion and receive $500.)
                try {
                    if (clicked.getItemMeta().getDisplayName().replaceAll("§\\w", "").equals(player.getDisplayName().replaceAll("§\\w", ""))) {
                        ConfirmDeleteGUI.openGUI(player, db.getWarpByName(clicked.getItemMeta().getDisplayName().replaceAll("§\\w", "")));
                    } else {
                        OfflinePlayer justADood = Bukkit.getOfflinePlayer(UUID.fromString(db.getUUIDByWarpName(clicked.getItemMeta().getDisplayName().replaceAll("§\\w", ""))));
                        ConfirmDeleteGUI.openGUI(player, db.getWarpByName(clicked.getItemMeta().getDisplayName().replaceAll("§\\w", "")), skullFactory.getHead(justADood, justADood.getName()));
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA) && clicked.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + "→" + ChatColor.RESET)) {
                //Pull next page.
// todo               listPagesGUI.openGUI(player, pagesListDeleteOwned.get(player.getUniqueId().toString()), inventory.getItem(inventory.getSize() - 1).getAmount());
                listPagesGUI.openGUI(player, pagesListDeleteOwned.get(pageUUID), inventory.getItem(inventory.getSize() - 1).getAmount());
            } else if (clickedType.equals(Material.BARRIER)) {
                //Go back one Page.
                if (inventory.getItem(0).getAmount() >= 1) { // TODO: 10/10/2018 make sure this is the right math (it likely is, so we'll probably fix this and the other barrier checks)
                    // TODO: 10/10/2018 MAJOR: THIS WHOLE PAGE BUTTON CHECK ISN'T NEEDED HERE. BUT!!! IT IS NEEDED FOR THE  NEXT PAGE BUTTONS SINCE YOU
// todo                   listPagesGUI.openGUI(player, pagesListDeleteOwned.get(player.getUniqueId().toString()), inventory.getItem(0).getAmount() - 1);
                    listPagesGUI.openGUI(player, pagesListDeleteOwned.get(pageUUID), inventory.getItem(0).getAmount() - 1);
                } /*else {
//todo                    listPagesGUI.openGUI(player, pagesListDeleteOwned.get(player.getUniqueId().toString()), inventory.getItem(0).getAmount() - 2);
                    listPagesGUI.openGUI(player, pagesListDeleteOwned.get(pageUUID), inventory.getItem(0).getAmount() - 2);
                }*/
            }
        } else if (inventory.getName().equals("Delete Confirmation") && event.getSlotType() != SlotType.OUTSIDE) {
            ItemStack clicked = event.getCurrentItem();
            Material clickedType = clicked.getType();
            ArrayList<Material> catMats = ButtonMaterials.getCategories();
            ArrayList<Material> skullMats = ButtonMaterials.getSkulls();
            Warp warpDeleting;

            if (clickedType.equals(Material.MAGENTA_GLAZED_TERRACOTTA)) {
                if (inventory.getItem(4) == null || inventory.getItem(4).getType().equals(Material.AIR)) {
                    //Then open this player's Delete Warps List.
                    listPagesGUI.openGUI(player, pagesListDeleteOwned.get(player.getUniqueId().toString()));
                } else {
                    //Then open this warp owner's warp delete list.
                    try {
                        listPagesGUI.openGUI(player, pagesListDeleteOwned.get(db.getUUIDByWarpName(inventory.getItem(2).getItemMeta().getDisplayName().replaceAll("§\\w", ""))));
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (catMats.contains(clickedType) || skullMats.contains(clickedType)) {
                System.out.println("Yup that's a category/skull material.");
                event.setCancelled(true);
                System.out.println("And so we've cancelled this click event");
            } else if (clickedType.equals(Material.LAVA_BUCKET)) {
                //If deleter owns this warp refund them, if deleter doesn't own this warp then refund the owner.
                if (skullMats.contains(inventory.getItem(1).getType())) {
                    //Deleter is deleting another player's warp.
                    if (inventory.getItem(1).getType().equals(Material.CREEPER_HEAD)) {
                        player.sendMessage(String.format("Well, we can't refund %s, because they haven't played on this server before.",
                                inventory.getItem(1).getItemMeta().getDisplayName().replaceAll("§\\w", "")));
                        System.out.println(String.format("Player '%s' has never played on the server. No refund was given.",
                                inventory.getItem(1).getItemMeta().getDisplayName().replaceAll("§\\w", "")));
                        // Although the player can't be refunded, their warp is still deletable.
                        try {
                            warpDeleting = db.getWarpByName(inventory.getItem(2).getItemMeta().getDisplayName().replaceAll("§\\w", ""));
                            db.deleteWarp(warpDeleting);
                            player.closeInventory();
                            player.sendMessage(warpDeleting.getDeletionMessage(warpDeleting.getName()));
                        } catch (SQLException | ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //Player has played before so we can delete the warp and refund them.
                        try {
                            warpDeleting = db.getWarpByName(inventory.getItem(2).getItemMeta().getDisplayName().replaceAll("§\\w", ""));
                            db.deleteWarp(warpDeleting);
                            SkullMeta skullMeta = (SkullMeta) inventory.getItem(1).getItemMeta();
                            OfflinePlayer refundee = skullMeta.getOwningPlayer();
                            double balToDate = MyWarpsGUI.getEconomy().getBalance(refundee);
                            MyWarpsGUI.getEconomy().depositPlayer(refundee, 500);
                            player.closeInventory();
                            player.sendMessage(warpDeleting.getDeletionMessage(warpDeleting.getName()));
                            player.sendMessage(String.format("%s had a balance of %f. Balance after refund: %f", refundee.getName(), balToDate, MyWarpsGUI.getEconomy().getBalance(refundee)));
                        } catch (SQLException | ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    //Deleter is deleting their own warp. Refund player $500 if the warp is successfully deleted (from both db and the yaml file in essentials).
                    try {
                        warpDeleting = db.getWarpByName(inventory.getItem(1).getItemMeta().getDisplayName().replaceAll("§\\w", ""));
                        db.deleteWarp(warpDeleting);
                        double balToDate = MyWarpsGUI.getEconomy().getBalance(player);
                        MyWarpsGUI.getEconomy().depositPlayer(player, 500);
                        player.closeInventory();
                        player.sendMessage(warpDeleting.getDeletionMessage());
                        player.sendMessage(String.format("You had a balance of %f. Balance after the refund: %f", balToDate, MyWarpsGUI.getEconomy().getBalance(player)));
                    } catch (SQLException | ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
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
        } else if (inventory.getName().equals("Warp Owners Page") || inventory.getName().equals("Delete Page")) {
            this.listPage = new ListPage();
        }
    }

}
