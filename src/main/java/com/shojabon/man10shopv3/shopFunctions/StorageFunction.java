package com.shojabon.man10shopv3.shopFunctions;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@ShopFunctionDefinition(
        name = "ストレージを購入する",
        explanation = {},
        enabledShopType = {"BUY", "SELL"},
        iconMaterial = Material.CHEST,
        category = "倉庫設定",
        allowedPermission = "STORAGE_ACCESS",
        isAdminSetting = false
)
public class StorageFunction extends ShopFunction {

    public StorageFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }


    public int getItemCount(){
        return shop.shopData.getJSONObject("storage").getInt("itemCount");
    }

    public int getStorageSize(){
        return shop.shopData.getJSONObject("storage").getInt("storageSize");
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        return super.getSettingItem(player, item);
    }
}
