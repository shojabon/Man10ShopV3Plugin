package com.shojabon.man10shopv3.shopFunctions.tradeAmount;

import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

@ShopFunctionDefinition(
        internalFunctionName = "limitUse",
        name = "使用回数制限",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.MELON_SEEDS,
        category = "取引量制限設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class LimitUseFunction extends ShopFunction {
    public LimitUseFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public boolean isEnabled(){
        return getFunctionData().get("count") != JSONObject.NULL;
    }

    public int getCount(){
        return getFunctionData().getInt("count");
    }

    @Override
    public String currentSettingString() {
        if(!isEnabled()) return "なし";
        return "残り" + getCount() + "回";
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //confirmation menu
            NumericInputMenu menu = new NumericInputMenu("残り使用回数を設定してください 0はoff", plugin);
            menu.setOnConfirm(number -> {
                Man10ShopV3.threadPool.submit(() -> {
                    Object data = JSONObject.NULL;
                    if(number != 0){
                        data = number;
                    }
                    if(!setVariable(player, "count", data)){
                        return;
                    }
                    success(player, "回数を設定しました");
                    new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);
                });
            });
            menu.setOnCancel(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnClose(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));

            menu.open(player);
        });
        return item;
    }

}
