package com.github.blarosen95.mywarpsgui.Data;

import java.util.UUID;

public class Warp {
    private String name;
    private String creatorUUID;
    private String category;
    private String essentialsFile;

    public Warp(String name, String creatorUUID, String category, String essentialsFile) {
        this.name = name;
        this.creatorUUID = creatorUUID;
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
}
