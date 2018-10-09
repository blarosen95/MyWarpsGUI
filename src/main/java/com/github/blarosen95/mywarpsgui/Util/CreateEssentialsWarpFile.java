package com.github.blarosen95.mywarpsgui.Util;

import com.github.blarosen95.mywarpsgui.Data.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;


public class CreateEssentialsWarpFile {
    private Warp warp;
    private World world;
    private Location location;

    public CreateEssentialsWarpFile(Warp warp, Player player) {
        this.warp = warp;
        this.world = player.getWorld();
        this.location = player.getLocation();
    }

    public void createFileContents() throws IOException {
        File essentialsDataFolder = Bukkit.getServer().getPluginManager().getPlugin("Essentials").getDataFolder();
        String essentialsWarpsFolder = essentialsDataFolder.getAbsolutePath() + File.separator + "warps";
        String warpFile = essentialsWarpsFolder + File.separator + warp.getEssentialsFile();

        System.out.println(warpFile);

        File warpYAMLFile = new File(warpFile);
        PrintWriter dataToWrite = new PrintWriter(new BufferedWriter(new FileWriter(warpYAMLFile)));

        dataToWrite.println(String.format("world: %s", world.getName()));
        dataToWrite.println(String.format("x: %f", location.getX()));
        dataToWrite.println(String.format("y: %f", location.getY()));
        dataToWrite.println(String.format("z: %f", location.getZ()));
        dataToWrite.println(String.format("yaw: %f", location.getYaw()));
        dataToWrite.println(String.format("pitch: %f", location.getPitch()));
        dataToWrite.println(String.format("name: %s", warp.getName()));
        dataToWrite.close();

        location.getWorld().playEffect(location, Effect.FIREWORK_SHOOT, null);
    }
}
