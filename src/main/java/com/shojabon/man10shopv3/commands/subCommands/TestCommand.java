package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.menus.EditableShopSelectorMenu;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class TestCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public TestCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
//            Player player = (Player) sender;
            JSONObject result = Man10ShopV3.api.getPlayerShops(player);
            if(!result.getString("status").equals("success")){
                Man10ShopV3API.warnMessage(player, result.getString("message"));
                return;
            }
            EditableShopSelectorMenu menu = new EditableShopSelectorMenu(player, Man10ShopV3.api.getPlayerShops(player), "その他", plugin);
            menu.setOnClick(e -> {
                Bukkit.broadcastMessage(e);
            });
            menu.open((Player) sender);
        });
        return true;
    }
}
