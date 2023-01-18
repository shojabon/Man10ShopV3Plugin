package com.shojabon.man10shopv3.dataClass.lootBox;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Random;

public class LootBox {
    public ArrayList<LootBoxGroupData> groupData = new ArrayList<>();

    public int getLackingWeight(){
        int total = 0;
        for(LootBoxGroupData data: groupData){
            total += data.percentageWeight;
        }
        return 100000000 - total;
    }

    public boolean canPlay(){
        if(getLackingWeight() != 0) return false;
        return true;
    }

    public LootBoxItem pickRandomItem(){
        if(!canPlay()) return null;
        Random rand = new Random();
        int result = rand.nextInt(100000000)+1;
        int currentCompound = 0;
        for(int i = 0; i < groupData.size(); i++){
            if(groupData.get(i).percentageWeight == 0) continue;
            currentCompound += groupData.get(i).percentageWeight;
            if(result <= currentCompound){
                return new LootBoxItem(groupData.get(i).pickRandomItem().clone(), i);
            }
        }
        return null;
    }

    public void loadFromJSON(JSONObject jsonObject){
        JSONArray groups = jsonObject.getJSONArray("groups");
        for(int i = 0; i < groups.length(); i++){
            JSONObject groupData = groups.getJSONObject(i);
            LootBoxGroupData data = new LootBoxGroupData();
            data.loadFromJSON(groupData);
            this.groupData.add(data);
        }
    }

    public JSONObject getJSON(){
        JSONObject result = new JSONObject();

        JSONArray groups = new JSONArray();
        for(LootBoxGroupData data: this.groupData){
            groups.put(data.getJSON());
        }

        result.put("groups", groups);
        return result;
    }



}

