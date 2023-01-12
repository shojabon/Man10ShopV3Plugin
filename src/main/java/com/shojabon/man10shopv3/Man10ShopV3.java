package com.shojabon.man10shopv3;

import com.shojabon.man10shopv3.commands.Man10ShopV3API;
import com.shojabon.man10shopv3.commands.Man10ShopV3Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Man10ShopV3 extends JavaPlugin {
    public static String prefix;
    public static FileConfiguration config;

    public static Man10ShopV3API api = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        config = getConfig();
        prefix = getConfig().getString("prefix");
        api = new Man10ShopV3API(this);

        Man10ShopV3Command command = new Man10ShopV3Command(this);
        getCommand("mshop").setExecutor(command);
        getCommand("mshop").setTabCompleter(command);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
