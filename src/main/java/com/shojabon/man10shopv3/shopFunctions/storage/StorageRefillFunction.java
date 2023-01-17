package com.shojabon.man10shopv3.shopFunctions.storage;

import ToolMenu.AutoScaledMenu;
import ToolMenu.NumericInputMenu;
import ToolMenu.TimeSelectorMenu;
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

import java.text.SimpleDateFormat;
import java.util.Date;

@ShopFunctionDefinition(
        internalFunctionName = "storageRefill",
        name = "分間ごとの補充設定",
        explanation = {"取引を制限します", "分間毎の取引を設定した個数までとします", "どちらかが0の場合設定は無効化"}, 
        enabledShopType = {},
        iconMaterial = Material.CHEST_MINECART,
        category = "倉庫設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class StorageRefillFunction extends ShopFunction {

    //init
    public StorageRefillFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public int getMinutes(){
        return getFunctionData().getInt("minutes");
    }

    public int getAmount(){
        return getFunctionData().getInt("amount");
    }

    public Long getLastRefillTime(){
        return getFunctionData().getLong("lastRefillTime");
    }

    public int getItemLeft(){
        return getFunctionData().getInt("itemLeft");
    }

    @Override
    public String currentSettingString() {
        if(getAmount() == 0 || getLastRefillTime() == -1 || getMinutes() == 0){
            return "無効";
        }
        return getMinutes() + "分毎に" + getAmount() + "個補充";
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
        AutoScaledMenu autoScaledMenu = new AutoScaledMenu("分間毎ごとの倉庫補充設定", plugin);

        SInventoryItem timeSetting = new SInventoryItem(new SItemStack(Material.CLOCK).setDisplayName(new SStringBuilder().green().text("時間設定").build()).build());
        timeSetting.clickable(false);
        timeSetting.setEvent(e -> {

            NumericInputMenu menu = new NumericInputMenu("時間を入力してください 0はoff", plugin);
            menu.setOnConfirm(number -> {
                if(!setVariable(player, "minutes", number)){
                    return;
                }
                success(player, "時間を設定しました");
                getInnerSettingMenu(player, plugin).open(player);
            });
            menu.setOnCancel(ee -> getInnerSettingMenu(player, plugin).open(player));
            menu.setOnClose(ee -> getInnerSettingMenu(player, plugin).open(player));

            menu.open(player);
        });


        SInventoryItem setRefillStartingTime = new SInventoryItem(new SItemStack(Material.COMPASS)
                .setDisplayName(new SStringBuilder().green().text("最終補充時間を設定する").build())
                .addLore("§d§l現在設定: §e§l" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(getLastRefillTime()*1000L)))
                .build());
        setRefillStartingTime.clickable(false);
        setRefillStartingTime.setEvent(e -> {
            TimeSelectorMenu menu = new TimeSelectorMenu(getLastRefillTime(), "最終補充時間を設定してくださ", plugin);
            menu.setOnConfirm(lastRefillTimeLocal -> {
                if(!setVariable(player, "lastRefillTime", lastRefillTimeLocal)){
                    return;
                }
                success(player, "最新の補充開始時間を現在に設定しました");
                getInnerSettingMenu(player, plugin).open(player);
            });
            menu.setOnCloseEvent(ee -> getInnerSettingMenu(player, plugin).open(player));
            menu.open(player);
        });


        SInventoryItem forceRefill = new SInventoryItem(new SItemStack(Material.REDSTONE_BLOCK).setDisplayName(new SStringBuilder().green().text("強制的に在庫を補充する").build())
                .addLore("§f補充スケジュールは保持したままアイテムを補充する").build());
        forceRefill.clickable(false);
        forceRefill.setEvent(e -> {
            if(!setVariable(player, "itemLeft", getAmount())){
                return;
            }
            success(player, "在庫を補充しました");
        });


        SInventoryItem amountSetting = new SInventoryItem(new SItemStack(Material.HOPPER).setDisplayName(new SStringBuilder().green().text("個数設定").build()).build());
        amountSetting.clickable(false);
        amountSetting.setEvent(e -> {

            NumericInputMenu menu = new NumericInputMenu("個数を入力してください 0はoff", plugin);
            menu.setOnConfirm(number -> {
                if(!setVariable(player, "amount", number)){
                    return;
                }
                success(player, "個数を設定しました");
                getInnerSettingMenu(player, plugin).open(player);
            });
            menu.setOnCancel(ee -> getInnerSettingMenu(player, plugin).open(player));
            menu.setOnClose(ee -> getInnerSettingMenu(player, plugin).open(player));

            menu.open(player);
        });

        autoScaledMenu.setOnCloseEvent(e -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));

        autoScaledMenu.addItem(timeSetting);
        autoScaledMenu.addItem(amountSetting);
        autoScaledMenu.addItem(setRefillStartingTime);
        autoScaledMenu.addItem(forceRefill);
        
        return autoScaledMenu;
    }
}
