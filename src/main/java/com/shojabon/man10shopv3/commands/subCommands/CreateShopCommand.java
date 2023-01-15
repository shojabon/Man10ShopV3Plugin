package com.shojabon.man10shopv3.commands.subCommands;

import ToolMenu.ConfirmationMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.mcutils.Utils.BaseUtils;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.UUID;

public class CreateShopCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public CreateShopCommand(Man10ShopV3 plugin){
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
        if(args[1].length() > 64){
            sender.sendMessage(Man10ShopV3.prefix + "§c§lショップ名は64文字以内でなければいけません");
            return false;
        }
        Player p = ((Player)sender);
        int shopPrice = Man10ShopV3.config.getInt("shop.creationPrice");
        if(shopPrice == 0){
            JSONObject request = Man10ShopV3.api.createShop(((Player) sender), args[1],false);
            if(!request.getString("status").equals("success")){
                sender.sendMessage(Man10ShopV3.prefix + "§c§l" + request.getString("message"));
                return false;
            }

            sender.sendMessage(Man10ShopV3.prefix + "§a§lショップを作成しました /" + label + " shopsで管理することができます");
            return true;
        }


        //balance check
        if(Man10ShopV3.vault.getBalance(p.getUniqueId()) < shopPrice){
            sender.sendMessage(Man10ShopV3.prefix + "§c§l所持金が" + BaseUtils.priceString(shopPrice) + "円に達してません");
            return false;
        }

        ConfirmationMenu menu = new ConfirmationMenu(BaseUtils.priceString(shopPrice) + "円支払いますか？", plugin);
        menu.setOnConfirm(e -> {
            if(!Man10ShopV3.vault.withdraw(p.getUniqueId(), shopPrice)){
                sender.sendMessage(Man10ShopV3.prefix + "§c§l内部エラーが発生しました");
                return;
            }

            JSONObject request = Man10ShopV3.api.createShop(((Player) sender), args[1], true);
            if(!request.getString("status").equals("success")){
                sender.sendMessage(Man10ShopV3.prefix + "§c§l" + request.getString("message"));
                return;
            }
            sender.sendMessage(Man10ShopV3.prefix + "§a§lショップを作成しました \n/" + label + " shopsで管理することができます");
            menu.close(p);
        });
        menu.setOnCancel(e -> menu.close(p));
        menu.open(p);

        return true;
    }
}
