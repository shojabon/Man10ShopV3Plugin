package com.shojabon.man10shopv3.shopFunctions.lootBox;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.dataClass.lootBox.LootBox;
import com.shojabon.man10shopv3.dataClass.lootBox.LootBoxFunction;
import com.shojabon.man10shopv3.menus.settings.lootBoxSettings.LootBoxGroupSelectorMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

@ShopFunctionDefinition(
        internalFunctionName = "lootBoxGroup",
        name = "アイテムグループ設定",
        explanation = {"グループ/アイテムの設定"},
        enabledShopType = {"LOOT_BOX"},
        iconMaterial = Material.CAULDRON,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class LootBoxGroupFunction extends ShopFunction {

    public LootBoxGroupFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public LootBox getLootBox(){
        LootBox box = new LootBox();
        box.loadFromJSON(getFunctionData());
        return box;
    }

    public boolean saveLootBox(Player player, LootBox lootBox){
        JSONObject requestUpdate = shop.setVariable(player, "lootBoxGroup", lootBox.getJSON());
        shop.updateData();
        if(!requestUpdate.getString("status").equals("success")){
            shop.lootBoxGroupFunction.warn(player, requestUpdate.getString("message"));
            return false;
        }
        return true;
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //confirmation menu
            new LootBoxGroupSelectorMenu(player, shop, getLootBox(), plugin).open(player);

        });
        return item;
    }

}
