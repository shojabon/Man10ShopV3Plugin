package com.shojabon.man10shopv3.shopFunctions.storage;

import ToolMenu.ConfirmationMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.ShopMainMenu;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.BaseUtils;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.json.JSONObject;

@ShopFunctionDefinition(
        name = "ストレージを購入する",
        explanation = {},
        enabledShopType = {"BUY", "SELL"},
        iconMaterial = Material.CHEST,
        category = "倉庫設定",
        allowedPermission = "STORAGE_ACCESS",
        isAdminSetting = false
)
public class StorageFunction extends ShopFunction {

    public StorageFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }


    public int getItemCount(){
        return shop.shopData.getJSONObject("storage").getInt("itemCount");
    }

    public int getStorageSize(){
        return shop.shopData.getJSONObject("storage").getInt("storageSize");
    }
    public int getStorageSizeMax(){
        return shop.shopData.getJSONObject("storage").getInt("storageSizeMax");
    }
    public int getStorageSlotPrice(){
        return shop.shopData.getJSONObject("storage").getInt("storageSlotPrice");
    }

    @Override
    public String currentSettingString() {
        String result  = getStorageSize() + "個\n";
        if(getStorageSizeMax() != getStorageSize()){

            int nextSize = getStorageSize() + 64;
            if(nextSize > getStorageSizeMax()) nextSize = getStorageSizeMax();

            result += new SStringBuilder().red().text("次のサイズ: ").text(nextSize).text("個").build() + "\n";
            result += new SStringBuilder().yellow().text("価格: ").text(BaseUtils.priceString(nextSize * getStorageSlotPrice())).text("円").build() + "\n";
            result += new SStringBuilder().white().bold().text("左クリックで購入").build() + "\n";
            result += new SStringBuilder().white().bold().text("左シフトクリックで最大まで買う").yellow().text("価格:")
                    .text(BaseUtils.priceString((getStorageSizeMax() - getStorageSize()) * getStorageSlotPrice())).text("円").build() + "\n";
        }
        return result;
    }
    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        int unitsTillMax = getStorageSizeMax() - getStorageSize();

        item.setAsyncEvent(e -> {
            int buyingUnits = 64;

            if(e.getClick() == ClickType.SHIFT_LEFT) buyingUnits = unitsTillMax;


            //confirmation menu
            ConfirmationMenu menu = new ConfirmationMenu("確認", plugin);
            menu.setOnClose(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));
            menu.setOnCancel(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));


            final int finalBuyingUnits = buyingUnits;
            menu.setOnConfirm(ee -> {
                JSONObject payload = new JSONObject();
                payload.put("amount", finalBuyingUnits);
                JSONObject request = shop.requestQueueTask(player, "storage.buy", payload);
                if(!request.getString("status").equals("success")){
                    warn(player, request.getString("message"));
                    return;
                }
                player.sendMessage(Man10ShopV3.prefix + "§a§lストレージを購入しました");
                new ShopMainMenu(player, shop, plugin).open(player);
            });
            menu.open(player);

        });


        return item;
    }
}
