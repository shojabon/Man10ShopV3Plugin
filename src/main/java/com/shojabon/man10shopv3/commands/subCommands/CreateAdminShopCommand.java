package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.UUID;

public class CreateAdminShopCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public CreateAdminShopCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!Man10ShopV3.config.getBoolean("pluginEnabled")){
            sender.sendMessage(Man10ShopV3.prefix + "§c§l現在このプラグインは停止中です");
            return false;
        }
        if(! (sender instanceof Player)){
            sender.sendMessage(Man10ShopV3.prefix + "§c§lこのコマンドはプレイヤーのみが実行可能です");
            return false;
        }
        JSONObject request = Man10ShopV3.api.createShop(((Player) sender), args[1],true);
        if(!request.getString("status").equals("success")){
            sender.sendMessage(Man10ShopV3.prefix + "§c§l" + request.getString("message"));
            return false;
        }
        sender.sendMessage(Man10ShopV3.prefix + "§a§l管理者ショップを作りました");
        return true;
    }
}
