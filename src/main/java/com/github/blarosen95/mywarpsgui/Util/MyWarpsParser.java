package com.github.blarosen95.mywarpsgui.Util;

import com.github.blarosen95.mywarpsgui.Data.Warp;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.commons.io.LineIterator;

public class MyWarpsParser {
    private ArrayList<Warp> warpList = new ArrayList<>();

    public MyWarpsParser() throws IOException {
        File warpsYAMLFolder = Bukkit.getPluginManager().getPlugin("MyWarps").getDataFolder();
        String warpsYAMLFile = warpsYAMLFolder.getAbsolutePath() + File.separator + "warps.yml";

        Pattern uuidPattern = Pattern.compile("^\\s*([\\w-]){36}\\s*:\\s*$");
        Pattern namePattern = Pattern.compile("^- (.)+$");

        String tempKey = "";

        if (Files.exists(Paths.get(warpsYAMLFile))) {
            LineIterator iterator = FileUtils.lineIterator(new File(warpsYAMLFile), "UTF-8");

            try {
                while (iterator.hasNext()) {
                    String line = iterator.nextLine();
                    if (uuidPattern.matcher(line).matches()) {
                        //Set tempKey to the UUID from this line.
                        tempKey = line.replace(":", "");
                    } else if (namePattern.matcher(line).matches()) {
                        //Add a new Warp object (using the current tempKey and formats of this line for the name/filename) to our Warp ArrayList.
                        warpList.add(new Warp(line.replace("- ", ""), tempKey, "Other", String.format("%s.yml", line.replace("- ", "").toLowerCase())));
                    }
                }
            } finally {
                iterator.close();
            }
        }

    }

    public ArrayList<Warp> getWarpList() {
        return warpList;
    }
}
