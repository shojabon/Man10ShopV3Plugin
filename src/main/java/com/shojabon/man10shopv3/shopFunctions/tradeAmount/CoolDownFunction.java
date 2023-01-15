package com.shojabon.man10shopv3.shopFunctions.tradeAmount;

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

import java.util.HashMap;
import java.util.UUID;

@ShopFunctionDefinition(
        name = "取引クールダウン設定",
        explanation = {"取引を制限する", "設定秒に1回のみしか取引できなくなります", "0の場合はクールダウンなし"},
        enabledShopType = {},
        iconMaterial = Material.CLOCK,
        category = "取引量制限設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class CoolDownFunction extends ShopFunction {
    public CoolDownFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public int getTime(){
        return shop.shopData.getJSONObject("coolDown").getInt("seconds");
    }

    @Override
    public String currentSettingString() {
        return getTime() + "秒";
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //number input menu
            NumericInputMenu menu = new NumericInputMenu(new SStringBuilder().green().text("取引クールダウン").build(), plugin);
            menu.setOnClose(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnCancel(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnConfirm(newValue -> {
                if(newValue < 0){
                    warn(player, "クールダウンタイムは正の数でなくてはならない");
                    return;
                }

                JSONObject request = shop.setVariable(player, "coolDown.seconds", newValue);
                if(!request.getString("status").equals("success")){
                    warn(player, request.getString("message"));
                    return;
                }
                success(player, "クールダウンを設定しました");
                new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);
            });
            menu.open(player);

        });

        return item;
    }
}
