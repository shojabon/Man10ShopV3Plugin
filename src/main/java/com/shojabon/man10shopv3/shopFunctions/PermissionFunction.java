package com.shojabon.man10shopv3.shopFunctions;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.Man10ShopModerator;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@ShopFunctionDefinition(
        name = "権限機能",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.DROPPER,
        category = "その他",
        allowedPermission = "STORAGE_ACCESS",
        isAdminSetting = false
)
public class PermissionFunction extends ShopFunction {
    public PermissionFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    //init


    public boolean hasPermission(UUID uuid, String permission){
        JSONObject permissionList = shop.shopData.getJSONObject("permission").getJSONObject("users");
        String userId = uuid.toString().replace("-", "");
        if(!permissionList.has(userId)) return false;
        return calculatePermissionLevel(permissionList.getJSONObject(userId).getString("permission")) >= calculatePermissionLevel(permission);
    }

    private int calculatePermissionLevel(String permission){
        int permissionLevel = 0;
        switch (permission){
            case "OWNER": permissionLevel = 10; break;
            case "MODERATOR": permissionLevel = 9; break;
            case "ACCOUNTANT": permissionLevel = 7; break;
            case "STORAGE_ACCESS": permissionLevel = 6; break;
        }
        return permissionLevel;
    }

    public String getPermission(UUID uuid){
        JSONObject permissionList = shop.shopData.getJSONObject("permission").getJSONObject("users");
        String userId = uuid.toString().replace("-", "");
        if(!permissionList.has(userId)) return "NONE";
        return permissionList.getString(userId);
    }

    public ArrayList<Man10ShopModerator> getModerators(){
        ArrayList<Man10ShopModerator> result = new ArrayList<>();
        JSONObject array = this.shop.shopData.getJSONObject("permission").getJSONObject("users");
        for(String key: array.keySet()){
            JSONObject userObject = array.getJSONObject(key);
            Man10ShopModerator moderator = new Man10ShopModerator(userObject.getString("name"), UUID.fromString(userObject.getString("uuid")), userObject.getString("permission"), userObject.getBoolean("notify"));
            result.add(moderator);
        }
        return result;
    }

    public boolean isModerator(UUID uuid){
        JSONObject permissionList = shop.shopData.getJSONObject("permission").getJSONObject("users");
        String userId = uuid.toString().replace("-", "");
        return permissionList.has(userId);
    }

    public String getPermissionString(String permission){
        if(permission == null) return "エラー";
        switch (permission){
            case "OWNER":
                return "オーナー";
            case "MODERATOR":
                return "管理者";
            case "ACCOUNTANT":
                return "会計";
            case "STORAGE_ACCESS":
                return "倉庫編集権";
        }
        return "エラー";
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        return super.getSettingItem(player, item);
    }
}
