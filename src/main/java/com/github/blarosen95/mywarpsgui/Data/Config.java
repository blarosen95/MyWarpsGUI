package com.github.blarosen95.mywarpsgui.Data;

import com.github.blarosen95.mywarpsgui.MyWarpsGUI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {
    private final File messagesFile = new File(MyWarpsGUI.getInstance().getDataFolder(), "config.yml");
    String playersOnly;

    public Config() {
        this.reloadMessages();
    }

    public void reloadMessages() {
        //if config.yml doesn't exist
        if (!this.messagesFile.exists()) {
            MyWarpsGUI.getInstance().saveResource("config.yml", true);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(this.messagesFile);
        this.playersOnly = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.PlayersOnly"));
    }
}
