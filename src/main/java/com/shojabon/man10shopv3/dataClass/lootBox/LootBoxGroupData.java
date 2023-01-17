package com.shojabon.man10shopv3.dataClass.lootBox;

import com.shojabon.man10shopv3.Man10ShopV3API;
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

    public LootBoxGroupData(Material icon, int percentageWeight) {
        this.icon = icon;
        this.percentageWeight = percentageWeight;
    }

    public float getPercentage() {
        return this.percentageWeight / 100000000f * 100;
    }

    public JSONObject getJSON(){
        JSONObject result = new JSONObject();
        result.put("icon", this.icon.name());
        result.put("weight", this.percentageWeight);
        JSONArray itemsJSON = new JSONArray();
        for(ItemStack item: itemStacks) itemsJSON.put(Man10ShopV3API.itemStackToJSON(item));
        result.put("items", itemsJSON);

        JSONArray countsJSON = new JSONArray();
        for(int count: counts) countsJSON.put(count);
        result.put("item_counts", counts);
        return result;
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
