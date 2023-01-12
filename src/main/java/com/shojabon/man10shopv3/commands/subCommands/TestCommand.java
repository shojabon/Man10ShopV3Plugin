package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.commands.Man10ShopV3API;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class TestCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public TestCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        JSONArray array =  Man10ShopV3.api.getPlayerShops((Player)sender);
        for(int i = 0; i < array.length(); i++){
            sender.sendMessage(array.getJSONObject(i).toString());
        }
        return true;
    }
}
