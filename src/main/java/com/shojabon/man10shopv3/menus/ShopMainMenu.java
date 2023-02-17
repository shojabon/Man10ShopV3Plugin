package com.shojabon.man10shopv3.menus;

import ToolMenu.AutoScaledMenu;
import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.Man10ShopV3API;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.menus.permission.PermissionSettingsMainMenu;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.BaseUtils;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class ShopMainMenu extends AutoScaledMenu {
    Man10ShopV3 plugin;
    Player player;

    Man10Shop shop;

    boolean actionHappened = false;

    public ShopMainMenu(Player p, Man10Shop shop, Man10ShopV3 plugin) {
        super(new SStringBuilder().green().text(shop.nameFunction.getName() + "設定").build(), plugin);
        this.player = p;
        this.plugin = plugin;
        this.shop = shop;

        shop.updateData();

        if(shop.getShopType().equals("BUY") || shop.getShopType().equals("SELL")){
            addItem(getShopSettingsItem());
            addItem(getShopInfoItem());
            addItem(getStorageSettingsItem());
            addItem(getTargetItemSettingsItem());
            addItem(getMoneySelectorMenu());
            addItem(getPermissionSettingsItem());
        }else if(shop.getShopType().equals("BARTER")){
            addItem(getShopSettingsItem());
            addItem(getShopInfoItem());
            addItem(getTargetItemSettingsItem());
            addItem(getPermissionSettingsItem());
        }else if(shop.getShopType().equals("LOOT_BOX")){
            addItem(getShopSettingsItem());
            addItem(getShopInfoItem());
            addItem(getTargetItemSettingsItem());
            addItem(getPermissionSettingsItem());
        } else{
            addItem(getShopSettingsItem());
            addItem(getShopInfoItem());
            addItem(getStorageSettingsItem());
            addItem(getTargetItemSettingsItem());
            addItem(getMoneySelectorMenu());
            addItem(getPermissionSettingsItem());
        }

        setOnCloseEvent(e -> {
            if(shop.isAdmin()){
                AdminShopSelectorMenu menu = new AdminShopSelectorMenu(player, shop.categoryFunction.getCategory(), plugin);
                menu.setOnClick(shopId -> {
                    Man10Shop shopInfo = Man10ShopV3.api.getShop(shopId, player);
                    if(shopInfo == null){
                        return;
                    }
                    new ShopMainMenu(player, shopInfo, plugin).open(player);
                });
                menu.open(player);
            }else{
                EditableShopSelectorMenu menu = new EditableShopSelectorMenu(player, shop.categoryFunction.getCategory(), plugin);
                menu.setOnClick(shopId -> {
                    Man10Shop shopInfo = Man10ShopV3.api.getShop(shopId, player);
                    if(shopInfo == null){
                        return;
                    }
                    new ShopMainMenu(player, shopInfo, plugin).open(player);
                });
                menu.open(player);
            }
        });
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
        SItemStack icon = new SItemStack(Material.EMERALD);
        if(shop.permissionFunction.hasPermission(player.getUniqueId(), "ACCOUNTANT")){
            iconName.green().bold().text("現金出し入れ").build();
        }else{
            iconName.gray().bold().strike().text("現金出し入れ");
            icon.addLore(new SStringBuilder().red().text("権限がありません").build());
            icon.addLore("");
        }
        icon.setDisplayName(iconName.build());

        icon.addLore(new SStringBuilder().white().text("現金出し入れを行うことができます").build());
        icon.addLore(new SStringBuilder().white().text("取引は電子マネーが使われます").build());
        SInventoryItem item = new SInventoryItem(icon.build());

        item.clickable(false);

        item.setEvent(e -> {
            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "ACCOUNTANT")){
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
            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "ACCOUNTANT")){
                player.sendMessage(Man10ShopV3.prefix + "§c§lこの項目を開く権限がありません");
                return;
            }

            JSONObject data = new JSONObject();
            data.put("amount", integer);
            if(deposit){
                shop.requestQueueTask(player, "money.deposit", data);
            }else{
                shop.requestQueueTask(player, "money.withdraw", data);
            }

            new ShopMainMenu(player, shop, plugin).open(player);
        });
        return menu;
    }
    public SInventoryItem getStorageSettingsItem(){
        SStringBuilder iconName = new SStringBuilder();

        if(shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
            iconName.gray().bold().text("アイテム倉庫").build();
        }else{
            iconName.gray().bold().strike().text("アイテム倉庫");
        }

        SItemStack icon = new SItemStack(Material.CHEST).setDisplayName(iconName.build());

        if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
            icon.addLore(new SStringBuilder().red().text("権限がありません").build());
            icon.addLore("");
        }
        icon.addLore(new SStringBuilder().white().text("アイテムの出しいれをすることができます").build());
        SInventoryItem item = new SInventoryItem(icon.build());

        item.clickable(false);

        item.setEvent(e -> {
            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
                player.sendMessage(Man10ShopV3.prefix + "§c§lこの項目を開く権限がありません");
                return;
            }

            InOutSelectorMenu menu = new InOutSelectorMenu(player, shop, plugin);
            menu.setOnClose(ee -> new ShopMainMenu(player, shop, plugin).open(player));
            menu.setOnInClicked(ee -> {
                //editing storage
                if(actionHappened) return;
                JSONObject data = new JSONObject();
                data.put("withdraw", false);
                shop.requestQueueTask(player, "storage.menu.open", data);
                actionHappened = true;
            });

            menu.setOnOutClicked(ee -> {
                //editing storage
                if(actionHappened) return;
                JSONObject data = new JSONObject();
                data.put("withdraw", true);
                shop.requestQueueTask(player, "storage.menu.open", data);
                actionHappened = true;
            });
            menu.setInText("倉庫にアイテムを入れる");
            menu.setOutText("倉庫からアイテムを出す");

            menu.open(player);
        });



        return item;
    }
