package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public TestCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//        Player player = (Player) sender;
//        new Thread( () -> {
//            JSONObject result = Man10ShopV3API.httpRequest(this.plugin.getConfig().getString("api.endpoint") + "/shop/allIds", "POST", new JSONObject());
//            List<String> idList = new ArrayList<>();
//            JSONArray ids = result.getJSONArray("data");
//            for(int i = 0; i < ids.length();i++) {
//                idList.add(ids.getString(i));
//            }
//
////            JSONArray ids = new JSONArray();
////            ids.put("f004776d-4631-4af5-9b8c-79856443c6e3");
//            for(String id: idList) {
//                try{
//                    Man10Shop shop = Man10ShopV3.api.getShop(id, null);
//                    ItemStack currentItem = shop.targetItemFunction.getTargetItem().build();
//                    shop.targetItemFunction.setVariable(null, "item", Man10ShopV3API.itemStackToJSON(currentItem));
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                    }
//                }catch (Exception e){
//                    Bukkit.broadcastMessage("error at" + id);
//                }
//            }
//            Bukkit.broadcastMessage("a");
//
//
//        }).start();
        return true;
    }
}
