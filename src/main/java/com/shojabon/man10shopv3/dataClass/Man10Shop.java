package com.shojabon.man10shopv3.dataClass;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.menus.action.BuySellActionMenu;
import com.shojabon.man10shopv3.shopFunctions.*;
import com.shojabon.man10shopv3.shopFunctions.allowedToUse.DisabledFromFunction;
import com.shojabon.man10shopv3.shopFunctions.allowedToUse.EnabledFromFunction;
import com.shojabon.man10shopv3.shopFunctions.allowedToUse.LimitUseFunction;
import com.shojabon.man10shopv3.shopFunctions.allowedToUse.WeekDayToggleFunction;
import com.shojabon.man10shopv3.shopFunctions.general.*;
import com.shojabon.man10shopv3.shopFunctions.storage.StorageCapFunction;
import com.shojabon.man10shopv3.shopFunctions.storage.StorageFunction;
import com.shojabon.man10shopv3.shopFunctions.storage.StorageRefillFunction;
import com.shojabon.man10shopv3.shopFunctions.tradeAmount.CoolDownFunction;
import com.shojabon.man10shopv3.shopFunctions.tradeAmount.PerMinuteCoolDownFunction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public PriceFunction priceFunction;
    public SignFunction signFunction;

    public DeleteShopFunction deleteShopFunction;

    public ShopEnabledFunction shopEnabledFunction;

    public ShopTypeFunction shopTypeFunction;




    // allowed to use
    public DisabledFromFunction disabledFromFunction;
    public EnabledFromFunction enabledFromFunction;
    public WeekDayToggleFunction weekDayToggleFunction;

    // storage

    public StorageFunction storageFunction;
    public StorageCapFunction storageCapFunction;
    public StorageRefillFunction storageRefillFunction;

    // trade amount

    public CoolDownFunction coolDownFunction;
    public LimitUseFunction limitUseFunction;
    public PerMinuteCoolDownFunction perMinuteCoolDownFunction;



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

    public String getName(){
        return shopData.getJSONObject("name").getString("name");
    }

    public String getShopType(){
        return shopData.getString("shopType");
    }

    public String getShopId(){
        return this.shopData.getString("shopId");
    }

    public void updateData(Player p){
        JSONObject shop = Man10ShopV3.api.getShopInformation(this.getShopId(), p);
        if(shop == null) return;
        shopData = shop.getJSONObject("data");
    }

    public void updateData(){
        updateData(null);
    }

    public JSONObject setVariable(Player p, String key, Object value){
        Map<String, Object> payload = new HashMap<>();
        payload.put("shopId", this.getShopId());
        if(p != null){
            payload.put("player", getPlayerJSON(p));
        }
        payload.put("key", key);
        payload.put("value", value);
        return httpRequest(this.plugin.getConfig().getString("api.endpoint") + "/shop/variable/set", "POST", new JSONObject(payload));
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

    public JSONObject getMenuInfo(){
        return shopData.getJSONObject("menuInfo");
    }

    public void openMenu(Player p){
        if(deleteShopFunction.isDeleted()) return;
        if(getShopType().equals("SELL") || getShopType().equals("BUY")){
            new BuySellActionMenu(p, this, plugin).open(p);
            return;
        }
    }

    public JSONObject createSign(Player p, Location loc){
        JSONObject data = new JSONObject();
        data.put("server", Man10ShopV3.config.getString("serverName"));
        data.put("world", loc.getWorld().getName());
        data.put("x", loc.getBlockX());
        data.put("y", loc.getBlockY());
        data.put("z", loc.getBlockZ());

        return requestQueueTask(p, "sign.register", data);
    }

    public JSONObject deleteSign(Player p, Location loc){
        JSONObject data = new JSONObject();
        data.put("server", Man10ShopV3.config.getString("serverName"));
        data.put("world", loc.getWorld().getName());
        data.put("x", loc.getBlockX());
        data.put("y", loc.getBlockY());
        data.put("z", loc.getBlockZ());

        return requestQueueTask(p, "sign.unregister", data);
    }

    public ArrayList<String> getSignInfo(){
        ArrayList<String> result = new ArrayList<>();
        JSONArray array = shopData.getJSONArray("signInfo");
        for(int i = 0; i < array.length(); i++){
            result.add(array.getString(i));
        }
        return result;
    }




}
