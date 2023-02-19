package com.shojabon.man10shopv3.commands.subCommands.internals;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class MessageCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public MessageCommand(Man10ShopV3 plugin){
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
            StringBuilder result = new StringBuilder();
            for(String segment : Arrays.copyOfRange(args, 2, args.length)){
                result.append(segment).append(" ");
            }
            p.sendMessage(result.substring(0, result.length()-1));
            sender.sendMessage("success");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            sender.sendMessage("error_internal");
        }
        return true;
    }
}
