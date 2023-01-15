package com.shojabon.man10shopv3.shopFunctions.general;

import ToolMenu.AutoScaledMenu;
import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

@ShopFunctionDefinition(
        name = "サブ垢制限",
        explanation = {"設定個までアカウント数を制限", "0の場合は制限なし"},
        enabledShopType = {},
        iconMaterial = Material.PLAYER_HEAD,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = true
)
public class IpLimitFunction extends ShopFunction {
    public IpLimitFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public int getTime(){
        return shop.shopData.getJSONObject("ipLimit").getInt("time");
    }
    public int getCount(){
        return shop.shopData.getJSONObject("ipLimit").getInt("count");
    }

    @Override
    public String currentSettingString() {
        if(getTime() == 0 || getCount() == 0) return "無効";
        return getTime() + "分の間に" + getCount() + "アカウントまで";
    }



    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            getInnerSettingMenu(player, plugin).open(player);
        });
        return item;
    }

    public AutoScaledMenu getInnerSettingMenu(Player player, Man10ShopV3 plugin){
        AutoScaledMenu autoScaledMenu = new AutoScaledMenu("IP制限設定", plugin);

        SInventoryItem timeSetting = new SInventoryItem(new SItemStack(Material.CLOCK).setDisplayName(new SStringBuilder().green().text("時間設定").build()).build());
        timeSetting.clickable(false);
        timeSetting.setEvent(ee -> {

            NumericInputMenu menu = new NumericInputMenu("時間を入力してください 0はoff", plugin);
            menu.setOnConfirm(number -> {
                JSONObject request = shop.setVariable(player, "ipLimit.time", number);
                if(!request.getString("status").equals("success")){
                    warn(player, request.getString("message"));
                    return;
                }
                success(player , "時間を設定しました");
                getInnerSettingMenu(player, plugin).open(player);
            });
            menu.setOnCancel(eee -> getInnerSettingMenu(player, plugin).open(player));
            menu.setOnClose(eee -> getInnerSettingMenu(player, plugin).open(player));

            menu.open(player);
        });

        SInventoryItem amountSetting = new SInventoryItem(new SItemStack(Material.PLAYER_HEAD).setDisplayName(new SStringBuilder().green().text("アカウント数設定").build()).build());
        amountSetting.clickable(false);
        amountSetting.setEvent(ee -> {

            NumericInputMenu menu = new NumericInputMenu("アカウント数を入力してください 0はoff", plugin);
            menu.setOnConfirm(number -> {
                JSONObject request = shop.setVariable(player, "ipLimit.count", number);
                if(!request.getString("status").equals("success")){
                    warn(player, request.getString("message"));
                    return;
                }
                success(player , "個数を設定しました");
                getInnerSettingMenu(player, plugin).open(player);
            });
            menu.setOnCancel(eee -> getInnerSettingMenu(player, plugin).open(player));
            menu.setOnClose(eee -> getInnerSettingMenu(player, plugin).open(player));

            menu.open(player);
        });
        autoScaledMenu.setOnCloseEvent(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));


        autoScaledMenu.addItem(timeSetting);
        autoScaledMenu.addItem(amountSetting);
        return autoScaledMenu;
    }
}
