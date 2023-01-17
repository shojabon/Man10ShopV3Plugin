package com.shojabon.man10shopv3.shopFunctions.tradeAmount;

import ToolMenu.BooleanInputMenu;
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
        internalFunctionName = "singleTransactionMode",
        name = "単品取引モード",
        explanation = {"まとめて取引ができなくなります", "1個ずつのみの取引になります", "イベントなど盛り上げたいときに使います"},
        enabledShopType = {"BUY", "SELL"},
        iconMaterial = Material.BOWL,
        category = "取引量制限設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class SingleTransactionModeFunction extends ShopFunction {
    public SingleTransactionModeFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }
    public boolean isEnabled(){
        return getFunctionData().getBoolean("enabled");
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //confirmation menu
            BooleanInputMenu menu = new BooleanInputMenu(isEnabled(), "単品取引モード", plugin);
            menu.setOnClose(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnCancel(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnConfirm(bool -> {
                if(!setVariable(player, "enabled", bool)){
                    return;
                }
                success(player, "取引モードを設定しました");
                new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);
            });
            menu.open(player);
        });
        return item;
    }
}
