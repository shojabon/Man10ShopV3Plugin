package com.shojabon.man10shopv3.menus;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

public class TargetItemSelectorMenu extends SInventory{

    Man10Shop shop;
    Man10ShopV3 plugin;
    Player player;

    public TargetItemSelectorMenu(Player p, Man10Shop shop, Man10ShopV3 plugin){
        super(new SStringBuilder().aqua().text("アイテムをクリックしてください").build(), 3, plugin);
        this.player = p;
        this.shop = shop;
        this.plugin = plugin;


    }

    public void renderMenu(){
        SInventoryItem background = new SInventoryItem(new SItemStack(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build());
        background.clickable(false);
        fillItem(background);


        SInventoryItem target = new SInventoryItem(shop.targetItemFunction.getTargetItem().build());
        target.clickable(false);
        setItem(13, target);
        renderInventory();
    }

    public void registerEvents(){
        setOnClickEvent(e -> {
            e.setCancelled(true);
            if(e.getCurrentItem() == null) return;
            if(e.getClickedInventory().getType() == InventoryType.CHEST) return;
            SItemStack newTargetItem = new SItemStack(new SItemStack(e.getCurrentItem()).getTypeItem(true));
            if(newTargetItem == null) return;

            SInventory.threadPool.execute(()->{
                // set target item variable if error return
                // send message from response?
                JSONObject request = shop.setVariable(player, "targetItem.item", Man10ShopV3API.itemStackToJSON(newTargetItem.build()));
                shop.updateData();
                renderMenu();
            });

        });

        setOnCloseEvent(e -> new ShopMainMenu(player, shop, plugin).open(player));
    }

}
