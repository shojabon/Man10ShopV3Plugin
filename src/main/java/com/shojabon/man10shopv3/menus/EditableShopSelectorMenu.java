package com.shojabon.man10shopv3.menus;

import ToolMenu.CategoricalSInventoryMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.commands.Man10ShopV3API;
import com.shojabon.mcutils.Utils.BaseUtils;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class EditableShopSelectorMenu extends CategoricalSInventoryMenu {

    Man10ShopV3 plugin;
    Player player;
    Consumer<String> onClick = null;

    JSONObject playerShopsRequest;

    public EditableShopSelectorMenu(Player p, JSONObject playerShopsRequest, String startingCategory, Man10ShopV3 plugin){
        super(new SStringBuilder().aqua().bold().text("管理可能ショップ一覧").build(), startingCategory, plugin);
        this.player = p;
        this.plugin = plugin;
        this.playerShopsRequest = playerShopsRequest;
        sort(true);

    }

    public void setOnClick(Consumer<String> event){
        this.onClick = event;
    }

    public void renderMenu(){
        addInitializedCategory("その他");

        JSONArray shops = playerShopsRequest.getJSONArray("data");
        for(int i = 0; i < shops.length(); i++){
            JSONObject shopInfo = shops.getJSONObject(i);

            SItemStack icon = SItemStack.fromBase64(shopInfo.getString("icon"));
            icon.setDisplayName(new SStringBuilder().green().bold().text(shopInfo.getString("name")).build());
            icon.addLore("§d§lショップタイプ: " + shopInfo.getString("shopType"));
            icon.addLore(new SStringBuilder().lightPurple().bold().text("権限: ").yellow().bold().text(shopInfo.getString("permission")).build());
            icon.addLore("");
            icon.addLore(new SStringBuilder().red().bold().text("在庫: ").yellow().bold().text(BaseUtils.priceString(shopInfo.getInt("itemCount"))).text("個").build());
            icon.addLore(new SStringBuilder().red().bold().text("残金: ").yellow().bold().text(BaseUtils.priceString(shopInfo.getInt("money"))).text("円").build());

            SInventoryItem item = new SInventoryItem(icon.build());
            item.clickable(false);
            item.setEvent(e -> {
                if(onClick != null) onClick.accept(shopInfo.getString("shopId"));
            });

            addItem(shopInfo.getString("category"), item);
        }
        //setItems(items);
    }

}
