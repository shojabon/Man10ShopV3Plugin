package com.shojabon.man10shopv3.menus.action;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.mcutils.Utils.BannerDictionary;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BarterActionMenu extends SInventory {

    Man10Shop shop;
    Man10ShopV3 plugin;
    Player player;
    BannerDictionary banner = new BannerDictionary();
    int[] slots = new int[]{10, 11, 12, 19, 20 ,21 ,28, 29, 30, 37, 38, 39, 34};
    List<ItemStack> current;
    boolean orderRequested = false;


    public BarterActionMenu(Player p, Man10Shop shop, Man10ShopV3 plugin){
        super("トレード設定",6, plugin);
        this.shop = shop;
        this.player = p;
        this.plugin = plugin;
        this.current = new ArrayList<>();
        this.current.addAll(shop.setBarterFunction.getRequiredItems());
        this.current.addAll(shop.setBarterFunction.getResultItems());

        SStringBuilder builder = new SStringBuilder().darkGray().text(new SItemStack(shop.setBarterFunction.getResultItems().get(0)).getDisplayName());

        int maxTradeItemCount = shop.getMenuInfo().getInt("tradeItemCount");
        builder.text("§b§lにトレードする");
        if(maxTradeItemCount != 0){
            builder.text(" 残り " + maxTradeItemCount + "個");
        }
        setTitle(builder.build());

        setOnClickEvent(e -> {
            if(e.getClickedInventory() == null) return;
            if(e.getClickedInventory().getType() == InventoryType.PLAYER)e.setCancelled(true);
        });

    }

    public void renderMenu(){
        SInventoryItem background = new SInventoryItem(new SItemStack(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build());
        background.clickable(false);
        fillItem(background);

        SInventoryItem arrow = new SInventoryItem(new SItemStack(banner.getSymbol("right")).setDisplayName(" ").build());
        arrow.clickable(false);
        setItem(32, arrow);

        SInventoryItem noItem = new SInventoryItem(new SItemStack(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build());
        noItem.clickable(false);
        setItem(slots, noItem);

        for(int i = 0; i < current.size(); i++){
            if(current.get(i) == null) continue;
            SInventoryItem item = new SInventoryItem(current.get(i));
            item.clickable(false);
            setItem(slots[i], item);
        }

        SInventoryItem confirm = new SInventoryItem(new SItemStack(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§a§l決定").build());
        confirm.clickable(false);
        confirm.setAsyncEvent(e -> {
            if(orderRequested) return;
            JSONObject data = new JSONObject();
            data.put("amount", 1);
            shop.requestQueueTask(player, "shop.order", data);
            close(player);
        });
        setItem(new int[]{48, 49, 50}, confirm);

        renderInventory();
    }


}