//    public SInventoryItem getStorageSettingsItem(){
//        SStringBuilder iconName = new SStringBuilder();
//
//        if(shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
//            iconName.gray().bold().text("アイテム倉庫").build();
//        }else{
//            iconName.gray().bold().strike().text("アイテム倉庫");
//        }
//
//        SItemStack icon = new SItemStack(Material.CHEST).setDisplayName(iconName.build());
//
//        if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
//            icon.addLore(new SStringBuilder().red().text("権限がありません").build());
//            icon.addLore("");
//        }
//        icon.addLore(new SStringBuilder().white().text("アイテムの出しいれをすることができます").build());
//        SInventoryItem item = new SInventoryItem(icon.build());
//
//        item.clickable(false);
//
//        item.setEvent(e -> {
//            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "STORAGE_ACCESS")){
//                player.sendMessage(Man10ShopV3.prefix + "§c§lこの項目を開く権限がありません");
//                return;
//            }
//
//            ItemInOutMenu menu = new ItemInOutMenu(player, shop, plugin);
//            menu.setOnCloseEvent(ee -> {
//                new ShopMainMenu(player, shop, plugin).open(player);
//            });
//            menu.open(player);
//        });



//        return item;
//    }

    public SInventoryItem getPermissionSettingsItem(){
        SStringBuilder iconName = new SStringBuilder();

        if(shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
            iconName.darkRed().bold().text("権限設定").build();
        }else{
            iconName.gray().bold().strike().text("権限設定");
        }

        SItemStack icon = new SItemStack(Material.BELL).setDisplayName(iconName.build());

        if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
            icon.addLore(new SStringBuilder().red().text("権限がありません").build());
            icon.addLore("");
        }
        icon.addLore(new SStringBuilder().white().text("ショップの設定をできる人などを設定することができます").build());
        SInventoryItem item = new SInventoryItem(icon.build());

        item.clickable(false);

        item.setEvent(e -> {
            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
                player.sendMessage(Man10ShopV3.prefix + "§c§lこの項目を開く権限がありません");
                return;
            }
            PermissionSettingsMainMenu menu = new PermissionSettingsMainMenu(player, shop, plugin);
            menu.open(player);
        });


        return item;
    }

}
