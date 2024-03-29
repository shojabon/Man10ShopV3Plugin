package com.shojabon.man10shopv3.shopFunctions;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@ShopFunctionDefinition(
        internalFunctionName = "targetItem",
        name = "ターゲットアイテム設定",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.LECTERN,
        category = "その他",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class TargetItemFunction extends ShopFunction {

    public TargetItemFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }


    public SItemStack getTargetItem(){
        return new SItemStack(Man10ShopV3API.JSONToItemStack(getFunctionData().getJSONObject("item")));
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        return super.getSettingItem(player, item);
    }


    public ItemStack getTargetTypeItem(){
        return shop.targetItemFunction.getTargetItem().build();
    }

    public String getTargetTypeMD5(){
        return shop.targetItemFunction.getTargetItem().getItemTypeMD5(true);
    }
}
