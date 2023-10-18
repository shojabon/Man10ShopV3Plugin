package com.shojabon.man10shopv3.shopFunctions.storage;

import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

@ShopFunctionDefinition(
        internalFunctionName = "storageCap",
        name = "買取制限",
        explanation = {"※買取ショップの場合のみ有効", "買取数の上限を設定する", "買取数上限を0にすると倉庫があるだけ買い取ります"},
        enabledShopType = {"SELL"},
        iconMaterial = Material.HOPPER,
        category = "倉庫設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class StorageCapFunction extends ShopFunction {
    public StorageCapFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //number input menu
            NumericInputMenu menu = new NumericInputMenu(new SStringBuilder().green().text("購入制限設定").build(), plugin);
            if(!shop.isAdmin()) menu.setMaxValue(shop.storageFunction.getStorageSize());
            menu.setOnCloseEvent(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnCancel(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnConfirm(newValue -> {
                Man10ShopV3.threadPool.submit(() -> {
                    if(newValue > shop.storageFunction.getStorageSize() && !shop.isAdmin()){
                        warn(player, "購入制限は倉庫以上の数にはできません");
                        return;
                    }
                    if(newValue < 0){
                        warn(player, "購入制限は正の数でなくてはならない");
                        return;
                    }

                    if(!setVariable(player, "size", newValue)){
                        return;
                    }
                    success(player, "上限を設定しました");
                    new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);
                });
            });
            menu.open(player);

        });

        return item;
    }
}
