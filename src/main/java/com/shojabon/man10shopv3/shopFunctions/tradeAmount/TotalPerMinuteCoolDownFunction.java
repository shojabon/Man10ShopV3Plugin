package com.shojabon.man10shopv3.shopFunctions.tradeAmount;

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
        internalFunctionName = "totalPerMinuteCoolDown",
        name = "全体分間毎ごとのクールダウン設定",
        explanation = {"取引を制限します", "分間毎の取引を設定した個数までとします", "プレイヤーごとではなくショップごとのクールダウン", "どちらかが0の場合設定は無効化"},
        enabledShopType = {},
        iconMaterial = Material.DROPPER,
        category = "取引量制限設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class TotalPerMinuteCoolDownFunction extends ShopFunction {
    public TotalPerMinuteCoolDownFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public int getTime(){
        return getFunctionData().getInt("time");
    }
    public int getAmount(){
        return getFunctionData().getInt("amount");
    }

    @Override
    public String currentSettingString() {
        if(getTime() == 0 || getAmount() == 0) return "無効";
        return getTime() + "分" + getAmount() + "個まで";
    }



    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            getInnerSettingMenu(player, plugin).open(player);
        });
        return item;
    }

    public AutoScaledMenu getInnerSettingMenu(Player player, Man10ShopV3 plugin){
        AutoScaledMenu autoScaledMenu = new AutoScaledMenu("分間毎ごとのクールダウン設定", plugin);

        SInventoryItem timeSetting = new SInventoryItem(new SItemStack(Material.CLOCK).setDisplayName(new SStringBuilder().green().text("時間設定").build()).build());
        timeSetting.clickable(false);
        timeSetting.setEvent(ee -> {

            NumericInputMenu menu = new NumericInputMenu("時間を入力してください 0はoff", plugin);
            menu.setOnConfirm(number -> {
                if(!setVariable(player, "time", number)){
                    return;
                }
                success(player , "時間を設定しました");
                getInnerSettingMenu(player, plugin).open(player);
            });
            menu.setOnCancel(eee -> getInnerSettingMenu(player, plugin).open(player));
            menu.setOnClose(eee -> getInnerSettingMenu(player, plugin).open(player));

            menu.open(player);
        });

        SInventoryItem amountSetting = new SInventoryItem(new SItemStack(Material.HOPPER).setDisplayName(new SStringBuilder().green().text("個数設定").build()).build());
        amountSetting.clickable(false);
        amountSetting.setEvent(ee -> {

            NumericInputMenu menu = new NumericInputMenu("個数を入力してください 0はoff", plugin);
            menu.setOnConfirm(number -> {
                if(!setVariable(player, "amount", number)){
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
