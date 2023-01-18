package com.shojabon.man10shopv3.shopFunctions.lootBox;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import com.shojabon.man10shopv3.menus.settings.lootBoxSettings.LootBoxBigWinSelectorMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;


@ShopFunctionDefinition(
        name = "通知・花火設定",
        explanation = {"このグループのアイテムが当たった際サーバー全体に通知する"},
        enabledShopType = {"LOOT_BOX"},
        iconMaterial = Material.FIREWORK_ROCKET,
        category = "一般設定",
        allowedPermission = "MODERATOR",
        isAdminSetting = false
)
public class LootBoxBigWinFunction extends ShopFunction {

    public LootBoxBigWinFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    @Override
    public SInventoryItem getSettingItem(Player player, SInventoryItem item) {
        item.setEvent(e -> {
            new LootBoxBigWinSelectorMenu(player, shop, shop.lootBoxGroupFunction.getLootBox(), plugin).open(player);
        });


        return item;
    }

}
