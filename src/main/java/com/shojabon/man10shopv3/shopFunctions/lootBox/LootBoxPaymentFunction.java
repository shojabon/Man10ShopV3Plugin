package com.shojabon.man10shopv3.shopFunctions.lootBox;

import ToolMenu.AutoScaledMenu;
import ToolMenu.NumericInputMenu;
import ToolMenu.SingleItemStackSelectorMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.dataClass.lootBox.LootBoxFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.BaseUtils;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

@ShopFunctionDefinition(
        internalFunctionName = "lootBoxPayment",
        name = "支払い方法設定",
        explanation = {},
        enabledShopType = {"LOOT_BOX"},
        iconMaterial = Material.EMERALD,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class LootBoxPaymentFunction extends ShopFunction {

    public LootBoxPaymentFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public ItemStack getItem(){
        if(getFunctionData().isNull("item")) return null;
        return Man10ShopV3API.JSONToItemStack(getFunctionData().getJSONObject("item"));
    }

    public int getCash(){
        return getFunctionData().getInt("cash");
    }

    @Override
    public String currentSettingString() {
        String result = "現在の設定\n";
        if(getItem() != null || getCash() != 0){
            if(getCash() != 0){
                result += "現金 " + BaseUtils.priceString(getCash()) + "円";
                return result;
            }
            if(getItem() != null){
                result += "アイテム " + new SItemStack(getItem()).getDisplayName() + " " + new SItemStack(getItem()).getAmount();
                return result;
            }
        }else{
            result += "なし";
            return result;
        }
        return result;
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            getInnerSettingMenu(player, plugin).open(player);
        });
        return item;
    }

    public AutoScaledMenu getInnerSettingMenu(Player player, Man10ShopV3 plugin){
        AutoScaledMenu autoScaledMenu = new AutoScaledMenu("支払い設定", plugin);

        SInventoryItem cashSetting = new SInventoryItem(new SItemStack(Material.EMERALD).setDisplayName(new SStringBuilder().green().text("現金設定").build()).build());
        cashSetting.clickable(false);
        cashSetting.setEvent(ee -> {

            NumericInputMenu menu = new NumericInputMenu("金額を設定してください", plugin);
            menu.setOnConfirm(number -> {
                Man10ShopV3.threadPool.submit(() -> {
                    if(!setVariable(player, "cash", number)){
                        return;
                    }
                    success(player, "値段を設定しました");
                    getInnerSettingMenu(player, plugin).open(player);
                });
            });
            menu.setOnCancel(eee -> getInnerSettingMenu(player, plugin).open(player));
            menu.setOnClose(eee -> getInnerSettingMenu(player, plugin).open(player));

            menu.open(player);
        });

        ItemStack settingItem = new SItemStack(Material.BARRIER).setDisplayName(new SStringBuilder().green().text("アイテム設定").build()).build();

        if(getItem() != null){
            settingItem = new SItemStack(getItem().clone()).setDisplayName(new SStringBuilder().green().text("アイテム設定").build()).build();
        }

        SInventoryItem itemSetting = new SInventoryItem(settingItem);
        itemSetting.clickable(false);
        itemSetting.setEvent(ee -> {

            SingleItemStackSelectorMenu menu = new SingleItemStackSelectorMenu("支払いアイテム選択", getItem(), plugin);
            menu.allowNullItem(true);
            menu.setOnConfirm(item -> {
                Man10ShopV3.threadPool.submit(() -> {
                    Object payload = JSONObject.NULL;
                    if(item != null){
                        payload = Man10ShopV3API.itemStackToJSON(item);
                    }
                    if(!setVariable(player, "item", payload)){
                        return;
                    }
                    success(player, "支払いアイテムを設定しました");
                    getInnerSettingMenu(player, plugin).open(player);
                });
            });

            menu.setOnCloseEvent(eee -> getInnerSettingMenu(player, plugin).open(player));
            menu.open(player);
        });
        autoScaledMenu.setOnCloseEvent(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));


        autoScaledMenu.addItem(cashSetting);
        autoScaledMenu.addItem(itemSetting);
        return autoScaledMenu;
    }
}
