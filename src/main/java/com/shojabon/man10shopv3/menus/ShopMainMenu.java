package com.shojabon.man10shopv3.menus;

import ToolMenu.AutoScaledMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.mcutils.Utils.BaseUtils;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShopMainMenu extends AutoScaledMenu {
    Man10ShopV3 plugin;
    Player player;

    Man10Shop shop;

    public ShopMainMenu(Player p, Man10Shop shop, Man10ShopV3 plugin) {
        super(new SStringBuilder().green().text(shop.nameFunction.getName() + "設定").build(), plugin);
        this.player = p;
        this.plugin = plugin;
        this.shop = shop;

        addItem(getShopInfoItem());
        addItem(getTargetItemSettingsItem());
    }


    public SInventoryItem getShopInfoItem(){
        String iconName = new SStringBuilder().gold().bold().text("ショップ情報").build();
        SItemStack icon = new SItemStack(Material.OAK_SIGN).setDisplayName(iconName);

        icon.addLore("§aショップ口座残高:§e " + BaseUtils.priceString(shop.shopData.getJSONObject("money").getInt("money")) + "円");
        icon.addLore("§7アイテム数:§e " + BaseUtils.priceString(shop.shopData.getJSONObject("storage").getInt("itemCount")));

        SInventoryItem shopInfo = new SInventoryItem(icon.build());
        shopInfo.clickable(false);

        return shopInfo;
    }

    public SInventoryItem getTargetItemSettingsItem(){
        SStringBuilder iconName = new SStringBuilder();

        if(shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
            iconName.aqua().bold().text("取引アイテム設定").build();
        }else{
            iconName.gray().bold().strike().text("取引アイテム設定");
        }

        SItemStack icon = new SItemStack(Material.LECTERN).setDisplayName(iconName.build());

        if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
            icon.addLore(new SStringBuilder().red().text("権限がありません").build());
            icon.addLore("");
        }
        icon.addLore(new SStringBuilder().white().text("取引する対象のアイテム種を設定することができます").build());
        SInventoryItem item = new SInventoryItem(icon.build());

        item.clickable(false);


        item.setAsyncEvent(e -> {
            shop.updateData();
            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
                player.sendMessage(Man10ShopV3.prefix + "§c§lこの項目を開く権限がありません");
                return;
            }
            new TargetItemSelectorMenu(player, shop, plugin).open(player);
        });


        return item;
    }

}
