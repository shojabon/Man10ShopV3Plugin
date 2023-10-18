package com.shojabon.man10shopv3.shopFunctions.general;

import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

@ShopFunctionDefinition(
        internalFunctionName = "price",
        name = "取引価格設定",
        explanation = {},
        enabledShopType = {"BUY", "SELL"},
        iconMaterial = Material.EMERALD,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class PriceFunction extends ShopFunction {

    //variables

    //init
    public PriceFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public int getPrice(){
        return getFunctionData().getInt("price");
    }

    //====================
    // settings
    //====================
    @Override
    public String currentSettingString() {
        return getPrice() + "円";
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //number input menu
            NumericInputMenu menu = new NumericInputMenu(new SStringBuilder().green().text("取引値段設定").build(), plugin);
            menu.setOnClose(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnCancel(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnConfirm(newValue -> {
                Man10ShopV3.threadPool.submit(() -> {
                    if(!setVariable(player, "price", newValue)){
                        return;
                    }
                    new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);
                });
            });
            menu.open(player);

        });

        return item;
    }
}
