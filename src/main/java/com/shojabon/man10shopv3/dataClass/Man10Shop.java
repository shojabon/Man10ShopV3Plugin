package com.shojabon.man10shopv3.dataClass;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.shopFunctions.MoneyFunction;
import com.shojabon.man10shopv3.shopFunctions.NameFunction;
import com.shojabon.man10shopv3.shopFunctions.PermissionFunction;
import com.shojabon.man10shopv3.shopFunctions.TargetItemFunction;
import org.bukkit.Bukkit;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shojabon.man10shopv3.Man10ShopV3API.getPlayerJSON;
import static com.shojabon.man10shopv3.Man10ShopV3API.httpRequest;

public class Man10Shop {

    public Man10ShopV3 plugin = (Man10ShopV3) Bukkit.getPluginManager().getPlugin("Man10ShopV3");

    public ArrayList<ShopFunction> functions = new ArrayList<>();

    // functions
    public TargetItemFunction targetItemFunction;
    public NameFunction nameFunction;
    public PermissionFunction permissionFunction;
    public MoneyFunction moneyFunction;

    public JSONObject shopData;

    public Man10Shop(JSONObject shopData){
        this.shopData = shopData;

        //load functions
        for(Field field: getClass().getFields()){
            try{
                if(ShopFunction.class.isAssignableFrom(field.getType())){
                    field.set(this, field.getType().getConstructor(Man10Shop.class, Man10ShopV3.class).newInstance(this, plugin));
                    functions.add((ShopFunction) field.get(this));
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isAdmin(){
        return shopData.getBoolean("admin");
    }

    public String getShopType(){
        return shopData.getString("shopType");
    }

    public String getShopId(){
        return this.shopData.getString("shopId");
    }

    public void updateData(){
        Man10Shop shop = Man10ShopV3.api.getShopInformation(this.getShopId(), null);
        if(shop == null) return;
        shopData = shop.shopData;
    }

    public JSONObject setVariable(Player p, String key, Object value){
        Map<String, Object> payload = new HashMap<>();
        payload.put("shopId", this.getShopId());
        if(p != null){
            payload.put("player", getPlayerJSON(p));
        }
        payload.put("key", key);
        payload.put("value", value);
        JSONObject result = httpRequest(this.plugin.getConfig().getString("api.endpoint") + "/shop/variable/set", "POST", new JSONObject(payload));
        return result;
    }

    public JSONObject requestQueueTask(Player p, String key, Object data){
        Map<String, Object> payload = new HashMap<>();
        payload.put("shopId", this.getShopId());
        if(p != null){
            payload.put("player", getPlayerJSON(p));
        }
        payload.put("key", key);
        payload.put("data", data);
        JSONObject result = httpRequest(this.plugin.getConfig().getString("api.endpoint") + "/shop/queue/add", "POST", new JSONObject(payload));
        return result;
    }



}
