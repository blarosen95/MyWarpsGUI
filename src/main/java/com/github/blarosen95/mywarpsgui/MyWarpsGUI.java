package com.github.blarosen95.mywarpsgui;

import com.github.blarosen95.mywarpsgui.Data.Config;
import com.github.blarosen95.mywarpsgui.Data.SQLiteDatabase;
import com.github.blarosen95.mywarpsgui.Data.Warp;
import com.github.blarosen95.mywarpsgui.GUI.MainGUI;
import com.github.blarosen95.mywarpsgui.Listeners.GUIListener;
import com.github.blarosen95.mywarpsgui.Util.UUIDToName;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;


public final class MyWarpsGUI extends JavaPlugin {

    private static MyWarpsGUI instance;
    private static Config config;
    private static SQLiteDatabase sqLiteDatabase;
    private static UUIDToName uuidToName;

    private MainGUI mainGUI;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        config = new Config();
        sqLiteDatabase = new SQLiteDatabase();
        uuidToName = new UUIDToName();

        mainGUI = new MainGUI();

        getServer().getPluginManager().registerEvents(new GUIListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mywarpsgui_test")) {
            SQLiteDatabase sqLiteDatabase = new SQLiteDatabase();
            try {
                sqLiteDatabase.deleteWarp(new Warp("atest", ((Player) cs).getUniqueId().toString(), "Other", "atest.yml"));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (cmd.getName().equalsIgnoreCase("mywarpsgui")) {
            boolean success = mainGUI.openGUI((Player) cs);
        }
        return true;
    }

    public static MyWarpsGUI getInstance() {
        return instance;
    }

    public static SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public static UUIDToName getUuidToName() {
        return uuidToName;
    }

}
