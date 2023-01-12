package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ItemTakeCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public ItemTakeCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try{
            SItemStack item = SItemStack.fromBase64(args[2]);
            Player p = Bukkit.getPlayer(UUID.fromString(args[1]));
            if(p == null){
                sender.sendMessage("no_player");
                return false;
            }
            int amount = Integer.parseInt(args[3]);
            item.setAmount(1);
            ItemStack completeItem = item.build();
            if(!p.getInventory().containsAtLeast(completeItem, amount)){
                sender.sendMessage("item_lacking");
                return false;
            }
            for(int i = 0; i < amount; i++){
                p.getInventory().remove(completeItem);
            }
            sender.sendMessage("success");
            return true;
        }catch (Exception e){
            sender.sendMessage("error_internal");
        }
        return true;
    }
}
