package com.shojabon.man10shopv3.dataClass.lootBox;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class LootBoxFunction extends ShopFunction {

    public LootBoxFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }

    public void afterLootBoxSpinFinished(Player player, ItemStack item, int groupId){}
}
