package com.shojabon.man10shopv3;

import com.shojabon.man10shopv3.commands.Man10ShopV3Command;
import com.shojabon.man10shopv3.listeners.SignListeners;
import com.shojabon.mcutils.Utils.VaultAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Man10ShopV3 extends JavaPlugin {
    public static String prefix;
    public static FileConfiguration config;
    public static ExecutorService threadPool = Executors.newCachedThreadPool();
    public static VaultAPI vault;

    public static Man10ShopV3API api = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        prefix = getConfig().getString("prefix");
        api = new Man10ShopV3API(this);

        vault = new VaultAPI();

        getServer().getPluginManager().registerEvents(new SignListeners(this), this);
        new Man10ShopV3Command(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
