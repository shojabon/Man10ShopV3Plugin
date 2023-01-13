package com.shojabon.man10shopv3.shopFunctions;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

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
        return calculatePermissionLevel(permissionList.getString(userId)) >= calculatePermissionLevel(permission);
    }

    private int calculatePermissionLevel(String permission){
        int permissionLevel = 0;
        switch (permission){
            case "OWNER": permissionLevel = 10; break;
            case "MODERATOR": permissionLevel = 9; break;
            case "ACCOUNTANT": permissionLevel = 7; break;
            case "STORAGE_ACCESS": permissionLevel = 7; break;
        }
        return permissionLevel;
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        return super.getSettingItem(player, item);
    }
}
