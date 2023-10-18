package com.shojabon.man10shopv3.shopFunctions.general;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SLongTextInput;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@ShopFunctionDefinition(
        internalFunctionName = "category",
        name = "ショップのカテゴリを設定する",
        explanation = {},
        enabledShopType = {},
        iconMaterial = Material.LEAD,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class CategoryFunction extends ShopFunction {
    public CategoryFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }


    public String getCategory(){
        return getFunctionData().getString("category");
    }

    @Override
    public String currentSettingString() {
        return getCategory();
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setAsyncEvent(e -> {
            //text input
            SLongTextInput textInput = new SLongTextInput("§d§lカテゴリ名を入力してください 空白の場合はその他になります", plugin);
            textInput.setOnConfirm(categoryName -> {
                Man10ShopV3.threadPool.submit(() -> {
                    String categoryNameFinal = categoryName;
                    if(categoryNameFinal.length() > 64){
                        warn(player, "ショップ名は64文字以内でなくてはなりません");
                        return;
                    }
                    if(categoryNameFinal.isEmpty()) categoryNameFinal = "その他";
                    if(!setVariable(player, "category", categoryNameFinal)){
                        warn(player, "内部エラーが発生しました");
                        return;
                    }
                    success(player, "カテゴリを変更しました");
                });
            });

            textInput.setOnCancel(ee -> warn(player, "キャンセルしました"));


            textInput.open(player);
            SInventory.closeNoEvent(player, plugin);
        });

        return item;
    }
}
