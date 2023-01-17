package com.shojabon.man10shopv3.shopFunctions.general;

import ToolMenu.AutoScaledMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.enums.Man10ShopType;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.UUID;

@ShopFunctionDefinition(
        internalFunctionName = "",
        name = "ショップタイプ設定",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.OAK_FENCE_GATE,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class ShopTypeFunction extends ShopFunction {

    //variables

    //init
    public ShopTypeFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    @Override
    public String currentSettingString() {
        return Man10ShopType.valueOf(shop.getShopType()).displayName;
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
        AutoScaledMenu menu = new AutoScaledMenu("ショップタイプ選択", plugin);
        for(Man10ShopType type: Man10ShopType.values()){
            if(type.admin && !shop.isAdmin()) continue;
            SInventoryItem mode = new SInventoryItem(new SItemStack(type.settingItem).setDisplayName("§a§l" + type.displayName).build());
            mode.clickable(false);
            mode.setAsyncEvent(e -> {
                JSONObject object = shop.setVariable(player, "shopType", type.name());
                if(!object.getString("status").equals("success")){
                    warn(player, object.getString("message"));
                    return;
                }
                player.sendMessage(Man10ShopV3.prefix + "§a§lショップタイプが設定されました");
                player.getServer().getScheduler().runTask(plugin, ()-> {
                    SInventory.closeInventoryGroup(UUID.fromString(shop.getShopId()), plugin);
                });
                new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);
            });
            menu.addItem(mode);
        }

        return menu;
    }
}
