package com.shojabon.man10shopv3.shopFunctions;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SLongTextInput;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@ShopFunctionDefinition(
        name = "お金",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.DROPPER,
        category = "その他",
        allowedPermission = "ACCOUNTANT",
        isAdminSetting = false
)
public class MoneyFunction extends ShopFunction {
    public MoneyFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }


    public int getMoney(){
        return this.shop.shopData.getJSONObject("money").getInt("money");
    }

}
