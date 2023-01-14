package com.shojabon.man10shopv3.commands.subCommands.internals;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SignUpdateCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public SignUpdateCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try{
                Man10Shop shop = Man10ShopV3.api.getShop(args[1], null);
                if(shop == null) return;

                Bukkit.getScheduler().runTask(plugin, () -> {
                    shop.signFunction.updateSigns();
                });

                sender.sendMessage("success");
            }catch (Exception e){
                sender.sendMessage("error_internal");
            }
        });
        return true;
    }
}
