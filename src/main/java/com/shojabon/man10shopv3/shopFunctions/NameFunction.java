package com.shojabon.man10shopv3.shopFunctions;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@ShopFunctionDefinition(
        name = "ショップ名設定",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.NAME_TAG,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class NameFunction extends ShopFunction {
    public NameFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }


    public String getName(){
        return this.shop.shopData.getJSONObject("name").getString("name");
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        return super.getSettingItem(player, item);
    }
}
