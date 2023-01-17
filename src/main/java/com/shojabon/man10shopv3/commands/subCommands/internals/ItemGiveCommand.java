package com.shojabon.man10shopv3.commands.subCommands.internals;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ItemGiveCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public ItemGiveCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try{
            SItemStack item = SItemStack.fromBase64(args[2]);
            Player p = Bukkit.getPlayer(UUID.fromString(args[1]));
            if(p == null){
                sender.sendMessage("player_invalid");
                return false;
            }
            int amount = Integer.parseInt(args[3]);
            item.setAmount(amount);
            if(p.getInventory().firstEmpty() == -1){
                sender.sendMessage("inventory_full");
                return false;
            }
            p.getInventory().addItem(item.build());
            sender.sendMessage("success");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            sender.sendMessage("error_internal");
        }
        return true;
    }
}
