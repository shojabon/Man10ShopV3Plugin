package com.shojabon.man10shopv3.dataClass;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.shopFunctions.NameFunction;
import com.shojabon.man10shopv3.shopFunctions.PermissionFunction;
import com.shojabon.man10shopv3.shopFunctions.TargetItemFunction;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

public class Man10Shop {

    public Man10ShopV3 plugin = (Man10ShopV3) Bukkit.getPluginManager().getPlugin("Man10ShopV3");

    public ArrayList<ShopFunction> functions = new ArrayList<>();

    // functions
    public TargetItemFunction targetItemFunction;
    public NameFunction nameFunction;
    public PermissionFunction permissionFunction;

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

    public String getShopId(){
        return this.shopData.getString("shopId");
    }

    public void updateData(){
        Man10Shop shop = Man10ShopV3.api.getShopInformation(this.getShopId(), null);
        if(shop == null) return;
        shopData = shop.shopData;
    }



}
