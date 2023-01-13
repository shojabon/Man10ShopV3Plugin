package com.shojabon.man10shopv3.menus;

import ToolMenu.AutoScaledMenu;
import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.BaseUtils;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
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
        addItem(getShopSettingsItem());
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

    public SInventoryItem getShopSettingsItem(){
        SStringBuilder iconName = new SStringBuilder();

        if(shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
            iconName.gray().bold().text("ショップ設定");
        }else{
            iconName.gray().bold().strike().text("ショップ設定");
        }

        SItemStack icon = new SItemStack(Material.IRON_DOOR).setDisplayName(iconName.build());

        if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
            icon.addLore(new SStringBuilder().red().text("権限がありません").build());
            icon.addLore("");
        }
        icon.addLore(new SStringBuilder().white().text("ショップの設定メニュー").build());
        SInventoryItem item = new SInventoryItem(icon.build());

        item.clickable(false);


        item.setEvent(e -> {
            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
                player.sendMessage(Man10ShopV3.prefix + "§c§lこの項目を開く権限がありません");
                return;
            }
            new SettingsMainMenu(player, shop, "その他", plugin).open(player);
        });


        return item;
    }

    public SInventoryItem getMoneySelectorMenu(){
        SStringBuilder iconName = new SStringBuilder();

        if(shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
            iconName.green().bold().text("現金出し入れ").build();
        }else{
            iconName.gray().bold().strike().text("現金出し入れ");
        }

        SItemStack icon = new SItemStack(Material.EMERALD).setDisplayName(iconName.build());

        if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "ACCOUNTANT") || shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
            icon.addLore(new SStringBuilder().red().text("権限がありません").build());
            icon.addLore("");
        }
        icon.addLore(new SStringBuilder().white().text("現金出し入れを行うことができます").build());
        icon.addLore(new SStringBuilder().white().text("取引は電子マネーが使われます").build());
        SInventoryItem item = new SInventoryItem(icon.build());

        item.clickable(false);

        item.setEvent(e -> {
            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS") || shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
                player.sendMessage(Man10ShopV3.prefix + "§c§lこの項目を開く権限がありません");
                return;
            }

            InOutSelectorMenu menu = new InOutSelectorMenu(player, shop, plugin);
            menu.setInText("口座に入金する");
            menu.setOutText("口座から出金をする");

            menu.setOnInClicked(ee -> generateMoneyEvent(true).open(player));
            menu.setOnOutClicked(ee -> generateMoneyEvent(false).open(player));

            menu.setOnClose(ee -> new ShopMainMenu(player, shop, plugin).open(player));


            menu.open(player);
        });



        return item;
    }

    public SInventory generateMoneyEvent(boolean deposit) {
        String title = "出金額を入力してください";
        if(deposit) title = "入金額を入力してください";

        NumericInputMenu menu = new NumericInputMenu( title, plugin);
        if (deposit) {
            menu.setMaxValue((int) Man10ShopV3.vault.getBalance(player.getUniqueId()));
        }
        menu.setAllowZero(false);
        menu.setOnCancel(e -> new ShopMainMenu(player, shop, plugin).open(player));
        menu.setOnClose(e -> new ShopMainMenu(player, shop, plugin).open(player));
        menu.setOnConfirm(integer -> {
            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "ACCOUNTANT") || shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
                player.sendMessage(Man10ShopV3.prefix + "§c§l権限がありません");
                return;
            }
            //  money request


//            if (deposit) {
//                if (Man10ShopV3.vault.getBalance(player.getUniqueId()) < integer) {
//                    player.sendMessage(Man10ShopV3.prefix + "§c§l現金が不足しています");
//                    return;
//                }
//                Man10ShopV3.vault.withdraw(player.getUniqueId(), integer);
//                shop.money.addMoney(integer); // add money request
//                player.sendMessage(Man10ShopV3.prefix + "§a§l" + BaseUtils.priceString(integer) + "円入金しました");
//            } else {
//                if (shop.moneyFunction.getMoney() < integer) {
//                    player.sendMessage(Man10ShopV3.prefix + "§c§l現金が不足しています");
//                    return;
//                }
//                Man10ShopV3.vault.deposit(player.getUniqueId(), integer);
//                shop.money.removeMoney(integer);// remove money request
//                player.sendMessage(Man10ShopV3.prefix + "§a§l" + BaseUtils.priceString(integer) + "円出金しました");
//            }
            new ShopMainMenu(player, shop, plugin).open(player);
        });
        return menu;
    }


}
