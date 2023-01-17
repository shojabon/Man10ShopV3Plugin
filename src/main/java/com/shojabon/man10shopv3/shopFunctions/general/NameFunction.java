package com.shojabon.man10shopv3.shopFunctions.general;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SLongTextInput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

@ShopFunctionDefinition(
        internalFunctionName = "name",
        name = "ショップ名設定",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.NAME_TAG,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class NameFunction extends ShopFunction {
    public NameFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }


    public String getName(){
        return getFunctionData().getString("name");
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setAsyncEvent(e -> {
            SLongTextInput textInput = new SLongTextInput("§d§lカテゴリ名を入力してください 空白の場合はその他になります", plugin);
            textInput.setOnConfirm(categoryName -> {
                if(categoryName.length() > 64){
                    warn(player, "ショップ名は64文字以内でなくてはなりません");
                    return;
                }
                if(categoryName.length() == 0) categoryName = "その他";
                if(!setVariable(player, "name", categoryName)){
                    return;
                }
                success(player, "名前を変更しました");
            });

            textInput.setOnCancel(ee -> warn(player, "キャンセルしました"));


            textInput.open(player);
            SInventory.closeNoEvent(player, plugin);
        });
        return item;
    }
}
