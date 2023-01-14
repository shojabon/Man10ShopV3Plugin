package com.shojabon.man10shopv3.shopFunctions.general;

import ToolMenu.BooleanInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.BaseUtils;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

@ShopFunctionDefinition(
        name = "ショップ有効化設定",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.LEVER,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class ShopEnabledFunction extends ShopFunction {

    public ShopEnabledFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public boolean isEnabled(){
        return shop.shopData.getJSONObject("shopEnabled").getBoolean("enabled");
    }

    @Override
    public String currentSettingString() {
        return BaseUtils.booleanToJapaneseText(isEnabled());
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //confirmation menu
            BooleanInputMenu menu = new BooleanInputMenu(isEnabled(), "ショップ有効化設定", plugin);
            menu.setOnClose(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnCancel(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnConfirm(bool -> {
                JSONObject requestValueUpdate = shop.setVariable(player, "shopEnabled.enabled", bool);
                if(!requestValueUpdate.getString("status").equals("success")){
                    warn(player, requestValueUpdate.getString("message"));
                    return;
                }
                new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);
            });
            menu.open(player);
        });
        return item;
    }

}
