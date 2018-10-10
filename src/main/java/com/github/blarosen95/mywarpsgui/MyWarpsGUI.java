package com.github.blarosen95.mywarpsgui;

import com.earth2me.essentials.Essentials;
import com.github.blarosen95.mywarpsgui.Data.Config;
import com.github.blarosen95.mywarpsgui.Data.SQLiteDatabase;
import com.github.blarosen95.mywarpsgui.Data.Warp;
import com.github.blarosen95.mywarpsgui.GUI.MainGUI;
import com.github.blarosen95.mywarpsgui.Listeners.GUIListener;
import com.github.blarosen95.mywarpsgui.Util.UUIDToName;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;


public final class MyWarpsGUI extends JavaPlugin {

    private static MyWarpsGUI instance;
    private static Config config;
    private static SQLiteDatabase sqLiteDatabase;
    private static UUIDToName uuidToName;
    private static Economy economy;
    private static Essentials essentials;

    private MainGUI mainGUI;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] Disabled due to missing dependency: Vault!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupEssentials()) {
            getLogger().severe(String.format("[%s] Disabled due to missing dependency: Essentials!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        config = new Config();
        sqLiteDatabase = new SQLiteDatabase(); // TODO: 10/9/2018 can we make this constructor initialize the database similiar to the Config() constructor?
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
            } catch (SQLException | ClassNotFoundException | IOException e) {
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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    private boolean setupEssentials() {
        if (getServer().getPluginManager().getPlugin("Essentials") == null) return false;
        essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        return true;
    }

    public static Essentials getEssentials() {
        return essentials;
    }
}
