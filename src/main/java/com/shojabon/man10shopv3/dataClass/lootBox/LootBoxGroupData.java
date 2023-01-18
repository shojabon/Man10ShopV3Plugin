package com.shojabon.man10shopv3.dataClass.lootBox;

import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class LootBoxGroupData {

    public Material icon;
    public int percentageWeight;
    public boolean bigWin;
    public ArrayList<ItemStack> itemStacks = new ArrayList<>();
    public ArrayList<Integer> counts = new ArrayList<>();


    public float getPercentage() {
        return this.percentageWeight / 100000000f * 100;
    }

    public JSONObject getJSON(){
        JSONObject result = new JSONObject();
        result.put("icon", this.icon.name());
        result.put("weight", this.percentageWeight);
        result.put("bigWin", bigWin);
        JSONArray itemsJSON = new JSONArray();
        for(ItemStack item: itemStacks) itemsJSON.put(Man10ShopV3API.itemStackToJSON(item));
        result.put("items", itemsJSON);

        JSONArray countsJSON = new JSONArray();
        for(int count: counts) countsJSON.put(count);
        result.put("item_counts", counts);
        return result;
    }

    public void loadFromJSON(JSONObject object){
        this.icon = Material.getMaterial(object.getString("icon"));
        this.percentageWeight = object.getInt("weight");
        this.bigWin = object.getBoolean("bigWin");

        JSONArray itemsArray = object.getJSONArray("items");
        for(int i = 0; i < itemsArray.length(); i++) itemStacks.add(Man10ShopV3API.JSONToItemStack(itemsArray.getJSONObject(i)));

        JSONArray countsArray = object.getJSONArray("itemCounts");
        for(int i = 0; i < countsArray.length(); i++) counts.add(countsArray.getInt(i));

    }

    public int getTotalItemCount(){
        int total = 0;
        for(int count: counts){
            total += count;
        }
        return total;
    }

    public ItemStack pickRandomItem(){
        Random rand = new Random();
        int result = rand.nextInt(getTotalItemCount()) + 1;
        int currentCompound = 0;
        for(int i = 0; i < itemStacks.size(); i++){
            currentCompound += counts.get(i);
            if(result <= currentCompound){
                return itemStacks.get(i);
            }
        }
        return null;
    }




}
