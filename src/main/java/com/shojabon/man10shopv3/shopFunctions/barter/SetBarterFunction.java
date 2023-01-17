package com.shojabon.man10shopv3.shopFunctions.barter;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.man10shopv3.menus.settings.innerSettings.BarterSettingMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@ShopFunctionDefinition(
        internalFunctionName = "setBarter",
        name = "トレード設定",
        explanation = {"トレード対象のアイテムなどを設定します"},
        enabledShopType = {"BARTER"},
        iconMaterial = Material.VILLAGER_SPAWN_EGG,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = true
)
public class SetBarterFunction extends ShopFunction {
    public SetBarterFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public List<ItemStack> getRequiredItems(){
        List<ItemStack> result = new ArrayList<>();

        JSONArray requiredItemsData = getFunctionData().getJSONArray("requiredItems");
        for(int i = 0; i < requiredItemsData.length(); i++){
            if(requiredItemsData.isNull(i)) {
                result.add(null);
                continue;
            }
            JSONObject itemData = requiredItemsData.getJSONObject(i);
            SItemStack item = SItemStack.fromBase64(itemData.getString("typeBase64"));
            item.setAmount(itemData.getInt("amount"));
            result.add(item.build());
        }
        return result;
    }
    public List<ItemStack> getResultItems(){
        List<ItemStack> result = new ArrayList<>();

        JSONArray requiredItemsData = getFunctionData().getJSONArray("resultItems");
        for(int i = 0; i < requiredItemsData.length(); i++){
            if(requiredItemsData.isNull(i)) {
                result.add(null);
                continue;
            }
            JSONObject itemData = requiredItemsData.getJSONObject(i);
            SItemStack item = SItemStack.fromBase64(itemData.getString("typeBase64"));
            item.setAmount(itemData.getInt("amount"));
            result.add(item.build());
        }
        return result;
    }

    public JSONArray itemStackListToJSONArray(List<ItemStack> items){
        JSONArray result = new JSONArray();

        for(ItemStack item: items){
            if(item == null) {
                result.put(JSONObject.NULL);
                continue;
            }
            SItemStack sItemStack = new SItemStack(item);
            JSONObject itemData = new JSONObject();
            itemData.put("typeBase64", sItemStack.getItemTypeBase64());
            itemData.put("typeMd5", sItemStack.getItemTypeMD5());
            itemData.put("displayName", sItemStack.getDisplayName());
            itemData.put("amount", sItemStack.getAmount());
            result.put(itemData);
        }
        return result;
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            //required
            List<ItemStack> required = getRequiredItems();
            List<ItemStack> result = getResultItems();
            List<ItemStack> both = new ArrayList<>();
            both.addAll(required);
            both.addAll(result);

            //confirmation menu
            BarterSettingMenu menu = new BarterSettingMenu(player, shop, both, plugin);

            menu.setOnCloseEvent(ee -> new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player));

            menu.setOnConfirm(items -> {
                if(!setVariable(player, "requiredItems", itemStackListToJSONArray(items.subList(0, 12)))){
                    return;
                }
                if(!setVariable(player, "setBarter.resultItems", itemStackListToJSONArray(items.subList(12, 13)))){
                    warn(player, "ターゲットアイテム設定中に保存が失敗しました、部分的に保存されている可能性があるのでご確認ください。");
                    return;
                }
                new SettingsMainMenu(player, shop, getDefinition().category(), plugin).open(player);

            });

            menu.open(player);

        });

        return item;
    }

}
