package com.shojabon.man10shopv3.commands.subCommands.internals;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.menus.storage.ItemStorageMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class StorageMenuOpenCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public StorageMenuOpenCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try{
            Man10Shop shop = Man10ShopV3.api.getShop(args[1], null);
            if(shop == null) {
                sender.sendMessage("shop_invalid");
                return false;
            }
            Player p = Bukkit.getPlayer(UUID.fromString(args[2]));
            if(p == null){
                sender.sendMessage("player_invalid");
                return false;
            }
            ItemStorageMenu menu = new ItemStorageMenu(p, shop, Integer.parseInt(args[3]), plugin);
            menu.open(p);

            sender.sendMessage("success");
        }catch (Exception e){
            sender.sendMessage("error_internal");
        }
        return true;
    }
}
