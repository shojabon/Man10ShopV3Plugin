package com.shojabon.man10shopv3.menus.settings.innerSettings;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.mcutils.Utils.BannerDictionary;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BarterSettingMenu extends SInventory{

    Man10Shop shop;
    Man10ShopV3 plugin;
    Player player;
    BannerDictionary banner = new BannerDictionary();
    int currentSelecting = 10;
    int[] slots = new int[]{10, 11, 12, 19, 20 ,21 ,28, 29, 30, 37, 38, 39, 34};
    List<ItemStack> current;

    Consumer<List<ItemStack>> onConfirm = null;

    public BarterSettingMenu(Player p, Man10Shop shop, List<ItemStack> currentItems, Man10ShopV3 plugin){
        super("トレード設定",6, plugin);
        this.shop = shop;
        this.player = p;
        this.plugin = plugin;
        this.current = currentItems;

        setOnClickEvent(e -> {
            if(e.getClickedInventory() == null) return;
            if(e.getClickedInventory().getType() != InventoryType.PLAYER) return;
            if(e.getCurrentItem() == null) return;
            e.setCancelled(true);
            current.set(Arrays.stream(slots).boxed().collect(Collectors.toList()).indexOf(currentSelecting), e.getCurrentItem().clone());
            renderMenu();
        });
    }

    public void setOnConfirm(Consumer<List<ItemStack>> event){
        this.onConfirm = event;
    }

    public void renderMenu(){
        SInventoryItem background = new SInventoryItem(new SItemStack(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build());
        background.clickable(false);
        fillItem(background);


        SInventoryItem arrow = new SInventoryItem(new SItemStack(banner.getSymbol("right")).setDisplayName(" ").build());
        arrow.clickable(false);
        setItem(32, arrow);

        SInventoryItem clearSlot = new SInventoryItem(new SItemStack(Material.BARRIER).setDisplayName("§c§l選択中のスロットをクリア").build());
        clearSlot.clickable(false);
        clearSlot.setAsyncEvent(e -> {
            current.set(Arrays.stream(slots).boxed().collect(Collectors.toList()).indexOf(currentSelecting), null);
            player.sendMessage(Man10ShopV3.prefix + "§c§l" + e.getRawSlot() + "をクリアしました");
            renderMenu();
        });
        setItem(45, clearSlot);

        SInventoryItem noItem = new SInventoryItem(new SItemStack(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build());
        noItem.clickable(false);
        noItem.setAsyncEvent(e -> {
            currentSelecting = e.getRawSlot();
            player.sendMessage(Man10ShopV3.prefix + "§a§l" + e.getRawSlot() + "を編集中");
        });
        setItem(slots, noItem);

        for(int i = 0; i < current.size(); i++){
            if(current.get(i) == null) continue;
            SInventoryItem item = new SInventoryItem(current.get(i).clone());
            item.clickable(false);
            item.setAsyncEvent(e -> {
                currentSelecting = e.getRawSlot();
                player.sendMessage(Man10ShopV3.prefix + "§a§l" + e.getRawSlot() + "を編集中");
            });
            setItem(slots[i], item);
        }

        SInventoryItem confirm = new SInventoryItem(new SItemStack(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§a§l決定").build());
        confirm.clickable(false);
        confirm.setAsyncEvent(e -> {
            if(onConfirm != null) onConfirm.accept(current);
        });
        setItem(new int[]{48, 49, 50}, confirm);

        renderInventory();
    }


}
