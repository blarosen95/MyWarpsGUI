package com.github.blarosen95.mywarpsgui.Data;

import com.github.blarosen95.mywarpsgui.Util.UUIDToName;

public class Warp {
    private UUIDToName toName = new UUIDToName();

    private String name;
    private String creatorUUID;
    private String creatorName;
    private String category;
    private String essentialsFile;

    public Warp(String name, String creatorUUID, String category, String essentialsFile) {
        this.name = name;
        this.creatorUUID = creatorUUID;
        this.category = category;
        this.essentialsFile = essentialsFile;

        this.creatorName = convertUUID(creatorUUID);
    }

    public Warp(String name, String creatorUUID, String creatorName, String category, String essentialsFile) {
        this.name = name;
        this.creatorUUID = creatorUUID;
        this.creatorName = creatorName;
        this.category = category;
        this.essentialsFile = essentialsFile;
    }

    public String getName() {
        return name;
    }

    public String getCreatorUUID() {
        return creatorUUID;
    }

    public String getCategory() {
        return category;
    }

    public String getEssentialsFile() {
        return essentialsFile;
    }

    public String getCreatorName() {
        return creatorName;
    }

    /**
     * Uses the Mojang API to get the (most recent) name associated with a given player's UUID.
     * Currently, the method here (and the NameToUUID class used here) are not asynchronous,
     * might change this if the test warps.yml takes too long to convert (especially since Acorn's real warps.yml is larger than mine).
     *
     * @param uuid the UUID to convert from.
     * @return the username currently associated with the given UUID.
     */
    public String convertUUID(String uuid) {
        return toName.getName(uuid);
    }

    public String getWarpAsString() {
        return String.format("%s: by %s (aka %s). Category is: %s, file named %s", name, creatorUUID, creatorName, category, essentialsFile);
    }
}