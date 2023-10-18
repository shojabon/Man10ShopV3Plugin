package com.shojabon.man10shopv3.shopFunctions.general;

import ToolMenu.ConfirmationMenu;
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
        internalFunctionName = "delete",
        name = "§e§l§k00§4§lショップを削除§e§l§k00",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.LAVA_BUCKET,
        category = "削除設定",
        allowedPermission = "OWNER",
        isAdminSetting = false
)
public class DeleteShopFunction extends ShopFunction {
    public DeleteShopFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public boolean isDeleted(){
        return getFunctionData().getBoolean("deleted");
    }
    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setAsyncEvent(e -> {
            //confirmation menu
            ConfirmationMenu menu = new ConfirmationMenu("確認", plugin);
            menu.setOnCloseEvent(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnConfirm(ee -> {
                Man10ShopV3.threadPool.submit(() -> {
                    if(!setVariable(player, "deleted", true)){
                        return;
                    }
                    menu.close(player);
                });
            });
            menu.open(player);
        });

        return item;
    }
}
