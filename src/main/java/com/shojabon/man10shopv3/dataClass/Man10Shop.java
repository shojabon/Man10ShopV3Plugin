package com.shojabon.man10shopv3.dataClass;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.lootBox.LootBoxFunction;
import com.shojabon.man10shopv3.menus.action.BarterActionMenu;
import com.shojabon.man10shopv3.menus.action.BuySellActionMenu;
import com.shojabon.man10shopv3.menus.action.LootBoxActionMenu;
import com.shojabon.man10shopv3.shopFunctions.*;
import com.shojabon.man10shopv3.shopFunctions.allowedToUse.*;
import com.shojabon.man10shopv3.shopFunctions.barter.SetBarterFunction;
import com.shojabon.man10shopv3.shopFunctions.general.*;
import com.shojabon.man10shopv3.shopFunctions.lootBox.LootBoxBigWinFunction;
import com.shojabon.man10shopv3.shopFunctions.lootBox.LootBoxGroupFunction;
import com.shojabon.man10shopv3.shopFunctions.lootBox.LootBoxPaymentFunction;
import com.shojabon.man10shopv3.shopFunctions.storage.PerPlayerStorageRefillFunction;
import com.shojabon.man10shopv3.shopFunctions.storage.StorageCapFunction;
import com.shojabon.man10shopv3.shopFunctions.storage.StorageFunction;
import com.shojabon.man10shopv3.shopFunctions.storage.StorageRefillFunction;
import com.shojabon.man10shopv3.shopFunctions.tradeAmount.*;
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
import java.util.UUID;

import static com.shojabon.man10shopv3.Man10ShopV3API.getPlayerJSON;
import static com.shojabon.man10shopv3.Man10ShopV3API.httpRequest;

public class Man10Shop {

    public Man10ShopV3 plugin = (Man10ShopV3) Bukkit.getPluginManager().getPlugin("Man10ShopV3");

    public ArrayList<ShopFunction> functions = new ArrayList<>();
    public ArrayList<LootBoxFunction> lootBoxFunctions = new ArrayList<>();

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
    public SecretPriceModeFunction secretPriceModeFunction;
    public IpLimitFunction ipLimitFunction;
    public RandomPriceFunction randomPriceFunction;
    public CategoryFunction categoryFunction;
    public SetMoneyFunction setMoneyFunction;
    public SetItemCountFunction setItemCountFunction;
    public MoneyRefillFunction moneyRefillFunction;
    public ItemRefillFunction itemRefillFunction;


    // allowed to use
    public DisabledFromFunction disabledFromFunction;
    public EnabledFromFunction enabledFromFunction;
    public WeekDayToggleFunction weekDayToggleFunction;

    public MemberShopModeFunction memberShopModeFunction;

    // storage

    public StorageFunction storageFunction;
    public StorageCapFunction storageCapFunction;
    public StorageRefillFunction storageRefillFunction;

    public PerPlayerStorageRefillFunction perPlayerStorageRefillFunction;

    // trade amount

    public CoolDownFunction coolDownFunction;
    public LimitUseFunction limitUseFunction;
    public PerPlayerLimitUseFunction perPlayerLimitUseFunction;
    public PerMinuteCoolDownFunction perMinuteCoolDownFunction;
    public TotalPerMinuteCoolDownFunction totalPerMinuteCoolDownFunction;
    public SingleTransactionModeFunction singleTransactionModeFunction;

    //barter
    public SetBarterFunction setBarterFunction;

    // loot box
    public LootBoxGroupFunction lootBoxGroupFunction;
    public LootBoxPaymentFunction lootBoxPaymentFunction;
    public LootBoxBigWinFunction lootBoxBigWinFunction;



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
                if(LootBoxFunction.class.isAssignableFrom(field.getType())) {
                    lootBoxFunctions.add((LootBoxFunction) field.get(this));
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

    public JSONObject setPlayerVariable(Player p, String key, Object value, UUID dataOfPlayer){
        Map<String, Object> payload = new HashMap<>();
        payload.put("shopId", this.getShopId());
        if(p != null){
            payload.put("player", getPlayerJSON(p));
        }
        payload.put("key", key);
        payload.put("value", value);
        payload.put("dataOfPlayer", dataOfPlayer.toString());
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

    public void requestQueueTaskLocallyQueued(Player p, String key, Object data){
        if(Man10ShopV3.transactionLock.contains(p.getUniqueId())){
            return;
        }
        QueueRequestObject request = new QueueRequestObject(p, this.getShopId(), key, data);
        Man10ShopV3.transactionLock.put(p.getUniqueId(), true);
        Man10ShopV3.transactionRequestQueue.add(request);
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
        if(getShopType().equals("BARTER")){
            new BarterActionMenu(p, this, plugin).open(p);
            return;
        }
        if(getShopType().equals("LOOT_BOX")){
            new LootBoxActionMenu(p, this, plugin).open(p);
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
