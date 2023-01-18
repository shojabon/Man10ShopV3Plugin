package com.shojabon.man10shopv3.commands.subCommands.internals;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.menus.action.LootBoxPlayMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LootBoxPlayCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public LootBoxPlayCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try{
                Man10Shop shop = Man10ShopV3.api.getShop(args[1], null);
                if(shop == null) {
                    sender.sendMessage("shop_invalid");
                    return;
                }
                Player p = Bukkit.getPlayer(UUID.fromString(args[2]));
                if(p == null){
                    sender.sendMessage("player_invalid");
                    return;
                }
                sender.sendMessage("success");
                LootBoxPlayMenu menu = new LootBoxPlayMenu(p, shop, args[3], plugin);
                menu.open(p);
            }catch (Exception e){
                e.printStackTrace();
                sender.sendMessage("error_internal");
            }
        });
        return true;
    }
}
