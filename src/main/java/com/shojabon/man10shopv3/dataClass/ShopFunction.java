package com.shojabon.man10shopv3.dataClass;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.concurrent.Future;

public abstract class ShopFunction {

    public final Man10ShopV3 plugin;
    public final  Man10Shop shop;

    public ShopFunction(Man10Shop shop, Man10ShopV3 plugin){
        this.plugin = plugin;
        this.shop = shop;
    }

    //tools

    public void warn(Player p, String message){
        p.sendMessage(Man10ShopV3.prefix + "§c§l" + message);
    }

    public void success(Player p, String message){
        p.sendMessage(Man10ShopV3.prefix + "§a§l" + message);
    }

    public ShopFunctionDefinition getDefinition(){
        if(!this.getClass().isAnnotationPresent(ShopFunctionDefinition.class)) return null;
        return this.getClass().getAnnotation(ShopFunctionDefinition.class);
    }

    public SInventoryItem getSettingBaseItem(){
        ShopFunctionDefinition def = getDefinition();
        SItemStack item = new SItemStack(def.iconMaterial()).setDisplayName("§a" + def.name());
        if(currentSettingString() !=  null) item.addLore("§d現在設定");
        if(currentSettingString() != null){
            for(String current: currentSettingString().split("\n")){
                item.addLore("§e" + current);
            }
        }
        item.addLore("");
        for(String explanation: def.explanation()){
            item.addLore("§f" + explanation);
        }
        return new SInventoryItem(item.build()).clickable(false);
    }


    // override functions
    public SInventoryItem getSettingItem(Player player, SInventoryItem item){
        return null;
    }

    public String currentSettingString(){
        return null;
    }

    public boolean setVariable(Player p, String key, Object value){
        JSONObject request = shop.setVariable(p, getDefinition().internalFunctionName() + "." + key, value);
        shop.updateData();
        if(!request.getString("status").equals("success")){
            warn(p, request.getString("message"));
            return false;
        }
        return true;
    }

    public boolean requestQueueTask(Player p, String key, Object value){
        JSONObject request = shop.requestQueueTask(p, getDefinition().internalFunctionName() + "." + key, value);
        if(!request.getString("status").equals("success")){
            warn(p, request.getString("message"));
            return false;
        }
        return true;
    }

    public JSONObject getFunctionData(){
        return shop.shopData.getJSONObject(getDefinition().internalFunctionName());
    }


}
