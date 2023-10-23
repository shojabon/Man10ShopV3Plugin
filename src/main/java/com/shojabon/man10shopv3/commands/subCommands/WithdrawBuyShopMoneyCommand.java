package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.menus.EditableShopSelectorMenu;
import com.shojabon.man10shopv3.menus.ShopMainMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class WithdrawBuyShopMoneyCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public WithdrawBuyShopMoneyCommand(Man10ShopV3 plugin){
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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
            Player player = (Player) sender;

            JSONObject result = Man10ShopV3.api.withdrawBuyShopMoney(player);
            if(!result.getString("status").equals("success")){
                Man10ShopV3API.warnMessage(player, result.getString("message"));
            }
        });
        return true;
    }
}
