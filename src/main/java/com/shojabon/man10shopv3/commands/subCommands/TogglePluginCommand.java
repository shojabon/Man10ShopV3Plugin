package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TogglePluginCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public TogglePluginCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1){
            if(Man10ShopV3.config.getBoolean("pluginEnabled")){
                sender.sendMessage(Man10ShopV3.prefix + "§a§lプラグインは現在有効です");
            }else{
                sender.sendMessage(Man10ShopV3.prefix + "§c§lプラグインは現在無効です");
            }
            return true;
        }else if(args.length == 2){
            boolean current = Boolean.parseBoolean(args[1]);
            if(!current){
                SInventory.closeAllSInventories();
                sender.sendMessage(Man10ShopV3.prefix + "§c§lプラグインは現在無効化されました");
            }else{
                sender.sendMessage(Man10ShopV3.prefix + "§a§lプラグインは現在有効化されました");
            }
            plugin.getConfig().set("pluginEnabled", current);
            plugin.saveConfig();
            Man10ShopV3.config = plugin.getConfig();
        }
        return true;
    }
}
