package com.shojabon.man10shopv3.menus;

import ToolMenu.AutoScaledMenu;
import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.mcutils.Utils.BaseUtils;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class ShopMainMenu extends AutoScaledMenu {
    Man10ShopV3 plugin;
    Player player;

    JSONObject shopInfo;

    public ShopMainMenu(Player p, JSONObject shopInfo, Man10ShopV3 plugin) {
        super(new SStringBuilder().green().text(shopInfo.getJSONObject("name").getString("name") + "設定").build(), plugin);
        this.player = p;
        this.plugin = plugin;
        this.shopInfo = shopInfo;

        addItem(getShopInfoItem());
    }


    public SInventoryItem getShopInfoItem(){
        String iconName = new SStringBuilder().gold().bold().text("ショップ情報").build();
        SItemStack icon = new SItemStack(Material.OAK_SIGN).setDisplayName(iconName);

        icon.addLore("§aショップ口座残高:§e " + BaseUtils.priceString(shopInfo.getJSONObject("money").getInt("money")) + "円");
        icon.addLore("§7アイテム数:§e " + BaseUtils.priceString(shopInfo.getJSONObject("storage").getInt("itemCount")));

        SInventoryItem shopInfo = new SInventoryItem(icon.build());
        shopInfo.clickable(false);

        return shopInfo;
    }

}
