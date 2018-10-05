package com.github.blarosen95.mywarpsgui.Util;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ListItemPagination {
    private boolean needsPagination;
    private ArrayList<ItemStack> warpItems;
    private int pagesNeeded;
    private boolean needsExtraPage;
    private int slotsUsedOnExtraPage;

    public ListItemPagination(ArrayList<ItemStack> warpItemsList) {
        this.warpItems = warpItemsList;
        this.needsPagination = this.warpItems.size() >= 25;
        if (this.needsPagination) {
            this.pagesNeeded = this.warpItems.size() / 25;
            if (this.warpItems.size() % 25 == 0) {
                this.needsExtraPage = false;
            } else {
                this.needsExtraPage = true;
                this.slotsUsedOnExtraPage = this.warpItems.size() % 25;
                //Add in the slot for the back button
                this.slotsUsedOnExtraPage += 1;
            }
        }
    }

    public boolean isNeedsPagination() {
        return needsPagination;
    }

    public int getPagesNeeded() {
        return pagesNeeded;
    }

    public boolean isNeedsExtraPage() {
        return needsExtraPage;
    }

    public int getSlotsUsedOnExtraPage() {
        return slotsUsedOnExtraPage;
    }
}
