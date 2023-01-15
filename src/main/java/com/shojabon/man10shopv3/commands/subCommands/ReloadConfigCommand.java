package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.mcutils.Utils.MySQL.ThreadedMySQLAPI;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public ReloadConfigCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        plugin.reloadConfig();
        Man10ShopV3.config = plugin.getConfig();

        SInventory.closeAllSInventories();

        Man10ShopV3.prefix = Man10ShopV3.config.getString("prefix");

        sender.sendMessage(Man10ShopV3.prefix + "§a§lプラグインがリロードされました");
        return true;
    }
}
