package com.shojabon.man10shopv3.shopFunctions.allowedToUse;

import ToolMenu.TimeSelectorMenu;
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
        name = "無効化開始時間設定",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.REDSTONE_BLOCK,
        category = "使用可条件設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class DisabledFromFunction extends ShopFunction {

    public DisabledFromFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public long getTime(){
        return this.shop.shopData.getJSONObject("disabledFrom").getLong("time");
    }

    @Override
    public String currentSettingString() {
        if(getTime() == -1){
            return "無効";
        }
        return BaseUtils.unixTimeToString(getTime());
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {

            TimeSelectorMenu menu = new TimeSelectorMenu(getTime(), "無効化開始時間を設定してください", plugin);
            menu.setOnCloseEvent(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnConfirm(time -> {
                JSONObject requestValueUpdate = shop.setVariable(player, "disabledFrom.time", time);
                if(!requestValueUpdate.getString("status").equals("success")){
                    warn(player, requestValueUpdate.getString("message"));
                    return;
                }
                success(player,"時間を設定しました");
                new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);
            });

            menu.open(player);

        });


        return item;
    }

}
