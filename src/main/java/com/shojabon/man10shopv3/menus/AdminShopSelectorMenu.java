package com.shojabon.man10shopv3.menus;

import ToolMenu.CategoricalSInventoryMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.function.Consumer;

public class AdminShopSelectorMenu extends CategoricalSInventoryMenu {

    Man10ShopV3 plugin;
    Player player;
    Consumer<String> onClick = null;
    JSONObject adminShopRequests;

    public AdminShopSelectorMenu(Player p, String startingCategory, Man10ShopV3 plugin){
        super(new SStringBuilder().aqua().bold().text("管理可能ショップ一覧").build(), startingCategory, plugin);
        this.player = p;
        this.plugin = plugin;

        adminShopRequests = Man10ShopV3.api.getAdminShops(player);
    }

    public void setOnClick(Consumer<String> event){
        this.onClick = event;
    }

    public void renderMenu(){
        addInitializedCategory("その他");
        if(adminShopRequests == null) return;
        JSONArray shops = adminShopRequests.getJSONArray("data");
        for(int i = 0; i < shops.length(); i++){
            JSONObject shopInfo = shops.getJSONObject(i);
            JSONObject iconData = shopInfo.getJSONObject("icon");
            SItemStack icon = new SItemStack(Material.valueOf(iconData.getString("material")));
            icon.setDisplayName(new SStringBuilder().green().bold().text(shopInfo.getString("name")).build());
            icon.addLore("§d§lショップタイプ: " + shopInfo.getString("shopType"));
            icon.setCustomModelData(iconData.getInt("customModelData"));

            SInventoryItem item = new SInventoryItem(icon.build());
            item.clickable(false);
            item.setEvent(e -> {
                if(onClick != null) onClick.accept(shopInfo.getString("shopId"));
            });

            addItem(shopInfo.getString("category"), item);
        }
    }

}
