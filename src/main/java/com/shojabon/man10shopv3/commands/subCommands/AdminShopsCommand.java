package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.menus.AdminShopSelectorMenu;
import com.shojabon.man10shopv3.menus.EditableShopSelectorMenu;
import com.shojabon.man10shopv3.menus.ShopMainMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AdminShopsCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public AdminShopsCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(! (sender instanceof Player)){
            sender.sendMessage(Man10ShopV3.prefix + "§c§lこのコマンドはプレイヤーのみが実行可能です");
            return false;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
            Player player = (Player) sender;

            String searchQuery = null;
            if(args.length == 2){
                searchQuery = args[1];
            }

//            JSONObject result = Man10ShopV3.api.getPlayerShops(player, searchQuery);
//            if(!result.getString("status").equals("success")){
//                Man10ShopV3API.warnMessage(player, result.getString("message"));
//                return;
//            }

            AdminShopSelectorMenu menu = new AdminShopSelectorMenu(player, "その他", searchQuery, plugin);

            menu.setOnClick(shopId -> {
                Man10Shop shopInfo = Man10ShopV3.api.getShop(shopId, player);
                if(shopInfo == null){
                    return;
                }
                new ShopMainMenu(player, shopInfo, plugin).open(player);
            });
            menu.open((Player) sender);
        });
        return true;
    }
}
