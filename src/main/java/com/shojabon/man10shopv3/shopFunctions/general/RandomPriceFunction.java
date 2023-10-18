package com.shojabon.man10shopv3.shopFunctions.general;

import ToolMenu.AutoScaledMenu;
import ToolMenu.NumericInputMenu;
import ToolMenu.TimeSelectorMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.man10shopv3.menus.settings.innerSettings.RandomPricePriceSelector;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ShopFunctionDefinition(
        internalFunctionName = "randomPrice",
        name = "ランダム価格設定",
        explanation = {"設定した分間毎に値段を設定する", "どちらかが0の場合設定は無効化"},
        enabledShopType = {"BUY", "SELL"},
        iconMaterial = Material.ENCHANTING_TABLE,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = true
)
public class RandomPriceFunction extends ShopFunction {

    public RandomPriceFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }


    public List<Integer> getPrices(){
        List<Integer> result = new ArrayList<>();
        JSONArray array = getFunctionData().getJSONArray("prices");
        for(int i = 0; i < array.length(); i++){
            result.add(array.getInt(i));
        }
        return result;
    }

    public int getTime(){
        return getFunctionData().getInt("time");
    }

    public int getLastRefillTime(){
        return getFunctionData().getInt("lastRefillTime");
    }

    @Override
    public String currentSettingString() {
        if(getTime() == 0 || getLastRefillTime() == -1 || getPrices().size() == 0){
            return "無効";
        }
        return this.getTime() + "分毎に決定";
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //confirmation menu
            getInnerSettingMenu(player, plugin).open(player);
        });

        return item;
    }


    public AutoScaledMenu getInnerSettingMenu(Player player, Man10ShopV3 plugin){

        AutoScaledMenu autoScaledMenu = new AutoScaledMenu("ランダム値段設定", plugin);

        SInventoryItem timeSetting = new SInventoryItem(new SItemStack(Material.CLOCK).setDisplayName(new SStringBuilder().green().text("時間設定").build()).build());
        timeSetting.clickable(false);
        timeSetting.setEvent(e -> {

            NumericInputMenu menu = new NumericInputMenu("時間を入力してください 0はoff", plugin);
            menu.setOnConfirm(number -> {
                Man10ShopV3.threadPool.submit(() -> {
                    if(!setVariable(player, "time", number)){
                        return;
                    }
                    success(player, "時間を設定しました");
                    getInnerSettingMenu(player, plugin).open(player);
                });
            });
            menu.setOnCancel(ee -> getInnerSettingMenu(player, plugin).open(player));
            menu.setOnClose(ee -> getInnerSettingMenu(player, plugin).open(player));

            menu.open(player);
        });


        SInventoryItem priceGroup = new SInventoryItem(new SItemStack(Material.EMERALD_BLOCK).setDisplayName(new SStringBuilder().green().text("値段群を設定").build()).build());
        priceGroup.clickable(false);
        priceGroup.setEvent(e -> {

            RandomPricePriceSelector menu = new RandomPricePriceSelector(player, shop, plugin);
            menu.open(player);
        });


        SInventoryItem setRefillStartingTime = new SInventoryItem(new SItemStack(Material.COMPASS)
                .setDisplayName(new SStringBuilder().green().text("最終値段選択時間を設定する").build())
                .addLore("§d§l現在設定: §e§l" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(shop.randomPriceFunction.getLastRefillTime()*1000L)))
                .build());
        setRefillStartingTime.clickable(false);
        setRefillStartingTime.setEvent(e -> {
            TimeSelectorMenu menu = new TimeSelectorMenu(System.currentTimeMillis()/1000L, "最終値段選択時間を設定してくださ", plugin);
            menu.setOnConfirm(lastRefillTimeLocal -> {
                Man10ShopV3.threadPool.submit(() -> {
                    if(!setVariable(player, "lastRefillTime", lastRefillTimeLocal)){
                        return;
                    }
                    success(player, "最新の補充開始時間を現在に設定しました");
                    getInnerSettingMenu(player, plugin).open(player);
                });
            });
            menu.setOnCloseEvent(ee -> getInnerSettingMenu(player, plugin).open(player));
            menu.open(player);
        });

        autoScaledMenu.setOnCloseEvent(e -> {new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);});

        autoScaledMenu.addItem(timeSetting);
        autoScaledMenu.addItem(setRefillStartingTime);
        autoScaledMenu.addItem(priceGroup);

        return autoScaledMenu;
    }
}
