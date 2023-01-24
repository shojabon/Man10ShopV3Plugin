package com.shojabon.man10shopv3.menus;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.mcutils.Utils.BannerDictionary;
import com.shojabon.mcutils.Utils.SBannerItemStack;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.json.JSONObject;

import java.util.function.Consumer;

public class ItemInOutMenu extends SInventory{
    Man10Shop shop;
    Man10ShopV3 plugin;
    Player player;

    public ItemInOutMenu(Player p, Man10Shop shop, Man10ShopV3 plugin){
        super("", 6, plugin);
        this.player = p;
        this.shop = shop;
        this.plugin = plugin;

        shop.updateData();

        int itemCount = shop.storageFunction.getItemCount();
        if(itemCount > 999999999){
            itemCount = 999999999;
        }
        if(itemCount < 0){
            itemCount = 0;
        }

        setTitle("§7§lアイテムを操作してください 現在:" + itemCount + "個");
        setOnClickEvent(e -> e.setCancelled(true));


    }

    public void renderMenu(){
        SInventoryItem background = new SInventoryItem(new SItemStack(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build());
        background.clickable(false);
        fillItem(background);

        SInventoryItem display = new SInventoryItem(shop.targetItemFunction.getTargetItem().build());
        display.clickable(false);
        setItem(13, display);

//        int itemCount = shop.storageFunction.getItemCount();
//        if(itemCount > 999999999){
//            itemCount = 999999999;
//        }
//        if(itemCount < 0){
//            itemCount = 0;
//        }

//        String itemCountString = String.valueOf(itemCount);


//        for(int i = 0; i < itemCountString.length(); i++){
//            char currentChar = itemCountString.charAt(i);
//            SInventoryItem numberDisplay = new SInventoryItem(new BannerDictionary().getItem(Integer.parseInt(currentChar + "")));
//            numberDisplay.clickable(false);
//            setItem(27 + i + (9 - itemCountString.length()), numberDisplay);
//        }

        SInventoryItem takeOutButton = new SInventoryItem(new SItemStack(Material.DROPPER).setDisplayName("§7§lお引き出し").addLore("§e§lクリックで1個お引き出し").addLore("§e§lシフトクリックで" + shop.targetItemFunction.getTargetItem().getMaxStackSize() +  "個お引き出し").build());
        takeOutButton.clickable(false);
        takeOutButton.setAsyncEvent(e -> {
            int amount = 1;
            if(e.isShiftClick()){
                amount = shop.targetItemFunction.getTargetItem().getMaxStackSize();
            }
            JSONObject data = new JSONObject();
            data.put("amount", amount);
            shop.requestQueueTask(player, "storage.item.withdraw",data);
        });

        setItem(new int[]{50, 51, 52}, takeOutButton);

        SInventoryItem putInButton = new SInventoryItem(new SItemStack(Material.HOPPER).setDisplayName("§7§lお預入れ").addLore("§e§lクリックで1個お預け入れ").addLore("§e§lシフトクリックで" + shop.targetItemFunction.getTargetItem().getMaxStackSize() +  "個お預け入れ").build());
        putInButton.clickable(false);
        putInButton.setAsyncEvent(e -> {
            int amount = 1;
            if(e.isShiftClick()){
                amount = shop.targetItemFunction.getTargetItem().getMaxStackSize();
            }
            JSONObject data = new JSONObject();
            data.put("amount", amount);
            shop.requestQueueTask(player, "storage.item.deposit",data);
        });

        setItem(new int[]{46, 47, 48}, putInButton);

    }
}
