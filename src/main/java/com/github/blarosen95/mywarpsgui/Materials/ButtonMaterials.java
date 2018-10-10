package com.github.blarosen95.mywarpsgui.Materials;

import org.bukkit.Material;

import java.util.ArrayList;

public class ButtonMaterials {

    public static ArrayList<Material> getCategories() {
        ArrayList<Material> catMaterials = new ArrayList<>();

        catMaterials.add(Material.SIGN);
        catMaterials.add(Material.WHEAT);
        catMaterials.add(Material.CHEST);
        catMaterials.add(Material.COOKIE);

        return catMaterials;
    }

    public static ArrayList<Material> getSkulls() {
        ArrayList<Material> skullMaterials = new ArrayList<>();

        skullMaterials.add(Material.PLAYER_HEAD);
        skullMaterials.add(Material.CREEPER_HEAD);

        return skullMaterials;
    }

}
