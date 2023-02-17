package com.shojabon.man10shopv3.commands.subCommands.internals;

import com.shojabon.man10shopv3.Man10ShopV3;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MoneyGetCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public MoneyGetCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try{
            Player p = Bukkit.getPlayer(UUID.fromString(args[1]));
            if(p == null){
                sender.sendMessage("player_invalid");
                return false;
            }
            sender.sendMessage("success," + Man10ShopV3.vault.getBalance(p.getUniqueId()));
            return true;
        }catch (Exception e){
            sender.sendMessage("error_internal");
        }
        return true;
    }
}
