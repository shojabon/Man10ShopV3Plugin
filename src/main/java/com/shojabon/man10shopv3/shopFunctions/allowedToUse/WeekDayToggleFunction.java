package com.shojabon.man10shopv3.shopFunctions.allowedToUse;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.man10shopv3.menus.settings.innerSettings.WeekdayShopToggleMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@ShopFunctionDefinition(
        internalFunctionName = "weekdayToggle",
        name = "曜日有効化設定",
        explanation = {"特定の曜日にショップを有効かするかを設定する"},
        enabledShopType = {},
        iconMaterial = Material.COMPARATOR,
        category = "使用可条件設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class WeekDayToggleFunction extends ShopFunction {

    public WeekDayToggleFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public boolean[] getEnabledDays(){
        JSONArray array = getFunctionData().getJSONArray("dates");
        boolean[] result = new boolean[]{true,true,true,true,true,true,true};
        for(int i = 0; i < 7; i++){
            result[i] = array.getBoolean(i);
        }
        return result;
    }

    public String weekToString(int week){
        return new String[]{"日曜日", "月曜日", "火曜日", "水曜日","木曜日", "金曜日", "土曜日"}[week];
    }

    //====================
    // settings
    //====================

    @Override
    public String currentSettingString() {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for(boolean res: getEnabledDays()){
            SStringBuilder builder = new SStringBuilder();
            builder.yellow().text(weekToString(i) + ": ");
            if(res){
                builder.green().text("有効");
            }else{
                builder.red().text("無効");
            }
            result.append(builder.build()).append("\n");
            i++;
        }
        return result.toString();
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //confirmation menu
            WeekdayShopToggleMenu menu = new WeekdayShopToggleMenu(player, shop, plugin);

            menu.setAsyncOnCloseEvent(ee -> {
                if(!setVariable(player, "dates", menu.states)){
                    return;
                }
                success(player, "曜日設定をしました");
                new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);
            });
            menu.open(player);
        });

        return item;
    }
}
