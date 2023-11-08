package com.shojabon.man10shopv3.shopFunctions.tradeAmount;

import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.UUID;

@ShopFunctionDefinition(
        internalFunctionName = "perPlayerLimitUse",
        name = "使用回数制限",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.MELON_SEEDS,
        category = "取引量制限設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class PerPlayerLimitUseFunction extends ShopFunction {
    public PerPlayerLimitUseFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public int getCountLeft(UUID uuid){
        try{
            String key = uuid.toString().replace("-", "").toLowerCase();
            if(!getFunctionData().has("userCount")) return -1;
            if(!getFunctionData().getJSONObject("userCount").has(key)) return -1;
            return getFunctionData().getJSONObject("userCount").getInt(key);
        }catch (Exception e){
            return -1;
        }
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        return null;
    }

}
